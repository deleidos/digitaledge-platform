package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class TransportProcess {
	
	private static final Lock l = new ReentrantLock();
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	public enum TransportStatus {
		Running, Stopped, Unknown
	}
	
	public static TransportProcess newInstance() {
		return new TransportProcess();
	}
	
	public TransportStatus getStatus() throws ServerException {
	
		if (! SoftwareUtil.isTransportInstalled()) {
			throw new ServerException("Transport server is not installed on this instance.");
		}
		
		TransportStatus status = TransportStatus.Unknown;

		String command = "/usr/bin/jps | grep TransportManager";
		Process process = null;

		try {
			logger.debug("Executing check transport process command '/bin/bash -c " + command + "'");

			process = new ProcessBuilder("/bin/bash", "-c", command).start();
			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}

			int exitValue = process.exitValue();

			logger.debug("Check transport process completed with exit value " + exitValue);

			if (exitValue == 0) {
				status = TransportStatus.Running;
			} else {
				status = TransportStatus.Stopped;
			}
		} catch (Exception e) {
			logger.error("Failed to get transport server status.", e);
		}

		return status;
		
	}
	
	public boolean start() throws ServerException {
		
		if (! SoftwareUtil.isTransportInstalled()) {
			throw new ServerException("Transport server is not installed on this instance.");
		}
		
		TransportStatus status = getStatus();
		if (status == TransportStatus.Running) {
			// The transport server is already running.
			return true;
		}
		
		if (l.tryLock()) {
			Process process = null;
		
			try {			
				logger.debug("Executing start transport command '/bin/su rtws -c" + SoftwareUtil.TRANSPORT_START_SCRIPT_PATH + "'.");

				process = new ProcessBuilder("/bin/su", "rtws", "-c", SoftwareUtil.TRANSPORT_START_SCRIPT_PATH)
					.directory(new File(SoftwareUtil.TRANSPORT_BIN_DIR)).start();
			
				synchronized (process) {
					process.waitFor();
				}
			
				for (int i = 0; i < 20; i++) {
					if (getStatus() == TransportStatus.Running) {
						return true;
					}
				
					try {
						Thread.sleep(3000); // Let the script finish running before we get the exit value.
					} catch(InterruptedException ie) {
						logger.debug("Get transport status wait was interrupted.", ie);
					}
				}
			} catch (Exception ex) {
				logger.error("Failed to start the transport server.", ex);
			} finally {
				l.unlock();
			}
			
			return false;
		}

		throw new ServerException("Failed to acquire lock on the transport server resource.");
		
	}
	
	public boolean stop() throws ServerException {
		
		if (! SoftwareUtil.isTransportInstalled()) {
			throw new ServerException("Transport server is not installed on this instance.");
		}
		
		TransportStatus status = getStatus();
		if (status == TransportStatus.Stopped) {
			// The transport server is already stopped.
			return true;
		}
		
		if (l.tryLock()) {
			Process process = null;
		
			try {
				logger.debug("Executing stop transport command '/bin/su rtws -c" + SoftwareUtil.TRANSPORT_STOP_SCRIPT_PATH + "'.");

				process = new ProcessBuilder("/bin/su", "rtws", "-c", SoftwareUtil.TRANSPORT_STOP_SCRIPT_PATH)
					.directory(new File(SoftwareUtil.TRANSPORT_BIN_DIR)).start();
			
				synchronized (process) {
					process.waitFor(); // Let the script finish running before we get the exit value.
				}
			
				for (int i = 0; i < 20; i++) {
					if (getStatus() == TransportStatus.Stopped) {
						return true;
					}
				
					try {
						Thread.sleep(3000); // Let the script finish running before we get the exit value.
					} catch(InterruptedException ie) {
						logger.debug("Get transport status wait was interrupted.", ie);
					}
				}
			} catch (Exception ex) {
				logger.error("Failed to stop the transport server.", ex);
			} finally {
				l.unlock();
			}
			
			return false;
		}

		
		throw new ServerException("Failed to acquire lock on the transport server resource.");
		
	}

	public boolean restart() throws ServerException {
		
		if (! SoftwareUtil.isTransportInstalled()) {
			throw new ServerException("Transport server is not installed on this instance.");
		}
		
		TransportStatus status = getStatus();
		if (status == TransportStatus.Stopped) {
			return start();
		} else if (status == TransportStatus.Running) {
			if (l.tryLock()) {
				try {
					return stop() && start();
				} finally {
					l.unlock();
				}
			} 
			
			throw new ServerException("Failed to acquire lock on the transport server resource.");
		} else {
			throw new ServerException("Failed to determine transport server status.");
		}
		
	}

}
