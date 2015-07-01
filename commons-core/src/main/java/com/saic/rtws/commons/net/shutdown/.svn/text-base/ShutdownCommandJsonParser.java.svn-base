package com.saic.rtws.commons.net.shutdown;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.util.LogDirectoryListingSender;
import com.saic.rtws.commons.util.LogSender;
import com.saic.rtws.commons.util.SimpleLogDirectoryListingSender;
import com.saic.rtws.commons.util.SimpleLogSender;

// TODO: Auto-generated Javadoc
/**
 * The Class ShutdownCommandJsonParser.
 */
public class ShutdownCommandJsonParser {

	/** The Constant GET_LOG_FILES_COMMAND. */
	private static final String GET_LOG_FILES_COMMAND = "RETRIEVE_LOG_FILES_LISTING";

	/** The Constant GET_LOGS_COMMAND. */
	private static final String GET_LOGS_COMMAND = "RETRIEVE_LOGS";

	private static final String TAIL_LOG_COMMAND = "TAIL_LOG";

	private static final String GET_STATUS_COMMAND = "RETRIEVE_STATUS";

	private static final String PROCESS_CHECK_COMMAND = "PROCESS_CHECK";

	/** The allowed directories. */
	private Map<String, Boolean> allowedDirectories = new HashMap<String, Boolean>(
			1024);

	/** The client. */
	private Socket client = null;

	/** The json req. */
	private JSONObject jsonReq = null;

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Instantiates a new shutdown command json parser.
	 */
	public ShutdownCommandJsonParser() {

		try {
			String[] dirs = RtwsConfig.getInstance()
					.getString("log.transport.allowed.directories").split(";");

			for (String dir : dirs) {
				logger.info("Allowing transmission of files under: '{}'", dir);
				allowedDirectories.put(dir, new Boolean(true));
			}
		} catch (Throwable e) {
			logger.error(
					"Failed to initialize source list of allowed log locations for transmission.  No logs will be allowed for tranmission.",
					e);
		}
	}

	/**
	 * Gets the client.
	 * 
	 * @return the client
	 */
	public Socket getClient() {
		return client;
	}

	/**
	 * Gets the json req.
	 * 
	 * @return the json req
	 */
	public JSONObject getJsonReq() {
		return jsonReq;
	}

	/**
	 * Checks if is valid request.
	 * 
	 * @param request
	 *            the request
	 * @return true, if is valid request
	 */
	public boolean isValidRequest() {
		boolean valid = false;
		try {
			if (jsonReq != null) {

				String command = (String) jsonReq.get("command");
				if (command != null) {
					if (command.equals(GET_LOGS_COMMAND)
							|| command.equals(GET_LOG_FILES_COMMAND)
							|| command.equals(TAIL_LOG_COMMAND)) {

						JSONArray logs = (JSONArray) jsonReq.get("logs");
						if (logs != null) {
							valid = true;
						}
					}
					if (command.equals(GET_STATUS_COMMAND)) {
						valid = true;
					}
				}
			}
		} catch (JSONException e) {

			logger.error(e.getMessage(), e);
		}

		return valid;
	}

	/**
	 * Checks if is allowed.
	 * 
	 * @param req
	 *            the req
	 * @return true, if is allowed
	 */
	private boolean isAllowed(String req) {
		File requestedLog = new File(req);
		String requestedLogDirectoryName = null;
		if (requestedLog.isFile()) {
			requestedLogDirectoryName = requestedLog.getParentFile()
					.getAbsolutePath();
		} else {
			requestedLogDirectoryName = requestedLog.getAbsolutePath();
		}

		logger.debug("Requested Log Directory: {" + requestedLogDirectoryName
				+ "}");

		return allowedDirectories.containsKey(requestedLogDirectoryName);
	}

