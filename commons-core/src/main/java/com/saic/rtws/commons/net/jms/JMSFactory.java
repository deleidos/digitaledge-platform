package com.saic.rtws.commons.net.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;


/**
 * Factory for creating convenient wrapper objects to manage JMS connections.
 * 
 * The objects created by this factory are "convenience" objects in that they manage their
 * own connection and session. This simplifies code as client only need to manage on object
 * instead of four (Connection, Session, Destination, and MessageProducer/MessageConsumer).
 * However, this only works for applications where transaction management is not needed. 
 */
public class JMSFactory {

	/** Constants for types of JMS destinations. */
	protected enum DestinationType {QUEUE, TOPIC};
	
	/** Connection factory that will be passed create the objects managed by this factory. */
	protected ConnectionFactory factory;

	/**
	 * Constructor.
	 */
	public JMSFactory() {
		super();
	}
	
	/**
	 * Convenience constructor for setting the delegate connection factory. 
	 */
	public JMSFactory(ConnectionFactory factory) {
		super();
		setConnectionFactory(factory);
	}
	
	/**
	 * Sets the connection factory that will be used to create new JMS objects.
	 */
	public void setConnectionFactory(ConnectionFactory value) {
		factory = value;
	}
	
	/**
	 * Creates a message producer for the given topic. The returned object will
	 * contain it's own (internally managed) connection and session. 
	 */
	public BasicMessageProducer createSimpleTopicProducer(String destination) {
		return new SimpleMessageProducer(factory, DestinationType.TOPIC, destination);
	}
	
	/**
	 * Creates a message producer for the given queue. The returned object will
	 * contain it's own (internally managed) connection and session. 
	 */
	public BasicMessageProducer createSimpleQueueProducer(String destination) {
		return new SimpleMessageProducer(factory, DestinationType.QUEUE, destination);
	}
	
	/**
	 * Creates a message consumer for the given queue. The returned object will
	 * contain it's own (internally managed) connection and session. 
	 */
	public BasicMessageConsumer createSimpleQueueConsumer(String source) {
		return new SimpleMessageConsumer(factory, source);
	}
	
	/**
	 * Creates a message producer that will publish a copy of each sent message to all
	 * of the given queues. The returned object will it's own (internally managed)
	 * connection and session. 
	 */
	public BasicMessageProducer createFanoutProducer(String... destinations) {
		return new FanoutMessageProducer(factory, DestinationType.QUEUE, destinations);
	}
	
	/**
	 * Create a Destination for the given name.
	 * 
	 * @param name The name of destination.
	 * @param type The type of destination (queue or topic)
	 * @param session The session object from with to create the destination.
	 */
	protected static Destination createDestination(String name, DestinationType type, Session session) throws JMSException {
		if(type == DestinationType.QUEUE) {
			return session.createQueue(name);
		} else if(type == DestinationType.TOPIC) {
			return session.createTopic(name);
		} else {
			throw new IllegalArgumentException("Invalid destination type '" + type + "'.");
		}
	}
	
}
