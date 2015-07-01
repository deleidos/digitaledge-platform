package com.saic.rtws.commons.net.listener.client.target;

import java.util.Properties;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.CommandClient;
import com.saic.rtws.commons.net.listener.client.LegacySingleTargetCommandClient;
import com.saic.rtws.commons.net.listener.exception.ClientException;
import com.saic.rtws.commons.net.listener.process.MasterProcess.MasterStatus;

public class LegacyMasterShutdownCommandClient extends LegacySingleTargetCommandClient {
	
	public static LegacyMasterShutdownCommandClient newInstance(String hostname) {
		return new LegacyMasterShutdownCommandClient(hostname);
	}
	
	public static LegacyMasterShutdownCommandClient newInstance(String hostname, int port) {
		return new LegacyMasterShutdownCommandClient(hostname, port);
	}
	
	private LegacyMasterShutdownCommandClient(String hostname) {
		super(hostname);
	}
	
	private LegacyMasterShutdownCommandClient(String hostname, int port) {
		super(hostname, port);
	}
	
	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		
		properties.put(CommandClient.COMMAND_KEY, Command.MASTER_SHUTDOWN);
		
		return properties;
	}
	
	@Override
	public String sendCommand() throws ClientException {
		
		String response = super.sendCommand();
		
		if (response.equals(MasterStatus.Stopping.toString()) || 
			response.equals(MasterStatus.Stopped.toString()) || 
			response.equals(MasterStatus.Error.toString())) {
			return response;
		}
		
		return MasterStatus.Unknown.toString();
		
	}
	
	public static void main(String [] args) {
		
		try {			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));
			
			LegacyMasterShutdownCommandClient client = LegacyMasterShutdownCommandClient.newInstance(hostname, port);
			String response = client.sendCommand();
			
			System.out.println("Response: " + response);
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
	
}