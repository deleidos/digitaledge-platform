package com.deleidos.rtws.tools.verify.exception;

/**
 * Exception for the GtmDataPushCheckerException application.
 */
public class GtmDataPushCheckerException extends Exception {
	
	private static final long serialVersionUID = 1326485930598898727L;

	public GtmDataPushCheckerException(String message) {
		super(message);
	}
	
	public GtmDataPushCheckerException(String message, Throwable th) {
		super(message, th);
	}

}
