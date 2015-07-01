package com.saic.rtws.commons.util.index;

import java.util.Collection;

public interface Searchable<S, E> {
	public Collection<E> find(S value);
}
