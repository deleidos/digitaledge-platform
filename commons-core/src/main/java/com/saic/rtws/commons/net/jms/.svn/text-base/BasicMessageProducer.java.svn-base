package com.saic.rtws.commons.net.jms;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

public interface BasicMessageProducer extends MessageProducer {

	public void open() throws JMSException;
	public void close();
	public void reset() throws JMSException;
	
	public boolean isConnected();
	
	public MapMessage createMapMessage() throws JMSException;
	public ObjectMessage createObjectMessage() throws JMSException;
	public StreamMessage createStreamMessage() throws JMSException;
	public BytesMessage createBytesMessage() throws JMSException;
	public TextMessage createTextMessage() throws JMSException;
	
	public void sendImmediately(Message message) throws JMSException;
		
}
