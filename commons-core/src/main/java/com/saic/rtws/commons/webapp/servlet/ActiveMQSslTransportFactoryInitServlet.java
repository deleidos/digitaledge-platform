package com.saic.rtws.commons.webapp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.saic.rtws.commons.exception.InitializationException;
import com.saic.rtws.commons.net.jms.ActiveMQSslTransportFactory;
import com.saic.rtws.commons.util.KeyStoreInterface;

public class ActiveMQSslTransportFactoryInitServlet extends HttpServlet {

	private static final long serialVersionUID = -2933004464414505657L;

	@Override
	public void init() throws ServletException {
		
		String keyStore = this.getInitParameter(KeyStoreInterface.SERVLET_CONFIG_KEYSTORE_PARAM_KEY);
		String keyStorePassword = this.getInitParameter(KeyStoreInterface.SERVLET_CONFIG_KEYSTORE_PASSWORD_PARAM_KEY);
		String trustStore = this.getInitParameter(KeyStoreInterface.SERVLET_CONFIG_TRUSTSTORE_PARAM_KEY);
		String trustStorePassword = this.getInitParameter(KeyStoreInterface.SERVLET_CONFIG_TRUSTSTORE_PASSWORD_PARAM_KEY);
		String certAlias = this.getInitParameter(KeyStoreInterface.SERVLET_CONFIG_CERT_ALIAS);
		
		if (keyStore == null || keyStorePassword == null || trustStore == null || trustStorePassword == null) {
			throw new InitializationException("Missing keyStore/trustStore servlet config parameters");
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