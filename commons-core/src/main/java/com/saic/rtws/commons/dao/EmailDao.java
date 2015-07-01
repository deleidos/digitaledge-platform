package com.saic.rtws.commons.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.DataAccessSession;
import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.dao.jdbc.DefaultStatementHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.model.user.EmailFilter;
import com.saic.rtws.commons.model.user.Filter;


public class EmailDao {
	
	/** SQL used to retrieve the email recipients from the table. */
	protected static final String recipientsByKeySql = "SELECT email_list FROM named_filter_users " + "" +
			"LEFT JOIN named_filter_watchlist on named_filter_users.key = named_filter_watchlist.user_key " + 
			"WHERE named_filter_watchlist.filter_key=? and named_filter_watchlist.email='Y'";
	
	/** SQL used to retrieve the email subject from the table. */
	protected static final String subjectMessageByKeySql = "SELECT email_subject, email_message FROM named_filter WHERE key = ?";
	
	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	/**
	 * Constructor.
	 */
	public EmailDao() {
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
	 * Returns the email subject and message from the NAMED_FILTER table associated with the filter key.
	 */
	public Filter findEmailDataByKey(Long key) {
		return session.executeSingleRowQuery(subjectMessageByKeySql, new DefaultStatementHandler(key), new EmailDataBuilder());
	}
	
	/**
	 * Returns all the recipients from the NAMED_FILTER_USERS table associated with the filter key.
	 */
	public Collection<EmailFilter> findRecipientsByKey(Long key) {
		return session.executeMultiRowQuery(recipientsByKeySql, new DefaultStatementHandler(key), new EmailRecipientBuilder());
	}
			
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class EmailDataBuilder implements RecordBuilder<EmailFilter> {
		public EmailFilter buildRecord(ResultSet result) {
			try {
				EmailFilter filter = new EmailFilter();
				
				filter.setEmailSubject(result.getString("email_subject"));
				filter.setEmailMessage(result.getString("email_message"));
			
				return filter;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class EmailRecipientBuilder implements RecordBuilder<EmailFilter> {
		public EmailFilter buildRecord(ResultSet result) {
			try {
				EmailFilter filter = new EmailFilter();
				
				filter.setEmailList(result.getString("email_list"));
											
				return filter;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
