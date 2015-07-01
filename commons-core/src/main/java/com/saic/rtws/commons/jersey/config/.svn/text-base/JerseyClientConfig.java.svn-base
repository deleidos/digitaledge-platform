package com.saic.rtws.commons.jersey.config;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.ssl.AliasSslContext;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;


public class JerseyClientConfig {
	
	private static Logger logger = LogManager.getLogger(JerseyClientConfig.class);
	
	private static class SingletonHolder{
		static final JerseyClientConfig CLIENT_CONFIG = new JerseyClientConfig();
	}
	
	private JerseyClientConfig(){
		
	}
	
	public static JerseyClientConfig getInstance(){
		return SingletonHolder.CLIENT_CONFIG;
	}
	
	public ClientConfig getInternalConfig(){
		return getInternalConfig(10000, 30000);
	}
	
	public ClientConfig getInternalConfig(int connectTimeout, int readTimeout){
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, connectTimeout);
		config.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, readTimeout);
		
		//unit test block indefinently during testing need to wrap this since no keystore is available during testing
		//need to revisit tests later and fix this problem
		try{
			config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, 
								new HTTPSProperties(HttpsURLConnection.getDefaultHostnameVerifier(), AliasSslContext.getSslContext()));
		}
		catch(Exception e){
			logger.error(String.format("Unable to initialize AliasSSLContext:  %s", e.toString()), e);
		}
		
		return config;
	}

}
