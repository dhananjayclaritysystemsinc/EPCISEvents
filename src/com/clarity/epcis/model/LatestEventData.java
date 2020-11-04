package com.clarity.epcis.model;

import java.sql.Timestamp;

public class LatestEventData {

	private String eventType;
	private String bizstep;
	private String disposition;
	private String readpoint;
	private Timestamp timestamp;
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getBizstep() {
		return bizstep;
	}
	public void setBizstep(String bizstep) {
		this.bizstep = bizstep;
	}
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	public String getReadpoint() {
		return readpoint;
	}
	public void setReadpoint(String readpoint) {
		this.readpoint = readpoint;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
