package com.saic.rtws.commons.net.listener.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.jersey.config.JerseyClientConfig;
import com.saic.rtws.commons.management.InitializableManagementContext;
import com.saic.rtws.commons.monitor.core.ProcessGroupMonitor;
import com.saic.rtws.commons.monitor.core.SystemStatusMonitor;
import com.saic.rtws.commons.net.listener.util.UserDataUtil;
import com.saic.rtws.commons.util.env.SystemEnvUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class CommandListenerMonitorAccess {

	private static Logger logger = LogManager.getLogger(CommandListenerMonitorAccess.class);
	
	/**
	 * A context to manage the monitors
	 */
	private static InitializableManagementContext jmx = null;
	
	/**
	 * The System status monitor will only be started correctly for the
	 * master instance.  All other system status monitors will be given
	 * an error status, an error message indicating that it is not running,
	 * and be locked in that state. 
	 */
	private static SystemStatusMonitor systemStatusMonitor = null;
	
	/**
	 * The monitor for the process group this CommandListener is running on.
	 * It is included with the CommandListener to make use of JVM external
	 * to the main DigitalEdge system.
	 */
	private static ProcessGroupMonitor processGroupMonitor = null;
	
	private static class CommandListenerMonitorAccessSingletonHolder { 
		static final CommandListenerMonitorAccess instance = new CommandListenerMonitorAccess();
	}

	public static CommandListenerMonitorAccess getInstance() {
		return CommandListenerMonitorAccessSingletonHolder.instance;
	}
	
	private CommandListenerMonitorAccess() {

		initializeJmxContext();
		
	}
	
	private void initializeJmxContext() {
		// This command listener is sitting behind a NAT so we must set the rmi server hostname
		// property to point to the NAT because that is the entry point. Only do this for
		// the master instance because it is the instance that serves the system status back
		// to the gateway instance.
		
		String host = UserDataUtil.getHostFromFqdn();
		if (host.equals("master") && SystemEnvUtil.isNatConfigured()) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("Fail to retrieve and set RMI server hostname. This will causes system status reporting problem.");
			
			try {
				Client client = Client.create(JerseyClientConfig.getInstance().getInternalConfig());
					
				WebResource resource = client.resource(String.format("%s/json/system/nat/%s", 
						RtwsConfig.getInstance().getString("webapp.gatewayapi.url.path"), 
						UserDataProperties.getInstance().getString(UserDataProperties.RTWS_DOMAIN)));
					
				String jsonString = resource.get(String.class);
				String publicNatIP = JSONObject.fromObject(jsonString).getString("publicIpAddress");
			
				if (publicNatIP != null) {
					System.setProperty("java.rmi.server.hostname", publicNatIP);
					logger.info("RMI server hostname set to NAT ip address '" + publicNatIP + "'.");
				} else {
					logger.warn(errorMsg.toString());
				}
			} catch (Exception ex) {
				logger.warn(errorMsg.toString(), ex);
			}
		}
				
		String jmxPort = System.getProperty("com.saic.rtws.management.commandListener.jmxPort", "61515");
		String rmiPort = System.getProperty("com.saic.rtws.management.commandListener.rmiPort", "1098");
		String connectorName = System.getProperty("com.saic.rtws.management.connectorName", "jmxrmi");
		
		// Make sure it is managed by JMX
		jmx = new InitializableManagementContext(jmxPort, rmiPort, connectorName, "rtws.saic.com");
		jmx.initialize();
		
		logger.info("JMX management context initialized.");
	}
	
	public void startSystemStatusMonitor() {
		if(systemStatusMonitor == null) {
			// Only allow the system status monitor to run if this is the master node
			String host = UserDataUtil.getHostFromFqdn();
			if(host.equals("master")) {
				// Get the domain of the system passed in with user data
				String name = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_DOMAIN);
				
				// Setup the monitor
				systemStatusMonitor = new SystemStatusMonitor(name);
				
				// Register the monitor with JMX
				jmx.register(systemStatusMonitor);
				
				// Start the monitor
				systemStatusMonitor.start();
				
				logger.info("System status monitor registered and started.");
			}
		}
		else {
			logger.info("Unable to start system status monitor - monitor is already running.");
		}
	}
	
	public void startProcessGroupMonitor() {
		if(processGroupMonitor == null) {
			// Get the name of the node we are on from the FQDN passed in with user data
			String name = UserDataUtil.getHostFromFqdn();
			
			// Setup the monitor instance
			processGroupMonitor = new ProcessGroupMonitor(name);
			jmx.register(processGroupMonitor);  // Register the monitor with JMX
			processGroupMonitor.start();
			
			logger.info("Process group monitor registered and started.");
		}
		else {
			logger.info("Unable to start process gorup monitor - monitor is already running.");
		}
	}
	
	public void shutdownProcessGroupMonitor() {
		if(processGroupMonitor != null) {
			processGroupMonitor.shutDown();
		}
		else {
			logger.info("Unable to stop process gorup monitor - monitor is not running.");
		}
	}
	
}
