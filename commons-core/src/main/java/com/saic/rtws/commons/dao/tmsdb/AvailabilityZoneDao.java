package com.saic.rtws.commons.dao.tmsdb;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.DataAccessSession;
import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.dao.jdbc.DefaultStatementHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.model.tmsdb.AvailabilityZone;
import com.saic.rtws.commons.model.tmsdb.EndPointURL;

/**
 * DAO accessor class for the TMS DB AvailabilityZone table.
 * 
 * @author LUCECU
 *
 */
public class AvailabilityZoneDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	private String byZoneQuery = "select * from availability_zones where iaas_azone = ?";
	
	public AvailabilityZoneDao(){
		
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
	 * Queries for all of the availabililty zones.
	 * 
	 * @return a list of {@link AvailabilityZone}
	 */
	public Collection<AvailabilityZone> getAvailabilityZones(){
		Collection<AvailabilityZone> zones = session.executeMultiRowQuery("select * from availability_zones", new DefaultStatementHandler(), new AvailabilityZoneBuilder());
		
		return zones;
	}
	
	/**
	 * Queries for a AvailabilityZone by availability zone.
	 * 
	 * @param zone AvailabilityZones zone
	 * @return {@link AvailabilityZone}
	 */
	public AvailabilityZone getAvailabilityZoneByZone(String zone){
		
		AvailabilityZone aZone = session.executeSingleRowQuery(byZoneQuery, new DefaultStatementHandler(zone), new AvailabilityZoneBuilder());
		
		return aZone;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class AvailabilityZoneBuilder implements RecordBuilder<AvailabilityZone> {
		public AvailabilityZone buildRecord(ResultSet rs) {
			try {
				
				AvailabilityZone zone = new AvailabilityZone();
				zone.setIaasAZone(rs.getString("iaas_azone"));
				zone.setIaasRegion(rs.getString("iaas_region"));
				zone.setIaasServiceName(rs.getString("iaas_service_name"));
				zone.setIaasSWBucket(rs.getString("iaas_sw_bucket"));
				zone.setDescription(rs.getString("description"));
				zone.setPropertiesFile(rs.getString("properties_file"));
				zone.setStorageEndpoint(new EndPointURL(rs.getString("storage_endpoint")));
				zone.setServiceEndpoint(new EndPointURL(rs.getString("service_endpoint")));
											
				return zone;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			} catch (MalformedURLException mfue) {
				mfue.printStackTrace();
				throw new DataRetrievalException(mfue);
			}
		}
	}
	
}
