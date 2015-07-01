package com.saic.rtws.commons.net.jmx;

import java.io.IOException;

import javax.management.remote.JMXServiceURL;

public class JmxConnectionFactoryImpl implements JmxConnectionFactory {

	private String host = "localhost";
	private int port = 1099;
	private String connector = "jmxrmi";

	public JmxConnectionFactoryImpl() {
		super();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public JmxMBeanServerConnection getConnection() throws IOException {
		return getConnection(host, port, connector);
	}

	public JmxMBeanServerConnection getConnection(String host) throws IOException {
		return getConnection(host, port, connector);
	}

	public JmxMBeanServerConnection getConnection(String host, int port, String connector) throws IOException {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/" + connector);
		return new JmxMBeanServerConnectionImpl(url);
	}

}
