package com.saic.rtws.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.net.jmx.JmxConnection;

public class JmsQueueDetailsUtil {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JmsQueueDetailsUtil app = new JmsQueueDetailsUtil();

		while (true)
			app.run(Integer.valueOf(args[0]));

	}

	private void run(int numOfBrokers) {

		String[] brokerTypes = { "ext", "int" };
		for (String brokerType : brokerTypes) {

			for (int i = 1; i <= numOfBrokers; i++) {
				JmxConnection connection = null;
				List<String> queues = new ArrayList<String>();
				try {

					connection = new JmxConnection();
					String brokerUrl = String.format("service:jmx:rmi:///jndi/rmi://jms-%s-node%s.%s:1099/jmxrmi", brokerType, i,
							UserDataProperties.getInstance().getString(UserDataProperties.RTWS_DOMAIN));
					System.out.println(String.format("Connecting to: %s", brokerUrl));
					connection.setJmxUrl(brokerUrl);

					MBeanServerConnection jmx = connection.connectToBeanServer();
					Set<ObjectInstance> allMbeans = jmx.queryMBeans(new ObjectName("org.apache.activemq:BrokerName=localhost,Type=Broker"), null);
					for (ObjectInstance objectInstance : allMbeans) {

						MBeanInfo mBeanInfo = jmx.getMBeanInfo(objectInstance.getObjectName());
						for (MBeanAttributeInfo attribute : mBeanInfo.getAttributes()) {

							if (attribute.getName().equals("MemoryPercentUsage"))
								System.out.println(attribute.getName() + " -> " + jmx.getAttribute(objectInstance.getObjectName(), attribute.getName()));

							if (attribute.getName().equals("Queues")) {
								ObjectName[] o = (ObjectName[]) jmx.getAttribute(objectInstance.getObjectName(), attribute.getName());
								for (ObjectName on : o) {
									queues.add(on.getCanonicalName());
								}
							}
						}

						StringBuilder sb = new StringBuilder();
						for (String queueON : queues) {
							Set<ObjectInstance> queueAllBeans = jmx.queryMBeans(new ObjectName(queueON), null);
							for (ObjectInstance oi : queueAllBeans) {
								MBeanInfo mbi = jmx.getMBeanInfo(oi.getObjectName());
								for (MBeanAttributeInfo attribute : mbi.getAttributes())
									if (attribute.getName().equals("Name") || attribute.getName().equals("QueueSize") || attribute.getName().equals("ConsumerCount"))
										sb.append(attribute.getName() + " -> " + jmx.getAttribute(oi.getObjectName(), attribute.getName()) + " \n");
							}
						}
						System.out.println(sb.toString());
						sb.setLength(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (connection != null)
						try {
							connection.closeConnection();

						} catch (Exception e) {
							e.printStackTrace();
						}
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
