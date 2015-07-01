package com.saic.rtws.commons.dao.source;

import com.saic.rtws.commons.config.RtwsConfig;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.apache.log4j.Logger;

/**
 * Factory to create and store an instance of the H2 JdbcConnectionPool.
 */
public class H2DataSourceFactory {
	
	/** Logger. */
	private Logger logger = Logger.getLogger(H2DataSourceFactory.class);
	/** JdbcConnectionPool object. */
	private JdbcConnectionPool cp = null;
	
	/** Constructor. */
	protected H2DataSourceFactory() { 
		super();
		// initialize the data source off of the configuration
		initDataSource();
	}
	
    /**
     * Holder class is loaded on the first execution of singleton getInstance() 
     * or the first access to holder class, not before.
     */
	protected static final class H2DataSourceFactoryHolder {
		/** Singleton instance. */
		protected static final H2DataSourceFactory INSTANCE = new H2DataSourceFactory();
	    
	    /** Private constructor. */
		protected H2DataSourceFactoryHolder() { }
	}
	
	/**
	 * Return a thread-safe singleton instance of class.
	 * @return Returns singleton instance of class
	 */
	public static H2DataSourceFactory getInstance() {
		return H2DataSourceFactoryHolder.INSTANCE;
	}
	
	/**
	 * Get the DataSource object.
	 * @return Return DataSource object
	 */
	public DataSource getDataSource() {
		return (DataSource) cp;
	}
	
	/**
	 * Get the JdbcConnectionPool object.
	 * @return Return JdbcConnectionPool object
	 */
	public JdbcConnectionPool getJdbcConnectionPool() {
		return cp;
	}
	
	/** 
	 * Initialize the Data Source.
	 */
	public void initDataSource() {
			cp = JdbcConnectionPool.create(RtwsConfig.getInstance().getConfiguration().getString("h2.app.connection.url"),
					RtwsConfig.getInstance().getConfiguration().getString("h2.app.connection.user"),
					RtwsConfig.getInstance().getConfiguration().getString("h2.app.connection.password"));
	}
	
	/**
	 * Set the DataSource.
	 * @param dataSource DataSource object
	 */
	public void setDataSource(final DataSource dataSource) {
		cp = (JdbcConnectionPool) dataSource;
	}
	
	/**
	 * Set the DataSource.
	 * @param dataSource JdbcDataSource object
	 */
	public void setDataSource(final JdbcConnectionPool dataSource) {
		cp = dataSource;
	}
	
}
