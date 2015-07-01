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
import com.saic.rtws.commons.model.tmsdb.TenantAccountAccess;

/**
 * DAO accessor for the TMS DB table TenantAccountAccess.
 * 
 * @author LUCECU
 *
 */
public class TenantAccountAccessDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public TenantAccountAccessDao(){
		
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
	 * Queries for all of the tenant accounts access values.
	 * 
	 * @return a collection of {@link TenantAccountAccess}
	 */
	public Collection<TenantAccountAccess> getTenantAccountsAccess(){
		Collection<TenantAccountAccess> access = session.executeMultiRowQuery("select * from tenant_account_access", new DefaultStatementHandler(), new TenantAccountAccessBuilder());
		
		return access;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class TenantAccountAccessBuilder implements RecordBuilder<TenantAccountAccess> {
		public TenantAccountAccess buildRecord(ResultSet rs) {
			try {
				
				TenantAccountAccess access = new TenantAccountAccess();
				access.setAccountId(rs.getInt("account_id"));
				access.setTenantId(rs.getString("tenant_id"));
											
				return access;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
