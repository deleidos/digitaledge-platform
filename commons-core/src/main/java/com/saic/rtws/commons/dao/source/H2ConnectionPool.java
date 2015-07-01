package com.saic.rtws.commons.dao.source;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.saic.rtws.commons.config.ConfigEncryptor;

/**
 * A wrapper around BoneCP's ConnectionPool class so it has a public constructor
 * and works with Ingests's XML configuration system. The class has an empty public constructor
 * so it will accept changes to its bean properties during XML load. Then when the first
 * callis made to get a connection, it creates the BoneCP and delegates the
 * remaining work there. It appears that once the URL, user, and password is set, it cannot
 * be changed.
 * 
 * @author cannaliatot
 *
 */
public class H2ConnectionPool implements DataSource {
	
	// still using the H2 driver even though we use the Bone connection pool
	static {
        org.h2.Driver.load();
    }
	
	private BoneCPConfig config;
	private BoneCP pool;
    private int loginTimeout = 10 * 60 * 1000; // 10 minutes
    private int maxConnectionsPerPartition = 5;
    private int partitionCount = 6;
    private String user = "";
    private String password = "";
    private String url = "";
    private PrintWriter printWriter = null;
	
	public H2ConnectionPool() {
		// Needs to be a public constructor so it can integrate with Ingest.
		config = new BoneCPConfig();
	}
	
    /**
     * Get the current URL.
     *
     * @return the URL
     */
    public String getURL() {
        return url;
    }

    /**
     * Set the current URL.
     *
     * @param url the new URL
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * Set the current password
     *
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password = ConfigEncryptor.instance().decryptWithWrapper(password);
    }

    /**
     * Get the current password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the current user name.
     *
     * @return the user name
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the current user name.
     *
     * @param user the new user name
     */
    public void setUser(String user) {
        this.user = user;
    }

    public void setMaxConnectionsPerPartition(int maxConnectionsPerPartition) {
        if (maxConnectionsPerPartition < 1) {
            throw new IllegalArgumentException("Invalid maxConnectionsPerPartition value: " + maxConnectionsPerPartition);
        }
        this.maxConnectionsPerPartition = maxConnectionsPerPartition;
    }

    public int getMaxConnectionsPerPartition() {
    	return maxConnectionsPerPartition;
    }
    
    protected int getPartitionCount() {
		return partitionCount;
	}

	protected void setPartitionCount(int partitionCount) {
		this.partitionCount = partitionCount;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return printWriter;
	}

	@Override
	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		this.printWriter = logWriter;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return loginTimeout;
	}

	@Override
	public void setLoginTimeout(int loginTimeout) throws SQLException {
		this.loginTimeout = loginTimeout;
	}

	@Override
	public boolean isWrapperFor(Class<?> classObj) throws SQLException {
		return classObj.isInstance(pool);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> classObj) throws SQLException {
		if (classObj.isInstance(pool)) {
			return (T)pool;			
		}
		else {
			return null;
		}
	}

	@Override
	public synchronized Connection getConnection() throws SQLException {
		if (pool == null) {
			config.setJdbcUrl(url);
			config.setUsername(user); 
			config.setPassword(password);
			config.setConnectionTimeoutInMs(loginTimeout);
			config.setMinConnectionsPerPartition(1);
			config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
			config.setPartitionCount(partitionCount);
			pool = new BoneCP(config); // setup the connection pool
		}
		return pool.getConnection();
	}
	
	@Override
	public Connection getConnection(String user, String password)
			throws SQLException {
		// The H2 JdbcConnectionPool class actually actually threw this exception as well
		// The BoneCP class does not even include a method with the signature getConnection(String user, String password)
		throw new UnsupportedOperationException();
	}

}
