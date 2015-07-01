package com.saic.rtws.commons.cloud.platform.aws;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.saic.rtws.commons.config.PropertiesLoader;
import com.saic.rtws.commons.config.RtwsConfig;

/**
 * Factory used to load AWS configuration/authentication data and instantiate an AmazonEC2Client.
 */
public class AwsConnectionFactory {

	private static Logger logger = Logger.getLogger(AwsConnectionFactory.class);
	
	/** The AWS credentials file. */
	private String file;

	/** AWSCredentials object loaded from the configured credentials file. */
	private AWSCredentials credentials;
	
	/** Endpoint to communicate with EC2 or Eucalyptus. Will communicate with AWS EC2 if not specified. */ 
	private String serviceEndpoint;
	
	/** Endpoint to communicate with S3 or Walrus. Will communicate with AWS S3 if not specified */
	private String storageEndpoint;
	
	/**
	 * Constructor.
	 */
	public AwsConnectionFactory() {
		super();
	}
	
	/**
	 * Constructor.
	 */
	public AwsConnectionFactory(String accessKey, String secretKey) {
		super();
		
		credentials = new BasicAWSCredentials(accessKey, secretKey);
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
	
	/**
	 * Returns the service endpoint for this connection factory.
	 */
	public String getServiceEndpoint() {
		return serviceEndpoint;
	}
	
	/**
	 * Sets the service endpoint for this connection factory.
	 */
	public void setServiceEndpoint(String endpoint) {
		this.serviceEndpoint = endpoint;
	}
	
	/**
	 * Returns the database endpoint for this connection factory.
	 */
	public String getStorageEndpoint() {
		return storageEndpoint;
	}
	
	/**
	 * Sets the database endpoint for this connection factory.
	 */
	public void setStorageEndpoint(String endpoint) {
		this.storageEndpoint = endpoint;
	}

	/**
	 * Creates a new Amazon EC2 Client. If no serviceEndpoint is specified will connect with EC2.
	 */
	public AmazonEC2Client getAmazonEC2Client() {
		// retrieve the timeout values for the ec2 connection
		int connectionTimeout = 90 * 1000;
		int socketTimeout = 90 * 1000;
		try {
			connectionTimeout = Integer.parseInt(RtwsConfig.getInstance().getString("ec2.client.config.connection.timeout","90")) * 1000;
			socketTimeout = Integer.parseInt(RtwsConfig.getInstance().getString("ec2.client.config.socket.timeout","90")) * 1000;
		} catch (NumberFormatException nfe) {
			logger.warn("Failed to parse EC2 timeout values, defaulting to ", nfe);
		}
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setConnectionTimeout(connectionTimeout);
		clientConfig.setSocketTimeout(socketTimeout);
		
		if (serviceEndpoint != null) {
			AmazonEC2Client client = new AmazonEC2Client(getCredentials(), clientConfig);
			client.setEndpoint(serviceEndpoint);
			return client;
		} else {
			return new AmazonEC2Client(getCredentials(), clientConfig);
		} 
	}
	
	public AmazonEC2Client getAmazonEC2Client(String accessKey, String secretKey) {
		return new AmazonEC2Client(new BasicAWSCredentials(accessKey, secretKey));
	}
	
	/**
	 * Creates a new Amazon S3 Client. If no databaseEndpoint is specified will connect with S3.
	 */
	public AmazonS3Client getAmazonS3Client() {
		if (storageEndpoint != null) {
			AmazonS3Client client = new AmazonS3Client(getCredentials());
			client.setEndpoint(storageEndpoint);
			return client;
		} else {
			return new AmazonS3Client(getCredentials());
		} 
	}

	public AmazonS3Client getAmazonS3Client(String accessKey, String secretKey) {
		return new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
	}
	
	/**
	 * Creates a new Amazon SES Client.
	 */
	public AmazonSimpleEmailServiceClient getAmazonSimpleEmailServiceClient() {
		return new AmazonSimpleEmailServiceClient(getCredentials());
	}
	
	public AmazonSimpleEmailServiceClient getAmazonSimpleEmailServiceClient(String accessKey, String secretKey) {
		return new AmazonSimpleEmailServiceClient(new BasicAWSCredentials(accessKey, secretKey));
	}
	
	/**
	 * Creates a new Amazon SNS Client.
	 */
	public AmazonSNSClient getAmazonSNSClient() {
		return new AmazonSNSClient(getCredentials());
	}

	public AmazonSNSClient getAmazonSNSClient(String accessKey, String secretKey) {
		return new AmazonSNSClient(new BasicAWSCredentials(accessKey, secretKey));
	}
	
	public void setCredentials(String accessKey, String secretKey) {
		credentials = new BasicAWSCredentials(accessKey, secretKey);
	}
	
	/**
	 * Returns the login credentials used to create connections.
	 */
	public synchronized AWSCredentials getCredentials() {
		if (credentials == null) {
			credentials = loadCredentials();
		}
		return credentials;
	}

	/**
	 * loads the configured credentials file.
	 */
	private AWSCredentials loadCredentials() {
		InputStream stream = null;
		try {
			stream = PropertiesLoader.getInputStream(file);
			return new PropertiesCredentials(stream);
		} catch (IOException e) {
			throw new AmazonClientException("Unable to load properties file '" + file + "'.", e);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (Exception ignore) {
			}
		}
	}

}
