package com.deleidos.rtws.tools.verify;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.deleidos.rtws.tools.verify.exception.GtmDataPushCheckerException;
import com.deleidos.rtws.commons.config.PropertiesUtils;

/**
 * This program checks the s3fs to see if the gzip files exists for the current day.
 */
public class GtmDataPushChecker {
	
	public static final String BUCKET_OPT_KEY = "bucket";
	
	public static final String PATH_OPT_KEY = "path";
	
	public static final String CRED_PROP_FILENAME = "AwsCredentials.properties";
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private CommandLine line;
	
	private Options options;
	
	/**
	 * Constructs the GTM data push checker instance.
	 */
	public GtmDataPushChecker(String [] args) throws GtmDataPushCheckerException {
		
		options = buildOptions();
		
		try {
			CommandLineParser parser = new PosixParser();
			this.line = parser.parse(options, args);
		} catch (ParseException pe) {
			throw new GtmDataPushCheckerException("GtmDataPushChecker - ParseException: " + pe.getMessage(), pe);
		}
		
		checkOptions(options);
		
	}
	
	/**
	 * Connects to amazon with a s3 client and checks to see if that gzip files exists in the s3fs.
	 */
	public void execute() throws GtmDataPushCheckerException {
		
		try {
			String bucket = line.getOptionValue(BUCKET_OPT_KEY);
			String path = line.getOptionValue(PATH_OPT_KEY);
			String format = df.format(new Date());
			String prefix = path + '/' + format;
		
			// Setup the amazon s3 client and list the all objects that match the criteria
		
			AmazonS3 as3 = new AmazonS3Client(
					new PropertiesCredentials(PropertiesUtils.loadResource(CRED_PROP_FILENAME)));
			
			ObjectListing objectListing = as3.listObjects(new ListObjectsRequest().withBucketName(bucket).withPrefix(prefix));
			
			int count = 0;
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				// System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ") (lastModified = " + objectSummary.getLastModified() + ")");
				
				if (objectSummary.getKey().endsWith(".gz") && objectSummary.getSize() > 0) {
					count++;
				}
			}
			
			if (count > 0) {
				System.out.println("GtmDataPushChecker located " + count + " gzip files with prefix " + prefix);
			}
			else {
				System.out.println("GtmDataPushChecker fail to locate gzip files with prefix " + prefix);
				System.exit(3);
			}
		} catch (IOException ioe) {
			throw new GtmDataPushCheckerException("execute - IOException: " + ioe.getMessage(), ioe);
		}
	}
	
	/**
	 * Build the supported options.
	 */
	private Options buildOptions() {
		
		Options options = new Options();
		
		options.addOption(BUCKET_OPT_KEY, true, "the s3fs bucket name");
		options.addOption(PATH_OPT_KEY, true, "the start path to check for the data push");
		
		return options;
		
	}
	
	/**
	 * Check for the command line for the required options.
	 * 
	 * @param options Supported options
	 * @throws S3DataLocatorException when it fails to format and print the help text
	 */
	private void checkOptions(Options options) throws GtmDataPushCheckerException {

		if (! line.hasOption(BUCKET_OPT_KEY) && ! line.hasOption(PATH_OPT_KEY)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("com.deleidos.rtws.tools.verify.GtmDataPushChecker", options);
			System.exit(1);
		}
		
	}
	
	/** 
	 * The program main driver method. 
	 */
	public static void main(String [] args) {
		
		try {
			GtmDataPushChecker checker = new GtmDataPushChecker(args);
			checker.execute();
		} catch(Exception ex) {
			System.out.println("GtmDataPushChecker failed execution. Reason: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(2);
		}
		
	}
}
