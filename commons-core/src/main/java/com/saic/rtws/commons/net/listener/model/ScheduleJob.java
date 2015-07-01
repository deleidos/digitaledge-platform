package com.saic.rtws.commons.net.listener.model;

import net.sf.json.JSONObject;

import org.quartz.CronScheduleBuilder;

import com.saic.rtws.commons.net.listener.exception.ScheduleException;

public class ScheduleJob {
	
	//JSON Keys
	private static final String PROCESS_GROUP_KEY = "ProcessGroup";
	private static final String SYSTEM_KEY = "System";
	private static final String NUMBER_NODES_KEY = "NumberNodes";
	private static final String SCRIPT_NAME_KEY = "ScriptName";
	private static final String ARGUMENTS_KEY = "Arguments";
	private static final String TRIGGER_CRON_KEY = "CronTrigger";
	private static final String JOB_ID_KEY = "JobId";

	//run on
	private String system;
	private String processGroup;
	private int numNodes;
	
	//task to run
	private String scriptName;
	private String arguments;
	
	//trigger
	private String triggerCron;
	
	//database id
	private long id;
	
	public ScheduleJob(String strDef) throws ScheduleException{
		JSONObject def=null;
		try{
			def = JSONObject.fromObject(strDef);
		}catch(Exception e){
			throw new ScheduleException("Invalid JSON job format.");
		}
		processGroup = def.optString(PROCESS_GROUP_KEY, "");
		system = def.optString(SYSTEM_KEY,"");
		numNodes = def.optInt(NUMBER_NODES_KEY, -1);
		scriptName = def.optString(SCRIPT_NAME_KEY,"");
		arguments = def.optString(ARGUMENTS_KEY, "");
		triggerCron = def.optString(TRIGGER_CRON_KEY, "");
		id = def.optLong(JOB_ID_KEY, 0);
		
		try{
			CronScheduleBuilder.cronSchedule(triggerCron);
		}catch(Exception e){
			throw new ScheduleException("Invalid Cron trigger expression.");
		}
		
		if(	numNodes<0 ||
				system.isEmpty() ||
				processGroup.isEmpty() ||
				scriptName.isEmpty() ||
				triggerCron.isEmpty()){
			throw new ScheduleException("Incomplete schedule format JSON",def);
		}
				
	}
	
	public String getProcessGroup(){
		return processGroup;
	}
	public int getNumNodes(){
		return numNodes;
	}
	public String getScriptName(){
		return scriptName;
	}
	public String getArguments(){
		return arguments;
	}
	public String getTriggerCron(){
		return triggerCron;
	}
	public long getId(){
		return id;
	}
	public String getSystem(){
		return system;
	}
	
	public ScheduleJob(long id, String sys, String pGroup,int nodes, String script, String args, String trigger){
		processGroup = pGroup;
		system = sys;
		numNodes = nodes;
		scriptName = script;
		arguments = args;
		triggerCron = trigger;
		this.id=id;
	}
	
	public ScheduleJob(JSONObject def) throws ScheduleException{
		processGroup = def.optString(PROCESS_GROUP_KEY, "");
		system = def.optString(SYSTEM_KEY,"");
		numNodes = def.optInt(NUMBER_NODES_KEY, -1);
		scriptName = def.optString(SCRIPT_NAME_KEY,"");
		arguments = def.optString(ARGUMENTS_KEY, "");
		triggerCron = def.optString(TRIGGER_CRON_KEY, "");
		id = def.optLong(JOB_ID_KEY, 0);
		if(	numNodes<0 ||
				system.isEmpty() ||
				processGroup.isEmpty() ||
				scriptName.isEmpty() ||
				triggerCron.isEmpty()){
			throw new ScheduleException("Incomplete schedule format JSON",def);
		}
	}

	public JSONObject toJSON(){
		JSONObject obj = new JSONObject();
		obj.put(ARGUMENTS_KEY, arguments);
		obj.put(NUMBER_NODES_KEY, numNodes);
		obj.put(PROCESS_GROUP_KEY, processGroup);
		obj.put(SCRIPT_NAME_KEY, scriptName);
		obj.put(TRIGGER_CRON_KEY, triggerCron);
		obj.put(SYSTEM_KEY, system);
		obj.put(JOB_ID_KEY, id);
		return obj;
		
	}
	
	@Override
	public int hashCode(){
		StringBuilder bld = new StringBuilder(arguments);
		bld.append(numNodes).append(processGroup).append(scriptName).append(triggerCron).append(system);
		return bld.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ScheduleJob){
			ScheduleJob job = (ScheduleJob)obj;
				return arguments.equals(job.arguments) && 
						numNodes==job.numNodes && 
						processGroup.equals(processGroup) && 
						scriptName.equals(job.scriptName) &&
						triggerCron.equals(job.triggerCron) &&
						system.equals(job.system);
		}
		return false;	
	}
	
}
