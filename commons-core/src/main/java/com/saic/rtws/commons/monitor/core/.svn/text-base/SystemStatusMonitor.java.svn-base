package com.saic.rtws.commons.monitor.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import javax.management.ObjectName;
import com.saic.rtws.commons.monitor.core.ManagedMonitor;
import com.saic.rtws.commons.net.jmx.JmxConnectionFactory;
import com.saic.rtws.commons.net.jmx.JmxConnectionFactoryImpl;
import com.saic.rtws.commons.net.jmx.JmxMBeanServerConnection;

public class SystemStatusMonitor extends ManagedMonitor {
	
	/** Factory used to connect to the master node's JMX server. */
	private JmxConnectionFactory connectionFactory = new JmxConnectionFactoryImpl();

	/**
	 * Instantiates a new system monitor with an immutable name.
	 *
	 * @param name the immutable name
	 */
	public SystemStatusMonitor(String name) {
		super(name);
	}

	/**
	 * This method allows the system status monitor to be set up
	 * with an error state and locked permanently.  This is necessary
	 * because SystemStatusMonitor is currently placed in CommandListener
	 * which is started on each node.  This results in a SystemStatusMonitor
	 * existing on every node in a system, when truly it should only live
	 * and function on the master node.
	 * 
	 * @param invalidationMessage  the message to set as the reason for invalidation
	 */
	public void invalidateMonitor(String invalidationMessage) {
		unlockState();
		removeAllErrors();
		addError(invalidationMessage);
		lockState();
	}
	
	/**
	 * Monitors the basic process controller monitors.  This will give
	 * a status on all the process groups to make sure they are reporting
	 * a OK status.
	 * 
	 * In addition to the basic process controller monitors, the process
	 * group monitor for master must also be monitored individually
	 * since master is not controlled by a basic process controller.
	 */
	@Override
	public void monitor() {
		boolean statusOk = true;	
		
		// Start with monitoring the master process group monitor
		if(!masterProcessGroupMonitorOk()) {
			statusOk = false;
		}
								
		// Monitor all the basic process controller monitor beans.
		if(!basicProcessControllerMonitorsOk()) {
			statusOk = false;
		}
		
		if(statusOk) {
			setStatus(MonitorStatus.OK);
		}
	}

	/**
	 * Monitor the process group monitor for master.  This is required
	 * because there is no basic process controller that controls
	 * the master process.
	 *
	 * @param connection the JMX connection to connect to the master process group monitor
	 * @return true, if the status for the master process group monitor was MonitoredStatus.OK
	 */
	private boolean masterProcessGroupMonitorOk() {
		JmxMBeanServerConnection connection = null;
		
		boolean statusOk = true;
		
		try {
			// Connect to the master node's JMX server for ProcessGroupMonitors
			((JmxConnectionFactoryImpl) connectionFactory).setPort(1098);
			connection = establishJmxConnection(connectionFactory);
			
			if(connection != null) {
				ObjectName pattern = ObjectName.getInstance("rtws.saic.com:type=ProcessGroupMonitor");
				
				// Get the status for each bean
				Set<ObjectName> availableMonitors = connection.queryNames(pattern, null);
				if(availableMonitors.size() == 0) {
					addError("No process group monitor found for master.");
					statusOk = false;
				}
				else {
					for (ObjectName monitor : availableMonitors) {
						MonitorStatus processGroupStatus = MonitorStatus.fromString((String)connection.getAttribute(monitor, "Status"));
						String monitorName = (String)connection.getAttribute(monitor, "Name");
						if(processGroupStatus != MonitorStatus.OK) {
							addErrors(monitorName, (String[])connection.getAttribute(monitor, "ErrorMessages"));
							statusOk = false;
						}
						else {
							removeErrors(monitorName);
						}
					}
				}
			}
			else {
				// Cannot establish a connection
				addError("Unable to connect to master process group monitor.");
				statusOk = false;
			}
			
		} catch (Exception e) {
			// Something is wrong, we have an error
			recordExceptionAsError(e);
			statusOk = false;
		} finally {
			try {connection.close();} catch (Exception ignore) { }
		}
		
		return statusOk;
	}
	
