package com.clarity.epcis.model;

import java.sql.Timestamp;

public class Event {

	private String id;
	private String isA;
	private String schemaVersion;
	private Timestamp creationDate;
	private String format;
	private EpcisBody epcisBody;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsA() {
		return isA;
	}
	public void setIsA(String isA) {
		this.isA = isA;
	}
	public String getSchemaVersion() {
		return schemaVersion;
	}
	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public EpcisBody getEpcisBody() {
		return epcisBody;
	}
	public void setEpcisBody(EpcisBody epcisBody) {
		this.epcisBody = epcisBody;
	}

	
}
