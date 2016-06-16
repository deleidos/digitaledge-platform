package com.deleidos.rtws.commons.net.listener.client.target;

import java.util.Properties;

import com.deleidos.rtws.commons.net.listener.Command;
import com.deleidos.rtws.commons.net.listener.client.CommandClient;
import com.deleidos.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.deleidos.rtws.commons.net.listener.common.ResponseBuilder;
import com.deleidos.rtws.commons.net.listener.exception.ClientException;

public class ShutdownCommandClient extends SingleTargetCommandClient {
	
	public static ShutdownCommandClient newInstance(String hostname) {
		return new ShutdownCommandClient(hostname);
	}
	
	public static ShutdownCommandClient newInstance(String hostname, int port) {
		return new ShutdownCommandClient(hostname, port);
	}
	
	private ShutdownCommandClient(String hostname) {
		super(hostname);
	}
	
	private ShutdownCommandClient(String hostname, int port) {
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
		
		if (! response.equals(ResponseBuilder.SUCCESS)) {
			throw new ClientException(String.format("Command '%s' was not successful.  Received response:  '%s'.", 
					getCommand().getProperty(CommandClient.COMMAND_KEY), response));
		}
		
		return response;
		
	}
	
	public static void main(String [] args) {
		
		try {			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));
			
			ShutdownCommandClient client = ShutdownCommandClient.newInstance(hostname, port);
			client.sendCommand();
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
	
}