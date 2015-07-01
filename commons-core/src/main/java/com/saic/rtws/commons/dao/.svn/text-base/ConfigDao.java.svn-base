package com.saic.rtws.commons.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.DataAccessSession;
import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.dao.jdbc.DefaultStatementHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;

public class ConfigDao {

	protected static final char ESCAPE = '!';
	
	/** SQL used to select the locations from the table. */
	protected static final String selectLocationSql = "SELECT prefix||'\\'||name as location FROM alertviz_graphsettings";
	
	/** SQL used to select a config file from the table. */
	protected static final String selectConfigSql = "SELECT xml FROM alertviz_graphsettings where prefix||'\\'||name = ?";

	/** SQL used to insert records into the table. */
	protected static final String insertSql = "INSERT INTO alertviz_graphsettings(xml, prefix, name) VALUES(?, ?, ?)";

	/** SQL used to update records in the table. */
	protected static final String updateSql = "UPDATE alertviz_graphsettings SET xml = ? WHERE prefix||'\\'||name = ?";
	
	/** SQL used to update records in the table. */
	protected static final String moveSql = "UPDATE alertviz_graphsettings SET prefix = ? WHERE prefix = ?";
	
	/** SQL used to update records in the table. */
	protected static final String renameSql = "UPDATE alertviz_graphsettings SET name = ? WHERE prefix||'\\'||name = ?";

	/** SQL used to delete records from the table. */
	protected static final String deleteSql = "DELETE FROM alertviz_graphsettings WHERE prefix||'\\'||name LIKE ? escape '" + ESCAPE + "'";
	

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;

	/** Utility instance used to manage connections. */
	private DataAccessSession session;

	/**
	 * Constructor.
	 */
	public ConfigDao() {
		super();
	}

	/**
	 * Data source through which SQL operations should be performed.
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Data source through which SQL operations should be performed.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.session = DataAccessUtil.session(dataSource);
	}

	/**
	 * Returns all the locations from the ALERTVIZ_GRAPHSETTINGS table.
	 */
	public Collection<String> findAllLocations() {
		return session.executeMultiRowQuery(selectLocationSql, null, new ConfigLocationBuilder());
	}
	
	/**
	 * Return the config file from the ALERTVIZ_GRAPHSETTINGS table.
	 */
	public String findConfigFile(String location) {
		return session.executeSingleRowQuery(selectConfigSql, new DefaultStatementHandler(location), new ConfigFileBuilder());
	}

	/**
	 * Inserts the given record into the ALERTVIZ_GRAPHSETTINGS table.
	 */
	public void insert(String xml, String prefix, String name) {
		session.executeStatement(insertSql, new DefaultStatementHandler(xml, prefix, name));
	}
	
	/**
	 * Updates the given record with a new configuration in the ALERTVIZ_GRAPHSETTINGS table.
	 */
	public void update(String xml, String location) {
		session.executeStatement(updateSql, new DefaultStatementHandler(xml, location));
	}
	
	/**
	 * Updates the given record with a new name in the ALERTVIZ_GRAPHSETTINGS table.
	 */
	public void rename(String location, String newName){
		session.executeStatement(renameSql, new DefaultStatementHandler(newName, location));
	}
	
	/**
	 * Updates the given record(s) with a new prefix in the ALERTVIZ_GRAPHSETTINGS table.
	 */
	public void move(String oldPrefix, String newPrefix){
		session.executeStatement(moveSql, new DefaultStatementHandler(newPrefix, oldPrefix));
	}

	/**
	 * Deletes the given record(s) from the ALERTVIZ_GRAPHSETTINGS table.
	 */
	public void delete(String location) {
		//makes it so end user can't enter in the special characters % or _ into the query
		location = location.replaceAll("%", ESCAPE + "%").replaceAll("_", ESCAPE + "_"); 
		
		//append the % character if the deletion target is a folder
		location += location.endsWith("\\") ? "%" : "";
		
		session.executeStatement(deleteSql, new DefaultStatementHandler(location));
	}

	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class ConfigLocationBuilder implements RecordBuilder<String> {
		public String buildRecord(ResultSet result) {
			try {
				return result.getString("location");
			} catch (SQLException e) {
				throw new DataRetrievalException(e);
			}
		}
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class ConfigFileBuilder implements RecordBuilder<String> {
		public String buildRecord(ResultSet result) {
			try {
				return result.getString("xml");
			} catch (SQLException e) {
				throw new DataRetrievalException(e);
			}
		}
	}
}
