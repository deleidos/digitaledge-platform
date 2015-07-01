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
import com.saic.rtws.commons.model.tmsdb.MachineImage;

/**
 * DAO accessor class for the TMS DB table MachineImage.
 * 
 * @author LUCECU
 *
 */
public class MachineImageDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public MachineImageDao(){
		
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
	 * Queries for all machine images.
	 * 
	 * @return a collection of {@link MachineImage}
	 */
	public Collection<MachineImage> getMachineImages(){
		Collection<MachineImage> images = session.executeMultiRowQuery("select * from machine_images", new DefaultStatementHandler(), new MachineImageBuilder());
		
		return images;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class MachineImageBuilder implements RecordBuilder<MachineImage> {
		public MachineImage buildRecord(ResultSet rs) {
			try {
				
				MachineImage image = new MachineImage();
				image.setIaasRegion(rs.getString("iaas_region"));
				image.setIaasServiceName(rs.getString("iaas_service_name"));
				image.setMI64BitInstance(rs.getString("mi_64bit_instance"));
				image.setSWVersionId(rs.getString("sw_version_id"));
				image.setTenantId(rs.getString("tenant_id"));
											
				return image;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
	
	
}
