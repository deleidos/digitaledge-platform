package com.saic.rtws.commons.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.type.sql.SqlTypeHandler;

/**
 * ResultHandler implementation that iterates over a result set and builds a collection from a single field. This is
 * useful for situations where you only want one field out of a table, but expect to retrieve more than one row.
 */
public class MultiValueResultHandler<T> extends AbstractResultHandler<Collection<T>> {

	private int fieldPosition;
	private String fieldName;
	private SqlTypeHandler<T> fieldType;

	public MultiValueResultHandler(int field, SqlTypeHandler<T> type) {
		super(new LinkedList<T>());
		this.fieldPosition = field;
		this.fieldType = type;
	}

	public MultiValueResultHandler(String field, SqlTypeHandler<T> type) {
		super(new LinkedList<T>());
		this.fieldName = field;
		this.fieldType = type;
	}

	public void handle(ResultSet result) {
		try {
			while(result.next()) {
				if (fieldName == null) {
					getResult().add(fieldType.get(result, fieldPosition));
				} else {
					getResult().add(fieldType.get(result, fieldName));
				}
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}

}
