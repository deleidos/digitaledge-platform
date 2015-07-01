package com.saic.rtws.commons.cloud.sweeper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Sweeper {
	
	public static final String SERVICE_ENDPOINT_HOST_KEY = "serviceHost";
	
	public static final String SERVICE_ENDPOINT_PORT_KEY = "servicePort";
	
	public static final String SERVICE_ENDPOINT_SERVICE_PATH_KEY = "servicePath";
	
	public static final String SERVICE_ENDPOINT_STORAGE_PATH_KEY = "storagePath";
	
	public static final String BUCKET_OPT_KEY = "bucket";
	
	public static final String PREFIX_OPT_KEY = "prefix";
	
	public static final String DAYS_OPT_KEY = "days";
	
	public static final String REMOVE_OPT_KEY = "remove";
	
	public static final String SECRET_KEY_KEY = "secret";
	
	public static final String ACCESS_KEY_KEY = "access";
	
	private static CommandLine line;
	
	private static Options options;
	
	public static PrintWriter delStream;
	
	public static void main(String[] args) throws Exception{
		
		options = buildOptions();
		
		try {
			CommandLineParser parser = new PosixParser();
			line = parser.parse(options, args);
		} catch (ParseException pe) {
			throw new S3SweeperException("S3Sweeper - ParseException: " + pe.getMessage(), pe);
		}
		
		checkRequiredOptions(options);
		
		File outFile = new File("toDel.txt");
		
		
		delStream = new PrintWriter(outFile);
		
				
		EBSVolumeSweeper volSweeper = new EBSVolumeSweeper();
		S3Sweeper s3Sweeper = new S3Sweeper();
		
		volSweeper.sweep(line);
		//s3Sweeper.sweep(line);
		
		delStream.flush();
		delStream.close();
	}
	
	
	/**
	 * Build the supported options.
	 */
	private static Options buildOptions() {
		
		Options options = new Options();
		
		options.addOption(SERVICE_ENDPOINT_HOST_KEY, true, "the service endpoint host to connect to");
		options.addOption(SERVICE_ENDPOINT_PORT_KEY, true, "the service endpoint port to connect to");
		options.addOption(SERVICE_ENDPOINT_STORAGE_PATH_KEY, true, "the service endpoint path to storage (walrus/s3) to connect to");
		options.addOption(SERVICE_ENDPOINT_SERVICE_PATH_KEY, true, "the service endpoint path to services (ec2/eucalyptus) to connect to");
		
		options.addOption(ACCESS_KEY_KEY, true, "the aws access key");
		options.addOption(SECRET_KEY_KEY, true, "the aws secret key");
		options.addOption(BUCKET_OPT_KEY, true, "the s3fs bucket name");
		options.addOption(PREFIX_OPT_KEY, true, "match object key in s3fs with this prefix");
		options.addOption(DAYS_OPT_KEY, true, "number days old object to keep around");
		options.addOption(REMOVE_OPT_KEY, false, "(optional) perform final, no-undo sweep (Otherwise just print things to sweep)");
		
		return options;
		
	}
	
	/**
	 * Check for the required options.
	 * 
	 * @param options Supported options
	 * @throws S3SweeperException when it fails to format and print the help text
	 */
	private static void checkRequiredOptions(Options options) throws S3SweeperException {
		
		if (! line.hasOption(BUCKET_OPT_KEY) 
				|| ! line.hasOption(PREFIX_OPT_KEY) 
				|| ! line.hasOption(DAYS_OPT_KEY)
				|| ! line.hasOption(SECRET_KEY_KEY)
				|| ! line.hasOption(ACCESS_KEY_KEY)
				|| ! line.hasOption(SERVICE_ENDPOINT_HOST_KEY)
				|| ! line.hasOption(SERVICE_ENDPOINT_SERVICE_PATH_KEY)
				|| ! line.hasOption(SERVICE_ENDPOINT_STORAGE_PATH_KEY)
				|| ! line.hasOption(SERVICE_ENDPOINT_PORT_KEY)
				) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("com.saic.rtws.tools.s3.S3Sweeper", options);
			System.exit(1);
		}
		
	}
}
