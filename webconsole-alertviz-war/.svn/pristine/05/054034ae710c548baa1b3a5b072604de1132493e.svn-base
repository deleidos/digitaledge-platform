package com.deleidos.rtws.alertviz.listeners;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.deleidos.rtws.commons.config.RtwsConfig;

public class AlertProcessingCfgListener implements ServletContextListener
{

	private static final Logger LOGGER = Logger
			.getLogger(AlertProcessingCfgListener.class);

	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		LOGGER.debug("Initializing Resources for Alert Processing");

		String topicName = null;
		try
		{
			topicName = RtwsConfig.getInstance().getString(
					"messaging.topic.alert.name", null);
		}
		catch (ConversionException conversionException)
		{
			topicName = null;
		}

		if (StringUtils.isBlank(topicName))
		{
			LOGGER
					.error("No alerts topicName has been configured.  Alert Processing cannot proceed.");
			return;
		}

		String alertsConnUrl = null;
		String jmsUsername = null;
		String jmsPassword = null;
		try
		{
			alertsConnUrl = RtwsConfig.getInstance().getString(
					"messaging.alerts.connection.url", null);
			jmsUsername = RtwsConfig.getInstance().getString(
					"messaging.external.connection.user", null);
			jmsPassword = RtwsConfig.getInstance().getString(
					"messaging.external.connection.password", null);
		}
		catch (ConversionException conversionException)
		{
			LOGGER
					.error(
							"Failed to fetch configuration properties for the Alert Processing Listener.  Alert Processing cannot proceed.",
							conversionException);
			alertsConnUrl = null;
			jmsUsername = null;
			jmsPassword = null;
		}

		if (StringUtils.isBlank(alertsConnUrl) || jmsUsername == null
				|| jmsPassword == null)
		{
			return;
		}

		LOGGER.debug("Registering Alert Processing Resources with JNDI");

		try
		{
			InitialContext ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:comp/env");
			
			ConnectionFactory jmsConnFact = new ActiveMQConnectionFactory(
					jmsUsername, jmsPassword, alertsConnUrl);
			envCtx.rebind("jms/flex/ActiveMqConnectionFactory", jmsConnFact);
			
			Destination alertsTopic = new ActiveMQTopic(topicName);
			envCtx.rebind("jms/com.deleidos.rtws.alert", alertsTopic);
		}
		catch (NamingException namingException)
		{
			LOGGER.error("Failed to bind Alert Processing components in JNDI.  Alert Processing cannot proceed.", namingException);
		}
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{
		// TODO Clean up
	}

}
