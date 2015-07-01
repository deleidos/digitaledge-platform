package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class InvalidCommandExecutor extends CommandExecutor {
	
	public static InvalidCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new InvalidCommandExecutor(command, request, os);
	}
	
	private InvalidCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}
	
	@Override
	public ExecuteResult execute() throws ServerException {
		throw new ServerException("Invalid request received. Request: " + this.request.toString());
	}
	
}