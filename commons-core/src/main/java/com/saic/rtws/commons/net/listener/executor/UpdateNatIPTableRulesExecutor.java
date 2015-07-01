package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.IPTablesProcess;
import com.saic.rtws.commons.net.listener.process.IPTablesProcess.IPTablesStatus;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class UpdateNatIPTableRulesExecutor extends CommandExecutor{

	public static UpdateNatIPTableRulesExecutor newInstance(String command, String request, OutputStream os){
		return new UpdateNatIPTableRulesExecutor(command, request, os);
	}
	
	public UpdateNatIPTableRulesExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {
		IPTablesStatus response = IPTablesProcess.newInstance(request).addRule();
		
		if(response == IPTablesStatus.Success){
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
		else{
			return buildTerminateResult(ResponseBuilder.buildErrorResponse("Error occurred, check logs on NAT."));
		}
		
	}

}
