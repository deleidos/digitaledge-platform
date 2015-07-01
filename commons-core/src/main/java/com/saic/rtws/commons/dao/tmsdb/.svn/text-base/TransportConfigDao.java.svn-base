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
import com.saic.rtws.commons.model.tmsdb.TransportConfig;

/**
 * DAO class for the TMS DB table TransportConfig.
 * 
 * @author LUCECU
 * 
 * @deprecated this database table no longer exists
 */
public class TransportConfigDao {
	
	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public TransportConfigDao(){
		
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
	 * Queries for all transport configurations.
	 * 
	 * @return a list of{@link TransportConfig}
	 */
	public Collection<TransportConfig> getTransportConfigurations(){
		Collection<TransportConfig> configs = session.executeMultiRowQuery("select * from transport_config", new DefaultStatementHandler(), new TransportConfigBuilder());
		
		return configs;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class TransportConfigBuilder implements RecordBuilder<TransportConfig> {
		public TransportConfig buildRecord(ResultSet rs) {
			try {
				
				TransportConfig config = new TransportConfig();
				config.setFqn(rs.getString("fqn"));
				config.setDescription(rs.getString("description"));
				config.setTransportXmlTemplate(rs.getString("transport_xml_template"));
											
				return config;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
