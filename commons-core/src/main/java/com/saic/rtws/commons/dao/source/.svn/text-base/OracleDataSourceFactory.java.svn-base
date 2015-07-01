package com.saic.rtws.commons.dao.source;

import java.sql.SQLException;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;

/**
 * Factory to create and store an instance of the OracleDataSource.
 */
public class OracleDataSourceFactory {
	/** Logger. */
	private Logger logger = Logger.getLogger(OracleDataSourceFactory.class);
	/** OracleDataSource object. */
	private OracleDataSource ds = null;
	
	/** Constructor. */
	protected OracleDataSourceFactory() { 
		super();
		// initialize the data source off of the configuration
		initDataSource();
	}
	
    /**
     * Holder class is loaded on the first execution of singleton getInstance() 
     * or the first access to holder class, not before.
     */
	protected static final class OracleDataSourceFactoryHolder {
		/** Singleton instance. */
		protected static final OracleDataSourceFactory INSTANCE = new OracleDataSourceFactory();
	    
	    /** Private constructor. */
		protected OracleDataSourceFactoryHolder() { }
	}
	
	/**
	 * Return a thread-safe singleton instance of class.
	 * @return Returns singleton instance of class
	 */
	public static OracleDataSourceFactory getInstance() {
		return OracleDataSourceFactoryHolder.INSTANCE;
	}
	
	/**
	 * Get the DataSource object.
	 * @return Return DataSource object
	 */
	public DataSource getDataSource() {
		return (DataSource) ds;
	}
	
	/**
	 * Get the OracleDataSource object.
	 * @return Return OracleDataSource object
	 */
	public OracleDataSource getOracleDataSource() {
		return ds;
	}
	
	/** 
	 * Initialize the Data Source.
	 */
	public void initDataSource() {
		try {
			ds = new OracleDataSource();
			ds.setURL(RtwsConfig.getInstance().getString("oracle.connection.url"));
			ds.setUser(RtwsConfig.getInstance().getString("oracle.connection.user"));
			ds.setPassword(RtwsConfig.getInstance().getString("oracle.conncetion.password"));
		} catch (SQLException e) {
			logger.error(e);
		}
	}
	
	/**
	 * Set the DataSource.
	 * @param dataSource DataSource object
	 */
	public void setDataSource(final DataSource dataSource) {
		ds = (OracleDataSource) dataSource;
	}
	
	/**
	 * Set the DataSource.
	 * @param dataSource OracleDataSource object
	 */
	public void setDataSource(final OracleDataSource dataSource) {
		ds = dataSource;
	}
	
}
