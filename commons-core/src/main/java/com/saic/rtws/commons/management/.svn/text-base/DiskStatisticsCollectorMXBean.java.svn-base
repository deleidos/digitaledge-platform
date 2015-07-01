package com.saic.rtws.commons.management;

import java.util.Map;

/**
 * Interface for the Disk Statistics Collector MXBean.  It is a helper bean that allows
 * the Disk Statistics Collector to communicate with other JMX Nodes to pull disk stats.
 * 
 * @author rainerr
 *
 */

public interface DiskStatisticsCollectorMXBean {
	public void updateStatus(String objectNameString, String jmxUrl);
	public Map<String, Double> fetch();
	
}
