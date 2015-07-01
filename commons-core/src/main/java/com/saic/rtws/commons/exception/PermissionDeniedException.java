package com.saic.rtws.commons.exception;

/** Exception for instance of permission denied.
 */
public class PermissionDeniedException extends Exception {

	/** Unique serial version. */
	private static final long serialVersionUID = 422153571454412573L;
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 */
	public PermissionDeniedException(final String message) {
		super(message);
	}
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 * @param cause Exception object
	 */
	public PermissionDeniedException(final String message, final Exception cause) {
		super(message, cause);
	}
}
