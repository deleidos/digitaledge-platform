package com.saic.rtws.commons.net.jms;

import java.util.Arrays;
import java.util.List;

import javax.jms.Destination;
import javax.jms.Message;

public class JMSMessageWrapper {

	private Message message;
	private List<Destination> destinations;
	private int deliveryMode;
	private int priority;
	private long timeToLive;
	
	public JMSMessageWrapper(Message message, Destination destination,
			int deliveryMode, int priority, long timeToLive) {
		super();
		this.message = message;
		this.destinations = Arrays.asList(destination);
		this.deliveryMode = deliveryMode;
		this.priority = priority;
		this.timeToLive = timeToLive;
	}
	
	public JMSMessageWrapper(Message message, Destination destination) {
		super();
		this.message = message;
		this.destinations = Arrays.asList(destination);
		this.deliveryMode = -1;
		this.priority = -1;
		this.timeToLive = -1;
	}
	
	public JMSMessageWrapper(Message message, List<Destination> destinations,
			int deliveryMode, int priority, long timeToLive) {
		super();
		this.message = message;
		this.destinations = destinations;
		this.deliveryMode = deliveryMode;
		this.priority = priority;
		this.timeToLive = timeToLive;
	}
	
	public JMSMessageWrapper(Message message, List<Destination> destinations) {
		super();
		this.message = message;
		this.destinations = destinations;
		this.deliveryMode = -1;
		this.priority = -1;
		this.timeToLive = -1;
	}

	protected Message getMessage() {
		return message;
	}

	protected List<Destination> getDestinations() {
		return destinations;
	}

	protected int getDeliveryMode() {
		return deliveryMode;
	}

	protected int getPriority() {
		return priority;
	}

	protected long getTimeToLive() {
		return timeToLive;
	}
	
}
