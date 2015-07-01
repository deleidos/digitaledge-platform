package com.saic.rtws.commons.util.index;

public class Range<T extends Comparable<T>> {
	
	private T low;
	private T high;
	
	public Range(T low, T high) {
		this.low = low;
		this.high = high;
	}
	
	public T getLowerLimit() {
		return low;
	}
	public void setLowerLimit(T low) {
		this.low = low;
	}
	
	public T getUpperLimit() {
		return high;
	}
	public void setUpperLimit(T high) {
		this.high = high;
	}
	
	public boolean contains(T value) {
		return low.compareTo(value) < 0 && high.compareTo(value) > 0;
	}
	
}
