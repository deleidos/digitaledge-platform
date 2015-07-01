package com.saic.rtws.commons.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;
import com.saic.rtws.commons.dao.exception.DuplicateKeyException;
import com.saic.rtws.commons.dao.type.sql.SqlTypeHandler;

/**
 * Utility class for performing common database access functions.
 */
public abstract class DataAccessUtil {

	/**
	 * Creates a data access object that can be used to execute a non-transactional operations. Each execution is
	 * performed with a separate connection from the given pool and is automatically committed.
	 */
	public static DataAccessSession session(DataSource dataSource) {
		return new DataAccessSession(dataSource);
	}

	/**
	 * Create a data access object that can be used to execute sql operations transactionally. The returned object
	 * caches a single connection that it uses for all executed operations. The client must explicitly call commit()
	 * or rollback() in order to complete the transaction.
	 */
	public static DataAccessTransaction transaction(DataSource dataSource) {
		return new DataAccessTransaction(dataSource);
	}

	/**
	 * Constructor.
	 */
	protected DataAccessUtil() {
		super();
	}

	/**
	 * Returns the connection object to be used to subsequent SQL operations. Extending classes are intended to
	 * implement their own connection management strategy. The common methods provided here will call this method to get
	 * a reference to the connection object that the child class is managing.
	 */
	protected abstract Connection connection();

	/**
	 * Signals that the given connection is done being used to perform SQL operations. Extending classes should take an
	 * appropriate action based on the connection management strategy being implemented. This could mean closing the
	 * connection, returning it to a pool, or perhaps doing nothing at all.
	 */
	protected abstract void dispose(Connection connection);

	/**
	 * Executes a query, allowing you to provide a custom handler for initializing a prepared statement, and for
	 * iterating over results.
	 * 
	 * @param <T>
	 *            The data type of the expected result.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameters of the prepared statement (null if no parameter).
	 * @param after
	 *            A handler for compiling the query results.
	 */
	@Deprecated
	public final <T> T executeQuery(String sql, StatementHandler before, AbstractResultHandler<T> after) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = connection();
			statement = connection.prepareStatement(sql);
			statement.setFetchSize(10000);

			if (before != null) {
				before.handle(statement);
			}

			results = statement.executeQuery();
			results.setFetchSize(10000);

			after.handle(results);