	/**
	 * Monitor basic process controller monitors. Verifies that all known process groups have running
	 * basic process controller monitors.
	 *
	 * @param connection the JMX connection to connect to the basic process controller monitors
	 * @return true, if the status for all the basic process controller monitors were MonitoredStatus.OK
	 */
	private boolean basicProcessControllerMonitorsOk() {
		JmxMBeanServerConnection connection = null;
		
		boolean statusOk = true;
		try {
			// Connect to the master node's JMX server.
			((JmxConnectionFactoryImpl) connectionFactory).setPort(1099);
			connection = establishJmxConnection(connectionFactory);
			
			if(connection != null) {
				// Check the names of the process group to verify we have monitors for each,
				// since each BasicProcessController controls one process group
				ArrayList<String> processGroupNames = new ArrayList<String>();
				ObjectName pattern = ObjectName.getInstance("rtws.saic.com:type=ProcessGroup,*");
				for (ObjectName processGroup : connection.queryNames(pattern, null)) {
					processGroupNames.add((String)connection.getAttribute(processGroup, "Name"));
				}
				
				if(processGroupNames.isEmpty()) {
					addError("No basic process controller monitors found.");
					statusOk = false;
					return statusOk;
				}
				
				// Get the BasicProcessControllers 
				pattern = ObjectName.getInstance("rtws.saic.com:type=BasicProcessControllerMonitor,*");
				for (ObjectName monitor : connection.queryNames(pattern, null)) {
					MonitorStatus processGroupStatus =  MonitorStatus.fromString((String)connection.getAttribute(monitor, "Status"));
					String monitorName = (String)connection.getAttribute(monitor, "Name");
					if( processGroupStatus != MonitorStatus.OK &&
						processGroupStatus != MonitorStatus.STARTING ) {
						addErrors(monitorName, (String[])connection.getAttribute(monitor, "ErrorMessages"));
						statusOk = false;
						
						// Alert if this BasicProcessController is of an unknown process group
						if(!processGroupNames.contains(monitorName)) {
							addError(String.format("WARNING: Unknown process group %s causing %s status.",
													monitorName, MonitorStatus.ERROR.toString()));
						}
					}
					else {
						removeErrors(monitorName);
					}
					
					// Remove the monitorName from the known process group names to track
					// which monitors we have checked
					processGroupNames.remove(monitorName);
				}
				
				// Make sure we checked all the known process groups
				if(!processGroupNames.isEmpty()) {
					statusOk = false;
					for(String processGroupName : processGroupNames) {
						addError(String.format("No process controller monitor found for %s.", processGroupName));
					}
				}
			}
			else {
				// Cannot establish a connection
				addError("Unable to connect to basic process controller monitors in the cluster manager.");
				statusOk = false;
			}
		} catch (Exception e) {
			// Something is wrong, we have an error
			recordExceptionAsError(e);			
			statusOk = false;
		} finally {
			try {connection.close();} catch (Exception ignore) { }
		}
		
		return statusOk;
	}
	
	private JmxMBeanServerConnection establishJmxConnection(JmxConnectionFactory connectionFactory) {
		int connectionRetryCount = 15;	// Try multiple times to establish a JMX connection
			
		while(connectionRetryCount > 0) {
			try {
				return connectionFactory.getConnection();
			} catch (IOException ioe) {
				connectionRetryCount--;
			}
		}
		
		return null;
	}
	
	/**
	 * Record exception as error.
	 *
	 * @param e the exception
	 */
	private void recordExceptionAsError(Exception e) {
		addError(String.format("Exception caught: %s", e.getMessage()));
	}

	/* (non-Javadoc)
	 * @see com.saic.rtws.commons.management.ManagedBean#buildObjectNameKeys(java.util.Properties)
	 */
	@Override
	public void buildObjectNameKeys(Properties keys) {
		keys.put("type", "SystemStatusMonitor");
	}
}
