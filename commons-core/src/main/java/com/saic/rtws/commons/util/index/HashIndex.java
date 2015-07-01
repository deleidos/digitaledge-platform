package com.saic.rtws.commons.util.index;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Index implementing an exact match algorithm through hashing.
 */
public class HashIndex<T, E> implements Index<T, E>, Searchable<T, E> {

	/** The index. */
	private HashMap<Object, Collection<E>> index = new HashMap<Object, Collection<E>>();

	/**
	 * Constructor.
	 */
	public HashIndex() {
		super();
	}
	
	/**
	 * Finds all entries in the index for the given value.
	 */
	public Collection<E> find(T value) {
		if(index.containsKey(value)) {
			return index.get(value);
		} else {
			return Collections.emptyList();
		}
	}
	
	/**
	 * Associates the given entry with the given value.
	 */
	public void associate(T value, E entry) {
		if(index.containsKey(value)) {
			index.get(value).add(entry);
		} else {
			index.put(value, new LinkedList<E>(Collections.singleton(entry)));
		}
	}
	
}
