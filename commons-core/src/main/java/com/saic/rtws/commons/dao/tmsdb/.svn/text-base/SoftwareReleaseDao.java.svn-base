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
import com.saic.rtws.commons.model.tmsdb.SoftwareRelease;

/**
 * DAO accessor class for the TMS DB table SoftwareRelease.
 * @author LUCECU
 *
 */
public class SoftwareReleaseDao {

	/** Data source through which SQL operations should be performed. */
	private DataSource dataSource;
	
	/** Utility instance used to manage connections. */
	private DataAccessSession session;
	
	public SoftwareReleaseDao(){
		
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
	 * Queries for all of the software releases.
	 * 
	 * @return a collection of {@link SoftwareRelease}{
	 */
	public Collection<SoftwareRelease> getSoftwareReleases(){
		Collection<SoftwareRelease> releases = session.executeMultiRowQuery("select * from software_releases", new DefaultStatementHandler(), new SoftwareReleaseBuilder());
		
		return releases;
	}
	
	/**
	 * Inner class used to populate beans with data from a result set.
	 */
	protected static final class SoftwareReleaseBuilder implements RecordBuilder<SoftwareRelease> {
		public SoftwareRelease buildRecord(ResultSet rs) {
			try {
				
				SoftwareRelease release = new SoftwareRelease();
				release.setDescription(rs.getString("description"));
				release.setReleaseNotesUrl("release_notes_url");
				release.setSWVersionId(rs.getString("sw_version_id"));
											
				return release;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e);
			}
		}
	}
}
