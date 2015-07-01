package com.saic.rtws.commons.util.index;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Index implementing range based searching for literal values using a TreeMap data structure. The
 * indexed values must be immutable literal values, and must either implement the Comparable interface,
 * or a suitable Comparator must be defined at construction time.
 */
public class TreeIndex<T, E> implements Index<T, E>, RangeSearchable<T, E> {

	/** The index. */
	private NavigableMap<T, Collection<E>> index;
	
	/**
	 * Constructor.
	 */
	public TreeIndex() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param comparator Comparator implementing ordering for the indexed values.
	 */
	public TreeIndex(Comparator<T> comparator) {
		super();
		index = new TreeMap<T, Collection<E>>(comparator);
	}
	
	/**
	 * Finds all entries associated with the given value.
	 */
	public Collection<E> find(T value) {
		if(index.containsKey(value)) {
			return index.get(value);
		} else {
			return Collections.emptyList();
		}
	}
	
	/**
	 * Finds all entries associated with values that are less than or equal to the given value.
	 */
	public Collection<E> findLessThan(T value, boolean inclusive) {
		LinkedList<E> buffer = new LinkedList<E>();
		for(Collection<E> match : index.headMap(value, inclusive).values()) {
			buffer.addAll(match);
		}
		return buffer;
	}
	
	/**
	 * Finds all entries associated with values that are greater than or equal to the given value.
	 */
	public Collection<E> findGreaterThan(T value, boolean inclusive) {
		LinkedList<E> buffer = new LinkedList<E>();
		for(Collection<E> match : index.tailMap(value, inclusive).values()) {
			buffer.addAll(match);
		}
		return buffer;
	}
	
	/**
	 * Finds all entries associated with values that are within the given range.
	 */
	public Collection<E> findBetween(T from, T to, boolean inclusive) {
		LinkedList<E> buffer = new LinkedList<E>();
		for(Collection<E> match : index.subMap(from, inclusive, to, inclusive).values()) {
			buffer.addAll(match);
		}
		return buffer;
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
