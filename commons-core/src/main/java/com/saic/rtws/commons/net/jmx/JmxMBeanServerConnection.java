package com.saic.rtws.commons.net.jmx;

import java.io.Closeable;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public interface JmxMBeanServerConnection extends MBeanServerConnection, Closeable {
	
	public MBeanProxy proxy(ObjectName name);
	
}
