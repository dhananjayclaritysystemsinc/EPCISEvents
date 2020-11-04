{
  "id": "_:document1",
  "isA": "EPCISDocument",
  "schemaVersion":2.0,
  "creationDate":"${creationDate}",
  "format":"application/ld+json",
  "epcisBody": {
   "eventList": [
     {
		  "id": "${uuid}",
		  "isA":"${isA}",
		  "eventTime":"${eventTime}",
		  "eventTimeZoneOffset":"${timezone}",
		  "parentID":"urn:epc:id:sscc:"${gsprefix}"."${recordId}",
		  "childEPCs":[<#list subProducts as subProducts>"urn:epc:id:sgtin:"${gsprefix}"."${itemRef}".${subProducts.id}"<#sep>,</#list>],
		  "action": "${action}",
		  "bizStep": "urn:epcglobal:cbv:bizstep:${bizStep}",
		  "disposition": "urn:epcglobal:cbv:disp:${disposition},
		  "readPoint": {"id": "urn:epc:id:sgln:${readPoint}"},
		  "bizLocation": {"id": "urn:epc:id:sgln:${userLocation}"},
		  
		  "childQuantityList": [
		  	{"epcClass":"urn:epc:idpat:sgtin:${productType}.*","quantity":${subProductCount}}
		  	]
    }
   ]
  }
}
}