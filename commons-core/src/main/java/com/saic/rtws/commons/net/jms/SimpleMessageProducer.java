package com.saic.rtws.commons.net.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * BasicMessageProducer implementation that sends messages to a single destination.
 */
public class SimpleMessageProducer extends AbstractMessageProducer implements BasicMessageProducer {

	private String destinationName;
	private DestinationType destinationType;
	
	private Destination destination;

	/**
	 * Constructor.
	 * 
	 * @param factory Factory to be used when opening a connection.
	 * @param type Type of destination to be used (topic/queue).
	 * @param destination Name of the topic/queue.
	 */
	protected SimpleMessageProducer(ConnectionFactory factory, DestinationType type, String destination) {
		super(factory);
		destinationName = destination;
		destinationType = type;
	}

	public void open() throws JMSException {
		super.open();
		destination = createDestination(destinationName, destinationType, session);
	}
	
	public void close() {
		destination = null;
		super.close();
	}

	
	
	public void send(Message message) throws JMSException {
		producer.send(destination, message);
	}

	public void send(Destination destination, Message message) throws JMSException {
		producer.send(destination, message);
	}

	public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		producer.send(destination, message, deliveryMode, priority, timeToLive);
	}

	public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		producer.send(destination, message, deliveryMode, priority, timeToLive);
	}

	@Override
	public void sendImmediately(Message message) throws JMSException {
		// SimpleMessageProducer is non-transactional so the regular send will send immediately
		send(message);
	}

}
