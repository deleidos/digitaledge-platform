package com.saic.rtws.commons.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.exception.DataAccessException;

public class DataAccessSession extends DataAccessUtil {

	private DataSource dataSource;
	
	public DataAccessSession(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	protected Connection connection() {
		try {
			return initialize(dataSource.getConnection());
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	private Connection initialize(Connection connection) {
		try {
			if (!connection.getAutoCommit()) {
				connection.setAutoCommit(true);
			}
			return connection;
		} catch (SQLException e) {
			dispose(connection);
			throw new DataAccessException(e);
		}
	}
	protected void dispose(Connection connection) {
		try {
			connection.close();
		} catch(Exception e) {
			// Ignore.
		}
	}
	
}
