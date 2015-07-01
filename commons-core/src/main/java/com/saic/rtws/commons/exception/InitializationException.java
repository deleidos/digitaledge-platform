package com.saic.rtws.commons.exception;

/** Initialization Exception.
 */
public class InitializationException extends RuntimeException {
	
	/** Unique serial version. */
	private static final long serialVersionUID = 1576784449627344938L;
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 */
	public InitializationException(String message) {
		super(message);
	}
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 * @param cause Exception object
	 */
	public InitializationException(String message, Exception cause) {
		super(message, cause);
	}
}
