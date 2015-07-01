package com.saic.rtws.commons.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import net.sf.json.util.JSONUtils;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SimpleLogSender.
 */
public class SimpleLogSender implements LogSender, FilenameFilter {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** The log file or directory. */
	private File logFileOrDirectory = null;

	/** The socket. */
	private Socket socket = null;

	/** The output stream. */
	private OutputStream os = null;

	/** The start. */
	private int start = 0;

	/** The end. */
	private int end = 0;

	private int numFilesInDirectory = 0;

	private static final int TAIL_NUM_LINES = 500;

	private static final String CHARSET = "UTF-8";

	/**
	 * Send.
	 * 
	 * @param log
	 *            the log
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param socket
	 *            the socket
	 */
	public void send(File log, int start, int end, Socket socket) {

		try {
			send(log, start, end, socket.getOutputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * Send.
	 * 
	 * @param log
	 *            the log
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param os
	 *            the output stream
	 */
	public void send(File log, int start, int end, OutputStream os) {

		if (log.exists()) {

			/*
			 * Read entire file to output N lines as requested. TODO change to seek to a line, then read when able/ready/better than current approach.....
			 */
			BufferedOutputStream out = null;
			logger.info(String.format("Processing file: %s start: %s end: %s", log.getAbsolutePath(), start, end));
			RandomAccessFile raf = null;
			FileChannel inChannel = null;
			ByteBuffer buffer = ByteBuffer.allocate(8096);
			StringBuilder sb = new StringBuilder(8096);
			try {

				raf = new RandomAccessFile(log, "r");
				inChannel = raf.getChannel();
				out = new BufferedOutputStream(os);

				out.write("{".getBytes(CHARSET));
				out.write(("\"log\":\"" + log.getName() + "\",").getBytes(CHARSET));

				int curLine = 1;
				boolean sentFirstEntry = false;
				String entry = null;
				String entry2 = null;

				if (start >= 0 && end >= 0) {

					while (inChannel.read(buffer) > 0) {
						buffer.flip();
						for (int i = 0; i < buffer.limit(); i++) {
							char c = (char) buffer.get();

							if (c == '\n') {
								String line = sb.toString();

								if (curLine >= start && curLine <= end) {
									entry = JSONUtils.valueToString(line);

									if (!sentFirstEntry) {
										entry2 = "\"" + curLine + "\":" + entry;
										sentFirstEntry = true;
									} else {
										entry2 = ",\"" + curLine + "\":" + entry;
									}
									out.write(entry2.getBytes(CHARSET));
								}
								curLine++;
								sb.setLength(0); // reset
							} else {
								sb.append(c);
							}
						}
						buffer.clear();
					}

					if (sb.length() > 0) {
						// Flush the last line in the case where the end happens to be the last line in the file without a newline
						entry = JSONUtils.valueToString(sb.toString());
						entry2 = ",\"" + curLine + "\":" + entry;
						out.write(entry2.getBytes(CHARSET));
						sb.setLength(0);
						buffer.clear();
					}

				} else {
					/*
					 * Simple tail -N
					 */
					CircularFifoBuffer cfBuffer = new CircularFifoBuffer(TAIL_NUM_LINES);

					while (inChannel.read(buffer) > 0) {
						buffer.flip();
						for (int i = 0; i < buffer.limit(); i++) {
							char c = (char) buffer.get();

							if (c == '\n') {
								String line = sb.toString();
								++curLine;
								cfBuffer.add(line);								
								sb.setLength(0); // reset
							} else {
								sb.append(c);
							}
						}
						buffer.clear();
					}

					if (curLine <= TAIL_NUM_LINES && sb.toString().length() > 0)
						cfBuffer.add(sb.toString());

					if (logger.isDebugEnabled())
						logger.debug(String.format("Log: %s curLine: %s TAIL_NUM_LINES: %s", log.getAbsolutePath(), curLine, TAIL_NUM_LINES));
					
					int ct = 1;
					if (curLine > TAIL_NUM_LINES) {
						ct = curLine - TAIL_NUM_LINES;
					}
					for (Object lne : cfBuffer) {
						entry = JSONUtils.valueToString(lne);
						if (!sentFirstEntry) {
							entry2 = "\"" + ct + "\":" + entry;
							sentFirstEntry = true;
						} else {
							entry2 = ",\"" + ct + "\":" + entry;
						}
						out.write(entry2.getBytes(CHARSET));
						ct++;
					}

					// add line total
					out.write(String.format(",\"totalLines\":%s", curLine).getBytes(CHARSET));
				}

				// add total number of files in directory
				numFilesInDirectory = 0; // reset
				log.getParentFile().listFiles(this);
				out.write(String.format(",\"totalFiles\":%s", numFilesInDirectory).getBytes(CHARSET));

				// closeout json response
				out.write("}".getBytes(CHARSET));
				out.flush();

			} catch (FileNotFoundException e) {
				try {
					IOUtils.write(e.getMessage(), out);
				} catch (IOException e1) {
					logger.error(e1.getMessage(), e1);
				}
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					if (out != null)
						out.flush();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				if (buffer != null)
					buffer.clear();
				if (inChannel != null)
					try {
						inChannel.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				if (raf != null)
					try {
						raf.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if ((socket != null || os != null) && logFileOrDirectory != null) {
			if (logFileOrDirectory.isFile()) {
				if (socket != null) {
					send(logFileOrDirectory, start, end, socket);
				} else {
					send(logFileOrDirectory, start, end, os);
				}
			} else {
				String msg = "Not a file:" + logFileOrDirectory;
				logger.warn(msg);
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.saic.rtws.commons.util.LogSender#setLogFile(java.io.File, int, int, java.net.Socket)
	 */
	@Override
	public void setLogFile(File log, int start, int end, Socket socket) {

		setLogFile(log);
		this.socket = socket;
		this.start = start;
		this.end = end;

	}

	/*
	 * (non-Javadoc)
	 * @see com.saic.rtws.commons.util.LogSender#setLogFile(java.io.File, int, int, java.io.OutputStream)
	 */
	@Override
	public void setLogFile(File log, int start, int end, OutputStream os) {

		setLogFile(log);
		this.os = os;
		this.start = start;
		this.end = end;

	}

	private void setLogFile(File log) {

		if (log.isFile() && log.canRead()) {
			this.logFileOrDirectory = log;
		} else if (log.isDirectory()) {
			this.logFileOrDirectory = log;
		} else {
			String msg = "Unable to transmit log: " + log.getAbsolutePath() + "\n";
			logger.warn(msg);

			try {
				IOUtils.write(msg, os);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	@Override
	public boolean accept(File dir, String name) {
		/*
		 * Simply used as a counter for the number of files in the directory. Don't return the array of files to reduce memory usage
		 */
		numFilesInDirectory++;
		return false;
	}

}
