package com.saic.rtws.commons.net.listener.client.target;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.CommandClient;
import com.saic.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.saic.rtws.commons.net.listener.common.BroadcastStatus;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ClientException;

public class PurgeLuceneIndexCommandClient extends SingleTargetCommandClient {
	
	public static PurgeLuceneIndexCommandClient newInstance(String hostname, int port) {
		return new PurgeLuceneIndexCommandClient(hostname, port, null);
	}
	
	public static PurgeLuceneIndexCommandClient newInstance(String hostname, int port, String [] args) {
		return new PurgeLuceneIndexCommandClient(hostname, port, args);
	}

	private PurgeLuceneIndexCommandClient(String hostname, int port, String [] args) {
		super(hostname, port, args);
	}
	
	@Override
	protected Properties getCommand() {
		
		Properties properties = new Properties();
		properties.put(CommandClient.COMMAND_KEY, Command.PURGE_LUCENE_INDEX);
		return properties;
		
	}
	
	@Override
	protected String buildCommand(Properties commandProperties) {
		
		if (args != null && args.length == 1) {
			List<String> hosts = Arrays.asList(args[0].split("[,]"));
			commandProperties.put("hosts", hosts);
		}
		
		return super.buildCommand(commandProperties);
		
	}
	
	@Override
	public String sendCommand() throws ClientException {
		
		String response = super.sendCommand();
		
		if (response.equals(BroadcastStatus.Idle.toString()) || 
			response.equals(BroadcastStatus.Working.toString()) || 
			response.equals(BroadcastStatus.Finished.toString()) ||
			response.equals(ResponseBuilder.SUCCESS)){
			return response;
		} else {
			throw new ClientException(String.format("Command '%s' was not successful. Received response: '%s'.", 
				getCommand().getProperty(CommandClient.COMMAND_KEY), response));
		}
		
	}
	
	private static void validArguments(String [] args) {
		
		if (args.length != 0 && args.length != 1) {
			System.out.println("Usage: PurgeLuceneIndexCommandClient [target1,target2,...]");
			System.exit(1);
		}
		
	}
	
	public static void main(String [] args) {
		
		try {
			validArguments(args);
			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));
			
			PurgeLuceneIndexCommandClient client = PurgeLuceneIndexCommandClient.newInstance(hostname, port, args);
			client.sendCommand();
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
	
}