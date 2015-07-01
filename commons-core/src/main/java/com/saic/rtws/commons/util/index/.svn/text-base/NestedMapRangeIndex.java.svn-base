package com.saic.rtws.commons.util.index;

import java.util.Collection;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * 
 * @author deckerjos
 * Index implementing searching over range relationships. This is backwards from a typical
 * range search where one searches a fixed list of values for those that satisfy a condition (i.e.
 * find my values that are between x and y). Instead, this indexes the condition; thus one searches
 * for conditions that are satisfied by a given value. Therefore, if indexing the range expression
 * "between three and five", searching for any of the values 3, 4 or 5 would all return same result.
 * Adding the the expression "between one and five", the values 3, 4 and 5 would return both entries,
 * while searching for 1 or 2 would return only the entry for "between one and five".
 */
public class NestedMapRangeIndex<T extends Comparable<T>, E> extends RangeIndex<T, E> {

	/** The index. */
	private NavigableMap<T, NavigableMap<T, Collection<E>>> index = new TreeMap<T, NavigableMap<T, Collection<E>>>();
	
	/**
	 * Constructor. Both upper and lower bound edges are treaded inclusively.
	 */
	public NestedMapRangeIndex() {
		super(true, true);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param leftInclusive Whether the left (lower bound) edge of the range is inclusive or exclusive.
	 * @param rightInclusive Whether the right (upper bound) edge of the range is inclusive or exclusive.
	 */
	public NestedMapRangeIndex(boolean leftInclusive, boolean rightInclusive) {
		super(leftInclusive,rightInclusive);
	}
	
	/**
	 * Finds all entries who's range expression is satisfied by the given value.
	 */
	public Collection<E> find(T value) {
		LinkedList<E> buffer = new LinkedList<E>();
		for(NavigableMap<T, Collection<E>> subindex : index.headMap(value, leftInclusive).values()) {
			for(Collection<E> match : subindex.tailMap(value, rightInclusive).values()) {
				buffer.addAll(match);
			}
		}
		return buffer;
	}
	
	/**
	 * Associates the given entry with the given range of values. 
	 */
	public void associate(Range<T> range, E entry) {
		associate(range.getLowerLimit(), range.getUpperLimit(), entry);
	}
	
	/**
	 * Associates the given entry with the given range of values. 
	 */
	public void associate(T lower, T upper, E entry) {
		
		if(!index.containsKey(lower)) {
			index.put(lower, new TreeMap<T, Collection<E>>());
		}
		NavigableMap<T, Collection<E>> subindex = index.get(lower);
		
		if(!subindex.containsKey(upper)) {
			subindex.put(upper, new LinkedList<E>());
		}
		Collection<E> list = subindex.get(upper);
		
		list.add(entry);
		
	}
	
}
