package com.saic.rtws.commons.net.jms;

import javax.jms.JMSException;
import javax.jms.MessageListener;

public interface BasicMessageConsumer {

	public void open() throws JMSException;
	public void close();
	public void reset() throws JMSException;
	
	public void start() throws JMSException;
	public void stop() throws JMSException;
	
	public boolean isConnected();
	public void setMessageListener(MessageListener messagelistener) throws JMSException;
	
}
