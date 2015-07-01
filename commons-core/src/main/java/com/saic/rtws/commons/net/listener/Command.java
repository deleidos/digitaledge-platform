package com.saic.rtws.commons.net.listener;

public final class Command {
	
	// Legacy commands where the command comes in plain text.
	
	public static final String MASTER_SHUTDOWN = "MASTER_SHUTDOWN";
	public static final String SHUTDOWN = "SHUTDOWN";
	
	// New commands comes via json format.
	
	public static final String ADD_IPTABLE_RULE = "ADD_IPTABLE_RULE";
	public static final String GET_LOG_FILES_COMMAND = "RETRIEVE_LOG_FILES_LISTING";
	public static final String GET_LOGS_COMMAND = "RETRIEVE_LOGS";
	public static final String MASTER_STATUS = "MASTER_STATUS";
	public static final String MASTER_DELETE_SCHEDULED_TASK = "MASTER_DELETE_SCHEDULED_TASK";
	public static final String MASTER_SCHEDULE_TASK = "MASTER_SCHEDULE_TASK";
	public static final String START_INGEST = "START_INGEST";
	public static final String STOP_INGEST = "STOP_INGEST";
	public static final String RESTART_INGEST = "RESTART_INGEST";
	public static final String RESTART_INGEST_WITH_DEBUG = "RESTART_INGEST_WITH_DEBUG";
	public static final String RESTART_ANY_INGEST_PROCESS = "RESTART_ANY_INGEST_PROCESS";
	public static final String UPDATE_CONFIGURATION = "UPDATE_CONFIGURATION";
	public static final String START_JETTY = "START_JETTY";
	public static final String STOP_JETTY = "STOP_JETTY";
	public static final String RESTART_JETTY = "RESTART_JETTY";
	public static final String RESTART_JETTY_WITH_DEBUG = "RESTART_JETTY_WITH_DEBUG";
	public static final String PURGE_LUCENE_INDEX = "PURGE_LUCENE_INDEX";
	public static final String START_TRANSPORT = "START_TRANSPORT";
	public static final String STOP_TRANSPORT = "STOP_TRANSPORT";
	public static final String RESTART_TRANSPORT = "RESTART_TRANSPORT";
	public static final String RESTART_ZOOKEEPER = "RESTART_ZOOKEEPER";
	public static final String RESTART_NAMENODE = "RESTART_NAMENODE";
	public static final String RESTART_HBASEMASTER = "RESTART_HBASEMASTER";
	public static final String RESTART_JOBTRACKER = "RESTART_JOBTRACKER";
	public static final String RESTART_DATANODE =  "RESTART_DATANODE";
	public static final String RESTART_TASKTRACKER = "RESTART_TASKTRACKER";
	public static final String RESTART_REGIONSERVER = "RESTART_REGIONSERVER";
	public static final String SCHEDULE_TASK = "SCHEDULE_TASK";
	public static final String DELETE_SCHEDULED_TASK = "DELETE_SCHEDULED_TASK";
	public static final String LOG_STRING = "LOG_STRING";
	public static final String START_PROCESS_GROUP_MONITOR = "START_PROCESS_GROUP_MONITOR";
	
	private Command() {
		// Not instantiable.
	}
	
	public static boolean isLegacyCommand(String command) {
		
		if (command.equals(Command.MASTER_SHUTDOWN) ||
			command.equals(Command.SHUTDOWN)) {
			return true;
		} 
		
		return false;
		
	}
	
}