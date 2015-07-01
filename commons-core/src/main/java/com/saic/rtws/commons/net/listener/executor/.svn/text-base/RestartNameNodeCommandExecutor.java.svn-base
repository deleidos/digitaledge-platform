package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.NameNodeProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class RestartNameNodeCommandExecutor extends CommandExecutor {

	public static RestartNameNodeCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new RestartNameNodeCommandExecutor(command, request, os);
	}

	private RestartNameNodeCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {

		if (NameNodeProcess.newInstance().restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}

		throw new ServerException("Fail to restart NameNode server.");
	}

}