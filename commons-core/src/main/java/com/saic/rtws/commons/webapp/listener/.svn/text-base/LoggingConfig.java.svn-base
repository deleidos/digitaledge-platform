package com.saic.rtws.commons.webapp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

/**
 * Performs a forceful re-load of the log4j configuration file. This is useful
 * if one of the jar files your application needs to import was kind enough to
 * bundle their own log4j.properties file (which will likely override any
 * logging configuration you are trying to configure yourself).
 */
public class LoggingConfig implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		try {
			if (event.getServletContext().getResource(
					"/WEB-INF/classes/log4j.properties") != null)
				PropertyConfigurator.configure(event.getServletContext()
						.getResource("/WEB-INF/classes/log4j.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		// Nothing to do.
	}
}
