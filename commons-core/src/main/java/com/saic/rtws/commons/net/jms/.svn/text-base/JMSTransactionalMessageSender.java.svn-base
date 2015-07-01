package com.saic.rtws.commons.net.jms;

import java.util.Timer;
import java.util.TimerTask;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.log4j.Logger;

/**
 * Sends JMS Messages in transactional batches to improve performance. Messages are committed in batches 
 * based on number of messages or time between batches.  Note that this is not the JMS Bundle concept we use 
 * elsewhere to bundle individual records into a larger message.  This is taking those bundled messages
 * and batching them up before we commit them to be sent over the network.
 */
public class JMSTransactionalMessageSender {

	public static final int DEFAULT_MESSAGES_PER_TRANSACTION = 100;
	public static final int DEFAULT_TIME_BEFORE_COMMIT = 30 * 1000;
	public static final int DEFAULT_TIMER_INTERVAL = 10 * 1000;
	
	private static final Logger logger = Logger.getLogger(JMSTransactionalMessageSender.class);

	// the JMS session and producer provided by the caller
	protected Session session;
	protected MessageProducer producer;

	// the number of msgs per transaction and a backup timer limit
	private int maxMessagesPerTransaction = DEFAULT_MESSAGES_PER_TRANSACTION;
	private long maxTimeBeforeCommit = DEFAULT_TIME_BEFORE_COMMIT;
	
	private Timer timer;
	private long lastCommitTime = System.currentTimeMillis();
	private int messagesSent = 0;
	
	public JMSTransactionalMessageSender(Session session, MessageProducer producer) {
		this.session = session;
		try {
			if (this.session.getTransacted() == false) {
				logger.warn("The JMS Session is not set to support transactions");
			}
		} catch (JMSException e) {
			logger.error(e);
		}
		this.producer = producer;
	}

	public synchronized void send(JMSMessageWrapper message) throws JMSException {
		sendMessage(message, false);
	}

	public synchronized void sendImmediately(JMSMessageWrapper message) throws JMSException {
		sendMessage(message, true);
	}

	public void start() {
		if (timer == null) {
			logger.info("Starting JMS Transaction Timer with time interval: " + maxTimeBeforeCommit);
			timer = new Timer();
			timer.scheduleAtFixedRate(new TransactionTimerTask(), maxTimeBeforeCommit/3, maxTimeBeforeCommit/3);
		}
	}

	public void stop() {
		if (timer != null) {
			logger.info("Stopped JMS Transaction Timer");
			timer.cancel();
			timer = null;
		}
	}

	private void sendMessage(JMSMessageWrapper message, boolean forceCommit) throws JMSException {
		try {
			if (message.getDeliveryMode() == -1) {
				for (Destination destination : message.getDestinations()) {
					producer.send(destination, message.getMessage());
				}
			} else {
				for (Destination destination : message.getDestinations()) {
					producer.send(destination, message.getMessage(), message.getDeliveryMode(), message.getPriority(), message.getTimeToLive());
				}
			}
		} catch (JMSException e) {
			logger.error("Error sending JMS Messages, attempting to rollback transaction");
			handleRollback();
			return;
		}
		messagesSent++;
		if (forceCommit || messagesSent >= maxMessagesPerTransaction) {
			handleCommit();
		}
	}
	
	private void handleRollback() throws JMSException {
		try {
			session.rollback();
		} catch (JMSException e) {
			logger.error("Error rolling back JMS transaction ", e);
			throw e;
		}
	}
	
	private void handleCommit() throws JMSException {
		try {
			session.commit();
			messagesSent = 0;
			lastCommitTime = System.currentTimeMillis();
		} catch (JMSException e) {
			logger.error("Error commiting JMS messages, attempting to rollback transaction", e);
			handleRollback();
			throw e;
		}
	}
	
	private synchronized void handleTimerCommit() {
		long timeSinceLastCommit = System.currentTimeMillis() - lastCommitTime;
		if (timeSinceLastCommit >= maxTimeBeforeCommit && messagesSent > 0) {
			logger.info(messagesSent + " JMS messages committed by timer");
			try {
				handleCommit();
			} catch (JMSException e) {
				logger.error("Timer failed to commit messages", e);
			}
		}
	}
	
	private class TransactionTimerTask extends TimerTask {
		@Override
		public void run() {
			handleTimerCommit();
		}
	}
	
	protected synchronized int getMaxMessagesPerTransaction() {
		return maxMessagesPerTransaction;
	}

	protected synchronized void setMaxMessagesPerTransaction(int maxMessagesPerTransaction) {
		this.maxMessagesPerTransaction = maxMessagesPerTransaction;
	}

	protected synchronized long getMaxTimeBeforeCommit() {
		return maxTimeBeforeCommit;
	}

	protected synchronized void setMaxTimeBeforeCommit(long maxTimeBeforeCommit) {
		this.maxTimeBeforeCommit = maxTimeBeforeCommit;
		this.stop();
		this.start();
	}

}
