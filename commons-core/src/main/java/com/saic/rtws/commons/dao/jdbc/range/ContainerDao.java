package com.saic.rtws.commons.dao.jdbc.range;

import java.util.Collection;

import com.saic.rtws.commons.util.Initializable;
import com.saic.rtws.commons.util.index.Range;

/**
 * Defines a generic interface for searching through records in order to take a key value and find
 * which records have a range which contains that value.
 * @author floydac
 *
 * @param <T> The type of value that the range extends over (ex. integers, etc)
 * @param <E> The object representation of a record from a table. (ex. JSON, XML, etc)
 */
public interface ContainerDao<T extends Comparable<T>, E> extends Initializable{
	
	public Collection<E> findContainers(T val);

}
