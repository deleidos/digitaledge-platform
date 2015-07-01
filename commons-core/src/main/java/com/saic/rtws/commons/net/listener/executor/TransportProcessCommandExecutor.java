package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.target.TransportProcessCommandClient;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.TransportProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class TransportProcessCommandExecutor extends CommandExecutor {

	private static final BroadcastStatusMap status = new BroadcastStatusMap();
	
	static {
		status.registerCommand(Command.START_TRANSPORT);
		status.registerCommand(Command.STOP_TRANSPORT);
		status.registerCommand(Command.RESTART_TRANSPORT);
	}
	
	public static TransportProcessCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new TransportProcessCommandExecutor(command, request, os);
	}

	private TransportProcessCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {
		if (canBroadcast()) {
			String [] args = { this.command };
			return broadcast(TransportProcessCommandClient.class.getName(), args, status);
		} else if (SoftwareUtil.isTransportInstalled()) {
			if (this.command.equals(Command.START_TRANSPORT)) {
				return startTransport();
			}
			
			if (this.command.equals(Command.STOP_TRANSPORT)) {
				return stopTransport();
			}
			
			if (this.command.equals(Command.RESTART_TRANSPORT)) {
				return restartTransport();
			}
		} else {
			throw new ServerException("This node does not have transport installed.");
		}
		
		throw new ServerException("Failed to invoke command on transport server.");
	}
	
	private ExecuteResult startTransport() throws ServerException {
		if (TransportProcess.newInstance().start()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Failed to start transport server.");
	}
	
	private ExecuteResult stopTransport() throws ServerException {
		if (TransportProcess.newInstance().stop()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Failed to stop ingest server.");
	}
	
	private ExecuteResult restartTransport() throws ServerException {
		if (TransportProcess.newInstance().restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Failed to restart any transport server.");
	}

}
