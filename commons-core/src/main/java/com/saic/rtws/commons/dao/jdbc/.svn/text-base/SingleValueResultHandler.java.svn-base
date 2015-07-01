package com.saic.rtws.commons.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.ResultOverflowException;
import com.saic.rtws.commons.dao.exception.ResultUnderflowException;
import com.saic.rtws.commons.dao.type.sql.SqlTypeHandler;

/**
 * ResultHandler implementation that returns a single field value from a single row result set.
 */
public class SingleValueResultHandler<T> extends AbstractResultHandler<T> {

	private int fieldPosition;
	private String fieldName;
	private SqlTypeHandler<T> fieldType;

	public SingleValueResultHandler(int field, SqlTypeHandler<T> type) {
		this.fieldPosition = field;
		this.fieldType = type;
	}

	public SingleValueResultHandler(String field, SqlTypeHandler<T> type) {
		this.fieldName = field;
		this.fieldType = type;
	}

	public void handle(ResultSet result) {
		try {
			if (!result.next()) {
				throw new ResultUnderflowException("Single row query returns less than one row.");
			}
			if (fieldName == null) {
				setResult(fieldType.get(result, fieldPosition));
			} else {
				setResult(fieldType.get(result, fieldName));
			}
			if (result.next()) {
				throw new ResultOverflowException("Single-row query returns more than one row.");
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}

}
