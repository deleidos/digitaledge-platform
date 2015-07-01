package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.target.JettyProcessCommandClient;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.JettyProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public class JettyProcessCommandExecutor extends CommandExecutor {
	
	private static final BroadcastStatusMap status = new BroadcastStatusMap();
	
	static {
		status.registerCommand(Command.START_JETTY);
		status.registerCommand(Command.STOP_JETTY);
		status.registerCommand(Command.RESTART_JETTY);
		status.registerCommand(Command.RESTART_JETTY_WITH_DEBUG);
	}
	
	public static JettyProcessCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new JettyProcessCommandExecutor(command, request, os);
	}
	
	private JettyProcessCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}
	
	@Override
	public ExecuteResult execute() throws ServerException {
		
		if (canBroadcast()) {
			String [] args = { this.command };
			return broadcast(JettyProcessCommandClient.class.getName(), args, status);
		} else if (SoftwareUtil.isJettyInstalled()) {
			if (this.command.equals(Command.START_JETTY)) {
				return startJetty();
			}
		
			if (this.command.equals(Command.STOP_JETTY)) {
				return stopJetty();
			}
		
			if (this.command.equals(Command.RESTART_JETTY)) {
				return restartJetty();
			}	
		
			if (this.command.equals(Command.RESTART_JETTY_WITH_DEBUG)) {
				return restartJettyWithDebug();
			}
		} else {
			throw new ServerException("This node does not have jetty installed.");
		}
		
		throw new ServerException("Failed to invoke comand on jetty web server.");
		
	}
	
	private ExecuteResult startJetty() throws ServerException {
		if (JettyProcess.newInstance(false).start()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Fail to restart jetty web server.");
	}
	
	private ExecuteResult stopJetty() throws ServerException {
		if (JettyProcess.newInstance(false).stop()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Fail to restart jetty web server.");
	}
	
	private ExecuteResult restartJetty() throws ServerException {
		if (JettyProcess.newInstance(false).restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Fail to restart jetty web server.");
	}
	
	private ExecuteResult restartJettyWithDebug() throws ServerException {
		if (JettyProcess.newInstance(true).restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		
		throw new ServerException("Fail to restart jetty web server with debug.");
	}
	
}