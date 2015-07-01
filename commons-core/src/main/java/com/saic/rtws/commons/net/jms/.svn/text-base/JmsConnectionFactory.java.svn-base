package com.saic.rtws.commons.net.jms;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Connection factories should be provided through dependency injection. This limits
 * testability and hard codes the application behavior to a specific vendor.
 * 
 * @deprecated
 */
public class JmsConnectionFactory {
	
	private String url = "";
	
	public JmsConnectionFactory(String jmsUrl) {
		url = jmsUrl;
	}
	
	public ConnectionFactory getConnectionFactory() {
		return new ActiveMQConnectionFactory(url);
	}
}
