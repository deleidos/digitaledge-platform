package com.saic.rtws.commons.cloud.sweeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.saic.rtws.commons.cloud.platform.aws.AwsConnectionFactory;
import com.saic.rtws.commons.config.PropertiesUtils;

/**
 * This tool is used to sweep up objects stored in the S3 file system. Below shows some common usage scenarios and
 * description of the options.
 * 
 * -bucket <bucket-name> : This option specifies the s3 bucket to process. (required)
 * 
 * -prefix <prefix-string> : This option specifies a s3 object key prefix to match. (required)
 * 
 * -days : This option specifies the max number of days old of the s3 objects to retain. (required)
 * 
 * -remove : This option tells the tool to go and remove all s3 objects that matches the supplied criteria. (optional)
 * 
 * Usage 1 : Find all s3 objects that starts with 'gtm/sales/2011' and older than a day and print them out
 * 	com.saic.rtws.tools.s3.S3Sweeper -bucket rtws.appfs -prefix gtm/sales/2011 -days 1
 * 
 * Usage 2: Find all s3 objects that starts with 'gtm/sales/2011' and older than a day and remove them
 *  com.saic.rtws.tools.s3.S3Sweeper -bucket rtws.appfs -prefix gtm/sales/2011 -days 1 -remove
 * 
 * Usage 3: Find all s3 objects that starts with 'misc/temp/models' and older than 5 days and remove them
 *   com.saic.rtws.tools.s3.S3Sweeper -bucket rtws.appfs -prefix misc/temp/models -days 5 -remove
 * 
 */
public class S3Sweeper {
	
	/**
	 * Construct the S3sweeper and parses the command line arguments.
	 * 
	 * @param args The command line arguments
	 * @throws S3SweeperException when it fails to parse the command line arguments
	 */
	public S3Sweeper() throws S3SweeperException {	
	}
	
	/**
	 * This method scans the s3fs for s3 object keys that matches the supplied prefix. It than looks for
	 * objects that are older than the supplied days and remove them from the s3fs.
	 * 
	 * @throws S3SweeperException when it fails to perform its duties
	 */
	public void sweep(CommandLine line) throws S3SweeperException {
		
		try {
			String bucket = line.getOptionValue(Sweeper.BUCKET_OPT_KEY);
			String prefix = line.getOptionValue(Sweeper.PREFIX_OPT_KEY);
			String secretKey = line.getOptionValue(Sweeper.SECRET_KEY_KEY);
			String accessKey = line.getOptionValue(Sweeper.ACCESS_KEY_KEY);
			String serviceEndpointHost = line.getOptionValue(Sweeper.SERVICE_ENDPOINT_HOST_KEY);
			String serviceEndpointPort = line.getOptionValue(Sweeper.SERVICE_ENDPOINT_PORT_KEY);
			String serviceEndpointStoragePath = line.getOptionValue(Sweeper.SERVICE_ENDPOINT_STORAGE_PATH_KEY);
			
			int days = Integer.parseInt(line.getOptionValue(Sweeper.DAYS_OPT_KEY));
			//Date date = new Date();
			
			Long now = System.currentTimeMillis();
			Long sub = 1000l * 60l * 60l * 24l * days;
			Long deleteUntil = now-sub;
			Date date = new Date(deleteUntil);
			
			boolean remove = line.hasOption(Sweeper.REMOVE_OPT_KEY);
			
			// Setup the amazon s3 client and list the all objects that match the criteria
			
			AwsConnectionFactory awsConnectionFactory = new AwsConnectionFactory(accessKey, secretKey);
			awsConnectionFactory.setServiceEndpoint("http://"+serviceEndpointHost+":"+serviceEndpointPort+serviceEndpointStoragePath);
			AmazonS3Client as3 = awsConnectionFactory.getAmazonS3Client();
			
			List<ObjectListing> objectListings = new ArrayList<ObjectListing>();
			ObjectListing objectListing = as3.listObjects(new ListObjectsRequest().withBucketName(bucket).withPrefix(prefix));
			objectListings.add(objectListing);
			while(objectListing.isTruncated()){
				objectListing = as3.listNextBatchOfObjects(objectListing);
				objectListings.add(objectListing);
			}
 
			// Print out the list of object that matches the criteria
			
			System.out.println("Printing files to sweep...");
			
			for(ObjectListing ol : objectListings){
			for (S3ObjectSummary objectSummary : ol.getObjectSummaries()) {
				if (objectSummary.getLastModified().before(date)) {
					String fileInfo = " - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ") (lastModified = " + objectSummary.getLastModified() + ")";
					Sweeper.delStream.println(fileInfo);
					//System.out.println(fileInfo);
				}
			}
			}
			
			// Remove the items that matches the criteria
			
			if (remove) 
			{
				System.out.println("Removing files...");
				
				for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
					if (objectSummary.getLastModified().before(date)) {
						as3.deleteObject(bucket, objectSummary.getKey());
					}
				}
			}
		} catch (AmazonServiceException ase) {
			throw new S3SweeperException("execute - AmazonServiceException: " + ase.getMessage(), ase);
		} catch (AmazonClientException ace) {
			throw new S3SweeperException("execute - AmazonClientException: " + ace.getMessage(), ace);
		} catch (NumberFormatException nfe) {
			throw new S3SweeperException("execute - NumberFormatException: " + nfe.getMessage(), nfe);
		} catch (Exception ioe) {
			throw new S3SweeperException("execute - Generic Exception: " + ioe.getMessage(), ioe);
		}
		
	}
}
