package com.clarity.epcis;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.clarity.epcis.kafka.ProducerCreator;
import com.clarity.epcis.model.*;
import com.clarity.rest.client.SiteWhereUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitewhere.rest.model.user.User;
import com.sitewhere.spi.SiteWhereException;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author dhananjay
 *
 */
public class EPCISEvents {

	private CacheManager cm;

	public EPCISEvents() {
		super();
		// Create a cache manager
		cm = CacheManager.getInstance();
	}

	// Get a cache called "cacheStore"
	Cache cache;

	private LatestEventData latestEventData = new LatestEventData();

	private EpcisBody epcisBody = new EpcisBody();

	private List<EventDetail> eventList = new ArrayList<EventDetail>();

	public boolean initializeEPCISEvents(InitializeData initialData, String userName) throws SiteWhereException {
		// add location and user data in cache
		User userData = getClientByUsername(userName);
		Object userLocation = userData.getMetadata().get("location");
		if (validateInitialData(initialData))
			// Create a cache called "cacheStore"
			cm.addCache("cacheStore");
		cache = cm.getCache("cacheStore");
		cache.put(new Element("initialData", initialData));
		cache.put(new Element("userLocation", userLocation));
		return true;
	}

	private User getClientByUsername(String userName) throws SiteWhereException {

		SiteWhereUtil client = SiteWhereUtil.newBuilder().withConnectionTo("http", "34.71.165.10", 31670)
				.withSyncopeConnection("34.71.165.10", 31451).build().initialize();
		User user = client.getUserByUsername(userName);
		return user;
	}

