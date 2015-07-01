package com.saic.rtws.commons.net.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;

public class AliasSslContext {
	
	private static final Logger logger = LogManager.getLogger(AliasSslContext.class);
	
	/**
	 * Creates a SSLContext that uses the certificate with the given alias.
	 * 
	 * @param alias String certificate alias
	 * @return {@link SSLContext}
	 */
	public static SSLContext getSslContext(String alias){
		return initialize(alias);
	}
	
	/**
	 * Creates a SSLContext that uses the default certificate alias.
	 * 
	 * @return {@SSLContext}
	 */
	public static SSLContext getSslContext(){
		return initialize(RtwsConfig.getInstance().getString("rtws.default.internal.certificate.alias"));
	}
	
	/**
	 * Initializes an SSLContext given a certificate alias.
	 * 
	 * @param alias String certificate alias
	 * @return{@link SSLContext}
	 */
	private static SSLContext initialize(String alias){
		SSLContext sslContext = null;
	    try{
	    	File keyStoreFile = new File(RtwsConfig.getInstance().getString("rtws.keystore"));
	        if (! keyStoreFile.exists() || ! keyStoreFile.canRead()) {
	            throw new IllegalArgumentException("keyStore");
	        }
	        
	        File trustStoreFile = new File(RtwsConfig.getInstance().getString("rtws.truststore"));
	        if (! trustStoreFile.exists() || ! trustStoreFile.canRead()) {
	            throw new IllegalArgumentException("trustStore");
	        }
	        
	        final KeyManager [] keyManagers = getKeyManagers(keyStoreFile, RtwsConfig.getInstance().getString("rtws.keystore.password"), alias);
	        final TrustManager [] trustManagers = getTrustManagers(trustStoreFile, RtwsConfig.getInstance().getString("rtws.truststore.password"));
	        final SecureRandom secureRandom = new SecureRandom();
	        
	        sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(keyManagers, trustManagers, secureRandom);
	    	
	    }catch(Exception e){
	    	logger.error(e.toString(), e);
	    	sslContext = null;
	    }
	    
	    return sslContext;
    }
	
	private static KeyManager [] getKeyManagers(File keyStoreFile, String keyStorePassword, String certAlias) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		
		KeyStore keyStore = KeyStore.getInstance("JKS");
	    char [] keyStorePwd = (keyStorePassword != null) ? keyStorePassword.toCharArray() : null;
	    keyStore.load(new FileInputStream(keyStoreFile), keyStorePwd);
	    
	    //load single cert with given alias
    	Enumeration<String> aliases = keyStore.aliases();
    	
    	while(aliases.hasMoreElements()){
    		String alias = aliases.nextElement();
    		
    		if(! certAlias.equals(alias)){
    			keyStore.deleteEntry(alias);  //remove cert only load certificate with given alias
    		}
    	}
    	
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	    kmf.init(keyStore, keyStorePwd);
	    
	    return kmf.getKeyManagers();
	
	}
	
	private static TrustManager[] getTrustManagers(File trustStoreFile, String trustStorePassword) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		
		KeyStore trustStore = KeyStore.getInstance("JKS");
	    char[] trustStorePwd = (trustStorePassword != null) ? trustStorePassword.toCharArray() : null;
	    trustStore.load(new FileInputStream(trustStoreFile), trustStorePwd);
	    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	    trustManagerFactory.init(trustStore);
	
	    return trustManagerFactory.getTrustManagers();
	    
	}

}
