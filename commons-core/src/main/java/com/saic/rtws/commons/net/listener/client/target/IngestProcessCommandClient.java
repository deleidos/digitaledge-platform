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

public class IngestProcessCommandClient extends SingleTargetCommandClient {
	
	public static IngestProcessCommandClient newInstance(String hostname, int port, String [] args) {
		return new IngestProcessCommandClient(hostname, port, args);
	}

	private IngestProcessCommandClient(String hostname, int port, String [] args) {
		super(hostname, port, args);
	}
	
	@Override
	protected boolean isCommandValid() {
		String command = args[0];
		
		if (command == null) {
			return false;
		}
		
		if (command.equals(Command.START_INGEST) ||
			command.equals(Command.STOP_INGEST) ||
			command.equals(Command.RESTART_INGEST) ||
			command.equals(Command.RESTART_INGEST_WITH_DEBUG) ||
			command.equals(Command.RESTART_ANY_INGEST_PROCESS)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		properties.put(CommandClient.COMMAND_KEY,  args[0]);
		return properties;
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
		String response = super.sendCommand();
		
		if (response.equals(BroadcastStatus.Idle.toString()) || 
			response.equals(BroadcastStatus.Working.toString()) || 
			response.equals(BroadcastStatus.Finished.toString()) ||
			response.equals(ResponseBuilder.SUCCESS)) {
			return response;
		} else {
			throw new ClientException(String.format("Command '%s' was not successful. Received response: '%s'.", 
				getCommand().getProperty(CommandClient.COMMAND_KEY), response));
		}
	}
	
	private static void validArguments(String [] args) {
		if (args == null || (args.length != 1 && args.length != 2)) {
			System.out.println("Usage: IngestProcessCommandClient <command> [target1,target2,...]");
			System.exit(1);
		}
	}
	
	public static void main(String [] args) {
		try {		
			validArguments(args);
			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));

			IngestProcessCommandClient client = IngestProcessCommandClient.newInstance(hostname, port, args);
			client.sendCommand();
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}	
	}
	
}