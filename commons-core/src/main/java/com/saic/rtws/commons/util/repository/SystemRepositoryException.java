package com.saic.rtws.commons.util.repository;

public class SystemRepositoryException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6403849697693998409L;

	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 */
	public SystemRepositoryException(final String message) {
		super(message);
	}
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 * @param cause Exception object
	 */
	public SystemRepositoryException(final String message, final Exception cause) {
		super(message, cause);
	}
}
