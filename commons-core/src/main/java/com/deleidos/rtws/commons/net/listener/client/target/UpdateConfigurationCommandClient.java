package com.deleidos.rtws.commons.net.listener.client.target;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.deleidos.rtws.commons.net.listener.Command;
import com.deleidos.rtws.commons.net.listener.client.CommandClient;
import com.deleidos.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.deleidos.rtws.commons.net.listener.common.BroadcastStatus;
import com.deleidos.rtws.commons.net.listener.common.ResponseBuilder;
import com.deleidos.rtws.commons.net.listener.exception.ClientException;

public class UpdateConfigurationCommandClient extends SingleTargetCommandClient {
	
	private static final int MAX_RETRIES = 10;
	
	private Logger log = Logger.getLogger(getClass());
	
	public static UpdateConfigurationCommandClient newInstance(String hostname) {
		return new UpdateConfigurationCommandClient(hostname);
	}
	
	public static UpdateConfigurationCommandClient newInstance(String hostname, String [] args) {
		return new UpdateConfigurationCommandClient(hostname, args);
	}
	
	public static UpdateConfigurationCommandClient newInstance(String hostname, int port, String [] args) {
		return new UpdateConfigurationCommandClient(hostname, port, args);
	}
	
	private UpdateConfigurationCommandClient(String hostname) {
		super(hostname);
	}
	
	private UpdateConfigurationCommandClient(String hostname, String [] args) {
		super(hostname, args);
	}
	
	private UpdateConfigurationCommandClient(String hostname, int port, String [] args) {
		super(hostname, port, args);
	}
	
	@Override
	protected Properties getCommand() {
		Properties properties = new Properties();
		properties.put(CommandClient.COMMAND_KEY, Command.UPDATE_CONFIGURATION);
		return properties;
	}
	
	@Override
	public String sendCommand() throws ClientException {
		int retries = 0;
		Exception lastException = null;
		
		while (retries++ < MAX_RETRIES) {
			try {
				return internalSendCommand();
			} catch (Exception ex) {
				log.warn("Failed updating configuration on host " + hostname + ". Retrying ...");
				lastException = ex;
			}
				
			try { Thread.sleep(10); } catch (InterruptedException ignore) { }
		}
		
		log.error("Failed updating configuration on host '" + hostname + "', exhausted retries.", lastException);
		log.debug("Reason may be connectivity issue, node no longer exists, or internal server error.");
			
		if (lastException == null) {
			throw new ClientException("Failed updating configuration on host '" + hostname + "'.");
		} else if (lastException instanceof ClientException) {
			throw (ClientException) lastException;
		} else {
			throw new ClientException("Failed updating configuration on host '" + hostname + "'.", lastException);
		}
	}
	
	private String internalSendCommand() throws ClientException {
		String response = super.sendCommand();
		
		if (response.equals(BroadcastStatus.Idle.toString()) || 
			response.equals(BroadcastStatus.Working.toString()) || 
			response.equals(BroadcastStatus.Finished.toString()) ||
			response.equals(ResponseBuilder.SUCCESS)) {
			return response;
		}
		
		throw new ClientException(String.format("Command '%s' was not successful. Received response: '%s'.", 
				getCommand().getProperty(CommandClient.COMMAND_KEY), response));
	}
}
