package com.saic.rtws.commons.util.index;

import java.util.Collection;

public interface RangeSearchable<S, E> extends Searchable<S, E> {
	public Collection<E> findLessThan(S value, boolean inclusive);
	public Collection<E> findGreaterThan(S value, boolean inclusive);
	public Collection<E> findBetween(S from, S to, boolean inclusive);
}
