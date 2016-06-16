package com.deleidos.rtws.commons.net.listener.client.target;

import java.util.Properties;

import net.sf.json.JSONObject;

import com.deleidos.rtws.commons.net.listener.client.CommandClient;
import com.deleidos.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.deleidos.rtws.commons.net.listener.common.ResponseBuilder;
import com.deleidos.rtws.commons.net.listener.exception.ClientException;

public class ScheduleJobCommandClient extends SingleTargetCommandClient{
	
	private static final String PROCESS_GROUP_KEY = "ProcessGroup";
	private static final String SYSTEM_KEY = "System";
	private static final String NUMBER_NODES_KEY = "NumberNodes";
	private static final String SCRIPT_NAME_KEY = "ScriptName";
	private static final String ARGUMENTS_KEY = "Arguments";
	private static final String TRIGGER_CRON_KEY = "CronTrigger";
	private static final String JOB_ID_KEY = "JobId";
	
	public static ScheduleJobCommandClient newInstance(String hostname, String [] args) {
		return new ScheduleJobCommandClient(hostname, args);
	}
	
	public static ScheduleJobCommandClient newInstance(String hostname, int port, String [] args) {
		return new ScheduleJobCommandClient(hostname, port, args);
	}
	
	private ScheduleJobCommandClient(String hostname, String [] args) {
		super(hostname, args);
	}
	
	private ScheduleJobCommandClient(String hostname, int port, String [] args) {
		super(hostname, port, args);
	}
	
	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		
		JSONObject jobDef = JSONObject.fromObject(args[0]);
		properties.put(ARGUMENTS_KEY, jobDef.getString(ARGUMENTS_KEY));
		properties.put(NUMBER_NODES_KEY, jobDef.getInt(NUMBER_NODES_KEY));
		properties.put(PROCESS_GROUP_KEY, jobDef.getString(PROCESS_GROUP_KEY));
		properties.put(SCRIPT_NAME_KEY, jobDef.getString(SCRIPT_NAME_KEY));
		properties.put(TRIGGER_CRON_KEY, jobDef.getString(TRIGGER_CRON_KEY));
		properties.put(SYSTEM_KEY, jobDef.getString(SYSTEM_KEY));
		properties.put(JOB_ID_KEY, jobDef.getLong(JOB_ID_KEY));
		
		properties.put(CommandClient.COMMAND_KEY, args[1]);
		
		return properties;
	}
	
	@Override
	public String sendCommand() throws ClientException{
		String response = super.sendCommand();
		
		if (! response.equals(ResponseBuilder.SUCCESS)) {
			throw new ClientException(String.format("Command '%s' was not successful.  Received response:  '%s'.", 
					getCommand().getProperty(CommandClient.COMMAND_KEY), response));
		}
		
		return response;
	}
}
