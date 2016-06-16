package com.deleidos.rtws.commons.net.listener.client.target;

import java.util.Properties;

import com.deleidos.rtws.commons.net.listener.Command;
import com.deleidos.rtws.commons.net.listener.client.CommandClient;
import com.deleidos.rtws.commons.net.listener.client.SingleTargetCommandClient;

public class MasterStatusCommandClient extends SingleTargetCommandClient {

	public static MasterStatusCommandClient newInstance(String hostname) {
		return new MasterStatusCommandClient(hostname);
	}
	
	public static MasterStatusCommandClient newInstance(String hostname, int port) {
		return new MasterStatusCommandClient(hostname, port);
	}
	
	private MasterStatusCommandClient(String hostname) {
		super(hostname);
	}
	
	private MasterStatusCommandClient(String hostname, int port) {
		super(hostname, port);
	}

	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		properties.put(CommandClient.COMMAND_KEY, Command.MASTER_STATUS);
		return properties;
	}
	
	public static void main(String [] args) {
		
		try {			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));
			
			MasterStatusCommandClient client = MasterStatusCommandClient.newInstance(hostname, port);
			String response = client.sendCommand();
			
			System.out.println("Response: " + response);
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
	
}