package com.saic.rtws.commons.dao.exception;

public class DataAccessException extends RuntimeException {

	private static final long serialVersionUID = -1555689641579791720L;

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}
