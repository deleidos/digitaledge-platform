package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.HbaseMasterProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class RestartHbaseMasterCommandExecutor extends CommandExecutor {

	public static RestartHbaseMasterCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new RestartHbaseMasterCommandExecutor(command, request, os);
	}

	private RestartHbaseMasterCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {

		if (HbaseMasterProcess.newInstance().restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}

		throw new ServerException("Fail to restart HbaseMaster server.");
	}

}