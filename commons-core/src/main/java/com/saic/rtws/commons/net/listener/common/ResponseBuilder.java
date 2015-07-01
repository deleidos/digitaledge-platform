package com.saic.rtws.commons.net.listener.common;

import net.sf.json.JSONObject;

public class ResponseBuilder {
	
	public static final String SUCCESS = "OK";
	public static final String ERROR = "ERROR";
	public static final String UNKNOWN = "UNKNOWN";
	public static final String TERMINATION = "OUTPUT_TERMINATION";
	
	private String status;
	private String message;

	public static String buildSuccessResponse() {
		
		ResponseBuilder response = new ResponseBuilder();
		response.setStatus(SUCCESS);
		return response.toString();
		
	}
	
	public static String buildErrorResponse(String message) {
		
		ResponseBuilder response = new ResponseBuilder();
		response.setStatus(ERROR);
		response.setMessage(message);
		return response.toString();
		
	}
	
	public static String buildTerminationResponse() {
		
		ResponseBuilder response = new ResponseBuilder();
		response.setStatus(TERMINATION);
		return response.toString();
		
	}
	
	public static String buildCustomResponse(String status, String message) {
		
		ResponseBuilder response = new ResponseBuilder();
		response.setStatus(status).setMessage(message);
		return response.toString();
		
	}
	
	public ResponseBuilder() {
		// Not instantiable.
	}
	
	protected ResponseBuilder setStatus(String status) {
		this.status = status;
		return this;
	}
	
	protected ResponseBuilder setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public String toString() {
		
		JSONObject response = new JSONObject();
		
		response.put("status", this.status);
		
		if (this.message != null) {
			response.put("message", this.message);
		}
		
		return response.toString();
		
	}
	
}