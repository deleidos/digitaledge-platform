package com.saic.rtws.commons.cloud.platform.jetset;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jets3t.service.Jets3tProperties;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.multithread.S3ServiceSimpleMulti;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.security.ProviderCredentials;
import com.saic.rtws.commons.config.PropertiesLoader;

/** 
 *
 * A factory class that will load the credentials and URL properties for a JetS3t Connection.  Jets3t is an API offered by Amazon that allows
 * clients to connect to S3.  This API also works with Eucalyptus Walrus which is a private cloud implementation of S3.  This class needs 
 * the AWS access key and the AWS secret key to gain access to the S3 system.  For a private cloud a URL is also necessary.  This class will 
 * return an S3Service which can be used to manipulate buckets on S3 or Walrus.  This implementation does not encrypt the access key and the
 * secret key.  Perhaps this will be supported later by the system.
 * 
 *  
 * Note: It is assumed that one connection factory will produce one type of connection.  If a client needs to connect to another URL, 
 * create another JetSeTConnectionFactory object.
 *
 */

public class JetSetConnectionFactory {
	
	/** Credentials to sign-in to S3 or a private cloud */
	private ProviderCredentials credentials;
	
	/** The jets3t properties file. */
	private String file;
	
	/** HTTP port number to communicate with S3 or Walrus. Will communicate with AWS S3 if not specified */
	private String storagePortNumber;
	
	/** Endpoint to communicate with S3 or Walrus. Will communicate with AWS S3 if not specified */
	private String storageEndpoint;

	/** HTTP path to communicate with S3 or Walrus. Will communicate with AWS S3 if not specified */
	private String storageVirtualPath;
	
	private static final Logger log = LogManager.getLogger(JetSetConnectionFactory.class);
	
	/**
	 * Constructor.
	 */
	public JetSetConnectionFactory() {
		super();
	}
	
	/**
	 * Returns the name of the AWS credentials file being used to create connections.
	 */
	public String getPropertiesFile() {
		return file;
	}

	/**
	 * Sets the name of the AWS credentials file to be used to create connections.
	 */
	public void setPropertiesFile(String value) {
		file = value;
	}
	
	public String getStorageEndpoint() {
		return storageEndpoint;
	}
	
	public void setStorageEndpoint(String endpoint) {
		this.storageEndpoint = endpoint;
	}
	
	public String getStoragePortNumber() {
		return storagePortNumber;
	}

	public void setStoragePortNumber(String storagePortNumber) {
		this.storagePortNumber = storagePortNumber;
	}

	public String getStorageVirtualPath() {
		return storageVirtualPath;
	}

	public void setStorageVirtualPath(String storageVirtualPath) {
		this.storageVirtualPath = storageVirtualPath;
	}

	/**
	 * This method will create a fully functional S3Service object.  It will use the access key and secret key in the 
	 * property file provided.  If the client is accessing a private cloud, a URL must be specified.  If no URL is specified, the
	 * routine will provide an S3Service that will try to access buckets on Amazon S3. 
	 * 
	 * @return S3Service used to manipulate S3 buckets.
	 * @throws S3ServiceException - Because it is necessary to set a URI if using a private cloud, there is the possbility that
	 * an URIException will be thrown.
	 */
	
	public S3Service getJets3tService() throws S3ServiceException {
		
		Jets3tProperties props = Jets3tProperties.getInstance(org.jets3t.service.Constants.JETS3T_PROPERTIES_FILENAME);
		props.clearAllProperties();

		// fixed properties for both Amazon S3 and Eucalyptus Walrus
		props.setProperty("httpclient.max-connections", "20");
		props.setProperty("s3service.max-thread-count", "1");
		props.setProperty("s3service.admin-max-thread-count", "1");
		props.setProperty("threaded-service.max-thread-count", "10");
		props.setProperty("threaded-service.admin-max-thread-count", "20");
		props.setProperty("s3service.https-only", "true");
		props.setProperty("s3service.disable-dns-buckets", "true");

		// dynamic properties that are different for Amazon S3 and Eucalyptus Walrus
		props.setProperty("s3service.s3-endpoint-http-port", getStoragePortNumber());
		props.setProperty("s3service.s3-endpoint", getStorageEndpoint());
		props.setProperty("s3service.s3-endpoint-virtual-path", getStorageVirtualPath());
		
		
		// load credentials or use previously loaded credentials
		// Create a new S3Service using those credentials.
		RestS3Service service = new RestS3Service(getCredentials(), null, null, props);
		
		//test https connection
		try{
			service.listAllBuckets();
		}
		//if connection refused or error then change to http connection
		catch(Exception e){
			log.info("HTTPS connection to Storage Service failed, using HTTP.");
			props.setProperty("s3service.https-only", "false");
			service = new RestS3Service(getCredentials(), null, null, props);
		}
		
		// Return newly created service object.
		return service;
	}
	
