package com.saic.rtws.commons.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import net.sf.json.JSONException;
import net.sf.json.JSONNull;
import net.sf.json.JSONSerializer;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.DataAccessSession;
import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.dao.jdbc.DefaultStatementHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.model.user.Filter;

/**
 * Data access object used to retrieve data from the NAMED_FILTER table.
 */
public class FilterDao {

	/** SQL used to select records from the table. */
	protected static final String selectSql = "SELECT key, name, model, definition, email_subject, email_message FROM named_filter";

	/** SQL used to insert records into the table. */
	protected static final String insertSql = "INSERT INTO named_filter(key, name, model, definition, email_subject, email_message) VALUES(?, ?, ?, ?, ?, ?)";

	/** SQL used to update records in the table. */
	protected static final String updateSql = "UPDATE named_filter SET name = ?, model = ?, definition = ?, email_subject = ?, email_message = ? WHERE key = ?";

	/** SQL used to delete records from the table. */
	protected static final String deleteSql = "DELETE FROM named_filter WHERE key = ?";

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;

	/** Utility instance used to manage connections. */
	private DataAccessSession session;

	/**
	 * Constructor.
	 */
	public FilterDao() {
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
	 * Returns all the records from the NAMED_FILTER table.
	 */
	public Collection<Filter> findAll() {
		return session.executeMultiRowQuery(selectSql, null, new FilterBuilder());
	}

	/**
	 * Inserts or updates the given record into the NAMED_FILTER table.
	 */
	public Filter store(Filter filter) {
		if (filter.getKey() == null) {
			filter.setKey(session.nextSequenceValue("named_filter_seq"));
			session.executeStatement(insertSql, new DefaultStatementHandler(filter.getKey(), filter.getName(), filter.getModel(), 
					filter.getDefinition().toString(), filter.getEmailSubject(), filter.getEmailMessage()));
		} else {
			session.executeStatement(updateSql, new DefaultStatementHandler(filter.getName(), filter.getModel(), 
					filter.getDefinition().toString(), filter.getEmailSubject(), filter.getEmailMessage(), filter.getKey()));
		}
		return filter;
	}

	/**
	 * Deletes the given record from the NAMED_FILTER table.
	 */
	public void delete(Filter filter) {
		session.executeStatement(deleteSql, new DefaultStatementHandler(filter.getKey()));
	}

	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class FilterBuilder implements RecordBuilder<Filter> {
		public Filter buildRecord(ResultSet result) {
			try {			
				Filter filter = new Filter();
				filter.setKey(result.getLong("key"));
				filter.setName(result.getString("name"));
				filter.setModel(result.getString("model"));
				
				try{
					filter.setDefinition(JSONSerializer.toJSON(result.getString("definition")));
				}catch(JSONException ignore){
					filter.setDefinition(JSONNull.getInstance());
				}
				filter.setEmailSubject(result.getString("email_subject"));
				filter.setEmailMessage(result.getString("email_message"));
				
				return filter;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}

}
