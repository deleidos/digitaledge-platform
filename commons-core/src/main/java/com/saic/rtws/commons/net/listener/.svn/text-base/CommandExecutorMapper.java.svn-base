package com.saic.rtws.commons.net.listener;

import java.util.HashMap;

import com.saic.rtws.commons.net.listener.executor.*;

/**
 * Maps a particular executor class to a set of commands that can handle the client request.
 */
public final class CommandExecutorMapper {
	
	private static HashMap<String, Class<? extends CommandExecutor>> mapping =
			new HashMap<String, Class<? extends CommandExecutor>>();
	
	static {
		register(IngestProcessCommandExecutor.class, Command.START_INGEST, Command.STOP_INGEST, 
			Command.RESTART_ANY_INGEST_PROCESS, Command.RESTART_INGEST, Command.RESTART_INGEST_WITH_DEBUG);
		register(JettyProcessCommandExecutor.class, Command.START_JETTY, Command.STOP_JETTY, 
			Command.RESTART_JETTY, Command.RESTART_JETTY_WITH_DEBUG);
		register(LogCommandExecutor.class, Command.GET_LOGS_COMMAND, Command.GET_LOG_FILES_COMMAND);
		register(MasterShutdownCommandExecutor.class, Command.MASTER_SHUTDOWN);
		register(MasterStatusCommandExecutor.class, Command.MASTER_STATUS);
		register(PurgeLuceneIndexCommandExecutor.class, Command.PURGE_LUCENE_INDEX);
		register(RestartNameNodeCommandExecutor.class, Command.RESTART_NAMENODE);
		register(RestartJobTrackerCommandExecutor.class, Command.RESTART_JOBTRACKER);
		register(RestartHbaseMasterCommandExecutor.class, Command.RESTART_HBASEMASTER);
		register(RestartDataNodeCommandExecutor.class, Command.RESTART_DATANODE);
		register(RestartTaskTrackerCommandExecutor.class, Command.RESTART_TASKTRACKER);
		register(RestartRegionServerCommandExecutor.class, Command.RESTART_REGIONSERVER);
		register(RestartZookeeperCommandExecutor.class, Command.RESTART_ZOOKEEPER);
		register(ScheduledTaskCommandExecutor.class, Command.MASTER_DELETE_SCHEDULED_TASK, 
			Command.MASTER_SCHEDULE_TASK, Command.SCHEDULE_TASK, Command.DELETE_SCHEDULED_TASK);
		register(ShutdownCommandExecutor.class, Command.SHUTDOWN);
		register(TransportProcessCommandExecutor.class, Command.START_TRANSPORT, Command.STOP_TRANSPORT, 
			Command.RESTART_TRANSPORT);
		register(UpdateConfigurationCommandExecutor.class, Command.UPDATE_CONFIGURATION);
		register(UpdateNatIPTableRulesExecutor.class, Command.ADD_IPTABLE_RULE);
		register(LogStringCommandExecutor.class, Command.LOG_STRING);
		register(ProcessGroupMonitorCommandExecutor.class, Command.START_PROCESS_GROUP_MONITOR);
	}

	private CommandExecutorMapper() {}
	
	public static void register(Class<? extends CommandExecutor> clazz, String ... commands) {
		if (clazz == null || commands == null || commands.length == 0) {
			return;
		}
		
		for (String command : commands) {
			mapping.put(command, clazz);
		}
	}
	
	public static Class<? extends CommandExecutor> findExecutorClass(String command) {
		if (mapping.containsKey(command)) {
			return mapping.get(command);
		}
		
		return InvalidCommandExecutor.class;
	}
	
}