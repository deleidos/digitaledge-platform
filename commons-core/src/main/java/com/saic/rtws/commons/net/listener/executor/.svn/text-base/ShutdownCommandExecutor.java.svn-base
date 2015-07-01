package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.listener.common.CommandListenerMonitorAccess;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class ShutdownCommandExecutor extends CommandExecutor {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	private int retryCount = 1;
	private int timeOut = 5 * 60 * 1000;
	
	public static ShutdownCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new ShutdownCommandExecutor(command, request, os);
	}
	
	private ShutdownCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}
	
	@Override
	public ExecuteResult execute() throws ServerException {
		
		if (SoftwareUtil.isMasterInstalled()) {
			throw new ServerException("The instance shutdown command can not be invoked on the master, uses the master shutdown command instead.");
		}
		
		loadProperties();
		
		for (int i = 1; i <= this.retryCount; i++) {
			logger.debug("Attempt number " + i + " to stop the processes that started in the manifest.");
			
			if (shutdown()) {
				return handleShutdownSuccess();
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException ie) {
				logger.debug("Fail to wait 3 seconds before retrying shutdown command.", ie);
			}
		}
		
		return handleShutdownError();
		
	}
	
	private void loadProperties() {
		
		try {
			this.retryCount = RtwsConfig.getInstance().getInt("shutdown.process.retry.count");
		} catch (Exception ex) {
			logger.debug("Fail to load shutdown retry count property.", ex);
		}

		try {
			this.timeOut = RtwsConfig.getInstance().getInt("shutdown.process.timeout.value");
			this.timeOut = timeOut * 60 * 1000; // The property is in minutes, so we convert it here
		} catch (Exception ex) {
			logger.debug("Fail to load shutdown timeout property.", ex);
		}
		
	}

	private void waitForProcessWithTimeout(Process process, final int requestedTimeout)
	{
		int timeout = requestedTimeout;
		assert(process != null);
		synchronized (process) {
			long started = System.currentTimeMillis();
			while(timeout > 0)
			{
				// wait for process to complete or timeout
				try {
					process.wait(timeout);					
				} catch ( InterruptedException ie) {
					logger.warn("Wait was interrupted", ie);
				}
				// try getting exit value of this process. We will get an exception if the process is still running
				try {
					process.exitValue();								// process exited, ignore returned value
					return;												// done waiting
				} catch (IllegalThreadStateException itse ) {
					// the process is still running, ignore this
				}
				// the process is still running...
				// check remaining timeout in case we got interrupted
				long current = System.currentTimeMillis();
				if ( current >= started + timeout*1000 )
					break;												// process timed out
				// we got interrupted, recalculate remaining time to wait
				timeout -= (int)Math.ceil((current - started)/1000.);	// calculate remaining wait time
				assert(timeout > 0);
				logger.warn("Wait again for " + timeout + " second(s) more.");
			}
		}
		logger.warn("Wait timed out after " + requestedTimeout + " second(s).");
	}
	
	private boolean shutdown() {
		
		Process process = null;
		
		try {
			CommandListenerMonitorAccess.getInstance().shutdownProcessGroupMonitor();
			
			process = new ProcessBuilder("/etc/init.d/rtws_init.sh", "stop").start();
			
			synchronized (process) {
				waitForProcessWithTimeout(process, timeOut);
			}
			
			return true;
		} catch (Exception ex) {
			logger.error("Fail to shutdown instance.", ex);
		} finally {
			try {
				if (process != null) {
					process.destroy();
				}
			} catch (Exception ex) {
				logger.error("Fail to destroy shutdown process.", ex);
			}
		}
		
		return false;
		
	}
	
	private ExecuteResult handleShutdownSuccess() {
		
		if (isLegacyCommand()) {
			return buildLegacyTerminateResult(ResponseBuilder.SUCCESS);
		} else {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
	}
	
	private ExecuteResult handleShutdownError() throws ServerException {
		
		if (isLegacyCommand()) {
			return buildLegacyTerminateResult(ResponseBuilder.ERROR);
		} else {
			throw new ServerException(
					"Unable to complete instance shutdown after maximum number of retry attempts: " + this.retryCount);
		}
		
	}
	
}