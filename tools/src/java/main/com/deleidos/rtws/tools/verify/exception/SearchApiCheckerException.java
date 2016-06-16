package com.deleidos.rtws.tools.verify.exception;

/**
 * Exception for the SearchApiChecker application.
 */
public class SearchApiCheckerException extends Exception {
	
	private static final long serialVersionUID = 3810499728234996695L;

	public SearchApiCheckerException(String message) {
		super(message);
	}
	
	public SearchApiCheckerException(String message, Throwable th) {
		super(message, th);
	}

}
