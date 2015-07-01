package com.saic.rtws.commons.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.shutdown.LogListingResponse;

/**
 * The Class SimpleLogDirectoryListingSender.
 */
public class SimpleLogDirectoryListingSender implements
		LogDirectoryListingSender, FilenameFilter {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** The directory. */
	private File directory = null;

	/** The socket. */
	private Socket socket = null;
	
	/** The output stream. */
	private OutputStream os = null;

	/** The container. */
	private StringBuilder container = new StringBuilder(1024);

	/** The response. */
	private LogListingResponse listingEntry = new LogListingResponse();

	public List<String> directoriesToFilter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (directory != null && (socket != null || os != null)) {

			directoriesToFilter = Arrays.asList(RtwsConfig.getInstance()
					.getString("log.transport.allowed.filter.directories")
					.split(";"));

			try {
				if (socket != null) {
					os = socket.getOutputStream();
				}
				
				/*
				 * Note: Response will be in a JSON format dynamically
				 * constructed to reduce memory footprint
				 */
				container.append("{\"directory\":\""
						+ directory.getAbsolutePath() + "\",\"contents\":[\n");

				os.write(container.toString().getBytes());

				container.delete(0, container.length());
				directory.listFiles(this);

				// Close json response
				os.write("{}]}".getBytes());

				if (os != null)
					os.flush();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.saic.rtws.commons.util.LogDirectoryListingSender#setLogDirectory(
	 * java.io.File, java.net.Socket)
	 */
	@Override
	public void setLogDirectory(File directoryOfLogs, Socket socket) {
		this.directory = directoryOfLogs;
		this.socket = socket;
	}
	
	@Override
	public void setLogDirectory(File directoryOfLogs, OutputStream os) {
		this.directory = directoryOfLogs;
		this.os = os;
	}

	private boolean shouldFilter(String dir) {
		boolean shouldFilter = false;
		for (String dirToFilter : directoriesToFilter) {
			if (dirToFilter.equals(dir))
				shouldFilter = true;
		}

		return shouldFilter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {

		LogListingUtil util = new LogListingUtil();
		try {
			if (socket != null) {
				os = socket.getOutputStream();
			}
			
			File file = new File(dir.getAbsolutePath() + File.separator + name);

			if (file.length() > 0 && file.isFile()) {

				boolean shouldProcess = true;

				if (shouldFilter(dir.getAbsolutePath())) {
					shouldProcess = util.isAcceptable(name);
				}

				if (shouldProcess) {
					listingEntry.setName(name);
					listingEntry.setSize(Long.toString(file.length()));

					container.append(JSONObject.fromObject(listingEntry)
							.toString());
					container.append(",\n");
					os.write(container.toString().getBytes());
					container.delete(0, container.length());
				}
			} else {
				logger.warn("Ignoring log: '{}'", file.getAbsolutePath());
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		// returning false to avoid array building of useless entries :)
		return false;
	}
}
