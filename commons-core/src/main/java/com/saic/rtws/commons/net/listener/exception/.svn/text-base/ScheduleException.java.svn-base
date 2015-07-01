package com.saic.rtws.commons.net.listener.exception;

import net.sf.json.JSONObject;

public class ScheduleException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 661722536058518524L;
	private JSONObject errorObj;
	
	public ScheduleException(String msg, JSONObject srcJSON){
		super(msg);
		errorObj = srcJSON;
	}
	
	public ScheduleException(String string) {
		super(string);
		errorObj = new JSONObject();
	}

	public ScheduleException(String msg, Exception e) {
		super(msg,e);
	}

	public JSONObject getCauseJSON(){
		return errorObj;
	}
}
