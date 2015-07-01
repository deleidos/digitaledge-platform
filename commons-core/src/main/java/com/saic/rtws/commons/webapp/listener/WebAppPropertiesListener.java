package com.saic.rtws.commons.webapp.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener.
 */
public class WebAppPropertiesListener
implements ServletContextAttributeListener, ServletContextListener  {

	//private ServletContext context = null;

	//This method is invoked when an attribute
	//is added to the ServletContext object
	public void attributeAdded (ServletContextAttributeEvent scab)
	{
		//System.out.println("An attribute was added to the " +
		//"ServletContext object");
		WebAppPropertiesCache.getInstance().addServletContextAttribute(scab);
	}

	//This method is invoked when an attribute
	//is removed from the ServletContext object
	public void attributeRemoved (ServletContextAttributeEvent scab)
	{
		//System.out.println("An attribute was removed from " +
		//"the ServletContext object");
		WebAppPropertiesCache.getInstance().removeServletContextAttribute(scab);
	}

	//This method is invoked when an attribute
	//is replaced in the ServletContext object
	public void attributeReplaced (ServletContextAttributeEvent scab)
	{
		//System.out.println("An attribute was replaced in the " +
		//"ServletContext object");
		WebAppPropertiesCache.getInstance().updateServletContextAttribute(scab);
	}


	public void contextDestroyed(ServletContextEvent event)
	{
		//Output a simple message to the server's console
		//System.out.println("The Simple Web App. Has Been Removed");
		
		//this.context = null;
	}


	//This method is invoked when the Web Application
	//is ready to service requests

	public void contextInitialized(ServletContextEvent event)
	{
		//this.context = event.getServletContext();
		
		WebAppPropertiesCache.getInstance().setServletContext(event.getServletContext());

		//Output a simple message to the server's console
		//System.out.println("The Simple Web App. Is Ready");
	}

}
