package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.ZookeeperProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class RestartZookeeperCommandExecutor extends CommandExecutor {

	public static RestartZookeeperCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new RestartZookeeperCommandExecutor(command, request, os);
	}

	private RestartZookeeperCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {

		if (ZookeeperProcess.newInstance().restart()) {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}

		throw new ServerException("Fail to restart Zookeeper server.");
	}

}