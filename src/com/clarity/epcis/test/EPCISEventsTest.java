package com.clarity.epcis.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;

import com.clarity.epcis.EPCISEvents;
import com.clarity.epcis.model.*;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;


public class EPCISEventsTest {

	@Test
	public void EPCISEventsTest() throws TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException, JSONException {
		InitializeData iData = new InitializeData();
		iData.setGsprefix("67890");
		iData.setItemRef("8908");
		iData.setTimezone("+5:30");
		String userName = "gurgaon17@mdh.com";
		assertEquals(true, new EPCISEvents().initializeEPCISEvents(iData,userName));
		ProductData productData = getProductData();
		assertEquals(true, new EPCISEvents().publishEPCISEvents(productData, null));
		ContainerData containerData = getContainerData();
		assertEquals(true, new EPCISEvents().publishEPCISEvents(containerData, null));
	}

	private static ProductData getProductData() {
		ProductData productData = new ProductData();
		productData.setProductCode("p08091653");
		productData.setRecordId("p08091653");
		productData.setRecordType("ayurvedicitemv2");
		productData.setStatus("Containerized");
		productData.setStatusUpdatedAt(Long.valueOf("1599564299407"));
		productData.setSubtype("subtype");
		List<CheckIn> checkInList = new ArrayList<CheckIn>();

		CheckIn checkIn = new CheckIn();
		checkIn.setTimestamp(Long.valueOf("1599569832956"));
		checkIn.setUser("INORBIT VADODARA");
		checkInList.add(checkIn);

		CheckIn checkIn1 = new CheckIn();
		checkIn1.setTimestamp(Long.valueOf("1599579982817"));
		checkIn1.setUser("GK-M");
		checkInList.add(checkIn1);

		checkIn.setTimestamp(Long.valueOf("1599582620830"));
		checkIn.setUser("GK-M");
		checkInList.add(checkIn);
		productData.setCheckin(checkInList);

		List<CheckOut> checkOutList = new ArrayList<CheckOut>();
		CheckOut checkOut = new CheckOut();
		checkOut.setCheckoutTo("End User");
		checkOut.setTimestamp(Long.valueOf("1599582658945"));
		checkOut.setUser("GK-M");
		checkOutList.add(checkOut);

		CheckOut checkOut1 = new CheckOut();
		checkOut1.setCheckoutTo("INORBIT VADODARA");
		checkOut1.setTimestamp(Long.valueOf("1599565965719"));
		checkOut1.setUser("Gurgaon");
		checkOutList.add(checkOut1);

		CheckOut checkOut2 = new CheckOut();
		checkOut2.setCheckoutTo("GK-M");
		checkOut2.setTimestamp(Long.valueOf("1599579840371"));
		checkOut2.setUser("INORBIT VADODARA");
		checkOutList.add(checkOut2);

		productData.setCheckout(checkOutList);

		return productData;
	}

	private static ContainerData getContainerData() {
		ContainerData containerData = new ContainerData();
		containerData.setRecordId("c08091652");
		containerData.setProductCode("c08091652");
		containerData.setProducttype("producttype");
		containerData.setRecordType("containerv2");
		containerData.setStatus("status");
		containerData.setStatusUpdatedAt(Long.valueOf("1599582658945"));

		List<CheckIn> checkInList = new ArrayList<CheckIn>();

		CheckIn checkIn = new CheckIn();
		checkIn.setTimestamp(Long.valueOf("1599569832956"));
		checkIn.setUser("INORBIT VADODARA");
		checkInList.add(checkIn);

		CheckIn checkIn1 = new CheckIn();
		checkIn1.setTimestamp(Long.valueOf("1599579982817"));
		checkIn1.setUser("GK-M");
		checkInList.add(checkIn1);

		checkIn.setTimestamp(Long.valueOf("1599582620830"));
		checkIn.setUser("GK-M");
		checkInList.add(checkIn);
		containerData.setCheckin(checkInList);

		List<CheckOut> checkOutList = new ArrayList<CheckOut>();
		CheckOut checkOut = new CheckOut();
		checkOut.setCheckoutTo("End User");
		checkOut.setTimestamp(Long.valueOf("1599582658945"));
		checkOut.setUser("GK-M");
		checkOutList.add(checkOut);

		CheckOut checkOut1 = new CheckOut();
		checkOut1.setCheckoutTo("INORBIT VADODARA");
		checkOut1.setTimestamp(Long.valueOf("1599565965719"));
		checkOut1.setUser("Gurgaon");
		checkOutList.add(checkOut1);

		CheckOut checkOut2 = new CheckOut();
		checkOut2.setCheckoutTo("GK-M");
		checkOut2.setTimestamp(Long.valueOf("1599579840371"));
		checkOut2.setUser("INORBIT VADODARA");
		checkOutList.add(checkOut2);

		containerData.setCheckout(checkOutList);

		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		SubProduct subProduct = new SubProduct();
		subProduct.setId("p08091652");
		subProducts.add(subProduct);
		SubProduct subProduct1 = new SubProduct();
		subProduct1.setId("p08091653");
		subProducts.add(subProduct1);
		containerData.setSubProducts(subProducts);
		return containerData;
	}
}
