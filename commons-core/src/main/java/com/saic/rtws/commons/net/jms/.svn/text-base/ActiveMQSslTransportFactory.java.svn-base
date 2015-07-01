package com.saic.rtws.commons.net.jms;

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

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.activemq.broker.SslContext;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.tcp.SslTransportFactory;
import org.apache.activemq.util.IOExceptionSupport;

public class ActiveMQSslTransportFactory extends SslTransportFactory {

    private String keyStore = null;
    private String keyStorePassword = null;
    private String trustStore = null;
    private String trustStorePassword = null;
    private String certAlias = null;

    private SslContext sslContext = null;
    
    public ActiveMQSslTransportFactory() {
    	super();
    }
    
    public ActiveMQSslTransportFactory(String keyStore, String keyStorePassword,
    		String trustStore, String trustStorePassword, String certAlias) {
    	super();
    	this.keyStore = keyStore;
    	this.keyStorePassword = keyStorePassword;
    	this.trustStore = trustStore;
    	this.trustStorePassword = trustStorePassword;
    	this.certAlias = certAlias;
    }
    
    public ActiveMQSslTransportFactory(String keyStore, String keyStorePassword,
    		String trustStore, String trustStorePassword) {
    	super();
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

    public void initialize() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException {
    	
    	if(keyStore == null && trustStore == null){
    		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
       	 	KeyStore keyStore = null;
            trustManagerFactory.init(keyStore);

            final TrustManager [] trustManagers = trustManagerFactory.getTrustManagers();
            final SecureRandom secureRandom = new SecureRandom();
            
            sslContext = new SslContext(null, trustManagers, secureRandom);
            
            TransportFactory.registerTransportFactory("ssl", this);
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
	        
	        sslContext = new SslContext(keyManagers, trustManagers, secureRandom);
	        
	        TransportFactory.registerTransportFactory("ssl", this);
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
	        
	        sslContext = new SslContext(keyManagers, trustManagers, secureRandom);
	        
	        TransportFactory.registerTransportFactory("ssl", this);
    	}
    }
    
    @Override
    protected ServerSocketFactory createServerSocketFactory() throws IOException {
    	
        if (null != sslContext) {
            try {
                return sslContext.getSSLContext().getServerSocketFactory();
            } catch (Exception e) {
                throw IOExceptionSupport.create(e);
            }
        }
        
        return super.createServerSocketFactory();
        
    }

    @Override
    protected SocketFactory createSocketFactory() throws IOException {
    	
        if (null != sslContext) {
            try {
                return sslContext.getSSLContext().getSocketFactory();
            } catch (Exception e) {
                throw IOExceptionSupport.create(e);
            }
        }
        
        return super.createSocketFactory();
        
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