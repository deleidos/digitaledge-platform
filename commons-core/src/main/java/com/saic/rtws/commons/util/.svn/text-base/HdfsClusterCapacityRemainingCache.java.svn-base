package com.saic.rtws.commons.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.net.jmx.JmxConnection;

public enum HdfsClusterCapacityRemainingCache {

	instance;

	public enum KEYS {
		CapacityTotal, CapacityUsed, CapacityRemaining
	}

	private double percentageUsed;

	private double percentageRemaining;

	private long lastCalculated;

	private boolean initialCheckPerformed = false;

	@SuppressWarnings("rawtypes")
	private Map cache = new HashMap();

	private HdfsClusterCapacityRemainingCache() {
		this.percentageUsed = 0.00d;
	}

	private void calculatePercentages() {
		Logger logger = Logger.getLogger(HdfsClusterCapacityRemainingCache.class);

		double capacityTotal = 0.00d;
		double capacityUsed = 0.00d;

		try {
			updateCache(logger);

			if (cache.get(KEYS.CapacityTotal.toString()) != null) {
				capacityTotal = ((Long) cache.get(KEYS.CapacityTotal.toString())).doubleValue();
			}

			if (cache.get(KEYS.CapacityUsed.toString()) != null) {
				capacityUsed = ((Long) cache.get(KEYS.CapacityUsed.toString())).doubleValue();
			}

			if (capacityTotal > 0) {

				if (logger.isDebugEnabled())
					logger.info(String.format("calculated capacity: '{%s}'", capacityUsed / capacityTotal));

				percentageUsed = (capacityUsed / capacityTotal) * 100.0d;
				percentageRemaining = 100.0d - percentageUsed;

				if (logger.isDebugEnabled())
					logger.info(String.format("PercentageRemaining: '{%s}'  PercentageUsed: '{%s}'", percentageRemaining, percentageUsed));

				lastCalculated = System.nanoTime();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
		}
	}

	public void fetch() {
		calculatePercentages();
	}

	@SuppressWarnings("rawtypes")
	public Map getCache() {
		init();
		return cache;
	}

	public Object getCachedValue(String key) {

		init();

		if (cache.get(key) != null)
			return cache.get(key);

		return null;
	}

	public double getRemainingCapacityPercentage() {

		init();

		// Testability hook
		if (Boolean.getBoolean("RTWS_TEST_MODE")) {
			percentageRemaining = Double.valueOf(System.getProperty("RTWS_TEST_HDFS_CLUSTER_REMAINING_CAPACITY", "0"));
		}
		return percentageRemaining;
	}

	public double getUsedCapacityPercentage() {

		init();

		// Testability hook
		if (Boolean.getBoolean("RTWS_TEST_MODE")) {
			percentageUsed = Double.valueOf(System.getProperty("RTWS_TEST_HDFS_CLUSTER_USED_CAPACITY", "0"));
		}
		return percentageUsed;
	}

	private void init() {
		if (!initialCheckPerformed) {
			calculatePercentages();
			initialCheckPerformed = true;
		}

		// Only query the namenode every N minutes for data
		long estimatedTimeInMinutes = TimeUnit.NANOSECONDS.toMinutes(System.nanoTime() - lastCalculated);

		if (estimatedTimeInMinutes > 3)
			calculatePercentages();
	}

	@SuppressWarnings("unchecked")
	private void updateCache(Logger logger) {

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
			logger.info(String.format("Fetching Hadoop Metrics from '{%s}'", connection.getJmxUrl()));

			MBeanServerConnection jmx = connection.connectToBeanServer();
			Set<ObjectInstance> allMbeans = jmx.queryMBeans(new ObjectName("Hadoop:service=NameNode,name=FSNamesystemState"), null);
			for (ObjectInstance objectInstance : allMbeans) {

				MBeanInfo mBeanInfo = jmx.getMBeanInfo(objectInstance.getObjectName());
				for (MBeanAttributeInfo attribute : mBeanInfo.getAttributes()) {

					if (logger.isDebugEnabled()) {
						logger.debug(attribute.getName() + " -> " + jmx.getAttribute(objectInstance.getObjectName(), attribute.getName()));
					}

					cache.put(attribute.getName(), jmx.getAttribute(objectInstance.getObjectName(), attribute.getName()));

				}
			}

		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (connection != null)
					connection.closeConnection();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
