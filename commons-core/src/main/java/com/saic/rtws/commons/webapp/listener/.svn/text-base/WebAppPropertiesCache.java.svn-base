package com.saic.rtws.commons.webapp.listener;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;

/**
 * Cache to hold properties about the web application.
 */
public class WebAppPropertiesCache {

	/** Properties instance. */
	Properties props = null;
	
	/** ServletContext object. */
	ServletContext servletContext;
	
	/** Constructor. */
	protected WebAppPropertiesCache() { 
		props = new Properties();
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class WebAppPropertiesCacheHolder { 
		public static final WebAppPropertiesCache instance = new WebAppPropertiesCache();
	}

	public static WebAppPropertiesCache getInstance() {
		return WebAppPropertiesCacheHolder.instance;
	}
	
	protected String getProperty(String key, String defaultValue) {
		if (props != null) {
			return props.getProperty(key, defaultValue);
		}
		return defaultValue;
	}
	
	public String getContextPath() {
		if (servletContext == null) {
			return "/";
		}
		return servletContext.getContextPath();
	}
	
	//public String getHostName() {
	//	return getProperty("webapp.hostname", "localhost");
	//}
	
	//public void setHostName(String hostname) {
	//	props.setProperty("webapp.hostname", hostname);
	//}
	
	//public String getHostPort() {
	//	return getProperty("webapp.hostport", "443");
	//}
	
	//public void setHostPort(String hostport) {
	//	props.setProperty("webapp.hostport", hostport);
	//}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public void setServletContext(ServletContext context) {
		servletContext = context;
		
		//System.out.println("context path = " + context.getContextPath());
		//System.out.println("server info  = " + context.getServerInfo());
		//System.out.println("servlet context name = " + context.getServletContextName());
		//System.out.println(" = " + context.g)
	}
	
	public void addServletContextAttribute(ServletContextAttributeEvent scab) {
		
	}
	
	public void removeServletContextAttribute(ServletContextAttributeEvent scab) {
		
	}
	
	public void updateServletContextAttribute(ServletContextAttributeEvent scab) {
		
	}
	
	//public String getWebappName() {
	//	return getProperty("webapp.name", "");
	//}
	
	//public void setWebappName(String name) {
	//	props.setProperty("webapp.name", name);
	//}
}
