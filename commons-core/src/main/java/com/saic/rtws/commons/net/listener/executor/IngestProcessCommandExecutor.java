package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.target.IngestProcessCommandClient;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.IngestProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.util.RestartAnyIngestProcessUtil;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class IngestProcessCommandExecutor extends CommandExecutor {
	
	private static final BroadcastStatusMap status = new BroadcastStatusMap();
	
	static {
		status.registerCommand(Command.RESTART_ANY_INGEST_PROCESS);
		status.registerCommand(Command.START_INGEST);
		status.registerCommand(Command.STOP_INGEST);
		status.registerCommand(Command.RESTART_INGEST);
		status.registerCommand(Command.RESTART_INGEST_WITH_DEBUG);
	}
	
	public static IngestProcessCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new IngestProcessCommandExecutor(command, request, os);
	}
	
	private IngestProcessCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}
	
	@Override
	public ExecuteResult execute() throws ServerException {
		if (canBroadcast()) {
			String [] args = { this.command };
			return broadcast(IngestProcessCommandClient.class.getName(), args, status);
		} else if (SoftwareUtil.isIngestInstalled()) {
			if (this.command.equals(Command.RESTART_ANY_INGEST_PROCESS)) {
				restartAnyIngest();
			} 
		
			if (this.command.equals(Command.START_INGEST)) {
				return startIngest();
			}
		
			if (this.command.equals(Command.STOP_INGEST)) {
				return stopIngest();
			}
		
			if (this.command.equals(Command.RESTART_INGEST)) {
				return restartIngest(false);
			}
		
			if (this.command.equals(Command.RESTART_INGEST_WITH_DEBUG)) {
				return restartIngest(true);
			}
		} else {
			throw new ServerException("This node does not have ingest installed.");
		}
		
		throw new ServerException("Failed to invoke command on ingest server.");
	}
	
	private ExecuteResult startIngest() throws ServerException {
		if (IngestProcess.newInstance(false).start()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Failed to start ingest server.");
	}
	
	private ExecuteResult stopIngest() throws ServerException {
		if (IngestProcess.newInstance(false).stop()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Failed to stop ingest server.");
	}
	
	private ExecuteResult restartAnyIngest() throws ServerException {
		if (RestartAnyIngestProcessUtil.restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Failed to restart any ingest servers.");
	}
	
	private ExecuteResult restartIngest(boolean debug) throws ServerException {
		if (IngestProcess.newInstance(debug).restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Failed to restart ingest server.");
	}
	
}