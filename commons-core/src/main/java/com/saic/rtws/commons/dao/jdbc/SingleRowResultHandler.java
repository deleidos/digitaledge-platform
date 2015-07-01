package com.saic.rtws.commons.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.ResultOverflowException;
import com.saic.rtws.commons.dao.exception.ResultUnderflowException;

/**
 * ResultHandler implementation that builds a single record from a result set that returns only one row. Extracting
 * field values and building the actual record object is delegated to the configured RecordBuilder. This class also
 * validated the the expected number of records are returned; if the result set contains no rows or more than one
 * row, an exception is thrown.
 * 
 * This class is useful for situations where you know only one record is expected (i.e. primary key lookup) and you
 * don't want to bother dealing with a collection of results.
 */
public class SingleRowResultHandler<T> extends AbstractResultHandler<T> {

	private RecordBuilder<T> builder;

	public SingleRowResultHandler(RecordBuilder<T> builder) {
		this.builder = builder;
	}

	public void handle(ResultSet result) {
		try {
			if (!result.next()) {
				throw new ResultUnderflowException("Single row query returns less than one row.");
			}
			setResult(builder.buildRecord(result));
			if (result.next()) {
				throw new ResultOverflowException("Single-row query returns more than one row.");
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}

}
