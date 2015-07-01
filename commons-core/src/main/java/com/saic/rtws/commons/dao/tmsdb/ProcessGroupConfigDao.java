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
import com.saic.rtws.commons.model.tmsdb.ProcessGroupConfig;

/**
 * DAO accessor class for the TMS DB table ProcessGroupConfig
 * @author LUCECU
 *
 */
public class ProcessGroupConfigDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public ProcessGroupConfigDao(){
		
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
	 * Queries for all process group configurations.
	 * 
	 * @return a collection of {@link ProcessGroupConfig}
	 */
	public Collection<ProcessGroupConfig> getProcessGroupConfigs(){
		Collection<ProcessGroupConfig> configs = session.executeMultiRowQuery("select * from process_group_config", new DefaultStatementHandler(), new ProcessGroupConfigBuilder());
		
		return configs;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class ProcessGroupConfigBuilder implements RecordBuilder<ProcessGroupConfig> {
		public ProcessGroupConfig buildRecord(ResultSet rs) {
			try {
				
				ProcessGroupConfig config = new ProcessGroupConfig();
				config.setConfigPermissions(rs.getString("config_permissions"));
				config.setDefaultInstanceType(rs.getString("default_instance_type"));
				config.setInstanceStorage(rs.getString("instance_storage"));
				config.setManifestFilename(rs.getString("manifest_filename"));
				config.setProcessGroupName(rs.getString("process_group_name"));
				config.setSecurityGroup(rs.getString("security_group"));
				config.setPublicDomainName(rs.getString("public_domain_name"));
				config.setInternalDomainName(rs.getString("internal_domain_name"));
				config.setManagementInterfaces(rs.getString("management_interfaces"));
				config.setFixedWebapps(rs.getString("fixed_webapps"));
				config.setIngestConfigFilename(rs.getString("ingest_config_filename"));
				config.setVpcSubnetPlacement(rs.getString("vpc_subnet"));
											
				return config;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
