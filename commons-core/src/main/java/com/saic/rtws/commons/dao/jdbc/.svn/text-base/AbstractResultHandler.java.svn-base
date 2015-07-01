package com.saic.rtws.commons.dao.jdbc;

/**
 * Base class for handlers that progressively build up a result. Provides a simple
 * getter/setter for the specified type. 
 * 
 * @param <T>
 *            Data type for built result object.
 */
public abstract class AbstractResultHandler<T> implements ResultHandler {

	private T result;

	public AbstractResultHandler() {
		super();
	}

	public AbstractResultHandler(T value) {
		result = value;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T value) {
		result = value;
	}

}
