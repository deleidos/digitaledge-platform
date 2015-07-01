package com.saic.rtws.commons.net.jmx;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import javax.xml.bind.annotation.XmlTransient;

import com.saic.rtws.commons.dao.exception.DataAccessException;

public class JmxConnection {
	
	private RMIConnector rmiConnector;
	private String jmxUrl;
	private String objectName;
	private ObjectName jmxObjectName;
	
	public JmxConnection() {
		
	}
	
	public JmxConnection(String jmxUrl, String objectName) {
		this.jmxUrl = jmxUrl;
		this.objectName = objectName;
	}
	
	public void setJmxUrl(String jmxUrl) {
		this.jmxUrl = jmxUrl;
	}
	
	public String getJmxUrl() {
		return jmxUrl;
	}
	
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	public String getObjectName() {
		return objectName;
	}
	
	@XmlTransient
	public void setJmxObjectName(ObjectName objectName) {
		this.jmxObjectName = objectName;
	}
	
	public ObjectName getJmxObjectName() {
		return jmxObjectName;
	}
	
	public MBeanServerConnection connectToBeanServer() throws Exception {
		
		MBeanServerConnection mbsc = null;
		JMXServiceURL jmxServiceURL = null;
		
		try {
			jmxServiceURL = new JMXServiceURL(jmxUrl);
		} catch (NullPointerException npe) {
			DataAccessException dae = new DataAccessException("Must specify a URL for the JMX connection, no URL specified.", npe);
			throw dae;  // rethrow exception 
		} catch (MalformedURLException mue) {
			DataAccessException dae = new DataAccessException("URL is not in the correct format, expecting format: service:jmx:rmi://host:port", mue);
			throw dae;  // rethrow exception
		} 
		
		if ((rmiConnector == null) || (!(isRMIConnectionOpen()))) {
			rmiConnector = new RMIConnector(jmxServiceURL, null); 
		} 
				
		try {
			// Has no effect if connector is already connected
			rmiConnector.connect();
			// Two calls to this method should return the same connection instance.
		    mbsc = rmiConnector.getMBeanServerConnection();
		} catch (IOException ioe) {
			throw(ioe);
		}
		
		return mbsc;
	}
	
	public void createObjectName() throws DataAccessException {
		try {
			jmxObjectName = new ObjectName(objectName);
		} catch (MalformedObjectNameException mne) {
			DataAccessException dae = new DataAccessException("Object Name is not in the correct format ", mne);
			throw dae;  // rethrow exception 
		} catch (NullPointerException npe) {
			DataAccessException dae = new DataAccessException("Must specify an ObjectName for the JMX connection, no ObjectName specified.", npe);
			throw dae;  // rethrow exception 
		}
	}
	
	public void closeConnection() throws Exception {
		if (rmiConnector != null) {
			rmiConnector.close();
		}
		// Connection is no longer good and cannot be used again once it has been closed.
		rmiConnector = null;
	}
	
	public boolean isRMIConnectionOpen() {
		boolean isOpen = false;
		
		try {
			rmiConnector.getConnectionId();
			isOpen = true;
		} catch (IOException ioe) {
			// Will throw this exception if the connection is not open.
			// This is the best test possible.
			isOpen = false;
		}
		
		return isOpen;
	}

}
