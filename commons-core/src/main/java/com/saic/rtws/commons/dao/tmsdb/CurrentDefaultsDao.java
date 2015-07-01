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
import com.saic.rtws.commons.model.tmsdb.CurrentDefaults;

/**
 * DAO accessor class used for the TMS DB table CurrentDefaults.
 * 
 * @author LUCECU
 *
 */
public class CurrentDefaultsDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public CurrentDefaultsDao(){
		
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
	 * Queries for all of the current defaults.
	 * 
	 * @return a list of {@link CurrentDefaults}
	 */
	public Collection<CurrentDefaults> getCurrentDefaults(){
		Collection<CurrentDefaults> defaults = session.executeMultiRowQuery("select * from current_defaults", new DefaultStatementHandler(), new CurrentDefaultsBuilder());
		
		return defaults;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class CurrentDefaultsBuilder implements RecordBuilder<CurrentDefaults> {
		public CurrentDefaults buildRecord(ResultSet rs) {
			try {
				
				CurrentDefaults defaults = new CurrentDefaults();
				defaults.setIaasRegion(rs.getString("iaas_region"));
				defaults.setIaasServiceName(rs.getString("iaas_service_name"));
				defaults.setIaasZone(rs.getString("iaas_azone"));
				defaults.setSWVersionID(rs.getString("sw_version_id"));
											
				return defaults;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
	
}
