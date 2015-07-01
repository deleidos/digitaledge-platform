package com.saic.rtws.commons.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;

/**
 * ResultHandler implementation that iterates over a result set, builds a record for each row, and returns the entire
 * result set as a collection. Extracting field values and building the actual record object is delegated to the
 * configured RecordBuilder.
 * 
 * By default, results are returned in a LinkedList, however an alternate collection object may be provided at
 * construction time.
 */
public class MultiRowResultHandler<T> extends AbstractResultHandler<Collection<T>> {

	private RecordBuilder<T> builder;

	public MultiRowResultHandler(RecordBuilder<T> builder) {
		super(new LinkedList<T>());
		this.builder = builder;
	}

	public MultiRowResultHandler(RecordBuilder<T> builder, Collection<T> buffer) {
		super(buffer);
		this.builder = builder;
	}

	public MultiRowResultHandler(RecordBuilder<T> builder, Class<? extends Collection<T>> type) {
		this(builder);
		try {
			setResult(type.newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void handle(ResultSet result) {
		try {
			while (result.next()) {
				getResult().add(builder.buildRecord(result));
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}

}
