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

public class SslContext{

	private static Logger logger = LogManager.getLogger(SslContext.class);
	private String keyStore;
	private String keyStorePassword;
	private String trustStore;
	private String trustStorePassword;
	private String certAlias;
	
	private SSLContext sslContext = null;
	
	public SslContext(String keyStore, String keyStorePassword,
    		String trustStore, String trustStorePassword, String certAlias){
		this.keyStore = keyStore;
		this.keyStorePassword = keyStorePassword;
		this.trustStore = trustStore;
		this.trustStorePassword = trustStorePassword;
		this.certAlias = certAlias;
	}
	
	public SslContext(String keyStore, String keyStorePassword,
    		String trustStore, String trustStorePassword){
		this.keyStore = keyStore;
		this.keyStorePassword = keyStorePassword;
		this.trustStore = trustStore;
		this.trustStorePassword = trustStorePassword;
	}
	
	public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }
    
    public void setCertAlias(String certAlias){
    	this.certAlias = certAlias;
    }
    
    public SSLContext getSslContext(){
    	if(sslContext == null){
    		logger.info("SSLContext has not yet been initialized calling it now.");
    		initialize();
    	}
    	
    	return sslContext;
    }
    
	public void initialize(){
	    try{
	    	if(keyStore == null && trustStore == null){
	    		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	       	 	KeyStore keyStore = null;
	            trustManagerFactory.init(keyStore);
	
	            final TrustManager [] trustManagers = trustManagerFactory.getTrustManagers();
	            final SecureRandom secureRandom = new SecureRandom();
	            
	            sslContext = SSLContext.getInstance("TLS");
	            sslContext.init(null, trustManagers, secureRandom);
	    	}
	    	else if(keyStore != null && trustStore == null){
	    		File keyStoreFile = new File(this.keyStore);
		        if (keyStore == null || ! keyStoreFile.exists() || ! keyStoreFile.canRead()) {
		            throw new IllegalArgumentException("keyStore");
		        }
		        
		        final KeyManager [] keyManagers = getKeyManagers(keyStoreFile, keyStorePassword, certAlias);
		        
		        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	       	 	KeyStore keyStore = null;
	            trustManagerFactory.init(keyStore);
	
	            final TrustManager [] trustManagers = trustManagerFactory.getTrustManagers();
	            final SecureRandom secureRandom = new SecureRandom();
		        
	            sslContext = SSLContext.getInstance("TLS");
	            sslContext.init(keyManagers, trustManagers, secureRandom);
	    	}
	    	else{
		    	File keyStoreFile = new File(this.keyStore);
		        if (keyStore == null || ! keyStoreFile.exists() || ! keyStoreFile.canRead()) {
		            throw new IllegalArgumentException("keyStore");
		        }
		        
		        File trustStoreFile = new File(this.trustStore);
		        if (trustStore == null || ! trustStoreFile.exists() || ! trustStoreFile.canRead()) {
		            throw new IllegalArgumentException("trustStore");
		        }
		        
		        final KeyManager [] keyManagers = getKeyManagers(keyStoreFile, keyStorePassword, certAlias);
		        final TrustManager [] trustManagers = getTrustManagers(trustStoreFile, trustStorePassword);
		        final SecureRandom secureRandom = new SecureRandom();
		        
		        sslContext = SSLContext.getInstance("TLS");
		        sslContext.init(keyManagers, trustManagers, secureRandom);
	    	}
	    }catch(Exception e){
	    	logger.error(e.toString(), e);
	    }
    }

	private KeyManager [] getKeyManagers(File keyStoreFile, String keyStorePassword, String certAlias) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
	
		KeyStore keyStore = KeyStore.getInstance("JKS");
	    char [] keyStorePwd = (keyStorePassword != null) ? keyStorePassword.toCharArray() : null;
	    keyStore.load(new FileInputStream(keyStoreFile), keyStorePwd);
	    
	    //if certAlias given then load single cert with given alias
	    if(certAlias != null){
	    	Enumeration<String> aliases = keyStore.aliases();
	    	
	    	while(aliases.hasMoreElements()){
	    		String alias = aliases.nextElement();
	    		
	    		if(! certAlias.equals(alias)){
	    			keyStore.deleteEntry(alias);  //remove cert only load certificate with given alias
	    		}
	    	}
	    }
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	    kmf.init(keyStore, keyStorePwd);
	    
	    return kmf.getKeyManagers();
	
	}

	private TrustManager[] getTrustManagers(File trustStoreFile, String trustStorePassword) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
	
		KeyStore trustStore = KeyStore.getInstance("JKS");
	    char[] trustStorePwd = (trustStorePassword != null) ? trustStorePassword.toCharArray() : null;
	    trustStore.load(new FileInputStream(trustStoreFile), trustStorePwd);
	    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	    trustManagerFactory.init(trustStore);
	
	    return trustManagerFactory.getTrustManagers();
	    
	}
}
