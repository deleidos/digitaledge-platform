package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.DataNodeProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class RestartDataNodeCommandExecutor extends CommandExecutor {

	public static RestartDataNodeCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new RestartDataNodeCommandExecutor(command, request, os);
	}

	private RestartDataNodeCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {

		if (DataNodeProcess.newInstance().restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}

		throw new ServerException("Fail to restart DataNode server.");
	}

}