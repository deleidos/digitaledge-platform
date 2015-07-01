package com.saic.rtws.commons.dao.exception;


public class ResultUnderflowException extends DataRetrievalException {

	private static final long serialVersionUID = -1555689641579791720L;

	public ResultUnderflowException(String message) {
		super(message);
	}

	public ResultUnderflowException(Throwable cause) {
		super(cause);
	}

	public ResultUnderflowException(String message, Throwable cause) {
		super(message, cause);
	}

}
