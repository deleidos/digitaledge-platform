package com.saic.rtws.commons.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;

public class ArrayRecordBuilder implements RecordBuilder<Object[]> {

	public ArrayRecordBuilder() {
		super();
	}

	public Object[] buildRecord(ResultSet result) {
		try {
			Object[] buffer = new Object[result.getMetaData().getColumnCount()];
			for(int i = 0; i < buffer.length; i++) {
				buffer[i] = result.getObject(i + 1);
			}
			return buffer;
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}
	
}
