package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.RegionServerProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class RestartRegionServerCommandExecutor extends CommandExecutor {

	public static RestartRegionServerCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new RestartRegionServerCommandExecutor(command, request, os);
	}

	private RestartRegionServerCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {

		if (RegionServerProcess.newInstance().restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}

		throw new ServerException("Fail to restart RegionServer server.");
	}

}