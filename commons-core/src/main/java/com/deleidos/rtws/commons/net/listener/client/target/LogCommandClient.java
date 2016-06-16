package com.deleidos.rtws.commons.net.listener.client.target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import javax.net.ssl.SSLSocket;

import net.sf.json.util.JSONStringer;

import com.deleidos.rtws.commons.net.listener.Command;
import com.deleidos.rtws.commons.net.listener.client.CommandClient;
import com.deleidos.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.deleidos.rtws.commons.net.listener.common.ResponseBuilder;
import com.deleidos.rtws.commons.net.listener.exception.ClientException;
import com.deleidos.rtws.commons.net.listener.util.SocketStreamWriter;

public class LogCommandClient extends SingleTargetCommandClient {
	
	private static final String LOGS_KEY 	= "logs";
	private static final String NAME_KEY 	= "name";
	private static final String START_KEY 	= "start";
	private static final String END_KEY 	= "end";
	
	public static LogCommandClient newInstance(String hostname, int port, String [] args) {
		return new LogCommandClient(hostname, port, args);
	}
	
	private LogCommandClient(String hostname, int port, String [] args) {
		super(hostname, port, args);
	}
	
	@Override
	protected boolean isCommandValid() {
		String command = args[0];
		
		if (command == null) {
			return false;
		}
		
		if (command.equals(Command.GET_LOG_FILES_COMMAND) ||
			command.equals(Command.GET_LOGS_COMMAND)) {
			return true;
		}
		
		return false;
	}

	@Override
	protected Properties getCommand() {
		
		Properties properties = new Properties();
		properties.put(CommandClient.COMMAND_KEY, args[0]);
		return properties;
		
	}
	
	@Override
	public String sendCommand() throws ClientException {
		
		if (Command.GET_LOGS_COMMAND.equals(getCommand().getProperty(CommandClient.COMMAND_KEY))) {
			String filepath = args[1];
			Integer start = Integer.parseInt(args[2]);
			Integer end = Integer.parseInt(args[3]);
			return sendRetrieveLogCommand(filepath, start, end);
		} else {
			String logdir = args[1];
			return sendRetrieveLogFilesCommand(logdir);
		} 
		
	}

	private String sendRetrieveLogCommand(String filepath, Integer start, Integer end) throws ClientException {
		
		try {
			return invokeRemoteServer(buildRetrieveLogCommand(filepath, start, end));
		} catch (IOException ioe) {
			throw new ClientException("Fail to send retrieve log command.", ioe);
		}
		
	}
	
	private String sendRetrieveLogFilesCommand(String logdir) throws ClientException {
		
		try {
			return invokeRemoteServer(buildRetrieveLogFilesListingCommand(logdir));
		} catch (IOException ioe) {
			throw new ClientException("Fail to send retrieve log files command.", ioe);
		}
		
	}
	
	private String invokeRemoteServer(String jsonCmd) throws IOException, ClientException {
		
		SSLSocket socket = null;
		OutputStream os = null;
		BufferedReader br = null;
		
		try {
			socket = connect(this.hostname, this.port);
			
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			SocketStreamWriter.write(jsonCmd, os);
			
			String line = null;
			StringBuilder sb = new StringBuilder();

			String endOfStreamResponse = ResponseBuilder.buildTerminationResponse();
			while((line = br.readLine()) != null){
				if (line.contains(endOfStreamResponse)) {
					sb.append(line.substring(0, line.indexOf(endOfStreamResponse)));
					break;
				}
				
				sb.append(line);
			}
			
			return sb.toString();
		} finally {
			close(br, os, socket);
		}
		
	}
	
	/**
	 * Builds a json retrieve log command.
	 * 
	 * {"command":"RETRIEVE_LOGS","logs":[{"name":"/var/log/rtws_start.log",
	 * "start":"0","end":"50"}]}
	 */
	protected String buildRetrieveLogCommand(String filepath, int start, int end) {

		JSONStringer json = new JSONStringer();
		json.object().key(COMMAND_KEY).value(Command.GET_LOGS_COMMAND)
				.key(LOGS_KEY).array();
		json.object().key(NAME_KEY).value(filepath).key(START_KEY)
				.value(Integer.toString(start)).key(END_KEY)
				.value(Integer.toString(end));
		json.endObject().endArray().endObject();

		return json.toString();

	}
	
	/**
	 * Builds a json retrieve log directory listing command.
	 * 
	 * {"command":"RETRIEVE_LOG_FILES_LISTING","logs":[{"name":"/var/log"}]}
	 */
	protected String buildRetrieveLogFilesListingCommand(String path) {

		JSONStringer json = new JSONStringer();
		json.object().key(COMMAND_KEY)
				.value(Command.GET_LOG_FILES_COMMAND).key(LOGS_KEY)
				.array();
		json.object().key(NAME_KEY).value(path);
		json.endObject().endArray().endObject();

		return json.toString();

	}
	
	private static void validArguments(String [] args) {
		
		if (args.length != 2 && args.length != 4) {
			System.out.println("Usage: LogCommandClient <command> <args ...>");
			System.exit(1);
		}
		
		if (! args[0].equals(Command.GET_LOGS_COMMAND) && ! args[0].equals(Command.GET_LOG_FILES_COMMAND)) {
			StringBuilder sb = new StringBuilder();
			sb.append("Usage: LogCommandClient [" + Command.GET_LOGS_COMMAND + "|" + Command.GET_LOG_FILES_COMMAND + "] <args ...>");
			
			System.out.println(sb.toString());
			System.exit(1);
		}
		
	}
	
	public static void main(String [] args) {
		
		try {
			validArguments(args);
			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));
			
			LogCommandClient client = LogCommandClient.newInstance(hostname, port, args);
			System.out.println("Response: " + client.sendCommand());
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(2);
		}
				
	}
	
}