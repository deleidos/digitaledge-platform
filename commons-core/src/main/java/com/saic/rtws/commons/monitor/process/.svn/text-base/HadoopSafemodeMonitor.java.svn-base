package com.saic.rtws.commons.monitor.process;

import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.jmx.JmxConnection;

public class HadoopSafemodeMonitor extends ProcessMonitor {

	private Logger log = LogManager.getLogger(getClass());

	public HadoopSafemodeMonitor(String name) {
		super(name);

		setMonitorInterval(1000 * 60 * 5);
	}

	@Override
	protected void monitor() {
		JmxConnection connection = null;
		String namenodeFqdn = null;
		// Test Hook
		if (Boolean.getBoolean("RTWS_TEST_MODE")) {
			namenodeFqdn = System.getProperty("RTWS_TEST_EXTERNAL_NAMENODE");
		} else {
			namenodeFqdn = "namenode." + UserDataProperties.getInstance().getString(UserDataProperties.RTWS_DOMAIN);
		}

		try {
			connection = new JmxConnection();
			// Example connection url: service:jmx:rmi://namenode.@build.domain@:8004/jndi/rmi://namenode.@build.domain@:8005/jmxrmi
			connection.setJmxUrl("service:jmx:rmi://" + namenodeFqdn + ":8004/jndi/rmi://" + namenodeFqdn + ":8005/jmxrmi");
			log.info(String.format("Fetching Hadoop Metrics from '{%s}'", connection.getJmxUrl()));

			MBeanServerConnection jmx = connection.connectToBeanServer();
			Set<ObjectInstance> allMbeans = jmx.queryMBeans(new ObjectName("Hadoop:service=NameNode,name=FSNamesystemState"), null);
			for (ObjectInstance objectInstance : allMbeans) {

				MBeanInfo mBeanInfo = jmx.getMBeanInfo(objectInstance.getObjectName());
				for (MBeanAttributeInfo attribute : mBeanInfo.getAttributes()) {

					if (log.isDebugEnabled()) {
						log.debug(attribute.getName() + " -> " + jmx.getAttribute(objectInstance.getObjectName(), attribute.getName()));
					}

					if (attribute.getName().equals("FSState")) {
						if (log.isDebugEnabled())
							log.debug(String.format("Hadoop cluster state: %s", jmx.getAttribute(objectInstance.getObjectName(), attribute.getName())));

						if (((String) jmx.getAttribute(objectInstance.getObjectName(), attribute.getName())).toLowerCase().equals("safemode")) {
							addError("The Hadoop cluster is in Safe Mode.  All dependent components will not start and/or work properly while the cluster is in this state.");
							setStatus(MonitorStatus.ERROR);
						} else {
							setStatus(MonitorStatus.OK);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (connection != null)
					connection.closeConnection();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

	}
}
