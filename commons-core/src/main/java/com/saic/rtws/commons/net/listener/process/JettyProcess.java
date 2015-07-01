package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class JettyProcess {
	
	private static final Lock l = new ReentrantLock();
	private static final int ONE_MINUTE = 60 * 1000;
	
	private Logger logger = LogManager.getLogger(this.getClass());
	private boolean debug = false;
	private boolean restarting = false;
	
	public enum JettyStatus {
		Running, Restarting, Stopped, Unknown
	}
	
	public static JettyProcess newInstance(boolean debug) {
		return new JettyProcess(debug);
	}
	
	private JettyProcess(boolean debug) {
		this.debug = debug;
	}
	
	public boolean restart() throws ServerException {
		
		if (! SoftwareUtil.isJettyInstalled()) {
			throw new ServerException("Jetty web server is not installed on this instance.");
		}
		
		JettyStatus status = getStatusHelper();
		if (status == JettyStatus.Stopped) {
			return start();
		} else if (status == JettyStatus.Running) {
			if (l.tryLock()) {
				try {
					restarting = true;
					return stop() && start();
				} finally {
					restarting = false;
					l.unlock();
				}
			}
			
			throw new ServerException("Failed to acquire lock on the jetty web server resource.");
		} else {
			throw new ServerException("Failed to determine jetty web server status.");
		} 
		
	}
	
	public JettyStatus getStatus() throws ServerException {
		
		if (restarting) {
			return JettyStatus.Restarting;
		}
		
		return getStatusHelper();
		
	}
	
	private JettyStatus getStatusHelper() throws ServerException {
		
		if (! SoftwareUtil.isJettyInstalled()) {
			throw new ServerException("Jetty web server is not installed on this instance.");
		}
		
		JettyStatus status = JettyStatus.Unknown;
		
		String command = "/usr/bin/jps | grep start.jar";
		Process process = null;
		
		try {
			logger.debug("Executing check jetty process command '/bin/bash -c " + command + "'");

			process = new ProcessBuilder("/bin/bash", "-c", command).start();
			
			synchronized (process) {
				process.wait(ONE_MINUTE);
			}

			int exitValue = process.exitValue();

			logger.debug("Check jetty process completed with exit value " + exitValue);
			
			status = (exitValue == 0) ? JettyStatus.Running : JettyStatus.Stopped;
		} catch (Exception ex) {
			logger.error("Failed to get jetty web server status.", ex);
		}
		
		return status;
		
	}
	
	public boolean start() throws ServerException {
		
		if (! SoftwareUtil.isJettyInstalled()) {
			throw new ServerException("Jetty web server is not installed on this instance.");
		}
		
		JettyStatus status = getStatusHelper();
		if (status == JettyStatus.Running) {
			// The jetty process is already running
			return true;
		}
		
		if (l.tryLock()) {
			Process process = null;
		
			try {
				String scriptPath = getScriptPath();
			
				logger.debug("Executing start jetty command '/bin/bash -c " + scriptPath + "'.");

				process = new ProcessBuilder("/bin/bash", "-c", scriptPath)
					.directory(new File(SoftwareUtil.JETTY_BIN_DIR)).start();
			
				synchronized (process) {
					process.wait(ONE_MINUTE);
				}
			
				// Give jetty some time to startup before we start checking.
			
				try {
					Thread.sleep(5000); 
				} catch(InterruptedException ie) {
					logger.debug("Waiting for jetty to startup was interrupted.", ie);
				}
			
				for (int i = 0; i < 20; i++) {
					if (getStatusHelper() == JettyStatus.Running) {
						return true;
					}
				
					try {
						Thread.sleep(3000); 
					} catch(InterruptedException ie) {
						logger.debug("Get jetty status wait was interrupted.", ie);
					}
				}
			} catch (Exception ex) {
				logger.error("Failed to start jetty web server.", ex);
			} finally {
				l.unlock();
			}

			return false;
		}
		
		throw new ServerException("Failed to acquire lock on the jetty web server resource.");
		
	}
	
	public boolean stop() throws ServerException {
		
		if (! SoftwareUtil.isJettyInstalled()) {
			throw new ServerException("Jetty web server is not installed on this instance.");
		}
		
		JettyStatus status = getStatusHelper();
		if (status == JettyStatus.Stopped) {
			// The jetty process is already stopped
			return true;
		}
		
		if (l.tryLock()) {
			Process process = null;
		
			try {
				logger.debug("Executing stop jetty command '/bin/bash -c " + SoftwareUtil.JETTY_STOP_SCRIPT_PATH + "'.");

				process = new ProcessBuilder("/bin/bash", "-c", SoftwareUtil.JETTY_STOP_SCRIPT_PATH)
					.directory(new File(SoftwareUtil.JETTY_BIN_DIR)).start();
			
				synchronized (process) {
					process.wait(ONE_MINUTE);
				}
			
				for (int i = 0; i < 20; i++) {
					if (getStatusHelper() == JettyStatus.Stopped) {
						return true;
					}
					
					try {
						Thread.sleep(3000); 
					} catch(InterruptedException ie) {
						logger.debug("Get jetty status wait was interrupted.", ie);
					}
				}
			} catch (Exception ex) {
				logger.error("Failed to stop jetty web server.", ex);
			} finally {
				l.unlock();
			}

			return false;
		}
		
		throw new ServerException("Failed to acquire lock on the jetty web server resource.");
		
	}
	
	private String getScriptPath() {
		
		if (! debug) {
			return SoftwareUtil.JETTY_START_SCRIPT_PATH;
		} else {
			return SoftwareUtil.JETTY_START_REMOTE_DEBUG_SCRIPT_PATH;
		} 
		
	}
}