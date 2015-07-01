package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class IngestProcess {
	
	private static final Lock l = new ReentrantLock();
	private static final int ONE_MINUTE = 60 * 1000;
	
	private Logger logger = LogManager.getLogger(this.getClass());
	private boolean debug = false;
	private boolean restarting = false;
	
	public enum IngestStatus {
		Running, Restarting, Stopped, Unknown
	}
	
	public static IngestProcess newInstance(boolean debug) {
		return new IngestProcess(debug);
	}
	
	private IngestProcess(boolean debug) {
		this.debug = debug;
	}
	
	public boolean restart() throws ServerException {
		
		if (! SoftwareUtil.isIngestInstalled()) {
			throw new ServerException("Ingest server is not installed on this instance.");
		}
		
		IngestStatus status = getStatusHelper();
		if (status == IngestStatus.Stopped) {
			return start();
		} else if (status == IngestStatus.Running) {
			if (l.tryLock()) {
				try {
					restarting = true;
					return stop() && start();
				} finally {
					restarting = false;
					l.unlock();
				}
			} 
			
			throw new ServerException("Failed to acquire lock on the ingest server resource.");
		} else {
			throw new ServerException("Failed to determine ingest server status.");
		} 
		
	}
	
	public IngestStatus getStatus() throws ServerException {

		if (restarting) {
			return IngestStatus.Restarting;
		}
		
		return getStatusHelper();
		
	}
	
	private IngestStatus getStatusHelper() throws ServerException {
		
		if (! SoftwareUtil.isIngestInstalled()) {
			throw new ServerException("Ingest server is not installed on this instance.");
		}
		
		IngestStatus status = IngestStatus.Unknown;
		
		String command = "/usr/bin/jps | grep IngestManager";
		Process process = null;
		
		try {
			logger.debug("Executing check ingest process command '/bin/bash -c " + command + "'");

			process = new ProcessBuilder("/bin/bash", "-c", command).start();
			
			synchronized (process) {
				process.wait(ONE_MINUTE);
			}

			int exitValue = process.exitValue();

			logger.debug("Check ingest process completed with exit value " + exitValue);

			status = (exitValue == 0) ? IngestStatus.Running : IngestStatus.Stopped;
		} catch (Exception ex) {
			logger.error("Failed to get ingest server status.", ex);
		}
		
		return status;
		
	}
	
	public boolean start() throws ServerException {
		
		if (! SoftwareUtil.isIngestInstalled()) {
			throw new ServerException("Ingest server is not installed on this instance.");
		}
		
		IngestStatus status = getStatusHelper();
		if (status == IngestStatus.Running) {
			// The ingest server is already running or is restarting.
			return true;
		}
		
		if (l.tryLock()) {
			Process process = null;
		
			try {
				String scriptPath = getScriptPath();
			
				logger.debug("Executing start ingest command '/bin/su rtws -c" + scriptPath + "'.");

				process = new ProcessBuilder("/bin/su", "rtws", "-c", scriptPath)
					.directory(new File(SoftwareUtil.INGEST_BIN_DIR)).start();
			
				synchronized (process) {
					process.wait(ONE_MINUTE);
				}
			
				for (int i = 0; i < 20; i++) {
					if (getStatusHelper() == IngestStatus.Running) {
						return true;
					}
				
					try {
						Thread.sleep(3000); 
					} catch(InterruptedException ie) {
						logger.debug("Get ingest status wait was interrupted.", ie);
					}
				}
			} catch (Exception ex) {
				logger.error("Failed to start the ingest server.", ex);
			} finally {
				l.unlock();
			}
			
			return false;
		}

		throw new ServerException("Failed to acquire lock on the ingest server resource.");
		
	}
	
	public boolean stop() throws ServerException {
		
		if (! SoftwareUtil.isIngestInstalled()) {
			throw new ServerException("Ingest server is not installed on this instance.");
		}
		
		IngestStatus status = getStatusHelper();
		if (status == IngestStatus.Stopped) {
			// The ingest process is already stopped.
			return true;
		}
		
		if (l.tryLock()) {
			Process process = null;
		
			try {
				logger.debug("Executing stop ingest command '/bin/su rtws -c" + SoftwareUtil.INGEST_STOP_SCRIPT_PATH + "'.");

				process = new ProcessBuilder("/bin/su", "rtws", "-c", SoftwareUtil.INGEST_STOP_SCRIPT_PATH)
					.directory(new File(SoftwareUtil.INGEST_BIN_DIR)).start();
			
				synchronized (process) {
					process.wait(ONE_MINUTE);
				}
			
				for (int i = 0; i < 20; i++) {
					if (getStatusHelper() == IngestStatus.Stopped) {
						return true;
					}
					
					try {
						Thread.sleep(3000); 
					} catch(InterruptedException ie) {
						logger.debug("Get ingest status wait was interrupted.", ie);
					}
				}
			} catch (Exception ex) {
				logger.error("Failed to stop the ingest server.", ex);
			} finally {
				l.unlock();
			}

			return false;
		}
		
		throw new ServerException("Failed to acquire lock on the ingest server resource.");
		
	}
	
	private String getScriptPath() {
		
		if (! debug) {
			return SoftwareUtil.INGEST_START_SCRIPT_PATH;
		} else {
			return SoftwareUtil.INGEST_START_WITH_DEBUG_SCRIPT_PATH;
		}
		
	}
	
}