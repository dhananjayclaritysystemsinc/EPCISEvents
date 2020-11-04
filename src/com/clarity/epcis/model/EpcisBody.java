package com.clarity.epcis.model;

import java.util.List;

public class EpcisBody {

	private List<EventDetail> eventList ;

	public List<EventDetail> getEventList() {
		return eventList;
	}

	public void setEventList(List<EventDetail> eventList) {
		this.eventList = eventList;
	}
	
}
