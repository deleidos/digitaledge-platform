package com.saic.rtws.commons.process.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Common class to debug Processes I/O started using execute.
 *
 *
 */
public class ProcessStream extends Thread{

	private Process process;
	private Logger logger;
	private Level logLevelStdErr;
	private Level logLevelStdOut;
	
	/**
	 * Constructor.
	 * 
	 * @param process
	 * @param logger
	 * @param logLevelStdErr
	 * @param logLevelStdOut
	 */
	public ProcessStream(Process process, Logger logger, Level logLevelStdErr, Level logLevelStdOut){
		this.process = process;
		this.logger = logger;
		this.logLevelStdErr = logLevelStdErr;
		this.logLevelStdOut = logLevelStdOut;
		
		//start monitoring
		this.start();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param process
	 * @param logger
	 */
	public ProcessStream(Process process, Logger logger){
		this.process = process;
		this.logger = logger;
		this.logLevelStdErr = Level.ERROR;
		this.logLevelStdOut = Level.INFO;
		
		//start monitoring
		this.start();
	}
	
	/**
	 * Processes the InputStream as long as it hasn't reached the end of the stream. 
	 */
	public void run(){
        try{
        	//log error stream
			InputStream ise = process.getErrorStream();
			if(ise != null){
				BufferedReader br = new BufferedReader(new InputStreamReader(ise));
				String line = null;
				
				while((line = br.readLine()) != null){
					logger.log(logLevelStdErr, String.format("stderr:  %s", line));
				}
				
				//cleanup
				ise.close();
				br.close();
			}
			
			//log std out
			InputStream is = process.getInputStream();
			if(is != null){
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = null;
				
				while((line = br.readLine()) != null){
					logger.log(logLevelStdOut, String.format("stdout:  %s", line));
				}
				
				//cleanup
				is.close();
				br.close();
			}
        } 
        catch (IOException ioe){
        	logger.error(ioe.toString(), ioe);
        }
    }
}
