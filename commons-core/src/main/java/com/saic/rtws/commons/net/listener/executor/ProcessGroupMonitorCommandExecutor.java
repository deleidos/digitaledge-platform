package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.common.CommandListenerMonitorAccess;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class ProcessGroupMonitorCommandExecutor extends CommandExecutor {
	
	private static final BroadcastStatusMap status = new BroadcastStatusMap();
	
	static {
		status.registerCommand(Command.START_PROCESS_GROUP_MONITOR);
	}
	
	public static ProcessGroupMonitorCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new ProcessGroupMonitorCommandExecutor(command, request, os);
	}
	
	private ProcessGroupMonitorCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {
		
		if (this.command.equals(Command.START_PROCESS_GROUP_MONITOR)) {
			return startProcessGroupMonitor();
		}
		
		throw new ServerException("Failed to invoke command for process group monitor.");
	}
	
	private ExecuteResult startProcessGroupMonitor() throws ServerException {

		CommandListenerMonitorAccess.getInstance().startProcessGroupMonitor();
		
		return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
	}

}
