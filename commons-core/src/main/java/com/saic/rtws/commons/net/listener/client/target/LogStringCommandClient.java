package com.saic.rtws.commons.net.listener.client.target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.net.ssl.SSLSocket;
import net.sf.json.util.JSONStringer;
import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.CommandClient;
import com.saic.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ClientException;
import com.saic.rtws.commons.net.listener.util.SocketStreamWriter;

public class LogStringCommandClient extends SingleTargetCommandClient{

	private static String LOGS_KEY = "logString";
	
	public static LogStringCommandClient newInstance(String hostname, int port, String [] args) {
		return new LogStringCommandClient(hostname, port, args);
	}
	
	private LogStringCommandClient(String hostname, int port, String [] args) {
		super(hostname, port, args);
	}

	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		properties.put(CommandClient.COMMAND_KEY, args[0]);
		return properties;
	}
	
	
	@Override
	protected boolean isCommandValid() {
		String command = args[0];
		
		if (command == null) {
			return false;
		}
		
		if (command.equals(Command.LOG_STRING)) {
			return true;
		}
		
		return false;
	}
	
	private static void validArguments(String [] args) {
		if (args == null || (args.length != 1 && args.length != 2)) {
			System.out.println("Usage: LogStringCommandClient <command> [target1,target2,...]");
			System.exit(1);
		}
	}
	
	
	@Override
	protected String buildCommand(Properties commandProperties) {
		if (args != null && args.length == 2) {
			List<String> hosts = Arrays.asList(args[1].split("[,]"));
			commandProperties.put("hosts", hosts);
		}
		
		return super.buildCommand(commandProperties);
	}

	
	@Override
	public String sendCommand() throws ClientException {
		
		if (Command.LOG_STRING.equals(getCommand().getProperty(CommandClient.COMMAND_KEY))) {
			String msg = args[1];
			return sendLogCommand(msg);
		}
		
		String errorMsg = getCommand().getProperty(CommandClient.COMMAND_KEY) + "did not equal the command LOG_STRING";
		
		throw new ClientException(errorMsg);
	}
	
	private String sendLogCommand(String jsonMsg) throws ClientException {
		
		try {
			return invokeRemoteServer(buildRetrieveLogCommand(jsonMsg));
		} catch (IOException ioe) {
			throw new ClientException("Fail to send log string command.", ioe);
		}
		
	}
	
	// TODO split this out into a util method / class
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
	 * {"command":"LOG_STRING","logString":"Log message"}
	 */
	protected String buildRetrieveLogCommand(String msg) {

		JSONStringer json = new JSONStringer();
		json.object().key(COMMAND_KEY).value(Command.LOG_STRING)
				.key(LOGS_KEY).value(msg);
		json.endObject();

		return json.toString();

	}
	
	public static void main(String [] args) {
		try {		
			validArguments(args);
			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));

			LogStringCommandClient client = LogStringCommandClient.newInstance(hostname, port, args);
			client.sendCommand();
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}	
	}
	
}
