package com.saic.rtws.commons.dao.exception;


public class DataRetrievalException extends DataAccessException {

	private static final long serialVersionUID = -1555689641579791720L;

	public DataRetrievalException(String message) {
		super(message);
	}

	public DataRetrievalException(Throwable cause) {
		super(cause);
	}

	public DataRetrievalException(String message, Throwable cause) {
		super(message, cause);
	}

}
