package com.saic.rtws.commons.util.index;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


/**
 * 
 * @author floydac
 * Index implementing searching over range relationships. This is backwards from a typical
 * range search where one searches a fixed list of values for those that satisfy a condition (i.e.
 * find my values that are between x and y). Instead, this indexes the condition; thus one searches
 * for conditions that are satisfied by a given value. Therefore, if indexing the range expression
 * "between three and five", searching for any of the values 3, 4 or 5 would all return same result.
 * 
 * This class handles a special case of range indexing where none of the ranges being indexed overlap.
 * Thus adding the range "one to five" when a range of "three to five" exists would throw an error.
 * This added constraint allows for the index to be decomposed to a simple binary tree, allowing all
 * the quick searching/inserting/deleting speeds of a binary tree to be applied to the range 
 * indexing problem.
 */
public class NoOverlapRangeIndex<T extends Comparable<T>, E> extends RangeIndex<T, E> {

	/** The index. */
	private NavigableMap<T,Entry<T,Collection<E>>> index;
	
	/**
	 * Constructor.
	 */
	public NoOverlapRangeIndex() {
		super();
		index = new TreeMap<T, Entry<T,Collection<E>>>();
	}
	
	/**
	 * Find the entry who's range expression is satisfied by the given value.
	 * @param T The value whose bounds are being searched for
	 * @return A collection of entries that correspond to the search value (or null if the value has no corresponding bound)
	 */
	public Collection<E> find(T value) {
		Map.Entry<T,Entry<T,Collection<E>>> entry = index.floorEntry(value);
		if(entry == null) return null;
		else{
			Entry<T, Collection<E>> highValue = entry.getValue();
			if(highValue.getKey().compareTo(value) >= 0)
				return highValue.getValue();
			else
				return null;
		}
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
			Collection<E> coll = new LinkedList<E>();
			coll.add(entry);
			index.put(lower, new Entry<T,Collection<E>>(upper, coll));
		}else{
			Entry<T,Collection<E>> curVal = index.get(lower);
			if(curVal.equals(upper))
				curVal.getValue().add(entry);
			else
				throw new UnsupportedOperationException("Attempted to create an overlapping range within a NoOverlapRangeIndex.");
		}
	}

	@SuppressWarnings("hiding")
	private class Entry<T, E>{
		private T key;
		private E value;
		
		public Entry(T key, E value){
			this.key = key;
			this.value = value;
		}
		
		public T getKey(){
			return key;
		}
		
		public E getValue(){
			return value;
		}
	}
}
