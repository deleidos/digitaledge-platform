package com.saic.rtws.commons.util.index;

public interface Index<I, E> {
	public void associate(I value, E entry);
}
