package com.saic.rtws.commons.net.listener.process;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class ActiveMQProcess {
	private Logger logger = LogManager.getLogger(this.getClass());
		
	public enum ActiveMQStatus {
		Running, Stopped, Unknown
	}
	
	public static ActiveMQProcess newInstance() {
		return new ActiveMQProcess();
	}
		
	public boolean restart() throws ServerException {
		
		if (! SoftwareUtil.isActiveMQInstalled()) {
			throw new ServerException("ActiveMQ server is not installed on this instance.");
		}
		
		ActiveMQStatus status = getStatus();
			
		if (status == ActiveMQStatus.Stopped) {
			return start();
		} else if (status == ActiveMQStatus.Running) {
			return stop() && start();
		} else {
			throw new ServerException("Failed to determine ActiveMQ server status.");
		} 
		
	}
	
	public ActiveMQStatus getStatus() throws ServerException {
		
		if (! SoftwareUtil.isActiveMQInstalled()) {
			throw new ServerException("ActiveMQ server is not installed on this instance.");
		}
		
		ActiveMQStatus status = ActiveMQStatus.Unknown;

		String command = String.format("ps aux | grep %s | grep -v grep", SoftwareUtil.ACTIVEMQ_JAR_PATH);
		Process process = null;
		
		try {
			logger.debug("Executing check ActiveMQ process command '/bin/bash -c " + command + "'");
			process = new ProcessBuilder("/bin/bash", "-c", command).start();
			
			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}

			int exitValue = process.exitValue();

			logger.debug("Check ActiveMQStatus process completed with exit value " + exitValue);

			if (exitValue == 0) {
				status = ActiveMQStatus.Running;
			} else {
				status = ActiveMQStatus.Stopped;
			}
		} catch (Exception ex) {
			logger.error("Failed to get ActiveMQ server status.", ex);
		}
		
		return status;
		
	}
	
	public boolean start() throws ServerException {
		
		if (! SoftwareUtil.isActiveMQInstalled()) {
			throw new ServerException("ActiveMQ server is not installed on this instance.");
		}
		
		Process process = null;
		
		try {
			logger.debug("Executing start ActiveMQStatus command '/bin/bash -c" + SoftwareUtil.ACTIVEMQ_START_SCRIPT_PATH + "'.");

			process = new ProcessBuilder("/bin/bash", "-c", SoftwareUtil.ACTIVEMQ_START_SCRIPT_PATH)
				.directory(new File(SoftwareUtil.ACTIVEMQ_CONTROL_DIR)).start();
			
			synchronized (process) {
				process.waitFor();
			}
			
			for (int i = 0; i < 20; i++) {
				if (getStatus() == ActiveMQStatus.Running) {
					return true;
				}
				
				try {
					Thread.sleep(3000); 
				} 
				catch(InterruptedException ie) {
					logger.debug("Get ActiveMQ status wait was interrupted.", ie);
				}
			}
		} catch (Exception ex) {
			logger.error("Failed to start ActiveMQ server.", ex);
		}

		return false;
		
	}
	
	public boolean stop() throws ServerException {
		
		if (! SoftwareUtil.isActiveMQInstalled()) {
			throw new ServerException("ActiveMQ server is not installed on this instance.");
		}
		
		Process process = null;
		
		try {
			logger.debug("Executing stop ActiveMQ command '/bin/bash -c" + SoftwareUtil.ACTIVEMQ_STOP_SCRIPT_PATH + "'.");

			process = new ProcessBuilder("/bin/bash", "-c", SoftwareUtil.ACTIVEMQ_STOP_SCRIPT_PATH)
				.directory(new File(SoftwareUtil.ACTIVEMQ_CONTROL_DIR)).start();
			
			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}
			
			if (getStatus() == ActiveMQStatus.Stopped) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("Failed to stop ActiveMQ server.", ex);
		}

		
		return false;
		
	}
	
}
