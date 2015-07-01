package com.saic.rtws.commons.exception;

/** Jmx Exception.
 */
public class JmxException extends RuntimeException {
	
	/** Unique serial version. */
	private static final long serialVersionUID = 1976774450627371269L;
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 */
	public JmxException(String message) {
		super(message);
	}
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 * @param cause Exception object
	 */
	public JmxException(String message, Exception cause) {
		super(message, cause);
	}
}
