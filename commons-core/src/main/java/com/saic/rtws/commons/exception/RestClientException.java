package com.saic.rtws.commons.exception;

public class RestClientException extends Exception {

	private static final long serialVersionUID = -5669274968174191493L;

	public RestClientException() {
		super();
	}

	public RestClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestClientException(String message) {
		super(message);
	}

	public RestClientException(Throwable cause) {
		super(cause);
	}

}
