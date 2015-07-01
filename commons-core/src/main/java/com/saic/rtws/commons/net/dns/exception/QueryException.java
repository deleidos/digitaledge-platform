package com.saic.rtws.commons.net.dns.exception;

public class QueryException  extends Exception {
	/** Serial Version UID. */
	private static final long serialVersionUID = 33928014L;
	

	/** 
	 * Constructor. 
	 */
	public QueryException() {
		super();
	}
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 */
	public QueryException(String message) {
		super(message);
	}
	
	/** 
	 * Constructor. 
	 * @param message Message to be returned
	 * @param cause Exception object
	 */
	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/** 
	 * Constructor. 
	 * @param cause Exception object
	 */
	public QueryException(Throwable cause) {
		super(cause);
	}
}
