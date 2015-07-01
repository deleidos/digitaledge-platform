package com.saic.rtws.commons.net.jms;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

/**
 * Base classes for instances of BasicMessageProducer. Encapsulates a connection, session and produces
 * through a delegation pattern.
 *
 */
public abstract class AbstractMessageProducer extends JMSFactory implements BasicMessageProducer {

	protected Connection connection;
	protected Session session;
	protected MessageProducer producer;
	
	private int deliveryMode = Message.DEFAULT_DELIVERY_MODE;
	private int priority = Message.DEFAULT_PRIORITY;
	private long timeToLive = Message.DEFAULT_TIME_TO_LIVE;
	private boolean disableMessageID = false;
	private boolean disableMessageTimestamp = false;
	
	private final boolean transacted;
	
	/**
	 * Constructor.
	 */
	protected AbstractMessageProducer(ConnectionFactory factory) {
		this(factory, false);
	}

	/**
	 * Constructor.
	 */
	protected AbstractMessageProducer(ConnectionFactory factory, boolean transacted) {
		super(factory);
		this.transacted = transacted;
	}

	/**
	 * Opens a new connection, session and producer (if one isn't already open). 
	 */
	public void open() throws JMSException {
		if(!isConnected()) try {
			connection = factory.createConnection();
			session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(null);
			producer.setDeliveryMode(deliveryMode);
			producer.setPriority(priority);
			producer.setTimeToLive(timeToLive);
			producer.setDisableMessageID(disableMessageID);
			producer.setDisableMessageTimestamp(disableMessageTimestamp);
		} catch(JMSException e) {
			close();
			throw e;
		}
	}
	
	/**
	 * Closes the current connection, session and producer.
	 */
	public void close() {
		try {producer.close();} catch (Exception ignore) { }
		try {session.close();} catch (Exception ignore) { }
		try {connection.close();} catch (Exception ignore) { }
		producer = null;
		connection = null;
		session = null;
	}

	/**
	 * Closes and reopens the current connection, session and producer.
	 */
	public void reset() throws JMSException {
		close();
		open();
	}
	
	/**
	 * Indicates whether this producer's underlying connection is currently usable.
	 */
	public boolean isConnected() {
		return connection != null;
	}
	
	
	public MapMessage createMapMessage() throws JMSException {
		return session.createMapMessage();
	}

	public ObjectMessage createObjectMessage() throws JMSException {
		return session.createObjectMessage();
	}

	public StreamMessage createStreamMessage() throws JMSException {
		return session.createStreamMessage();
	}

	public TextMessage createTextMessage() throws JMSException {
		return session.createTextMessage();
	}

	public BytesMessage createBytesMessage() throws JMSException {
		return session.createBytesMessage();
	}
	
	public Destination getDestination() {
		return null;
	}

	public int getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(int value) {
		deliveryMode = value;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int value) {
		priority = value;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long value) {
		timeToLive = value;
	}
	
	public boolean getDisableMessageID() {
		return disableMessageID;
	}

	public void setDisableMessageID(boolean value) {
		disableMessageID = value;
	}

	public void setDisableMessageTimestamp(boolean value) {
		disableMessageTimestamp = value;
	}

	public boolean getDisableMessageTimestamp() {
		return disableMessageTimestamp;
	}

}
