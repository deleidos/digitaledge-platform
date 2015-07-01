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
import com.saic.rtws.commons.model.tmsdb.DataSinkConfig;

/**
 * DAO class for the TMS DB table DataSinkConfigDao.
 * 
 * @author LUCECU
 *
 */
public class DataSinkConfigDao {
	
	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public DataSinkConfigDao(){
		
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
	 * Queries for all data sink configurations.
	 * 
	 * @return a list of{@link DataSinkConfig}
	 */
	public Collection<DataSinkConfig> getDataSinkConfigs(){
		Collection<DataSinkConfig> configs = session.executeMultiRowQuery("select * from datasink_config", new DefaultStatementHandler(), new DataSinkConfigBuilder());
		
		return configs;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class DataSinkConfigBuilder implements RecordBuilder<DataSinkConfig> {
		public DataSinkConfig buildRecord(ResultSet rs) {
			try {
				
				DataSinkConfig config = new DataSinkConfig();
				config.setCanAutoScale(rs.getString("can_autoscale").charAt(0));  //non null field
				config.setFqn(rs.getString("fqn"));
				config.setIngestConfigFilename(rs.getString("ingest_config_filename"));
				config.setPipelineXmlFilename(rs.getString("pipeline_xml_filename"));
				config.setPipelineXmlTemplate(rs.getString("pipeline_xml_template"));
				config.setProcessGroupDependencies(rs.getString("process_group_dependencies"));
				
											
				return config;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
