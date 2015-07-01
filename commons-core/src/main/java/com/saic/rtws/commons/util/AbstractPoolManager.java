package com.saic.rtws.commons.util;

import java.util.Stack;

/**
 * Implements a simple pool of objects.
 * 
 * @param <T>
 */
public abstract class AbstractPoolManager<T> {
	
	protected Stack<T> freePool;

	/**
	 * Create an initially empty pool.
	 */
	public AbstractPoolManager() {
		freePool = new Stack<T>();
	}
	
	/**
	 * Builds a new object to be included in the pool.
	 * @return T
	 */
	public abstract T buildNew(Object... params) throws Exception;

	/**
	 * Gets an object from the pool or creating a new one.
	 * @return T
	 */
	public T get(Object... buildParams) throws Exception {
		T result;
		
		if (freePool.empty())
			result = buildNew(buildParams);
		else
			result = freePool.pop();
		
		return result;
	}
	
	/**
	 * Returns an item to the pool.
	 * @param item
	 */
	public void returnIt(T item) {
		freePool.push(item);
	}

	/**
	 * Returns the size of the pool.
	 */
	public int poolSize() {
		return freePool.size();
	}
}
