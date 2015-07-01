package com.saic.rtws.commons.net.listener.client.target;

import java.util.Properties;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.CommandClient;
import com.saic.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.saic.rtws.commons.net.listener.exception.ClientException;
import com.saic.rtws.commons.net.listener.process.MasterProcess.MasterStatus;

public class MasterShutdownCommandClient extends SingleTargetCommandClient {
	
	public static MasterShutdownCommandClient newInstance(String hostname) {
		return new MasterShutdownCommandClient(hostname);
	}
	
	public static MasterShutdownCommandClient newInstance(String hostname, int port) {
		return new MasterShutdownCommandClient(hostname, port);
	}
	
	private MasterShutdownCommandClient(String hostname) {
		super(hostname);
	}
	
	private MasterShutdownCommandClient(String hostname, int port) {
		super(hostname, port);
	}
	
	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		
		properties.put(CommandClient.COMMAND_KEY,  Command.MASTER_SHUTDOWN);
		
		return properties;
	}
	
	@Override
	public String sendCommand() throws ClientException {
		
		String status = super.sendCommand();
		
		if (status.equals(MasterStatus.Stopping.toString()) || 
			status.equals(MasterStatus.Stopped.toString()) || 
			status.equals(MasterStatus.Error.toString())) {
			return status;
		}
		
		return MasterStatus.Unknown.toString();
		
	}
	
	public static void main(String [] args) {
		
		try {			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));
			
			MasterShutdownCommandClient client = MasterShutdownCommandClient.newInstance(hostname, port);
			String response = client.sendCommand();
			
			System.out.println("Response: " + response);
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
	
}