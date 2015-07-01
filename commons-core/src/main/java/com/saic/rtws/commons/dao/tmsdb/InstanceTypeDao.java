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
import com.saic.rtws.commons.model.tmsdb.InstanceType;

/**
 * DAO accessor class for the TMS DB table InstanceType.
 * @author LUCECU
 *
 */
public class InstanceTypeDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public InstanceTypeDao(){
		
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
	 * Queries for all instance types.
	 * 
	 * @return a collection of {@link InstanceType}
	 */
	public Collection<InstanceType> getInstanceTypes(){
		Collection<InstanceType> types = session.executeMultiRowQuery("select * from instance_types", new DefaultStatementHandler(), new InstanceTypeBuilder());
		
		return types;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class InstanceTypeBuilder implements RecordBuilder<InstanceType> {
		public InstanceType buildRecord(ResultSet rs) {
			try {
				
				InstanceType instanceType = new InstanceType();
				instanceType.setInstanceType(rs.getString("instance_type"));
				instanceType.setMemoryMB(rs.getInt("memory_mb"));
				instanceType.setNumBits(rs.getInt("num_bits"));
				instanceType.setNumCores(rs.getInt("num_cores"));
											
				return instanceType;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
