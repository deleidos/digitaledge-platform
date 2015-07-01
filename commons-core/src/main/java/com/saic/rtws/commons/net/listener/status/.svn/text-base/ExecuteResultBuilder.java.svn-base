package com.saic.rtws.commons.net.listener.status;

public class ExecuteResultBuilder {
	
	private ExecuteResultBuilder() { }
	
	public static ExecuteResult buildContinueResult(String command, String message) {
		return new ExecuteResult(command, ExecuteState.Continue, message);
	}
	
	public static ExecuteResult buildTerminateResult(String command) {
		return new ExecuteResult(command, ExecuteState.Terminate);
	}
	
	public static ExecuteResult buildLegacyTerminateResult(String command) {
		return new ExecuteResult(command, ExecuteState.LegacyTerminate);
	}
	
}