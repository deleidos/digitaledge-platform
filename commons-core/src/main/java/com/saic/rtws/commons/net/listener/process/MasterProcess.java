package com.saic.rtws.commons.net.listener.process;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.listener.common.CommandListenerEnv;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class MasterProcess {
	
	private static final Lock l = new ReentrantLock();
	private static final int timeOut = 10000;
	
	private Logger logger = LogManager.getLogger(this.getClass());
	private int retryCount = 1;
	
	public enum MasterStatus {
		Running, Stopping, Stopped, Error, Unknown
	}
	
	public static MasterProcess newInstance() {
		return new MasterProcess();
	}
	
	private MasterProcess() {
		
		try {
			this.retryCount = RtwsConfig.getInstance().getInt("shutdown.process.retry.count", 1);
		} catch (Exception ex) {
			logger.debug("Fail to load shutdown retry count property.", ex);
		}
		
	}
	
	public MasterStatus getStatus() throws ServerException {
	
		if (! SoftwareUtil.isMasterInstalled()) {
			throw new ServerException("Master service is not installed on this instance.");
		}
		
		MasterStatus status = MasterStatus.Error;

		String command = "/usr/bin/jps | grep ClusterManager";
		Process process = null;

		try {
			logger.debug("Executing check master process command '/bin/bash -c " + command + "'");

			process = new ProcessBuilder("/bin/bash", "-c", command).start();
			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}

			int exitValue = process.exitValue();

			logger.debug("Check master process completed with exit value " + exitValue);

			if (exitValue == 0) {
				status = MasterStatus.Running;
			} else {
				status = MasterStatus.Stopped;
			}
		} catch (Exception e) {
			logger.error("Fail to get master process status.", e);
		}

		return status;
		
	}
	
	public MasterStatus stop() throws ServerException {
		
		if (! SoftwareUtil.isMasterInstalled()) {
			throw new ServerException("Master service is not installed on this instance.");
		}
		
		MasterStatus status = getStatus();
		
		if (l.tryLock()) {
			try {
				if (status == MasterStatus.Running) {
					logger.debug("Initiating master shutdown command.");

					for (int i = 1; i <= this.retryCount; i++) {
						logger.debug("Attempt number " + i + " to stop the processes that started in the manifest.");
						Process process = null;

						try {
							process = new ProcessBuilder("/etc/init.d/rtws_init.sh", "stop").start();
					
							synchronized (process) {
								process.wait(timeOut); // Give the script 10 seconds to run before exiting.
							}
					
							CommandListenerEnv.getInstance().setToShuttingDown();

							logger.debug("Master shutdown command initiated.");

							status = MasterStatus.Stopping;
						} catch (Exception e) {
							logger.error("Failed to stop master server.", e);
						}
					}
				}
			} finally {
				l.unlock();
			}
			
			return status;
		}
		
		throw new ServerException("Failed to acquire lock on the master server resource.");
		
	}
	
}