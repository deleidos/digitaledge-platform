package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.TaskTrackerProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class RestartTaskTrackerCommandExecutor extends CommandExecutor {

	public static RestartTaskTrackerCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new RestartTaskTrackerCommandExecutor(command, request, os);
	}

	private RestartTaskTrackerCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {

		if (TaskTrackerProcess.newInstance().restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}

		throw new ServerException("Fail to restart TaskTracker server.");
	}

}