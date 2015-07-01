package com.saic.rtws.commons.util.index;

/**
 * Index for searching over range relationships. This is backwards from a typical
 * range search where one searches a fixed list of values for those that satisfy a condition (i.e.
 * find my values that are between x and y). Instead, this indexes the condition; thus one searches
 * for conditions that are satisfied by a given value. Therefore, if indexing the range expression
 * "between three and five", searching for any of the values 3, 4 or 5 would all return same result.
 * Adding the the expression "between one and five", the values 3, 4 and 5 would return both entries,
 * while searching for 1 or 2 would return only the entry for "between one and five".
 */
public abstract class RangeIndex<T extends Comparable<T>, E> implements Index<Range<T>, E>, Searchable<T,E> {
	
	/** Whether the left (lower bound) edge of the range is inclusive or exclusive. */
	protected final boolean leftInclusive;
	
	/** Whether the right (upper bound) edge of the range is inclusive or exclusive. */
	protected final boolean rightInclusive;
	
	/**
	 * Constructor. Both upper and lower bound edges are treaded inclusively.
	 */
	public RangeIndex() {
		this(true, true);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param leftInclusive Whether the left (lower bound) edge of the range is inclusive or exclusive.
	 * @param rightInclusive Whether the right (upper bound) edge of the range is inclusive or exclusive.
	 */
	public RangeIndex(boolean leftInclusive, boolean rightInclusive) {
		super();
		this.leftInclusive = leftInclusive;
		this.rightInclusive = rightInclusive;
	}
}
