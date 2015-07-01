package com.saic.rtws.commons.net.listener.model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.jersey.config.JerseyClientConfig;
import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.target.ScheduleJobCommandClient;
import com.saic.rtws.commons.net.listener.exception.ScheduleException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public enum ScheduleClient {
	INSTANCE;

	private Scheduler scheduler;
	private Map<ScheduleJob, JobTriggerHolder> currentJobs;
	private Logger logger;
	private Client client;
	private Matcher nodeMatcher;
	
	public boolean startup() throws SchedulerException {
		logger = Logger.getLogger(getClass());

		nodeMatcher = Pattern.compile(".+?(\\d+)").matcher("");

		client = Client.create(JerseyClientConfig.getInstance().getInternalConfig());

		currentJobs = new HashMap<ScheduleJob, JobTriggerHolder>();

		scheduler = StdSchedulerFactory.getDefaultScheduler();

		// and start it off
		scheduler.start();

		try{
			List<ScheduleJob> jobList = getInitialJobList();
			for (ScheduleJob job : jobList){
				try{
					schedule(job);
				}catch(Exception e){
					logger.error("Error starting scheduler - an initial job could not be scheduled.  Will continue starting...",e);
				}
			}
			
			return true;
		}catch(Exception e){
			logger.error("Error starting scheduler - Could not get initial job list.  Scheduler will not be enabled.",e);
			scheduler.shutdown();
			scheduler=null;
		}
		
		return false;
	}

	public void stop() throws SchedulerException {
		if (scheduler != null)
			scheduler.shutdown();
	}

	public void schedule(ScheduleJob job) throws SchedulerException, ScheduleException {
		if (scheduler != null) {
			try{
				downloadScript(job.getScriptName());
			}catch(Exception e){
				throw new ScheduleException("Could not download script",e);
			}
			JobTriggerHolder hldr = new JobTriggerHolder();
			JobDetail jobDetail = JobBuilder.newJob(ScriptJob.class).withIdentity(buildName(job), "DE Scheduled Tasks")
					.usingJobData("script", job.getScriptName()).usingJobData("args", job.getArguments()).build();
			hldr.jobDetail = jobDetail;
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(buildName(job))
					.withSchedule(CronScheduleBuilder.cronSchedule(job.getTriggerCron()))
					.forJob(buildName(job), "DE Scheduled Tasks").build();
			hldr.trigger = trigger;
			scheduler.scheduleJob(jobDetail, trigger);
			currentJobs.put(job, hldr);
		}
	}
	
	public void scheduleMaster(ScheduleJob job) throws SchedulerException, ScheduleException{
		if(job.getProcessGroup().trim().equalsIgnoreCase("master")){
			schedule(job);
		}else{
			//it goes to a child node (and, if '*', goes here as well
			List<String> hosts = getRelevantChildren(job);
			sendToChildren(hosts,job,Command.SCHEDULE_TASK);
		}
	}
	
	public void unScheduleMaster(ScheduleJob job) throws SchedulerException, ScheduleException{
		if(job.getProcessGroup().equalsIgnoreCase("master")){
			unSchedule(job);
		}else{
			//it goes to a child node
			List<String> hosts = getRelevantChildren(job);
			sendToChildren(hosts,job,Command.DELETE_SCHEDULED_TASK);
		}
	}

	private void sendToChildren(List<String> hosts, ScheduleJob job, String command) throws ScheduleException {
		String[] args = new String[2];
		args[0] = job.toJSON().toString();
		args[1] = command;
		
		for(String host : hosts){
			try{
			ScheduleJobCommandClient client = ScheduleJobCommandClient.newInstance(host, args);
			client.sendCommand();
			}catch(Exception e){
				throw new ScheduleException("Could not schedule job",e);
			}
		}
	}

	public void unSchedule(ScheduleJob job) throws SchedulerException {
		if (scheduler != null) {
			if (currentJobs.containsKey(job)) {
				JobTriggerHolder hldr = currentJobs.get(job);
				scheduler.unscheduleJob(hldr.trigger.getKey());
				currentJobs.remove(job);
			} else {
				logger.error("Attempted to unSchedule nonexistent job.");
			}
			boolean found=false;
			String script = job.getScriptName();
			for(ScheduleJob existingJob : currentJobs.keySet()){
				if(existingJob.getScriptName().equalsIgnoreCase(script))
					found=true;
			}
			if(!found){
				//no jobs are now using this script
				if(!deleteScript(script)){
					logger.error("Error deleting script from this node.  Script may be in use.");
					//don't throw it up, we don't want to have this fail here, I think
				}
			}
		}
	}

	public List<ScheduleJob> getInitialJobList() throws ScheduleException{
		//TODO
		logger.info("Getting initial job list...");
		String gwPath = RtwsConfig.getInstance().getString("webapp.scheduleapi.url.path");
		String url = gwPath+"/rest/scheduler/list/jobs/%s/%s/%s";
				//should be url,system,process,node
		String fqdn = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_FQDN);
		
		boolean isGateway = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_IS_GATEWAY)!=null;
		
		
		String host = RtwsConfig.getInstance().getString("tenant.gateway.host");
		String system = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_DOMAIN);
		String process = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_PROCESS_GROUP);
		
		if(fqdn==null || fqdn.isEmpty()){
			//it's a master node
			fqdn = "master."+system;
			process = "master";
		}
		
		String hostPart = fqdn.substring(0,fqdn.indexOf('.'));
		String node = extractNodeNumber(hostPart);
		
		if(isGateway){
			host = "localhost";
			system = "gateway";
			process = "gateway";
			node = "0";
		}

		String jobsUrl = String.format(url, system, process, node);
		int retryCount = 0;
		int maxRetries = 10;
		Exception savedException = null;
		logger.info("Connecting to: " + jobsUrl);
		List<ScheduleJob> jobsToSchedule = new ArrayList<ScheduleJob>();
		while (retryCount++ < maxRetries) {
			try {
				String jobs = client.resource(jobsUrl).get(String.class);
				JSONArray jobList = JSONArray.fromObject(jobs);
				logger.info("Got " + jobList.size() + " initial jobs.");
				for (int i = 0; i < jobList.size(); i++) {
					JSONObject thisJob = jobList.getJSONObject(i);
					logger.info("Initial job: " + thisJob.toString());
					jobsToSchedule.add(new ScheduleJob(thisJob));
				}
				//success
				savedException=null;
				break;
			} catch (Exception e) {
				// there was an error - but it could just be slow startup, so
				// we'll save the error and re-throw it if we've run out of retries
				savedException = e;
				logger.warn(e);
				try {
					Thread.sleep(30000);//wait half a minute to let the rest of the software catch up.
				} catch (InterruptedException e1) { }//nop
			}
		}
		if(savedException!=null){
			throw new ScheduleException("Error downloading initial scheduled jobs.",savedException);
		}

		return jobsToSchedule;
	}

	private String extractNodeNumber(String host) {
		nodeMatcher.reset(host);
		String node;
		if(nodeMatcher.matches()){
			node=nodeMatcher.group(nodeMatcher.groupCount());
		}else{
			node = "0";
		}
		return node;
	}

	private String buildName(ScheduleJob job) {
		return job.getSystem() + "-" + job.getProcessGroup() + "-" + job.getScriptName() + "-" + job.getArguments()
				+ "-" + job.getTriggerCron();
	}

	private synchronized void downloadScript(String scriptName) throws Exception {
		File dir = ScriptJob.scriptDirectory;
		File script = new File(dir.getAbsolutePath() + File.separatorChar + scriptName);

		if (!script.exists()) {
			String repoURL = RtwsConfig.getInstance().getString("webapp.repository.url.path");
			String relativePath = "%s/rest/content/retrieve/%s/public/scripts?userId=%s";
			String tenantName = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_TENANT_ID);
			String url = String.format(relativePath, repoURL, scriptName, tenantName);
			WebResource res = client.resource(url);
			String scriptText=null;
			int tries=0;
			while(scriptText==null && tries<10){
				try{
					tries++;
					scriptText = res.get(String.class);
				}catch(Exception e){
					logger.warn("Could not download script.  Repository may not be ready.  Retrying...");
					Thread.sleep(1000*60);
				}
				
			}
			if(scriptText==null)
				throw new Exception("Could not download script.  Error contacting script repository.");
			
			scriptText = scriptText.replaceAll("\r\n", "\n");

			dir.mkdirs();

			if (script.createNewFile()) {
				FileOutputStream fs = new FileOutputStream(script);
				fs.write(scriptText.getBytes());
				fs.flush();
				fs.close();
			} else {
				logger.warn("Unable to create script file, and file does not exist.  Check permissions.");
			}
		}
	}
	
	private synchronized boolean deleteScript(String scriptName){
		File dir = ScriptJob.scriptDirectory;
		File script = new File(dir.getAbsolutePath() + File.separatorChar + scriptName);
		if(script.exists()){
			return script.delete();
		}//else, it doesn't exist, so it's deleted, sort-of
		return true;
	}

	private class JobTriggerHolder {
		public JobDetail jobDetail;
		public Trigger trigger;
	}
	
	private List<String> getRelevantChildren(ScheduleJob job){
		List<String> children = new ArrayList<String>();
		
		int numNodes = job.getNumNodes();
		WebResource resource = client.resource(RtwsConfig.getInstance().getString("webapp.repository.url.path"));
		resource = resource.path("json/process/retrieve/all");
		String nodeInfo = resource.get(String.class);
		JSONArray nodes = JSONObject.fromObject(nodeInfo).getJSONArray("node");
		JSONObject node;
		for(int i=0;i<nodes.size();i++){
			node = nodes.getJSONObject(i);
			if(job.getProcessGroup().equals("*") || node.getString("group").equalsIgnoreCase(job.getProcessGroup())){
				//same group, do we use it or not?  depends on the node number, based on host
				String nodeNumber = extractNodeNumber(node.getString("host"));
				int nodeNum = Integer.parseInt(nodeNumber);
				if (numNodes <= nodeNum) {
					children.add(node.getString("privateIp"));
				}
			}
		}
		
		return children;
	}
}
