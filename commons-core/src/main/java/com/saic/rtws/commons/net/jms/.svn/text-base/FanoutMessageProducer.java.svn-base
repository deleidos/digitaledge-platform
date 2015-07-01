package com.saic.rtws.commons.net.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * BasicMessageProducer implementation that sends a copy of a message to multiple destinations.
 */
public class FanoutMessageProducer extends AbstractMessageProducer implements BasicMessageProducer {

	private DestinationType destinationType;
	private String[] destinationNames;

	private List<Destination> destinations;
	private JMSTransactionalMessageSender messageSender;

	/**
	 * Constructor.
	 * 
	 * @param factory
	 *            Factory to be used when opening a connection.
	 * @param type
	 *            Type of destination to be used (topic/queue).
	 * @param destination
	 *            List of topic/queue destination names.
	 */
	protected FanoutMessageProducer(ConnectionFactory factory, DestinationType type, String... destinations) {
		super(factory, true);
		if (type == null) {
			throw new IllegalArgumentException("Destination type cannot be null.");
		} else if (destinations == null || destinations.length == 0) {
			throw new IllegalArgumentException("Destination list cannot be null or empty.");
		}
		destinationType = type;
		destinationNames = destinations;
	}

	public void open() throws JMSException {
		super.open();
		destinations = new ArrayList<Destination>(destinationNames.length);
		for (String destName : destinationNames) {
			destinations.add(createDestination(destName, destinationType, session));
		}
		messageSender = new JMSTransactionalMessageSender(session, producer);
		messageSender.start();
	}

	public void close() {
		if (messageSender != null) {
			messageSender.stop();
		}
		destinations = null;
		super.close();
	}

	public void reset() throws JMSException {
		close();
		open();
	}

	public void send(Message message) throws JMSException {
		send(destinations, message);
	}
	
	protected void send(List<Destination> destinations, Message message) throws JMSException {
		JMSMessageWrapper msgWrapper = new JMSMessageWrapper(message, destinations);
		messageSender.send(msgWrapper);
	}

	public void send(Destination destination, Message message) throws JMSException {
		JMSMessageWrapper msgWrapper = new JMSMessageWrapper(message, destination);
		messageSender.send(msgWrapper);
	}

	public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		JMSMessageWrapper msgWrapper = new JMSMessageWrapper(message, destinations, deliveryMode, priority, timeToLive);
		messageSender.send(msgWrapper);
	}

	public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		JMSMessageWrapper msgWrapper = new JMSMessageWrapper(message, destination, deliveryMode, priority, timeToLive);
		messageSender.send(msgWrapper);
	}

	@Override
	public void sendImmediately(Message message) throws JMSException {
		JMSMessageWrapper msgWrapper = new JMSMessageWrapper(message, destinations);
		messageSender.sendImmediately(msgWrapper);
	}
	
	protected List<Destination> getDestinations() {
		return destinations;
	}
}
