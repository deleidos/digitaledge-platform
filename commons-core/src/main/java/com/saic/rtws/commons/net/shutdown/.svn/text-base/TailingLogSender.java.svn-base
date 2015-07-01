package com.saic.rtws.commons.net.shutdown;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import net.sf.json.util.JSONUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saic.rtws.commons.util.LogSender;

/**
 * The Class TailingLogSender.
 */
public class TailingLogSender extends TailerListenerAdapter implements
		LogSender {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** The log file or directory. */
	private File logFileOrDirectory = null;

	/** The socket. */
	private Socket socket = null;
	
	/** The output stream. */
	private OutputStream os = null;

	/** The tailer. */
	private Tailer tailer = null;

	/** The line counter. */
	private int lineCounter = 0;

	/** The current line counter. */
	private int currentLineCounter = 0;

	/** The batch size. */
	private int batchSize = 10;

	/** The out. */
	private BufferedOutputStream out = null;

	/** The max lines. */
	private int maxLines = 100;

	/** The continue tailing. */
	private boolean continueTailing = true;

	/** The sent first entry. */
	private boolean sentFirstEntry = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (logFileOrDirectory != null) {
			logger.info("Starting tailing of '{}'",
					logFileOrDirectory.getAbsolutePath());

			try {
				out = (socket != null) ? new BufferedOutputStream(socket.getOutputStream()) :
					new BufferedOutputStream(os);
				
				out.write("{".getBytes("UTF-8"));
				out.write(("\"log\":\"" + logFileOrDirectory.getAbsolutePath() + "\",")
						.getBytes("UTF-8"));
				out.flush();

				tailer = Tailer.create(logFileOrDirectory, this, 2000, true);

				while (continueTailing) {
					Thread.sleep(1000);
				}

				// closeout json response
				out.write("}".getBytes("UTF-8"));
				out.flush();
				logger.info("Tailing of '{}' has completed.",
						logFileOrDirectory.getAbsolutePath());

			} catch (IOException e) {
				logger.error(e.getMessage(), e);

				if (tailer != null)
					tailer.stop();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
	}
	
	@Override
	public void setLogFile(File log, int start, int end, OutputStream os) {
		
		setLogFile(log);
		this.os = os;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.saic.rtws.commons.util.LogSender#setLogFile(java.io.File, int,
	 * int, java.net.Socket)
	 */
	@Override
	public void setLogFile(File log, int start, int end, Socket socket) {

		setLogFile(log);
		this.socket = socket;
		
	}
	
	private void setLogFile(File log) {
		
		if (log.isFile() && log.canRead()) {
			this.logFileOrDirectory = log;
		} else {
			String msg = "Unable to transmit log: " + log.getAbsolutePath()
					+ "\n";
			logger.warn(msg);
			try {
				IOUtils.write(msg, socket.getOutputStream());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);

				if (tailer != null)
					tailer.stop();
			}
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.io.input.TailerListenerAdapter#handle(java.lang.String
	 * )
	 */
	@Override
	public void handle(String line) {
		try {

			if (socket.isClosed()) {
				tailer.stop();
			} else {

				String entry = null;
				String entry2 = null;
				entry = JSONUtils.valueToString(line);

				if (!sentFirstEntry) {
					entry2 = "\"" + lineCounter + "\":" + entry;
					sentFirstEntry = true;
				} else {
					entry2 = ",\"" + lineCounter + "\":" + entry;
				}
				out.write(entry2.getBytes("UTF-8"));
				lineCounter++;
				currentLineCounter++;

				if (currentLineCounter >= batchSize) {
					out.flush();
					currentLineCounter = 0; // reset
				} else if (lineCounter > maxLines) {
					/*
					 * stop the tailer to prevent a client from tailing a file
					 * forever
					 */
					logger.info("Max line count reched, stopping the tail....");
					continueTailing = false;
					if (tailer != null)
						tailer.stop();
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);

			if (tailer != null)
				tailer.stop();

		}
	}

}
