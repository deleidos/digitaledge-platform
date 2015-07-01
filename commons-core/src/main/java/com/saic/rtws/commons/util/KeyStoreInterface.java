package com.saic.rtws.commons.util;

public interface KeyStoreInterface {
	
	// keystore and truststore system propery keys
	
	public final static String SYS_KEYSTORE_PROP_KEY = "javax.net.ssl.keyStore";
	public final static String SYS_KEYSTORE_PASSWORD_PROP_KEY = "javax.net.ssl.keyStorePassword";
	public final static String SYS_TRUSTSTORE_PROP_KEY = "javax.net.ssl.trustStore";
	public final static String SYS_TRUSTSTORE_PASSWORD_PROP_KEY = "javax.net.ssl.trustStorePassword";
	
	// keystore and truststore servlet config parameter keys
	
	public final static String SERVLET_CONFIG_KEYSTORE_PARAM_KEY = "keyStore";
	public final static String SERVLET_CONFIG_KEYSTORE_PASSWORD_PARAM_KEY = "keyStorePassword";
	public final static String SERVLET_CONFIG_TRUSTSTORE_PARAM_KEY = "trustStore";
	public final static String SERVLET_CONFIG_TRUSTSTORE_PASSWORD_PARAM_KEY = "trustStorePassword";
	public final static String SERVLET_CONFIG_CERT_ALIAS = "certAlias";
	
}