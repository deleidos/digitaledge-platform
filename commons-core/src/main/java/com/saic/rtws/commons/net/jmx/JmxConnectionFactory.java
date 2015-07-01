package com.saic.rtws.commons.net.jmx;

import java.io.IOException;

public interface JmxConnectionFactory {
	public JmxMBeanServerConnection getConnection() throws IOException;
	public JmxMBeanServerConnection getConnection(String host) throws IOException;
	public JmxMBeanServerConnection getConnection(String host, int port, String connector) throws IOException;
}
