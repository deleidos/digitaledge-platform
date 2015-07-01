package com.saic.rtws.commons.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataAccessException;

public class DefaultStatementHandler implements StatementHandler {

	Object[] values;
	
	public DefaultStatementHandler(Object... values) {
		this.values = values;
	}

	public void handle(PreparedStatement statement) {
		try {
			for(int i = 0; i < values.length; i++) {
				Object value = values[i];
				statement.setObject(i + 1, value);
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
}
