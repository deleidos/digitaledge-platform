package com.saic.rtws.commons.dao.exception;


public class ResultOverflowException extends DataRetrievalException {

	private static final long serialVersionUID = -1555689641579791720L;

	public ResultOverflowException(String message) {
		super(message);
	}

	public ResultOverflowException(Throwable cause) {
		super(cause);
	}

	public ResultOverflowException(String message, Throwable cause) {
		super(message, cause);
	}

}
