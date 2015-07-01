package com.saic.rtws.commons.util.index;

import java.util.Collection;

public interface WildcardSearchable<E> {
	public Collection<E> findLike(String pattern);
}
