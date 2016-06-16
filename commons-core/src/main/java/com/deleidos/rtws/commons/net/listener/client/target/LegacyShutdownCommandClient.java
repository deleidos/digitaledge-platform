package com.deleidos.rtws.commons.net.listener.client.target;

import java.util.Properties;

import com.deleidos.rtws.commons.net.listener.Command;
import com.deleidos.rtws.commons.net.listener.client.CommandClient;
import com.deleidos.rtws.commons.net.listener.client.LegacySingleTargetCommandClient;
import com.deleidos.rtws.commons.net.listener.common.ResponseBuilder;
import com.deleidos.rtws.commons.net.listener.exception.ClientException;

public class LegacyShutdownCommandClient extends LegacySingleTargetCommandClient {
	
	public static LegacyShutdownCommandClient newInstance(String hostname) {
		return new LegacyShutdownCommandClient(hostname);
	}
	
	public static LegacyShutdownCommandClient newInstance(String hostname, int port) {
		return new LegacyShutdownCommandClient(hostname, port);
	}
	
	private LegacyShutdownCommandClient(String hostname) {
		super(hostname);
	}
	
	private LegacyShutdownCommandClient(String hostname, int port) {
		super(hostname, port);
	}

	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		
		properties.put(CommandClient.COMMAND_KEY, Command.SHUTDOWN);
		
		return properties;
	}
	
	@Override
	public String sendCommand() throws ClientException {
		
		String response = super.sendCommand();
		
		if (response.equals(ResponseBuilder.SUCCESS)) {
			return response;
		}
		
		throw new ClientException(String.format("Command '%s' was not successful.  Received response:  '%s'.", 
				getCommand().getProperty(CommandClient.COMMAND_KEY), response));
		
	}
	
	public static void main(String [] args) {
		
		try {			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));
			
			LegacyShutdownCommandClient client = LegacyShutdownCommandClient.newInstance(hostname, port);
			client.sendCommand();
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
	
}