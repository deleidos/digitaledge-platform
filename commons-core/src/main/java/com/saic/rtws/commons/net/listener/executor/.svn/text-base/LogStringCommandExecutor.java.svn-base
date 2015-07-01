package com.saic.rtws.commons.net.listener.executor;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.status.ExecuteResultBuilder;

public class LogStringCommandExecutor extends CommandExecutor {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	public static LogStringCommandExecutor newInstance (String command, String request,OutputStream os) {
		return new LogStringCommandExecutor(command, request, os);
	}
	
	private LogStringCommandExecutor(String command, String request,OutputStream os) {
		super(command, request, os);
	}

	
	@Override
	public ExecuteResult execute() throws ServerException {
				
		JSONObject jsonReq = JSONObject.fromObject(request);
		
		if (command.equals(Command.LOG_STRING)) {
			return logMsg(jsonReq);
		} else {
			throw new ServerException("Invalid log command recieved.");
		}
		
	}

	private ExecuteResult logMsg(JSONObject jsonReq) {

		String logString = (String) jsonReq.get("logString");
		logger.info(logString);
		
		return ExecuteResultBuilder.buildTerminateResult(this.command);
	}
	


}