			return after.getResult();

		} catch (SQLException e) {
			throw new DataRetrievalException(e);

		} finally {
			try { results.close(); } catch (Exception ignore) { }
			try { statement.close(); } catch (Exception ignore) { }
			dispose(connection);
		}

	}
	
	/**
	 * Executes a query, allowing you to provide a custom handler for initializing a prepared statement, and for
	 * iterating over results.
	 * 
	 * @param <T>
	 *            The data type of the expected result.
	 * 
	 * @param stmt
	 *            The prepared sql statement to execute
	 * @param before
	 *            A handler for setting up the parameters of the prepared statement (null if no parameter).
	 * @param after
	 *            A handler for compiling the query results.
	 */
	public final <T> T executeQuery(PreparedStatement stmt, StatementHandler before, AbstractResultHandler<T> after) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = connection();
			statement = stmt;
			statement.setFetchSize(10000);

			if (before != null) {
				before.handle(statement);
			}

			results = statement.executeQuery();
			results.setFetchSize(10000);

			after.handle(results);

			return after.getResult();

		} catch (SQLException e) {
			throw new DataRetrievalException(e);

		} finally {
			try { results.close(); } catch (Exception ignore) { }
			dispose(connection);
		}

	}

	/**
	 * Executes a DML query (aka insert/update/delete), allowing you to provide a custom handler for initializing a
	 * prepared statement.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameters of the prepared statement (null if no parameter).
	 * @return Count of statement actions
	 */
	@Deprecated
	public final int executeStatement(final String sql, final StatementHandler before) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = connection();
			statement = connection.prepareStatement(sql);
			if (before != null) {
				before.handle(statement);
			}

			return statement.executeUpdate();

		} catch (SQLException e) {

			if (e.getErrorCode() == 1) {
				throw new DuplicateKeyException(e);
			} else {
				throw new DataStorageException(e);
			}

		} finally {
			try { statement.close(); } catch (Exception ignore) { }
			dispose(connection);
		}

	}
	
	/**
	 * Executes a DML query (aka insert/update/delete), allowing you to provide a custom handler for initializing a
	 * prepared statement.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameters of the prepared statement (null if no parameter).
	 * @return Count of statement actions
	 */
	public final int executeStatement(final PreparedStatement stmt, final StatementHandler before) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = connection();
			statement = stmt;
			if (before != null) {
				before.handle(statement);
			}

			return statement.executeUpdate();

		} catch (SQLException e) {

			if (e.getErrorCode() == 1) {
				throw new DuplicateKeyException(e);
			} else {
				throw new DataStorageException(e);
			}

		} finally {
			dispose(connection);
		}

	}

	/**
	 * Executes a query that returns multiple row.
	 * 
	 * @param <R>
	 *            Object type representing a record.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameter of the prepared statement (null if no parameters).
	 * @param after
	 *            A handler for building a record from the returned result set.
	 * @return Returns multiple rows.
	 */
	@Deprecated
	public final <R> Collection<R> executeMultiRowQuery(String sql, StatementHandler before, RecordBuilder<R> after) {
		return executeQuery(sql, before, new MultiRowResultHandler<R>(after));
	}
	
	/**
	 * Executes a query that returns multiple row.
	 * 
	 * @param <R>
	 *            Object type representing a record.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameter of the prepared statement (null if no parameters).
	 * @param after
	 *            A handler for building a record from the returned result set.
	 * @return Returns multiple rows.
	 */
	public final <R> Collection<R> executeMultiRowQuery(PreparedStatement stmt, StatementHandler before, RecordBuilder<R> after) {
		return executeQuery(stmt, before, new MultiRowResultHandler<R>(after));
	}

	/**
	 * Executes a query that returns a single row.
	 * 
	 * @param <R>
	 *            Object type representing a record.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameter of the prepared statement (null if no parameters).
	 * @param after
	 *            A handler for building a record from the returned result set.
	 * @return Returns a single row
	 */
	@Deprecated
	public final <R> R executeSingleRowQuery(String sql, StatementHandler before, RecordBuilder<R> after) {
		return executeQuery(sql, before, new SingleRowResultHandler<R>(after));
	}
	
	/**
	 * Executes a query that returns a single row.
	 * 
	 * @param <R>
	 *            Object type representing a record.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameter of the prepared statement (null if no parameters).
	 * @param after
	 *            A handler for building a record from the returned result set.
	 * @return Returns a single row
	 */
	public final <R> R executeSingleRowQuery(PreparedStatement stmt, StatementHandler before, RecordBuilder<R> after) {
		return executeQuery(stmt, before, new SingleRowResultHandler<R>(after));
	}

	/**
	 * Executes a query that returns a list of values from a single field.
	 * 
	 * @param <V>
	 *            Object type representing the expected data type of the field.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameter of the prepared statement (null if no parameters).
	 * @param type
	 *            A handler for the SQL type.
	 * @return Returns a list of values from a single field
	 */
	@Deprecated
	public final <V> Collection<V> executeMultiValueQuery(String sql, StatementHandler before, SqlTypeHandler<V> type) {
		return executeQuery(sql, before, new MultiValueResultHandler<V>(1, type));
	}
	
	/**
	 * Executes a query that returns a list of values from a single field.
	 * 
	 * @param <V>
	 *            Object type representing the expected data type of the field.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameter of the prepared statement (null if no parameters).
	 * @param type
	 *            A handler for the SQL type.
	 * @return Returns a list of values from a single field
	 */
	public final <V> Collection<V> executeMultiValueQuery(PreparedStatement stmt, StatementHandler before, SqlTypeHandler<V> type) {
		return executeQuery(stmt, before, new MultiValueResultHandler<V>(1, type));
	}

	/**
	 * Executes a query that returns a single field value.
	 * 
	 * @param <V>
	 *            Object type representing the expected data type of the field.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameter of the prepared statement (null if no parameters).
	 * @param type
	 *            A handler for the SQL type.
	 * @return Returns a single field value.
	 */
	@Deprecated
	public final <V> V executeSingleValueQuery(String sql, StatementHandler before, SqlTypeHandler<V> type) {
		return executeQuery(sql, before, new SingleValueResultHandler<V>(1, type));
	}
	
	/**
	 * Executes a query that returns a single field value.
	 * 
	 * @param <V>
	 *            Object type representing the expected data type of the field.
	 * 
	 * @param sql
	 *            The sql to be executed.
	 * @param before
	 *            A handler for setting up the parameter of the prepared statement (null if no parameters).
	 * @param type
	 *            A handler for the SQL type.
	 * @return Returns a single field value.
	 */
	public final <V> V executeSingleValueQuery(PreparedStatement stmt, StatementHandler before, SqlTypeHandler<V> type) {
		return executeQuery(stmt, before, new SingleValueResultHandler<V>(1, type));
	}

	/**
	 * Retrieves the next value of a sequence.
	 * 
	 * @param name
	 *            The name of the sequence
	 */
	public final Long nextSequenceValue(String name) {
		String sql = "SELECT " + name + ".nextval FROM dual";
		return executeSingleValueQuery(sql, null, SqlTypeHandler.LONG);
	}
	
	/**
	 * Prepare a statement for future use.
	 * @param statement a statement to prepare for later use
	 * @return a prepared statement
	 * @throws SQLException if the database throws an exception when preparing a statement
	 */
	public PreparedStatement prepareStatement(String statement) throws SQLException{
		Connection conn = null;
		try{
			conn = connection();
			return conn.prepareStatement(statement);
		} catch (SQLException e) {
			// The statement failed to prepare... we have a useless statement and an open connection:
			// Gotta free that connection.
			dispose(conn);
			
			// Pass the exception on
			throw e;
		}
		/*}finally{//cannot close here - we will lose the connection if we do.  Find a way to stop the leak?
			if(conn!=null){
				conn.close();
			}
		}
		*/
	}

}