	/**
	 * Process.
	 * 
	 */
	public void process() {

		String command = (String) jsonReq.get("command");
		LogTransmissionInputParser requestParser = new LogTransmissionInputParser();
		if (command.equals(GET_LOGS_COMMAND)) {
			/*
			 * See log-transmission-request.json for sample format.
			 */

			Object parsedRequest = jsonReq.get("logs");
			if (parsedRequest != null) {
				try {
					List<LogTransmissionRequest> logRequest = requestParser
							.parse(parsedRequest);
					ExecutorService logTransmitters = new ThreadPoolExecutor(1,
							1, 0L, TimeUnit.MILLISECONDS,
							new LinkedBlockingQueue<Runnable>());

					for (LogTransmissionRequest req : logRequest) {
						logger.debug("Processing request:", req.toString());

						boolean enableWildCardSupport = req.getName().contains(
								"?")
								|| req.getName().contains("*");

						File requestedLog = new File(req.getName());
						String requestedLogDirectoryName = requestedLog
								.getParentFile().getAbsolutePath();

						if (isAllowed(req.getName())) {

							// Support wildcard(s) if present
							if (enableWildCardSupport) {

								FileFilter filter = new WildcardFileFilter(
										requestedLog.getName());

								File[] requestedFilesExpanded = new File(
										requestedLogDirectoryName)
										.listFiles(filter);

								for (File evaluatedFile : requestedFilesExpanded) {
									LogSender sender = new SimpleLogSender();
									sender.setLogFile(evaluatedFile,
											req.getStart(), req.getEnd(),
											client);
									logTransmitters.submit(sender);
								}
							} else {

								// No wildcard(s) present

								LogSender sender = new SimpleLogSender();
								sender.setLogFile(requestedLog, req.getStart(),
										req.getEnd(), client);
								logTransmitters.submit(sender);

							}

						} else {
							String msg = "{\"directory\":\"" + req.getName()
									+ "\" \"error\":\"Not Authorized\"}";
							ShutdownUtil.writeToSocketStream(msg, client.getOutputStream());
							logger.warn(msg);
						}

					}

					// Wait for all requested logs to be
					// transmitted before continuing otherwise,
					// the socket will be dereferenced and
					// closed
					logTransmitters.shutdown();
					logTransmitters.awaitTermination(Long.MAX_VALUE,
							TimeUnit.MINUTES);
				} catch (JSONException e) {
					String msg = "Invalid log retrieval request received, not processing.";
					try {
						ShutdownUtil.writeToSocketStream(msg, client.getOutputStream());
					} catch (IOException e1) {
						logger.error(e1.getMessage(), e1);
					}
					logger.error(msg, e);
				} catch (InvalidLogFileRequest e) {
					// TODO Should caller be notified of this????
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				} finally {
					if (client != null)
						try {
							client.getInputStream().close();
							client.getOutputStream().close();
							client.close();
						} catch (IOException e) {
							logger.error(e.getMessage(), e);
						}
				}
			}

		} else if (command.equals(GET_LOG_FILES_COMMAND)) {
			Object parsedRequest = jsonReq.get("logs");

			if (parsedRequest != null) {
				try {
					List<LogTransmissionRequest> logRequest = requestParser
							.parse(parsedRequest);
					ExecutorService logTransmitters = Executors
							.newFixedThreadPool(1);
					for (LogTransmissionRequest req : logRequest) {

						if (isAllowed(req.getName())) {

							LogDirectoryListingSender sender = new SimpleLogDirectoryListingSender();
							sender.setLogDirectory(new File(req.getName()),
									client);
							logTransmitters.submit(sender);

						} else {
							String msg = "{\"directory\":\"" + req.getName()
									+ "\",\"error\":\"Not Authorized\"}";
							// TODO Should caller be notified of this????
							logger.warn(msg);
						}

					}

					// Wait for all requested logs to be
					// transmitted before continuing otherwise,
					// the socket will be dereferenced and
					// closed
					logTransmitters.shutdown();
					logTransmitters.awaitTermination(Long.MAX_VALUE,
							TimeUnit.MINUTES);
				} catch (JSONException e) {
					String msg = "Invalid log retrieval request received, not processing.\n";
					try {
						ShutdownUtil.writeToSocketStream(msg, client.getOutputStream());
					} catch (IOException e1) {
						logger.error(e1.getMessage(), e1);
					}
					logger.error(msg, e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}

			}
		} else if (command.equals(TAIL_LOG_COMMAND)) {
			Object parsedRequest = jsonReq.get("logs");

			if (parsedRequest != null) {
				try {
					List<LogTransmissionRequest> logRequest = requestParser
							.parse(parsedRequest);
					ExecutorService pool = Executors.newFixedThreadPool(1);
					if (logRequest.size() > 0) {
						LogTransmissionRequest req = logRequest.get(0);

						if (isAllowed(req.getName())) {

							LogSender sender = new TailingLogSender();
							sender.setLogFile(new File(req.getName()),
									req.getStart(), req.getEnd(), client);
							pool.submit(sender);

							pool.shutdown();
							pool.awaitTermination(Long.MAX_VALUE,
									TimeUnit.MINUTES);
							logger.info("Tailing has finished.");

						} else {
							String msg = "{\"directory\":\"" + req.getName()
									+ "\",\"error\":\"Not Authorized\"}";
							// TODO Should caller be notified of this????
							logger.warn(msg);
						}
					}
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		} else if (command.equals(GET_STATUS_COMMAND)) {
			sendStringAsLog(runStatusCheck());
		} else if (command.equals(PROCESS_CHECK_COMMAND)) {
			String pattern = "";
			String line = null;
			Process p = null;
			BufferedReader reader = null;
			Hashtable<Integer, String> processPorts = new Hashtable<Integer, String>();

			String execCommand = "sudo find / -name ";
			ArrayList<String> groups = new ArrayList<String>();
			groups.add("anchor");
			groups.add("master");
			groups.add("transport");
			// groups.add("jms.external");
			// groups.add("jms.internal");
			groups.add("ingest");

			execCommand += "ingest";
			pattern = "ingest";
			boolean correctNode = false;

			try {
				p = Runtime.getRuntime().exec(execCommand);
				reader = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				line = reader.readLine();

				Pattern patternObject = Pattern.compile(pattern);
				boolean nullPattern = pattern.equals("");
				while (line != null) {

					if (!nullPattern) {
						Matcher matchObject = patternObject.matcher(line);
						if (matchObject.find()) {
							correctNode = true;
						}
					}
					line = reader.readLine();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}

			if (correctNode) {
				processPorts.put(8089, "splunkd");
				processPorts.put(1099, "ingest java process");
				processPorts.put(61616, "ingest java process");
				processPorts.put(5555, "shutdown");
			}
			int goalProcCount = processPorts.size();
			String status = runStatusCheck();
			String list[] = status.split("\\r?\\n");
			int procCount = 0;
			for (String item : list) {
				String process = processPorts.get(Integer.parseInt(item));
				if (process != null) {
					procCount++;
					processPorts.remove(process);
				}
			}

			String result = "";
			if (procCount == goalProcCount) {
				// everything is running
				logger.debug("All processes in list were detected.");
				result += "SUCCESS\n";
			} else {
				result += "FAILURE\n";
				logger.debug("Could not find one or more processes.");
				for (String proc : processPorts.values()) {
					result += proc + "\n";
				}
			}

			sendStringAsLog(result);

			// String processGroup = "";
			// if(processGroup.equals("anchor")){
			// processPorts.put(8089,"splunkd");
			// processPorts.put(80,"jetty");
			// processPorts.put(443,"jetty");
			// processPorts.put(8161,"H2");
			// processPorts.put(8082,"H2");
			// processPorts.put(5435,"H2");
			// processPorts.put(10389,"apacheds");
			// processPorts.put(5555,"shutdown");
			// }else if(processGroup.equals("master")){
			// processPorts.put(8089,"splunkd");
			// processPorts.put(8080,"splunkd");
			// processPorts.put(9997,"splunkd");
			// processPorts.put(53,"named");
			// processPorts.put(1099,"master java process");
			// processPorts.put(61516,"master java process");
			// processPorts.put(80,"jetty");
			// processPorts.put(443,"jetty");
			// processPorts.put(5555,"shutdown");
			// }else if(processGroup.equals("transport")){
			// processPorts.put(8089,"splunkd");
			// processPorts.put(5555,"shutdown");
			// }else if(processGroup.equals("jms.external") ||
			// processGroup.equals("jms.internal")){
			// processPorts.put(8089,"splunkd");
			// processPorts.put(5555,"shutdown");
			// processPorts.put(1099,"activemq java process");
			// processPorts.put(61616,"activemq java process");
			// processPorts.put(61516,"activemq java process");
			// processPorts.put(8161,"activemq java process");
			// processPorts.put(5555,"shutdown");
			// }else if(processGroup.equals("ingest.all")){
			// processPorts.put(8089,"splunkd");
			// processPorts.put(1099,"ingest java process");
			// processPorts.put(61616,"ingest java process");
			// processPorts.put(5555,"shutdown");
			// }
		}
	}

	/**
	 * Treats a string as though it was a log and sends it to client as a JSON
	 * object
	 * 
	 * @param output
	 *            string to be treated as log
	 */
	private void sendStringAsLog(String output) {

		File temp;
		try {
			temp = File.createTempFile("listenPorts", "txt");
			temp.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			out.write(output);
			out.close();
			File tempFile = temp; // needed to flush changes to file
			ExecutorService logTransmitters = Executors.newFixedThreadPool(1);
			LogSender sender = new SimpleLogSender();
			sender.setLogFile(tempFile, 0, 100, client);
			logTransmitters.submit(sender);
			logTransmitters.shutdown();
			try {
				logTransmitters.awaitTermination(Long.MAX_VALUE,
						TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * Runs system call and returns string with a list of the ports that are
	 * open and how much disk space is being used
	 */
	private String runStatusCheck() {

		String cmdListPorts = "";
		String cmdDiskCheck = "";
		String patternList = "";
		String line = null;
		Process p = null;
		BufferedReader reader = null;
		String listenPortsStr = "PORTS\n";
		String diskSpaceStr = "DISKCHECK\n";

		// get process list, check against list of known processes given node
		// type
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			// the following is a place-holder for windows systems, for
			// debugging
			cmdListPorts = "tasklist";
			cmdDiskCheck = "";
		} else {
			cmdListPorts = "sudo lsof -i";
			cmdDiskCheck = "df -k";
			patternList = "(\\d+) \\(LISTEN";
		}

		// list ports and collect those that are marked as listening (open)
		ArrayList<String> listenPorts = new ArrayList<String>();
		try {
			p = Runtime.getRuntime().exec(cmdListPorts);
			reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			line = reader.readLine();

			Pattern patternObject = Pattern.compile(patternList);
			boolean nullPattern = patternList.equals("");
			while (line != null) {

				if (!nullPattern) {
					Matcher matchObject = patternObject.matcher(line);
					if (matchObject.find()) {
						listenPorts.add(matchObject.group(1));
					}
				} else {
					listenPorts.add(line);
				}

				line = reader.readLine();

			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		// checking disk space
		ArrayList<String> diskSpace = new ArrayList<String>();
		try {
			p = Runtime.getRuntime().exec(cmdDiskCheck);

			reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			line = reader.readLine();
			while (line != null) {
				diskSpace.add(line);
				line = reader.readLine();

			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		// log and create string that will become JSON obj
		logger.info("Ran '" + cmdListPorts);
		for (String port : listenPorts) {
			listenPortsStr += port + "\n";
			logger.info("Port " + port + " is listening.");

		}
		logger.info("Ran '" + cmdDiskCheck + "' in linux.");
		for (String commandLine : diskSpace) {
			diskSpaceStr += commandLine + "\n";
			logger.info(commandLine);
		}

		return (listenPortsStr + diskSpaceStr);

	}

	public void run() {
		process();
		try {
			if (client != null){
				client.getInputStream().close();
				client.getOutputStream().close();
				client.close();
			}	
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Sets the client.
	 * 
	 * @param client
	 *            the new client
	 */
	public void setClient(Socket client) {
		this.client = client;
	}

	/**
	 * Sets the json req.
	 * 
	 * @param jsonReq
	 *            the new json req
	 */
	public void setJsonReq(JSONObject jsonReq) {
		this.jsonReq = jsonReq;
	}
}
