package com.saic.rtws.commons.dao.jdbc;

import java.sql.ResultSet;

/**
 * Defines an interface for classes capable of building a record object from a result set's current fetch position.
 */
public interface RecordBuilder<T> {
	public T buildRecord(ResultSet result);
}
