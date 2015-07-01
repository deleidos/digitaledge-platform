package com.saic.rtws.commons.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.exception.DataAccessException;

public class DataAccessTransaction extends DataAccessUtil {

	protected Connection connection;
	
	protected DataAccessTransaction(DataSource dataSource) {
		super();
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	protected Connection connection() {
		return connection;
	}
	
	protected void dispose(Connection connection) {
		// Keep the connection open until the entire transaction is complete.
	}
	
	public void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public void rollback() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			// Ignore.
		}
	}
	
	public void finalize() {
		close();
	}
	
}
