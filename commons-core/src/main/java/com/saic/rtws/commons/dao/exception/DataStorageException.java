package com.saic.rtws.commons.dao.exception;


public class DataStorageException extends DataAccessException {

	private static final long serialVersionUID = -1555689641579791720L;

	public DataStorageException(String message) {
		super(message);
	}

	public DataStorageException(Throwable cause) {
		super(cause);
	}

	public DataStorageException(String message, Throwable cause) {
		super(message, cause);
	}

}
