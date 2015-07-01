package com.saic.rtws.commons.webapp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.saic.rtws.commons.exception.InitializationException;
import com.saic.rtws.commons.net.jms.ActiveMQSslTransportFactory;
import com.saic.rtws.commons.util.KeyStoreInterface;

public class ActiveMQSslTransportFactoryServlet extends HttpServlet {

	private static final long serialVersionUID = 9092500273461573276L;
	
	@Override
	public void init() throws ServletException {
		
		// Load keystore properties from java system environment
		
		String keyStore = System.getProperty(KeyStoreInterface.SYS_KEYSTORE_PROP_KEY);
		String keyStorePassword = System.getProperty(KeyStoreInterface.SYS_KEYSTORE_PASSWORD_PROP_KEY);
		String trustStore = System.getProperty(KeyStoreInterface.SYS_TRUSTSTORE_PROP_KEY);
		String trustStorePassword = System.getProperty(KeyStoreInterface.SYS_TRUSTSTORE_PASSWORD_PROP_KEY);
		String certAlias = null;
		
		
		// Load keystore properties from servlet init parameters
		
		if (keyStore == null || keyStorePassword == null || trustStore == null || trustStorePassword == null) {
			keyStore = this.getServletConfig().getInitParameter(KeyStoreInterface.SERVLET_CONFIG_KEYSTORE_PARAM_KEY);
			keyStorePassword = this.getServletConfig().getInitParameter(KeyStoreInterface.SERVLET_CONFIG_KEYSTORE_PASSWORD_PARAM_KEY);
			trustStore = this.getServletConfig().getInitParameter(KeyStoreInterface.SERVLET_CONFIG_TRUSTSTORE_PARAM_KEY);
			trustStorePassword = this.getServletConfig().getInitParameter(KeyStoreInterface.SERVLET_CONFIG_TRUSTSTORE_PASSWORD_PARAM_KEY);
			certAlias = this.getServletConfig().getInitParameter(KeyStoreInterface.SERVLET_CONFIG_CERT_ALIAS);
		}
		
		try {
			// Create an ActiveMQSslTransportFactory using the supplied keyStore and trustStore
			// and register the factory to be used for the ssl protocol.
			
			ActiveMQSslTransportFactory tf = 
					new ActiveMQSslTransportFactory(keyStore, keyStorePassword, trustStore, trustStorePassword, certAlias);
			
			tf.initialize();
		} catch (Exception ex) {
			throw new InitializationException("Fail to initialize ActiveMQSslTransportFactory instance: " + ex.getMessage(), ex);
		}
		
	}
	
}