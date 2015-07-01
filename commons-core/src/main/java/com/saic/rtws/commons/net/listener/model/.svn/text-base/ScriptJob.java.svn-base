package com.saic.rtws.commons.net.listener.model;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.saic.rtws.commons.process.log.ProcessStream;

public class ScriptJob implements Job{
	
	public static File scriptDirectory = new File("/tmp/DigitalEdgeScripts/");
	private static Logger log = Logger.getLogger(ScriptJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String script = context.getMergedJobDataMap().getString("script");
		String args = context.getMergedJobDataMap().getString("args");
		
		
		try {
			
			File scriptFile = new File(scriptDirectory.getCanonicalPath()+File.separatorChar+script);
			scriptFile.setExecutable(true);
			
			ProcessBuilder procBuild = new ProcessBuilder("/bin/bash", "-c", String.format("%s %s", scriptFile.getCanonicalPath(), args)).directory(scriptDirectory).redirectErrorStream(true);
			
			log.info("starting script job: \""+script+"\" with arguments \""+args+"\"");
			Process p = procBuild.start();
			new ProcessStream(p, log);//consume and log the output and error streams
			p.waitFor();
			log.info("script job \""+script+"\" with arguments \""+args+"\" finished with exit value "+p.exitValue()+".");
			
		} catch (IOException e) {
			log.error("IOException executing script job \""+script+"\" with arguments \""+args+"\"",e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("InterruptedException executing script job \""+script+"\" with arguments \""+args+"\"",e);
			e.printStackTrace();
		}
	}

}
