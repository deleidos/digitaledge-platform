package com.saic.rtws.commons.util.index;

import java.util.Collection;

/**
 * Index implementing searching over inequality relationships. This is backwards from a typical
 * range search where one searches a fixed list of values for those that satisfy a condition (i.e.
 * find my values that are greater than n). Instead, this indexes the condition; thus one searches
 * for conditions that are satisfied by a given value. Therefore, if indexing the inequality expression
 * "less than five", searching for any of the values 1, 2, 3 or 4 would all return same result.
 * Adding the the expression "less than three", the values 1 and 2 would return both entries,
 * while searching 3 or 4 would return only the entry for "less than five".
 */
public class InequalityIndex<T, E> implements Index<T, E>, Searchable<T, E> {

	/** Types of relationships that can be indexed. */
	public enum Mode {LessThan, GreaterThan};
	
	/** The Index. */
	private TreeIndex<T, E> index = new TreeIndex<T, E>();
	
	/** The type of inequality expression this index can resolve. */
	private Mode mode;
	
	/** Whether the edge of the inequality is inclusive or exclusive. */
	private boolean inclusive;
	
	/**
	 * Constructor.
	 * 
	 * @param mode The type of inequality expression this index can resolve.
	 * @param inclusive Whether the edge of the inequality is inclusive or exclusive.
	 */
	public InequalityIndex(Mode mode, boolean inclusive) {
		super();
		this.mode = mode;
		this.inclusive = inclusive;
	}
	
	/**
	 * Finds all entries who's inequality expression is satisfied by the given value.
	 */
	public Collection<E> find(T value) {
		if(mode == null) {
			throw new RuntimeException("This can't happen");
		} else if(mode == Mode.LessThan) {
			return index.findGreaterThan(value, inclusive);
		} else if(mode == Mode.GreaterThan) {
			return index.findLessThan(value, inclusive);
		} else {
			throw new RuntimeException("This can't happen");
		}
	}
	
	/**
	 * Associates the given entry with the defined inequality for the give edge value. 
	 */
	public void associate(T value, E entry) {
		index.associate(value, entry);
	}
	
}
