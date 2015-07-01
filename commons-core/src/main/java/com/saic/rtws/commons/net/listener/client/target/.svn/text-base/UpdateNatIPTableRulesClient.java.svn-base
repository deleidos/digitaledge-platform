package com.saic.rtws.commons.net.listener.client.target;

import java.util.Properties;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.CommandClient;
import com.saic.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ClientException;

public class UpdateNatIPTableRulesClient extends SingleTargetCommandClient{

	public static final String SERVER_IP = "serverIp";
	public static final String PORT = "port";
	public static final String SUBNET_CIDR = "subnetCidr";
	
	/**
	 * 
	 * @param hostname
	 * @param args {@link Array} of {@link String} containing {serverIp, port, subnetCIDR}
	 * @return
	 */
	public static UpdateNatIPTableRulesClient newInstance(String hostname, String [] args) {
		return new UpdateNatIPTableRulesClient(hostname, args);
	}
	
	/**
	 * 
	 * @param hostname
	 * @param port
	 * @param args {@link Array} of {@link String} containing {serverIp, port, subnetCIDR}
	 * @return
	 */
	public static UpdateNatIPTableRulesClient newInstance(String hostname, int port, String [] args) {
		return new UpdateNatIPTableRulesClient(hostname, port, args);
	}
	
	/**
	 * 
	 * @param hostname
	 * @param args {@link Array} of {@link String} containing {serverIp, port, subnetCIDR}
	 */
	private UpdateNatIPTableRulesClient(String hostname, String [] args) {
		super(hostname, args);
	}
	
	/**
	 * 
	 * @param hostname
	 * @param port
	 * @param args {@link Array} of {@link String} containing {serverIp, port, subnetCIDR}
	 */
	private UpdateNatIPTableRulesClient(String hostname, int port, String [] args) {
		super(hostname, port, args);
	}

	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		
		properties.put(CommandClient.COMMAND_KEY, Command.ADD_IPTABLE_RULE);
		properties.put(SERVER_IP, args[0]);
		properties.put(PORT, args[1]);
		properties.put(SUBNET_CIDR, args[2]);
		
		return properties;
	}
	
	@Override
	public String sendCommand() throws ClientException{
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
			
			UpdateNatIPTableRulesClient client = UpdateNatIPTableRulesClient.newInstance(hostname, port, args);
			client.sendCommand();
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
}
