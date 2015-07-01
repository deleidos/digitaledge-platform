package com.saic.rtws.commons.net.listener.executor;

import java.io.File;
import java.io.FileFilter;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.status.ExecuteResultBuilder;
import com.saic.rtws.commons.net.shutdown.LogTransmissionInputParser;
import com.saic.rtws.commons.net.shutdown.LogTransmissionRequest;
import com.saic.rtws.commons.util.LogDirectoryListingSender;
import com.saic.rtws.commons.util.LogSender;
import com.saic.rtws.commons.util.SimpleLogDirectoryListingSender;
import com.saic.rtws.commons.util.SimpleLogSender;

public class LogCommandExecutor extends CommandExecutor {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	private Map<String, Boolean> allowedDirectories = new HashMap<String, Boolean>(1024);
	
	private LogTransmissionInputParser requestParser = new LogTransmissionInputParser();
	
	public static LogCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new LogCommandExecutor(command, request, os);
	}
	
	private LogCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}
	
	@Override
	public ExecuteResult execute() throws ServerException {
		
		loadProperties();
		
		JSONObject jsonReq = JSONObject.fromObject(request);
		
		if (command.equals(Command.GET_LOGS_COMMAND)) {
			return getLogs(jsonReq);
		} else if (command.equals(Command.GET_LOG_FILES_COMMAND)) {
			return getLogFiles(jsonReq);
		} else {
			throw new ServerException("Invalid log command recieved.");
		}
		
	}
	
	private void loadProperties() {
		
		try {
			String [] dirs = RtwsConfig.getInstance().getString("log.transport.allowed.directories").split(";");

			for (String dir : dirs) {
				if (logger.isDebugEnabled())
					logger.debug("Allowing transmission of files under: '" + dir + "'.");
				
				allowedDirectories.put(dir, new Boolean(true));
			}
		} catch (Exception ex) {
			logger.error("Failed to initialize source list of allowed log locations for transmission.  No logs will be allowed for tranmission.", ex);
		}
		
	}
	
	private ExecuteResult getLogs(JSONObject jsonReq) {
		
		Object parsedRequest = jsonReq.get("logs");
		
		if (parsedRequest != null) {
			try {
				List<LogTransmissionRequest> logRequest = requestParser.parse(parsedRequest);
				ExecutorService logTransmitters = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

				for (LogTransmissionRequest req : logRequest) {
					logger.debug("Processing request: " + jsonReq.toString());

					boolean enableWildCardSupport = req.getName().contains("?") || req.getName().contains("*");

					File requestedLog = new File(req.getName());
					String requestedLogDirectoryName = requestedLog.getParentFile().getAbsolutePath();

					if (isAllowed(req.getName())) {
						// Support wildcard(s) if present
						
						if (enableWildCardSupport) {
							FileFilter filter = new WildcardFileFilter(requestedLog.getName());

							File [] requestedFilesExpanded = new File(requestedLogDirectoryName).listFiles(filter);

							for (File evaluatedFile : requestedFilesExpanded) {
								LogSender sender = new SimpleLogSender();
								sender.setLogFile(evaluatedFile, req.getStart(), req.getEnd(), os);
								logTransmitters.submit(sender);
							}
						} else {
							// No wildcard(s) present

							LogSender sender = new SimpleLogSender();
							sender.setLogFile(requestedLog, req.getStart(), req.getEnd(), os);
							logTransmitters.submit(sender);
						}

					} else {
						String msg = "{\"directory\":\"" + req.getName() + "\" \"error\":\"Not Authorized\"}";
						
						writeToSocketStream(msg);
						logger.warn(msg);
					}
				}

				// Wait for all requested logs to be transmitted before continuing otherwise,
				// the socket will be dereferenced and closed
				
				logTransmitters.shutdown();
				logTransmitters.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			} catch (Exception ex) {
				String msg = "Fail log retrieval request. Message: " + ex.getMessage();
				logger.error(msg, ex);

				writeToSocketStream(ResponseBuilder.buildErrorResponse(msg));
			}  
		}
		
		return ExecuteResultBuilder.buildTerminateResult(this.command);
		
	}
	
	private ExecuteResult getLogFiles(JSONObject jsonReq) {
		
		Object parsedRequest = jsonReq.get("logs");

		if (parsedRequest != null) {
			try {
				List<LogTransmissionRequest> logRequest = requestParser.parse(parsedRequest);
				ExecutorService logTransmitters = Executors.newFixedThreadPool(1);
				for (LogTransmissionRequest req : logRequest) {
					if (isAllowed(req.getName())) {
						LogDirectoryListingSender sender = new SimpleLogDirectoryListingSender();
						sender.setLogDirectory(new File(req.getName()), os);
						logTransmitters.submit(sender);
					} else {
						String msg = "{\"directory\":\"" + req.getName() + "\",\"error\":\"Not Authorized\"}";
						// TODO Should caller be notified of this????
						logger.warn(msg);
					}
				}

				// Wait for all requested logs to be transmitted before continuing otherwise,
				// the socket will be dereferenced and closed
				
				logTransmitters.shutdown();
				logTransmitters.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			} catch (Exception ex) {
				String msg = "Fail to retrieve log files.";
				logger.error(msg, ex);
				
				writeToSocketStream(ResponseBuilder.buildErrorResponse(msg));
			}
		}
		
		return ExecuteResultBuilder.buildTerminateResult(this.command);
		
	}
	
	private boolean isAllowed(String req) {
		
		File requestedLog = new File(req);
		String requestedLogDirectoryName = null;
		
		if (requestedLog.isFile()) {
			requestedLogDirectoryName = requestedLog.getParentFile().getAbsolutePath();
		} else {
			requestedLogDirectoryName = requestedLog.getAbsolutePath();
		}

		logger.debug("Requested Log Directory: {" + requestedLogDirectoryName + "}");

		return allowedDirectories.containsKey(requestedLogDirectoryName);
		
	}

	
}