package com.clarity.epcis.model;

import java.sql.Timestamp;
import java.util.List;

public class EventDetail {

	private String uuid;
	private String isA;
	private Timestamp eventTime;
	private String eventTimeZoneOffset;
	private String parentId;
	private String recordId;
	private List<ChildEPCs> childEPCs;
	private String epcList;
	private String action;
	private String bizStep;
	private String disposition;
	private String readPoint;
	private String bizlocation;
	private String productType;
	private int subProductCount;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getIsA() {
		return isA;
	}
	public void setIsA(String isA) {
		this.isA = isA;
	}
	public Timestamp getEventTime() {
		return eventTime;
	}
	public void setEventTime(Timestamp eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventTimeZoneOffset() {
		return eventTimeZoneOffset;
	}
	public void setEventTimeZoneOffset(String eventTimeZoneOffset) {
		this.eventTimeZoneOffset = eventTimeZoneOffset;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public List<ChildEPCs> getChildEPCs() {
		return childEPCs;
	}
	public void setChildEPCs(List<ChildEPCs> childEPCs) {
		this.childEPCs = childEPCs;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getBizStep() {
		return bizStep;
	}
	public void setBizStep(String bizStep) {
		this.bizStep = bizStep;
	}
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	public String getReadPoint() {
		return readPoint;
	}
	public void setReadPoint(String readPoint) {
		this.readPoint = readPoint;
	}
	public String getBizlocation() {
		return bizlocation;
	}
	public void setBizlocation(String bizlocation) {
		this.bizlocation = bizlocation;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public int getSubProductCount() {
		return subProductCount;
	}
	public void setSubProductCount(int subProductCount) {
		this.subProductCount = subProductCount;
	}
	public String getEpcList() {
		return epcList;
	}
	public void setEpcList(String epcList) {
		this.epcList = epcList;
	}
}