	/**
	 * This method will create a fully functional S3ServiceSimpleMulti object.  It will use the access key and secret key in the 
	 * property file provided.  If the client is accessing a private cloud, a URL must be specified.  If no URL is specified, the
	 * routine will provide an S3Service that will try to access buckets on Amazon S3. The S3ServiceSimpleMulti is deprecated and has been
	 * replaced with the ThreadedStorageService.  However, Eucalyptus does not support the ThreadedStorageService object at this time.   
	 * 
	 * 
	 * @return S3ServiceSimpleMulti used to upload multiple files to S3 buckets.
	 * @throws S3ServiceException - Because it is necessary to set a URI if using a private cloud, there is the possibility that
	 * an URIException will be thrown.
	 */
	
	public S3ServiceSimpleMulti getThreadedJets3tService(int maxConnections) throws S3ServiceException {
		
		Jets3tProperties props = Jets3tProperties.getInstance(org.jets3t.service.Constants.JETS3T_PROPERTIES_FILENAME);
		props.clearAllProperties();

		String maxConnectionString = Integer.toString(maxConnections);
		
		// fixed properties for both Amazon S3 and Eucalyptus Walrus
		props.setProperty("httpclient.socket-timeout-ms", "60000");
		props.setProperty("httpclient.max-connections", maxConnectionString);
		props.setProperty("threaded-service.max-thread-count", maxConnectionString);
		props.setProperty("threaded-service.admin-max-thread-count", maxConnectionString);
		props.setProperty("s3service.https-only", "true");
		props.setProperty("s3service.disable-dns-buckets", "true");

		// dynamic properties that are different for Amazon S3 and Eucalyptus Walrus
		props.setProperty("s3service.s3-endpoint-http-port", getStoragePortNumber());
		props.setProperty("s3service.s3-endpoint", getStorageEndpoint());
		props.setProperty("s3service.s3-endpoint-virtual-path", getStorageVirtualPath());
		
		
		// load credentials or use previously loaded credentials
		// Create a new S3Service using those credentials.
		RestS3Service service = new RestS3Service(getCredentials(), null, null, props);
		
		//test https connection
		try{
			service.listAllBuckets();
		}
		//if connection refused or error then change to http connection
		catch(Exception e){
			log.info("HTTPS connection to Storage Service failed, using HTTP.");
			props.setProperty("s3service.https-only", "false");
			service = new RestS3Service(getCredentials(), null, null, props);
		}
		
		// Return newly created service object.
		S3ServiceSimpleMulti threadedService = null;
		
		try {
			threadedService = new S3ServiceSimpleMulti(service); 
		} catch (Exception se) {
			throw new S3ServiceException(se);
		}
		return threadedService;
	}
	
	/**
	 * Returns the login credentials used to create connections.
	 */
	public synchronized ProviderCredentials getCredentials() {
		
		if (credentials == null) {
			credentials = loadCredentialsFromFile();
		}
		return credentials;
	}
	
	/**
	 * Set the credentials used to create connections.
	 */
	public synchronized void setCredentials(String accessKey, String secretKey) {
		credentials = new AWSCredentials(accessKey, secretKey);
	}
	
	/**
	 * This method will load AWS/Walrus credentials from a properties file.  The file is generally specified when the factory is 
	 * unmarshalled.  If a file is not specified at this time, it can be set later.  If a file cannot be found is not specified, 
	 * an AmazonClientException will be thrown.
	 * 
	 * Format of properties file:
	 * 
	 * accessKey=XXXXXXX
	 * secretKey=YYYYYYY
	 * 
	 * @return AWSCrednetials
	 */
	
	private ProviderCredentials loadCredentialsFromFile() {
		// Initialize the Properties
		InputStream stream = null;
		Properties props = new Properties();
		
		// Load the Properties from a file.
		try {
			stream = PropertiesLoader.getInputStream(file);
			props.load(stream);
		} catch (Exception ex) {
			
		}finally{
			if(stream!=null)
				try{
				stream.close();
				}catch(IOException ignore){}
		}
		
		// Extract the accessKey and secretKey from the properties
		String accessKey = props.getProperty("accessKey");
		String secretKey = props.getProperty("secretKey");
		
		// return a new AWSProperties Object.
		return new AWSCredentials(accessKey, secretKey);
	}

}