	private boolean validateInitialData(InitializeData initialData) {
		if (initialData.getGsprefix().isEmpty() && initialData.getItemRef().isEmpty()
				&& initialData.getTimezone().isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean publishEPCISEvents(Object data, String eventType) throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException, JSONException {
		JSONObject json = null;
		cache = cm.getCache("cacheStore");
		Element userElement = cache.get("userLocation");
		Object userLocation = userElement.getObjectValue();
		Element ele = cache.get("initialData");
		InitializeData initialData = (InitializeData) ele.getObjectValue();

		// Common for Agreegant and object event
		Event event = new Event();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		event.setCreationDate(now);

		EventDetail eventDetail = new EventDetail();
		UUID uuid = UUID.randomUUID();

		eventDetail.setUuid(uuid.toString());
		
		String[] latLong = userLocation.toString().split(",");
		eventDetail.setBizlocation(latLong[2]+":"+latLong[3]);
		json = new JSONObject(data);
		try {
			latestEventData = manipulateLatestEvent(json, eventType);
			eventDetail.setEventTimeZoneOffset(initialData.getTimezone());
			eventDetail.setIsA("AggregationEvent");

			eventDetail.setParentId(initialData.getGsprefix());
			eventDetail.setRecordId(json.get("recordId").toString());
			List<ChildEPCs> childEpcsList = new ArrayList<ChildEPCs>();
			ChildEPCs childEpcs = new ChildEPCs();
			childEpcs.setGsPrefix(initialData.getGsprefix());
			childEpcs.setItemRef(initialData.getItemRef());
			ObjectMapper mapper = new ObjectMapper();
			SubProduct[] subProductIds = mapper.readValue(json.get("subProducts").toString(), SubProduct[].class);
			childEpcs.setSubProducts(subProductIds);
			childEpcsList.add(childEpcs);
			eventDetail.setChildEPCs(childEpcsList);
			eventDetail.setAction("Observe");
			eventDetail.setProductType(json.get("producttype").toString());
			eventDetail.setSubProductCount(subProductIds.length);
			eventList.add(eventDetail);
			epcisBody.setEventList(eventList);
			event.setEpcisBody(epcisBody);
			eventDetail.setBizStep(latestEventData.getBizstep());
			eventDetail.setDisposition(latestEventData.getDisposition());
			eventDetail.setReadPoint(latestEventData.getReadpoint());
			eventDetail.setEventTime(latestEventData.getTimestamp());
			String aggregationEventTemplate = createTemplate(initialData, event);
			runProducer(aggregationEventTemplate, latestEventData.getEventType());
		} catch (JSONException e) {
			eventDetail.setIsA("ObjectEvent");

			if (((JSONArray) json.get("checkin")).length() != 0) {
				eventDetail.setAction("OBSERVE");
			} else {
				eventDetail.setAction("ADD");
			}

			latestEventData = manipulateLatestEvent(json, eventType);
			eventDetail.setBizStep(latestEventData.getBizstep());
			// epcList
			eventDetail.setEpcList(
					initialData.getGsprefix() + "." + initialData.getItemRef() + "." + json.get("recordId").toString());
			eventDetail.setEventTimeZoneOffset(initialData.getTimezone());
			eventDetail.setEventTime(latestEventData.getTimestamp());
			eventDetail.setReadPoint(latestEventData.getReadpoint());
			eventDetail.setRecordId(json.get("recordId").toString());
			eventDetail.setDisposition(latestEventData.getDisposition());
			eventList.add(eventDetail);
			epcisBody.setEventList(eventList);
			event.setEpcisBody(epcisBody);
			String creationEventTemplate = createTemplateForCreationEvent(initialData, event);
			runProducer(creationEventTemplate, latestEventData.getEventType());
		}

		return true;
	}

	private String createTemplateForCreationEvent(InitializeData initialData, Event event)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
			TemplateException {

		Configuration cfg = new Configuration();

		cfg.setClassForTemplateLoading(EPCISEvents.class, "templates");

		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		Map<String, Object> input = new HashMap<String, Object>();

		input.put("timezone", initialData.getTimezone());
		input.put("gsprefix", initialData.getGsprefix());
		input.put("itemRef", initialData.getItemRef());
		input.put("creationDate", event.getCreationDate());
		EventDetail eventDetails = null;
		try {
			input.put("isA", event.getEpcisBody().getEventList().get(0).getIsA());
			eventDetails = event.getEpcisBody().getEventList().get(0);
		} catch (Exception e) {
			System.out.println("exception in template creation" + e);
		}

		input.put("uuid", eventDetails.getUuid());
		input.put("eventTime", eventDetails.getEventTime());
		input.put("recordId", eventDetails.getRecordId());
		input.put("action", eventDetails.getAction());
		input.put("bizStep", eventDetails.getBizStep());
		input.put("disposition", eventDetails.getDisposition());
		input.put("readPoint", eventDetails.getReadPoint());
		input.put("epcList", eventDetails.getEpcList());
		input.put("userLocation", eventDetails.getBizlocation());
		Template template = cfg.getTemplate("epcis_creation.ftl");

		String str;
		try (StringWriter out = new StringWriter()) {
			template.process(input, out);
			str = out.getBuffer().toString();
			out.flush();

		}
		return str;
	}

	private LatestEventData manipulateLatestEvent(JSONObject json,String eventType)
			throws JSONException, JsonParseException, JsonMappingException, IOException {
		ArrayList<Long> checkInCheckOutTimestamp = new ArrayList<Long>();
		JSONArray checkInArryJson = (JSONArray) json.get("checkin");
		JSONArray checkOutArryJson = (JSONArray) json.get("checkout");

		ObjectMapper mapper = new ObjectMapper();
		// 1. convert JSON array to Array objects
		CheckIn[] checkInArry = mapper.readValue(checkInArryJson.toString(), CheckIn[].class);
		for (int i = 0; i < checkInArry.length; i++) {
			CheckIn checkIn = (CheckIn) checkInArry[0];
			checkInCheckOutTimestamp.add(checkIn.getTimestamp());
		}
		CheckOut[] checkOutArry = mapper.readValue(checkOutArryJson.toString(), CheckOut[].class);
		for (int i = 0; i < checkOutArry.length; i++) {
			CheckOut checkOut = (CheckOut) checkOutArry[0];
			checkInCheckOutTimestamp.add(checkOut.getTimestamp());
		}

		java.util.Collections.reverse(checkInCheckOutTimestamp);
		if (!checkInCheckOutTimestamp.isEmpty()) {
			// fetch latest event data
			latestEventData = checkFromJsonByTimestamp(checkInCheckOutTimestamp.get(0), checkInArryJson,
					checkOutArryJson,eventType);
		} else {
			latestEventData = new LatestEventData();
		}

		return latestEventData;
	}

	private LatestEventData checkFromJsonByTimestamp(Long searchTimestamp, JSONArray checkInArryJson,
			JSONArray checkOutArryJson, String eventType) throws JsonParseException, JsonMappingException, IOException {
		latestEventData = null;
		ObjectMapper mapper = new ObjectMapper();
		if (eventType==null || eventType.equals("checkin") || eventType.equals("checkout")) {
			// 1. convert JSON array to Array objects
			CheckIn[] checkInArry = mapper.readValue(checkInArryJson.toString(), CheckIn[].class);
			for (int i = 0; i < checkInArry.length; i++) {
				CheckIn checkIn = (CheckIn) checkInArry[0];
				if (latestEventData == null && checkIn.getTimestamp() == searchTimestamp) {
					latestEventData = new LatestEventData();
					latestEventData.setBizstep("accepting");
					latestEventData.setEventType("checkin");
					latestEventData.setDisposition("in_progess");
					latestEventData.setReadpoint(checkIn.getUser());
					latestEventData.setTimestamp(new Timestamp(checkIn.getTimestamp()));
				}
			}
			CheckOut[] checkOutArry = mapper.readValue(checkOutArryJson.toString(), CheckOut[].class);
			for (int i = 0; i < checkOutArry.length; i++) {
				CheckOut checkOut = (CheckOut) checkOutArry[0];
				if (latestEventData == null && checkOut.getTimestamp() == searchTimestamp) {
					latestEventData = new LatestEventData();
					latestEventData.setBizstep("departing");
					latestEventData.setEventType("checkout");
					latestEventData.setDisposition("in_transit");
					latestEventData.setReadpoint(checkOut.getUser());
					latestEventData.setTimestamp(new Timestamp(checkOut.getTimestamp()));
				}
			}
		} else if (eventType.equals("forbidden") || eventType.equals("scan")) {
			// else forbidden/scan other
		}

		return latestEventData;
	}

	void runProducer(String template, String topic) {
		Producer<Long, String> producer = ProducerCreator.createProducer();

		final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(topic,
				"This is record -> " + template);
		try {
			producer.send(record).get();
		} catch (ExecutionException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
		} catch (InterruptedException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
		}
	}

	/*
	 * Client invokes EPCISevents (DeInitalize()) Close the kakfa topics
	 */
	public boolean deInitializeEPCISEvents() {
		// Call shutdown to close the cache manager and close kafka producer
		cm.shutdown();
		try {
			Producer<Long, String> producer = ProducerCreator.createProducer();
			producer.close();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public String createTemplate(InitializeData initialData, Event event) throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException {

		Configuration cfg = new Configuration();

		cfg.setClassForTemplateLoading(EPCISEvents.class, "templates");

		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		Map<String, Object> input = new HashMap<String, Object>();

		input.put("timezone", initialData.getTimezone());
		input.put("gsprefix", initialData.getGsprefix());
		input.put("itemRef", initialData.getItemRef());
		input.put("creationDate", event.getCreationDate());
		EventDetail eventDetails = null;
		try {
			input.put("isA", event.getEpcisBody().getEventList().get(0).getIsA());
			eventDetails = event.getEpcisBody().getEventList().get(0);
		} catch (Exception e) {
			System.out.println("exception in template creation" + e);
		}

		input.put("uuid", eventDetails.getUuid());
		input.put("eventTime", eventDetails.getEventTime());
		input.put("parentId", eventDetails.getParentId());
		input.put("recordId", eventDetails.getRecordId());
		input.put("action", eventDetails.getAction());
		input.put("bizStep", eventDetails.getBizStep());
		input.put("disposition", eventDetails.getDisposition());
		input.put("readPoint", eventDetails.getReadPoint());
		input.put("productType", eventDetails.getProductType());
		input.put("subProductCount", eventDetails.getSubProductCount());
		input.put("subProducts", eventDetails.getChildEPCs().get(0).getSubProducts());
		input.put("epcList", eventDetails.getEpcList());
		input.put("userLocation", eventDetails.getBizlocation());
		Template template;
		if (eventDetails.getChildEPCs().isEmpty()) {
			template = cfg.getTemplate("epcis_creation.ftl");
		} else {
			template = cfg.getTemplate("epcis_aggregation.ftl");
		}

		String str;
		try (StringWriter out = new StringWriter()) {
			template.process(input, out);
			str = out.getBuffer().toString();
			out.flush();

		}
		return str;
	}

}
