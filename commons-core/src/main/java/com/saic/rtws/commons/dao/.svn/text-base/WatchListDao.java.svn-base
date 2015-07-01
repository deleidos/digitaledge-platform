package com.saic.rtws.commons.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONNull;
import net.sf.json.JSONSerializer;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.AbstractResultHandler;
import com.saic.rtws.commons.dao.jdbc.DataAccessSession;
import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.dao.jdbc.DefaultStatementHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.model.user.UserWatchlist;
import com.saic.rtws.commons.model.user.WatchListFilter;

public class WatchListDao {
	
	private static final Logger logger = Logger.getLogger(WatchListDao.class);
	
	/** SQL used to select records from the filter watchlist table */
	protected static final String selectByUserSql = 
		"SELECT nf.key, nf.name, nf.model, nf.definition, nf.email_subject, nf.email_message, nfw.color, nfw.email" + 
		" FROM named_filter nf, named_filter_users nfu, named_filter_watchlist nfw " + 
		" WHERE nfu.username = ? AND nf.key = nfw.filter_key AND nfu.key = nfw.user_key";
	
	/** SQL used to delete records from the filter watchlist table */
	protected static final String deleteByUserSql =
		"DELETE FROM named_filter_watchlist" +
		" WHERE filter_key = ? AND user_key = (SELECT KEY FROM named_filter_users WHERE username = ?)";
	
	/** SQL used to insert records in the filter watchlist table */
	protected static final String insertByUserSql = 
		"INSERT INTO named_filter_watchlist (user_key, filter_key, color)" +
		" SELECT nfu.key, ?, ? FROM named_filter_users nfu" +
		" WHERE nfu.username = ?";
	
	/** SQL used to update records in the filter watchlist table */
	protected static final String updateByUserSql =
		"UPDATE named_filter_watchlist" +
		" SET color = ?" +
		" WHERE filter_key = ? AND user_key = (SELECT KEY FROM named_filter_users WHERE username = ?)";
	
	private static final String fetchUsersWatchlistsSql = //
			" SELECT watchlistUsers.username, watchlist.filter_key" + //
			" FROM application.named_filter_watchlist watchlist, application.named_filter_users watchlistUsers" + //
			"   WHERE watchlistUsers.username IS NOT NULL" + //
			"   AND watchlistUsers.username <> ''" + //
			"   AND watchlist.user_key = watchlistUsers.key" + //
			" ORDER BY watchlistUsers.username";//
	
	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;

	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	/**
	 * Constructor.
	 */
	public WatchListDao() {
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
	 * Returns all the records from the NAMED_FILTER_WATCHLIST table associated with the user.
	 */
	public Collection<WatchListFilter> findByUser(String username) {
		return session.executeMultiRowQuery(selectByUserSql, new DefaultStatementHandler(username), new WatchListBuilder());
	}
	
	/**
	 * Inserts the given record into the NAMED_FILTER_WATCHLIST table.
	 */
	public void storeByUser(WatchListFilter filter, String username, Long color) {
		session.executeStatement(insertByUserSql, new DefaultStatementHandler(filter.getKey(), color, username));
	}
	
	/**
	 * Updates the given record into the NAMED_FILTER_WATCHLIST table.
	 */
	public void updateByUser(WatchListFilter filter, String username, Long color) {
		session.executeStatement(updateByUserSql, new DefaultStatementHandler(color, filter.getKey(), username));
	}
	
	/**
	 * Deletes the given record from the NAMED_FILTER_WATCHLIST table.
	 */
	public void deleteByUser(WatchListFilter filter, String username) {
		session.executeStatement(deleteByUserSql, new DefaultStatementHandler(filter.getKey(), username));
	}
	
	/**
	 * Deletes the given record from the NAMED_FILTER_WATCHLIST table.
	 */
	public List<UserWatchlist> fetchUsersWatchlists()
	{
		return session.executeQuery(fetchUsersWatchlistsSql, null, new UsersWatchListsResultHandler());
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class WatchListBuilder implements RecordBuilder<WatchListFilter> {
		public WatchListFilter buildRecord(ResultSet result) {
			try {
				WatchListFilter filter = new WatchListFilter();
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
				
				filter.setColor(result.getLong("color"));
				filter.setEmail(result.getString("email"));
								
				return filter;
			} catch (SQLException e) {
				throw new DataRetrievalException(e);
			}
		}
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class UsersWatchListsResultHandler extends AbstractResultHandler<List<UserWatchlist>> {
		public void handle(ResultSet result)
		{
			this.setResult(new ArrayList<UserWatchlist>());
			
			if(result != null)
			{
				String currUsername = null;
				String currFilterKey = null;
				UserWatchlist currUserWatchlist = null;
				try
				{
					while(result.next())
					{
						currUsername = result.getString("username");
						currFilterKey = result.getString("filter_key");
						
						if(StringUtils.isNotBlank(currUsername) && StringUtils.isNotBlank(currFilterKey))
						{
							if(currUserWatchlist == null)
							{
								currUserWatchlist = buildInitWatchlist(currUsername, currFilterKey);
							}
							else if(currUserWatchlist.getUsername().equals(currUsername))
							{
								currUserWatchlist.getWatchedFilterIds().add(currFilterKey);
							}
							else
							{
								this.getResult().add(currUserWatchlist);
								currUserWatchlist = buildInitWatchlist(currUsername, currFilterKey);
							}
						}
						else
						{
							logger.warn("A watchlist filter key or username is blank");
						}
					}
				}
				catch (SQLException sqlException)
				{
					logger.error("Failed to read User Watchlists", sqlException);
					throw new DataRetrievalException(sqlException);
				}
				
				if(currUserWatchlist != null)
				{
					// Add the last UserWatchlist
					this.getResult().add(currUserWatchlist);
				}
			}
		}
		
		private UserWatchlist buildInitWatchlist(String username, String... filterKeys)
		{
			UserWatchlist result = new UserWatchlist();
			
			result.setUsername(username);
			result.setWatchedFilterIds(new ArrayList<String>(Arrays.asList(filterKeys)));
			
			return result;
		}
	}
	
}