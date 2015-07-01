package com.saic.rtws.commons.net.jms;

import java.util.Vector;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;

import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;

/**
 * A specialized JMSMessageProducer for sending messages to the Dead Letter Queue (DLQ).
 * The Dead Letter Queue is for messages that cannot be processed by DE for some reason.
 */
public class DeadLetterQueueMessageProducer extends AbstractMessageProducer {

	private static final Logger log = Logger.getLogger(DeadLetterQueueMessageProducer.class);
	private static final int MAX_MESSAGE_BUFFER_SIZE = 5000;
	
	private String queueName;
	private Destination destination;
	private Vector<Message> messageList = new Vector<Message>();
	private boolean stop;
	private SenderThread senderThread;
	private MessageConsumer consumer;
	
	public DeadLetterQueueMessageProducer(ConnectionFactory factory) {
		super(factory);
		this.queueName = RtwsConfig.getInstance().getString("messaging.queue.dlq.name", "com.saic.rtws.DLQ?consumer.prefetchSize=0");
	}
	
	public DeadLetterQueueMessageProducer(ConnectionFactory factory, String queueName) {
		super(factory);
		this.queueName = queueName;
	}
	
	public void start() throws JMSException {
		super.open();
		connection.start();
		// TODO connection retry logic
		destination = createDestination(queueName, DestinationType.QUEUE, session);
		consumer = session.createConsumer(destination, null, false);
		stop = false;
		senderThread = new SenderThread();
		senderThread.start();
	}
	
	public void stop() {
		try {consumer.close();connection.stop();} catch (Exception ignore) { }
		super.close();
		stop = true;
		try {
			if (senderThread != null) {
				senderThread.join();
			}
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	@Override
	public void send(Message message) throws JMSException {
		// if for some reason we fall behind in sending messages to the DLQ
		// make sure we don't take too much memory storing messages headed to the DLQ
		if (messageList.size() > MAX_MESSAGE_BUFFER_SIZE) {
			log.warn("Reached maximum number of buffered dead letter messages, dropping message");
		} else {
			messageList.add(message);
		}
	}

	@Override
	public void send(Destination destination, Message message) throws JMSException {
		throw new UnsupportedOperationException("Method not implemented.");
	}

	@Override
	public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		throw new UnsupportedOperationException("Method not implemented.");
	}

	@Override
	public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		throw new UnsupportedOperationException("Method not implemented.");
	}

	@Override
	public void sendImmediately(Message message) throws JMSException {
		send(message);
	}
	
	private class SenderThread extends Thread {
		public void run() {
			if (producer instanceof ActiveMQMessageProducer) {
				ActiveMQMessageProducer prod = (ActiveMQMessageProducer)producer;
				prod.setSendTimeout(3000);
			}
			while (!stop) {
				if (!messageList.isEmpty()) {
					while (!messageList.isEmpty()) {
						Message message = null;
						try {
							message = messageList.remove(0);
							producer.send(destination, message);
						} catch (JMSException e) {
							log.info("JMS error sending to DLQ, assuming queue is full: " + e.getMessage());
							messageList.add(0, message);
							popMessagesFromDLQ(messageList.size() / 4);
						} catch (Exception e) {
							log.error("Unexpected error sending message to DLQ", e);
						}
					}
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignore) {
					}
				}
			}
		}
		
		private void popMessagesFromDLQ(int numberToPop) {
			try {
				log.info("Popping " + numberToPop + " messages from DLQ to make room");
				int numberPopped = 0;
				while ((numberToPop < numberPopped) && (consumer.receiveNoWait() != null)) {
					numberPopped++;
				}
			} catch (Exception e) {
				log.error("Error popping message from DLQ", e);
			}
		}
	}
}
