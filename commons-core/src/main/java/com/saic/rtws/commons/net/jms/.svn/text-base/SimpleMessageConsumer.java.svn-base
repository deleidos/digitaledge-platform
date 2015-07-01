package com.saic.rtws.commons.net.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class SimpleMessageConsumer implements BasicMessageConsumer {

	private ConnectionFactory factory;
	private String queue;
	private MessageConsumer consumer;
	private Connection connection;
	private Session session;

	public SimpleMessageConsumer(ConnectionFactory factory, String queue) {
		this.factory = factory;
		this.queue = queue;
	}

	@Override
	public void open() throws JMSException {
		if(!isConnected()) try {
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(this.queue);
			consumer = session.createConsumer(dest, null, false);

		} catch(JMSException e) {
			close();
			throw e;
		}
	}

	@Override
	public void close() {
		try {consumer.close();} catch (Exception ignore) { }
		try {session.close();} catch (Exception ignore) { }
		try {connection.close();} catch (Exception ignore) { }
		consumer = null;
		connection = null;
		session = null;
	}

	@Override
	public void reset() throws JMSException {
		close();
		open();
	}
	
	@Override
	public void start() throws JMSException {
		if (connection != null) {
			connection.start();
		}
	}
	
	@Override
	public void stop() throws JMSException {
		if (connection != null) {
			connection.stop();
		}
	}

	@Override
	public boolean isConnected() {
		return connection != null;
	}

	@Override
	public void setMessageListener(MessageListener messagelistener) throws JMSException {
		if (consumer != null) {
			consumer.setMessageListener(messagelistener);
		}
	}

}
