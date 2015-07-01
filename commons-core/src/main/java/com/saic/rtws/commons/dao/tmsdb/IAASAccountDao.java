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
import com.saic.rtws.commons.model.tmsdb.IAASAccount;

/**
 * DAO accessor for the TMS DB IAASAccount table.
 * 
 * @author LUCECU
 *
 */
public class IAASAccountDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public IAASAccountDao(){
		
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
	 * Queries for all of the IAAS accounts.
	 * 
	 * @return a collection of {@link IAASAccount}
	 */
	public Collection<IAASAccount> getIAASAccounts(){
		Collection<IAASAccount> accounts = session.executeMultiRowQuery("select * from iaas_accounts", new DefaultStatementHandler(), new IAASAccountBuilder());
		
		return accounts;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class IAASAccountBuilder implements RecordBuilder<IAASAccount> {
		public IAASAccount buildRecord(ResultSet rs) {
			try {
				IAASAccount account = new IAASAccount();
				account.setAccountId(rs.getInt("account_id"));
				account.setDescription(rs.getString("description"));
				account.setIaasServiceName(rs.getString("iaas_service_name"));
				account.setKeypairName(rs.getString("keypair_name"));
				account.setAnchorInstanceId(rs.getString("anchor_instance_id"));
				account.setAccountName(rs.getString("account_name"));
				account.setAccountEmail(rs.getString("aws_account_email"));
				account.setIaasServiceNumber(rs.getString("iaas_service_number"));
				account.setDomainNameSuffix(rs.getString("domain_name_suffix"));
				account.setEulaAcceptedTimestamp(rs.getTimestamp("eula_accepted_timestamp"));
							
				return account;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
