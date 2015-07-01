package com.saic.rtws.commons.monitor.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.jmx.JmxConnection;
import com.saic.rtws.commons.net.listener.exception.ServerException;

/**
 * Simple monitor which re-writes the /etc/hosts file with the hadoop/hive/hbase/hue process group instances
 * 
 * TODO Delete Monitor with reverse dns ticket has been resolved D-01924
 * 
 */
public class HadoopEtcHostsMonitor extends ProcessMonitor {

	private File etcHosts;

	private File etcHostsWorking;

	private List<String> header = new LinkedList<String>();

	private String provider;

	private List<String> processGroups = new LinkedList<String>();

	private Logger logger = LogManager.getLogger(getClass());

	public HadoopEtcHostsMonitor(String name) {
		super(name);

		provider = RtwsConfig.getInstance().getString("rtws.cloud.provider");

		/*
		 * Hook for local testing
		 */
		if (Boolean.getBoolean("RTWS_TEST_MODE")) {
			etcHosts = new File("/tmp/hosts");
			etcHostsWorking = new File("/tmp/hosts.working");

			// force EUC provider
			provider = "EUC";

		} else {
			etcHosts = new File("/etc/hosts");
			etcHostsWorking = new File("/etc/hosts.working");
		}

		/*
		 * only monitor at 3 minute intervals
		 */
		setMonitorInterval(1000 * 60 * 3);

		// Always add the following to the created /etc/hosts
		header.add("127.0.0.1 localhost");
		header.add("# The following lines are desirable for IPv6 capable hosts");
		header.add("::1 ip6-localhost ip6-loopback");
		header.add("fe00::0 ip6-localnet");
		header.add("ff00::0 ip6-mcastprefix");
		header.add("ff02::1 ip6-allnodes");
		header.add("ff02::2 ip6-allrouters");
		header.add("ff02::3 ip6-allhosts");

		/*
		 * Populated those process groups to query Expected format <pgrpName>;<pgrpName>;<pgrpName>
		 */
		processGroups.addAll(Arrays.asList(RtwsConfig.getInstance().getString("rtws.pgroups.to.etc.hosts").split(";")));
	}

	private List<String> queryForProcessGroupHostnames() {
		List<String> hostnames = new LinkedList<String>();

		JmxConnection connection = new JmxConnection();
		connection.setJmxUrl("service:jmx:rmi:///jndi/rmi://master." + UserDataProperties.getInstance().getString(UserDataProperties.RTWS_DOMAIN) + ":1099/jmxrmi");

		try {
			MBeanServerConnection jmx = connection.connectToBeanServer();
			Set<ObjectInstance> allMbeans = jmx.queryMBeans(new ObjectName("rtws.saic.com:name=*,type=Process,group=*"), null);
			for (ObjectInstance objectInstance : allMbeans) {

				if (processGroups.contains(objectInstance.getObjectName().getKeyProperty("group"))) {

					MBeanInfo mBeanInfo = jmx.getMBeanInfo(objectInstance.getObjectName());
					for (MBeanAttributeInfo attribute : mBeanInfo.getAttributes()) {

						if (attribute.getName().equals("HostName")) {
							hostnames.add((String) jmx.getAttribute(objectInstance.getObjectName(), attribute.getName()));
						}
					}
				}
			}

		} catch (Exception e) {
			addError(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.closeConnection();
			} catch (Exception e) {
				addError(e.getMessage());
			}
		}

		return hostnames;
	}

	@Override
	protected void monitor() {
		try {

			// only run for cloud environments that aren't AWS
			if (StringUtils.isNotBlank(provider) == true && !"AWS".equals(provider.toUpperCase())) {

				if (!etcHosts.canWrite()) {
					throw new ServerException("Unable to modify: " + etcHosts.getAbsolutePath());
				}
				if (!etcHostsWorking.createNewFile()) {
					if (!etcHostsWorking.exists())
						throw new ServerException("Unable to create working copy of /etc/hosts");
				}

				String hostname = InetAddress.getLocalHost().getHostName();
				String ip = InetAddress.getLocalHost().getHostAddress();

				if (hostname == null && ip == null) {
					throw new ServerException("Unable to determine hostname and/or ip address of this host.");
				}

				/*
				 * Query master for the process groups
				 */
				List<String> hostnamesToApply = queryForProcessGroupHostnames();
				
				if (hostnamesToApply.size() == 0) {
					throw new ServerException("Unable to determine hostnames for the requested process groups.");
				}

				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new FileWriter(etcHostsWorking));

					for (String hdrStr : header) {
						bw.write(hdrStr);
						bw.newLine();
					}

					for (String host : hostnamesToApply) {
						String hostIp = InetAddress.getByName(host).getHostAddress();
						bw.write(String.format("%s ip-%s %s", hostIp, hostIp.replaceAll("\\.", "-"), host));
						bw.newLine();
					}

					bw.flush();
				} finally {
					IOUtils.closeQuietly(bw);
				}

				/*
				 * Only write out /etc/hosts if required
				 */
				if (etcHostsWorking.length() != etcHosts.length()) {
					logger.info("Updating /etc/hosts");
					if (!etcHostsWorking.renameTo(etcHosts))
						addError("Unable to update /etc/hosts");
				}

				if (!etcHostsWorking.delete())
					etcHostsWorking.deleteOnExit();
			}

			setStatus(MonitorStatus.OK);
		} catch (ServerException e) {
			addError(e.getMessage());
		} catch (UnknownHostException e) {
			addError(e.getMessage());
		} catch (IOException e) {
			addError(e.getMessage());
		}
	}
}
