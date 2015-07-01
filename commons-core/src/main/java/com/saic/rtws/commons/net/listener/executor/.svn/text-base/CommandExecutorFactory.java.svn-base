package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.CommandExecutorMapper;
import com.saic.rtws.commons.net.listener.exception.ServerException;

public class CommandExecutorFactory {

	private static final String COMMAND_KEY = "command";

	private CommandExecutorFactory() {
		// All invocation to this class should be made through static methods.
	}

	/**
	 * Takes a client request, extracts out the command, and instantiates a new command executor
	 * to service the request.
	 */
	public static CommandExecutor createExecutor(String request, OutputStream os) throws ServerException { 
		
		try {
			String command = getCommand(request);
			Class<? extends CommandExecutor> clazz = CommandExecutorMapper.findExecutorClass(command);
			return (CommandExecutor) clazz.getMethod("newInstance", String.class, String.class, OutputStream.class)
						.invoke(null, command, request, os);
		} catch (Exception ex) {
			throw new ServerException("Failed to instantiate command executor.", ex);
		}
		
	}

	/** 
	 * Extract the command out of the client request.
	 */
	private static String getCommand(String request) throws ServerException {

		if (getLegacyCommand(request) != null) {
			return request;
		}

		JSONObject jsonRequest = JSONObject.fromObject(request);

		if (jsonRequest != null && jsonRequest.has(COMMAND_KEY)) {
			return jsonRequest.getString(COMMAND_KEY);
		}

		throw new ServerException("Failed to parse out command from request '" + request + "'.");

	}

	/**
	 * Check to see if this is a command we support from the legacy shutdown listener.
	 */
	private static String getLegacyCommand(String request) {

		if (Command.isLegacyCommand(request)) {
			return request;
		}

		return null;

	}

}