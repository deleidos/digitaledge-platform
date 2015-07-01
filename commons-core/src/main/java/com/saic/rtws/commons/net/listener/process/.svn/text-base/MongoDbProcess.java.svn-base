package com.saic.rtws.commons.net.listener.process;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class MongoDbProcess {
	
	private static final Lock l = new ReentrantLock();
	private static final String SERVICE_NAME = "mongodb";
	
	private Logger logger = LogManager.getLogger(getClass());
	
	public enum MongoDbStatus {
		Running, Stopped, Unknown
	}
	
	public static MongoDbProcess newInstance() {
		return new MongoDbProcess();
	}
	
	private MongoDbProcess() {
		// Uses the newInstance() method when creating new instances.
	}
	
	public MongoDbStatus getStatus() {
		try {
			if (LinuxServiceUtil.statusService(SERVICE_NAME) != 0) {
				return MongoDbStatus.Stopped;
			} else {
				return MongoDbStatus.Running;
			}
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
		}
		return MongoDbStatus.Unknown;
	}
	
	public boolean start() throws ServerException {
		if (l.tryLock()) try {
			try {
				LinuxServiceUtil.startService(SERVICE_NAME);
				return true;
			} catch (IOException ioe) {
				logger.error(ioe.getMessage(), ioe);
			} 
			return false;
		} finally {
			l.unlock();
		}
		
		throw new ServerException("Failed to acquire lock on the MongoDb resource.");
	}
	
	public boolean stop() throws ServerException {
		if (l.tryLock()) try {
			try {
				LinuxServiceUtil.stopService(SERVICE_NAME);
				return true;
			} catch (IOException ioe) {
				logger.error(ioe.getMessage(), ioe);
			}
			return false;
		} finally {
			l.unlock();
		}
		
		throw new ServerException("Failed to acquire lock on the MongoDb resource.");
	}
	
	public boolean restart() throws ServerException {
		MongoDbStatus status = getStatus();
		if (status == MongoDbStatus.Stopped) {
			return start();
		} else if (status == MongoDbStatus.Running) {
			if (l.tryLock()) try {
				return stop() && start();
			} finally {
				l.unlock();
			}
			throw new ServerException("Failed to acquire lock on the MongoDb resource.");
		} else {
			throw new ServerException("Failed to determine MongoDb status.");
		}
	}
	
	public boolean testConnection(String hostname, int port) {
		// Uses the mongo shell to connect to the database and have it print out the currently set db
		String command = String.format("echo \"db\" | mongo --host %s --port %s --quiet", hostname, port);
		Process process = null;
		
		try {
			logger.debug("Executing test MongoDb connection command '/bin/bash -c " + command + "'");
			
			process = new ProcessBuilder("/bin/bash", "-c", command).start();
			
			synchronized (process) {
				process.wait(3000);
			}
			
			int exitValue = process.exitValue();
			
			logger.debug("Test MongoDb connection completed with exit value " + exitValue);
			
			if (exitValue == 0) return true;
		} catch (Exception ex) {
			logger.error("Failed to test MongoDb connection.", ex);
		}
		
		return false;
	}
	
}