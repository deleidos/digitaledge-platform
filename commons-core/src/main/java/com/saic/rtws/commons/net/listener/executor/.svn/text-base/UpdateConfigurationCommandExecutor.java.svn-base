package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.jersey.config.JerseyClientConfig;
import com.saic.rtws.commons.net.jmx.JmxConnection;
import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.target.UpdateConfigurationCommandClient;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.TransportProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.util.ConfigurationUtil;
import com.saic.rtws.commons.net.listener.util.RestartAnyIngestProcessUtil;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class UpdateConfigurationCommandExecutor extends CommandExecutor {

	private static final String PROCESS_RETRIEVE_ALL_URL = "/json/process/retrieve/all";
	private static final String SERVICES_RETRIEVE_ALL_URL = "/json/services/retrieve/all";
	
	private static final BroadcastStatusMap status = new BroadcastStatusMap();
	
	static {
		status.registerCommand(Command.UPDATE_CONFIGURATION);
	}
	
	private Logger log = Logger.getLogger(getClass());

	public static UpdateConfigurationCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new UpdateConfigurationCommandExecutor(command, request, os);
	}

	public UpdateConfigurationCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}
	
	@Override
	public ExecuteResult execute() throws ServerException {
		if (SoftwareUtil.isMasterInstalled()) {
			String [] args = { this.command };
			return broadcast(UpdateConfigurationCommandClient.class.getName(), args, status);
		} else if (SoftwareUtil.isIngest() || SoftwareUtil.isTransportInstalled()) {
			log.info("Updating node configuration ...");
			if (downloadAndInstallConfiguration() && restartProcess()) {
				log.info("Updating node with new configuration complete.");
				return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
			} else {
				throw new ServerException("Failed to update configuration.");
			}
		} else {
			return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
		}
	}
	
	@Override
	protected List<String> parseHosts() {
		return getChildNodes();
	}
	
	private boolean downloadAndInstallConfiguration() {
		boolean status = false;
		
		if (SoftwareUtil.isInternalShardSearchApi()) {
			status = ConfigurationUtil.download() && ConfigurationUtil.untar(SoftwareUtil.SEARCHAPI_WEBAPP_CLASSES_PATH, SoftwareUtil.INGEST_CONF_FILE);
		} else if (SoftwareUtil.isIngestInstalled()) {
			status = ConfigurationUtil.download() && ConfigurationUtil.untar(SoftwareUtil.INGEST_CONF_DIR, SoftwareUtil.INGEST_CONF_FILE);
		} else if (SoftwareUtil.isTransportInstalled()) {
			status = ConfigurationUtil.download() && ConfigurationUtil.untar(SoftwareUtil.TRANSPORT_CONF_DIR, SoftwareUtil.TRANSPORT_CONF_FILE);
		} else {
			// We only support updating configuration for transport and ingest nodes.
			// If more are supported add here.
			
			status = true;
		}
		
		return status;
	}
	
	private boolean restartProcess() throws ServerException {
		boolean status = false;
		
		if (SoftwareUtil.isIngest()) {
			log.info("Restarting ingest process ...");
			if (RestartAnyIngestProcessUtil.restart()) {
				status = true;
			} 
		} else if (SoftwareUtil.isTransportInstalled()) {
			log.info("Restarting transport process ...");
			if (TransportProcess.newInstance().restart()) {
				status = true;
			}
		} else {
			// Add additional processes to start here.
		}
		
		return status;
	}
	
	/**
	 * Retrieve a list of nodes to update from retrieving the service.xml
	 * and processes.xml files.
	 */
	private List<String> getChildNodes() {
		return retrieveNodesFromProcessesFile(retrieveProcessGroupsFromServicesFile());
	}
	
	/**
	 * Invoke the repository retrieve services file REST service and store
	 * all the process groups in a set data structure. Expecting the response
	 * to be in the form.
	 * 
	 * {
	 *   "allocationPolicy": [
	 *     {
	 *       "group": "jms.external",
	 *       "min": 1",
	 *       "max": 1",
	 *       "autoScale" : false
	 *     },
	 *     ...
	 *   ]
	 * }
	 */
	private Set<String> retrieveProcessGroupsFromServicesFile() {
		HashSet<String> groups = new HashSet<String>();
		
		Client client = Client.create(JerseyClientConfig.getInstance().getInternalConfig());
		
		String url = RtwsConfig.getInstance().getString("webapp.repository.url.path");
		WebResource res = client.resource(url).path(SERVICES_RETRIEVE_ALL_URL);
		
		String response = res.get(String.class);
		
		log.debug("Retrieved services.xml file response: " + response);
		
		JSONArray policies = JSONObject.fromObject(response).getJSONArray("allocationPolicy");
		for (int i = 0; i < policies.size(); i++) {
			JSONObject policy = policies.getJSONObject(i);
			if (policy.has("group")) {
				String group = policy.getString("group");
				if (! group.equals("master")) {
					groups.add(group);
				}
			}
		}
		
		return groups;
	}
	
	/**
	 * Invoke the repository retrieve processes file REST service and return
	 * a list of child nodes (private ip addresses) that exists in the given
	 * process group set data structure. Expecting the response to be in the 
	 * form.
	 * 
	 * {
     *   "node": [
     *     {
     *       "id": "i-e653a780",
     *       "domain": "dm-sync.us-east1.deleidos.com",
     *       "group": "ingest.all",
     *       "host": "ingest-all-node1",
     *       "dnsName": "ingest-all-node1.dm-sync.us-east1.deleidos.com",
     *       "privateIp": "10.117.77.94",
     *       "persistentIp": null,
     *       "volumeIds": null
     *     },
	 *     ...
	 *   ]
	 * } 
	 */ 
	private List<String> retrieveNodesFromProcessesFile(Set<String> processGroups) {
		List<String> children = new ArrayList<String>();
		
		Set<String> activeDnsNames = getActiveNodeDnsNames();
		Client client = Client.create(JerseyClientConfig.getInstance().getInternalConfig());
		
		String url = RtwsConfig.getInstance().getString("webapp.repository.url.path");
		WebResource res = client.resource(url).path(PROCESS_RETRIEVE_ALL_URL);
		
		String response = res.get(String.class);
		
		log.debug("Retrieved processes.xml file response: " + response);
		
		JSONArray nodes = JSONObject.fromObject(response).getJSONArray("node");
		
		for (int i = 0; i < nodes.size(); i++) {
			JSONObject node = nodes.getJSONObject(i);
			String group = (node.has("group")) ? node.getString("group") : null;
			String privateIp = (node.has("privateIp")) ? node.getString("privateIp") : null;
			String dnsName = (node.has("dnsName")) ? node.getString("dnsName") : null;
			if (group != null && privateIp != null) {
				if (processGroups.contains(group) && ! group.equals("master") && 
					(activeDnsNames.size() > 0 && activeDnsNames.contains(dnsName))) {
					log.info("Adding host " + privateIp + " => " + group + " to update configuration list.");
					children.add(privateIp);
				} else {
					log.debug("Excluding host " + privateIp + " => " + group + " because its is either the master node or currently not active.");
				}
			} else {
				log.debug("Excluding node " + node.toString() + " because its missing group and/or private ip information.");
			}
		}
		
		return children;
	}
	
	/**
	 * Builds a list of the DNS names of nodes tracked by the local MBean server.  The Master process keeps
	 * a current list of live Nodes.  Nodes that are killed, de-scaled, etc. should not appear in this list.
	 * 
	 * @return the list of DNS values for Active nodes
	 */
	private Set<String> getActiveNodeDnsNames() {
		HashSet<String> activeDnsNames = new HashSet<String>();
		try {
			String rmiPort = System.getProperty("com.saic.rtws.management.rmiPort", "1099");
			String connectorName = System.getProperty("com.saic.rtws.management.connectorName", "jmxrmi");
			String jmxUrl = String.format("service:jmx:rmi:///jndi/rmi://localhost:%s/%s", rmiPort, connectorName);
			String domain = "rtws.saic.com:*";
			String processClass = "com.saic.rtws.master.core.management.ManagedProcess";
			
			JmxConnection connection = new JmxConnection();
			connection.setJmxUrl(jmxUrl);
			MBeanServerConnection jmx = connection.connectToBeanServer();
			ObjectName name = new ObjectName(domain);
			QueryExp query = Query.isInstanceOf(Query.value(processClass));
				     
			Set<ObjectInstance> mBeans = jmx.queryMBeans(name, query);
			for (ObjectInstance mBean: mBeans) {
				activeDnsNames.add(mBean.getObjectName().getKeyProperty("name"));
			}
		} catch (Exception e) {
			log.error("Failed to retrieve active node information from MBean server", e);
		}
		return activeDnsNames;
	}
	
}
