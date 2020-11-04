{
  "id": "_:document1",
  "isA": "EPCISDocument",
  "schemaVersion":2.0,
  "creationDate":"${creationDate}",
  "format":"application/ld+json",
  "epcisBody": {
   "eventList": [
     {
		  "id":"${uuid}",
		  "isA":"${isA}",
		  "action":"${action}",
		  "bizStep":"urn:epcglobal:cbv:bizstep:${bizStep}",
		  "disposition":"urn:epcglobal:cbv:disp: ${disposition}",
		  "epcList": ["urn:epc:id:sgtin:${epcList}"],
		  "eventTime":"${eventTime}",
		  "eventTimeZoneOffset":"${timezone}",
		  "readPoint": {"id": "urn:epc:id:sgln:${readPoint}"},
		  "bizLocation": {"id": "urn:epc:id:sgln:${userLocation}"},
		  
    }
   ]
  }
}
}