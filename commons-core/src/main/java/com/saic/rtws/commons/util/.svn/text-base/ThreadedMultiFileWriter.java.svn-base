package com.saic.rtws.commons.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ThreadedMultiFileWriter {

	private Logger logger = Logger.getLogger(ThreadedMultiFileWriter.class);
	
	// 30 minutes
	public static final long DEFAULT_MAX_WAIT_TIME = 1000 * 60 * 30;
	public static final int DEFAULT_NUM_WRITE_THREADS = 5;
	
	private ExecutorService executor;
	
	public ThreadedMultiFileWriter() {
		executor = Executors.newFixedThreadPool(DEFAULT_NUM_WRITE_THREADS);
	}
	
	public ThreadedMultiFileWriter(int maxWriteThreads) {
		executor = Executors.newFixedThreadPool(maxWriteThreads);
	}
	
	public void writeFile(InputStream stream, File destFile) {
		FileStreamWriter fsw = new FileStreamWriter(stream, destFile);
		executor.execute(fsw);
	}
	
	public void waitUntilCompletion() {
		waitUntilCompletion(DEFAULT_MAX_WAIT_TIME);
	}
	
	public void waitUntilCompletion(long maxWaitTime) {
		long start = System.currentTimeMillis();
		executor.shutdown();
		
		long current = System.currentTimeMillis();
		while (!executor.isTerminated() && ((current - start) < maxWaitTime)) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.warn("ThreadedMultiFileWriter executor interrupted", e);
			}
        }
		if (!executor.isTerminated()) {
			logger.error("ThreadedMultiFileWriter timed out waiting to write all files");
		}
	}
	
	private class FileStreamWriter extends Thread {
		private InputStream in;
		private File destFile;

		public FileStreamWriter(InputStream in, File destFile) {
			this.in = in;
			this.destFile = destFile;
		}
		
		public void run() {
			FileOutputStream out = null;
			try {
				logger.debug("Writting file: " + destFile.getAbsolutePath());
				out = new FileOutputStream(destFile);
				IOUtils.copy(in, out);
			} catch (FileNotFoundException fnfe) {
				logger.error(fnfe);
			} catch (IOException ioe) {
				logger.error(ioe);
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
	}

}
