package com.saic.rtws.commons.dao.tmsdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.DataAccessSession;
import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.dao.jdbc.DefaultStatementHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.model.tmsdb.GatewayAccount;

/**
 * DAO accessor for the TMS DB table GatewayAccount.
 * 
 *
 */
public class GatewayAccountDao {
	
	private static final String FETCH_ALL_GW_INFO_SQL = //
			"SELECT * FROM application.gateway_accounts"; //
	
	private static final String FETCH_GW_INFO_BY_TENANT_ID_SQL = //
			"SELECT *" + //
			" FROM application.gateway_accounts gwAccounts, application.tenant_account_access tenantAccounts" + //
			" WHERE gwAccounts.account_id = tenantAccounts.account_id" + //
			" AND tenantAccounts.tenant_id = ?"; //

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public GatewayAccountDao(){
		
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
	 * Queries for all of the GatewayAccount values.
	 * 
	 * @return a collection of {@link GatewayAccount}
	 */
	public Collection<GatewayAccount> getGatewayAccounts(){
		Collection<GatewayAccount> gwAccounts = session.executeMultiRowQuery(FETCH_ALL_GW_INFO_SQL, new DefaultStatementHandler(), new GatewayAccountAccessBuilder());
		
		return gwAccounts;
	}
	
	/**
	 * Queries for the GatewayAccount associated with the given tenantId
	 * 
	 * @param 
	 * @return a {@link GatewayAccount}
	 */
	public GatewayAccount getGatewayAccount(String tenantId){
		return session.executeSingleRowQuery(FETCH_GW_INFO_BY_TENANT_ID_SQL, new DefaultStatementHandler(tenantId), new GatewayAccountAccessBuilder());
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class GatewayAccountAccessBuilder implements RecordBuilder<GatewayAccount> {
		public GatewayAccount buildRecord(ResultSet rs) {
			try {
				
				GatewayAccount gwAccount = new GatewayAccount();
				gwAccount.setAccountId(rs.getInt("account_id"));
				gwAccount.setInstanceId(rs.getString("instance_id"));
				gwAccount.setPublicDns(rs.getString("public_dns"));
				gwAccount.setRegion(rs.getString("region"));
				gwAccount.setRegistrationTimestamp(rs.getTimestamp("registration_timestamp"));
											
				return gwAccount;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
