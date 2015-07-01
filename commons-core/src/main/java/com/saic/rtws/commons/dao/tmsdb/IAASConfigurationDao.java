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
import com.saic.rtws.commons.model.tmsdb.IAASConfiguration;

/**
 * DAO accessor class for the TMS DB table IAASConfiguration.
 * 
 * @author LUCECU
 *
 */
public class IAASConfigurationDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	private String byNameQuery = "select * from iaas_configuration where iaas_service_name = ?";
	
	public IAASConfigurationDao(){
		
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
	 * Queries for all of the IAASConfigurations.
	 * 
	 * @return a collection of {@link IAASConfigurations}
	 */
	public Collection<IAASConfiguration> getIAASConfigurations(){
		Collection<IAASConfiguration> configs = session.executeMultiRowQuery("select * from iaas_configuration", new DefaultStatementHandler(), new IAASConfigurationBuilder());
		
		return configs;
	}
	
	/**
	 * Queries for a IAASConfigurations by name.
	 * 
	 * @param name IAASConfigurations name
	 * @return {@link IAASConfigurations}
	 */
	public IAASConfiguration getIAASConfigurationByName(String name){
		
		IAASConfiguration config = session.executeSingleRowQuery(byNameQuery, new DefaultStatementHandler(name), new IAASConfigurationBuilder());
		
		return config;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class IAASConfigurationBuilder implements RecordBuilder<IAASConfiguration> {
		public IAASConfiguration buildRecord(ResultSet rs) {
			try {
				
				IAASConfiguration config = new IAASConfiguration();
				config.setDescription(rs.getString("description"));
				config.setIaasServiceName(rs.getString("iaas_service_name"));
				config.setInternetDnsXml(rs.getString("internet_dns_xml"));
				config.setServiceInterfaceXml(rs.getString("service_interface_xml"));
				config.setStorageInterfaceXml(rs.getString("storage_interface_xml"));
											
				return config;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
