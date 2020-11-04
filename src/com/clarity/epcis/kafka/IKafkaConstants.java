package com.clarity.epcis.kafka;


public interface IKafkaConstants {
	//TO-DO : need to be initialize
	public static String KAFKA_BROKERS = "localhost:9092";
	
	public static Integer MESSAGE_COUNT=10;
	
	public static String CLIENT_ID="client1";
	
	public static String GROUP_ID_CONFIG="consumerGroupEPCIS";
	
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT=1;
	
	public static String OFFSET_RESET_LATEST="latest";
	
	public static String OFFSET_RESET_EARLIER="earliest";
	
	public static Integer MAX_POLL_RECORDS=1;
}
