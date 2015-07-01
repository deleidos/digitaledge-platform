package com.saic.rtws.commons.util;

/**
 * Exception that when raised indicates a likely bug or unanticipated condition in the code. 
 */
public class BugCheckException extends RuntimeException {
	
	/** Serial version UID. */
	private static final long serialVersionUID = -6083902448134348804L;
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 */
	public BugCheckException(final String message) {
		super(message);
	}
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 * @param cause Exception object
	 */
	public BugCheckException(final String message, final Exception cause) {
		super(message, cause);
	}
}
