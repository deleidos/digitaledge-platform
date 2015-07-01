package com.saic.rtws.commons.monitor.process;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.jms.RoundRobinJMSConnectionFactory;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.ActiveMQProcess;

public class ActiveMQMonitor extends ProcessMonitor {

	// ActiveMQProcess is a class provided in the CommandListener which determines
	// if the ActiveMQ software is installed and if it is currently running or not
	ActiveMQProcess activeMQProcess = ActiveMQProcess.newInstance();
	
	// Connection factory used to test connectivity
	RoundRobinJMSConnectionFactory connectionFactory = new RoundRobinJMSConnectionFactory();
	
	public ActiveMQMonitor(String processName) {
		super(processName);
		setStartupPeriod(1000 * 60 * 6);  // Give the ActiveMQ process 6 minutes to start
		setMonitorInterval(1000 * 30);	// Only monitor every 30 seconds
		
		// Initialize the connection factory used to test connectivity
		String url = buildBrokerUrl();
		String user = RtwsConfig.getInstance().getString("messaging.internal.connection.user", "system");
		String password = RtwsConfig.getInstance().getString("messaging.internal.connection.password", "<redacted>");
		
		if(url != null) {
			connectionFactory.setBrokerURL(url);
			connectionFactory.setUserName(user);
			connectionFactory.setPassword(password);
		}
		else {
			// Unable to determine the URL to use for the connection factory.
			// Report it and lock the monitor
			addError("Instance FQDN not found in User Data - unable to determine JMS Broker URL.");
			lockState();
		}
	}
	
	private String buildBrokerUrl() {
		String url = null;
		
		String fqdn = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_FQDN);
		if(fqdn != null) {
			StringBuilder sb = new StringBuilder("nio://");
			sb.append(fqdn);
			sb.append(":61617");
			
			url = sb.toString();
		}
		
		return url;
	}

	@Override
	public void monitor() {
		try {
			switch(activeMQProcess.getStatus()) {
				case Running :
					boolean success = testActiveMQConnection();
					if(success) {
						setStatus(MonitorStatus.OK);
					}
					break;
				case Stopped :
					addError("ActiveMQ server is not running.");
					break;
				case Unknown :
				default :
					addError("ActiveMQ server status is currently unknown.");
					break;
			}
		} catch (ServerException e) {
			// A ServerException is thrown if the ActiveMQ software
			// is not installed on the instance
			addError(e.getMessage());
		}
	}
	
	private boolean testActiveMQConnection() {
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		MessageConsumer consumer = null;
		try {
			
			// Create a connection
			try {
				connection = connectionFactory.createConnection();
				connection.start();
			} catch(JMSException jmse) {
				addError(String.format("Failed to establish connection: %s", jmse.getMessage()));
				return false;
			}
			
			// Create a session
			try {
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			} catch(JMSException jmse) {
				addError(String.format("Failed to establish session: %s", jmse.getMessage()));
				return false;
			}
	
			// Create/connect to a queue
			Destination testDestination = null;
			String queueName = "com.saic.rtws.monitor";
			try {
				testDestination = session.createQueue(queueName);
			} catch(JMSException jmse) {
				addError(String.format("Failed to connect to '%s' queue: %s", queueName, jmse.getMessage()));
				return false;
			}
			
			// Create a producer for the test queue
			try {
			    producer = session.createProducer(testDestination);
			} catch(JMSException jmse) {
				addError(String.format("Unable to create producer for '%s' queue: %s", queueName, jmse.getMessage()));
				return false;
			}
			
			// Create a consumer for the test queue
			try {
			    consumer = session.createConsumer(testDestination);
			} catch(JMSException jmse) {
				addError(String.format("Unable to create consumer for '%s' queue: %s", queueName, jmse.getMessage()));
				return false;
			}
			
			// Send a message to the test queue
			try {
			 	TextMessage message = session.createTextMessage("Monitoring ActiveMQ");
			    producer.send(message);
			    
			    // Give the message time to reach the queue
			    try {
			    	Thread.sleep(2000);
			    } catch (InterruptedException ie) { }
			} catch(JMSException jmse) {
				addError(String.format("Failed to send message to '%s' queue: %s", queueName, jmse.getMessage()));
				return false;
			}
			
			// Read the message from the test queue
			try {
			    consumer.receive();
			} catch(JMSException jmse) {
				addError(String.format("Failed to receive message from '%s' queue: %s", queueName, jmse.getMessage()));
				return false;
			}
		} catch (Exception e) {
			addError(String.format("Unable to verify ActiveMQ connectivity: %s", e.getMessage()));
			return false;
		} finally {
		    // Clean up
			try { producer.close();   } catch(Exception ignore) {}
			try { consumer.close();   } catch(Exception ignore) {}
			try { session.close();    } catch(Exception ignore) {}
			try { connection.close(); } catch(Exception ignore) {}
		}
		
		return true;
	}
}
