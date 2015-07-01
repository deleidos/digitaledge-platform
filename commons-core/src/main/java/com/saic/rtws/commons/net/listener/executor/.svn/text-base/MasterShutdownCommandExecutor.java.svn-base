package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.common.CommandListenerEnv;
import com.saic.rtws.commons.net.listener.common.CommandListenerMonitorAccess;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.MasterProcess;
import com.saic.rtws.commons.net.listener.process.MasterProcess.MasterStatus;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class MasterShutdownCommandExecutor extends CommandExecutor {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	public static MasterShutdownCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new MasterShutdownCommandExecutor(command, request, os);
	}
	
	private MasterShutdownCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}
	
	@Override
	public ExecuteResult execute() throws ServerException {
		
		try {
			MasterStatus status = MasterStatus.Error;
			MasterProcess master = MasterProcess.newInstance();
		
			if (! CommandListenerEnv.getInstance().isShuttingDown()) {
				status = master.stop();
				
				CommandListenerMonitorAccess.getInstance().shutdownProcessGroupMonitor();
			} else {
				logger.debug("Master shutdown already initiated.");
			
				status = master.getStatus();
				status = (status == MasterStatus.Running) ? status = MasterStatus.Stopping : status;
			}
			
			if (isLegacyCommand()) {
				return buildLegacyTerminateResult(status.toString());
			} else {
				return buildTerminateResult(ResponseBuilder.buildCustomResponse(status.toString(), null));
			}
		} catch (ServerException ex) {
			if (isLegacyCommand()) {
				return buildLegacyTerminateResult(ResponseBuilder.ERROR);

			} else {
				throw ex;
			}
		}
		
	}
	
}