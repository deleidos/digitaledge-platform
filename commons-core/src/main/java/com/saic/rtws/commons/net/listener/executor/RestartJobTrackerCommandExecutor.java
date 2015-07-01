package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.JobTrackerProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class RestartJobTrackerCommandExecutor extends CommandExecutor {

	public static RestartJobTrackerCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new RestartJobTrackerCommandExecutor(command, request, os);
	}

	private RestartJobTrackerCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {

		if (JobTrackerProcess.newInstance().restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}

		throw new ServerException("Fail to restart JobTracker server.");
	}

}