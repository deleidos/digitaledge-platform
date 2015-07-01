package com.saic.rtws.commons.net.shutdown;

// TODO: Auto-generated Javadoc
/**
 * The Class InvalidLogFileRequest.
 */
public class InvalidLogFileRequest extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid log file request.
	 */
	public InvalidLogFileRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new invalid log file request.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public InvalidLogFileRequest(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new invalid log file request.
	 * 
	 * @param message
	 *            the message
	 */
	public InvalidLogFileRequest(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new invalid log file request.
	 * 
	 * @param cause
	 *            the cause
	 */
	public InvalidLogFileRequest(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
