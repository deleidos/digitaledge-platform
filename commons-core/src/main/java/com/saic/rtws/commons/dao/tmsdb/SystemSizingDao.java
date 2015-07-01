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
import com.saic.rtws.commons.model.tmsdb.SystemSizing;

/**
 * DAO accessor class for the TMS DB table SystemSizing
 * @author LUCECU
 *
 */
public class SystemSizingDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	private String sizeQuery = "select * from system_sizing where system_size=?";
	
	public SystemSizingDao(){
		
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
	 * Queries for all system sizes.
	 * 
	 * @return a collection of {@link SystemSizing}
	 */
	public Collection<SystemSizing> getSystemSizes(){
		Collection<SystemSizing> sizes = session.executeMultiRowQuery("select * from system_sizing", new DefaultStatementHandler(), new SystemSizeBuilder());
		
		return sizes;
	}
	
	/**
	 * Queries for a system size by size.
	 * @param size system size
	 * @return a {@link SystemSizing}
	 */
	public SystemSizing getSystemBySize(String size){
		
		SystemSizing system = session.executeSingleRowQuery(sizeQuery, new DefaultStatementHandler(size), new SystemSizeBuilder());
		
		return system;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class SystemSizeBuilder implements RecordBuilder<SystemSizing> {
		public SystemSizing buildRecord(ResultSet rs) {
			try {
				
				SystemSizing size = new SystemSizing();
				size.setCombineIntExtFlag((rs.getString("combine_int_ext_jms_flag")).charAt(0));  //non null field
				size.setJmsInstanceCount(rs.getInt("jms_instance_count"));
				size.setJmsInstanceType(rs.getString("jms_instance_type"));
				size.setSystemSize(rs.getString("system_size"));
											
				return size;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
