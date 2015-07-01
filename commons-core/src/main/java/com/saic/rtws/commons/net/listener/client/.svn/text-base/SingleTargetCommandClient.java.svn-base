package com.saic.rtws.commons.net.listener.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import javax.net.ssl.SSLSocket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ClientException;
import com.saic.rtws.commons.net.listener.util.SocketStreamWriter;

/**
 * Override this class if you are targeting a specific instance.
 */
public abstract class SingleTargetCommandClient extends CommandClient {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	protected SingleTargetCommandClient(String hostname) {
		this(hostname, DEFAULT_PORT, null);
	}
	
	protected SingleTargetCommandClient(String hostname, String [] args) {
		this(hostname, DEFAULT_PORT, args);
	}
	
	protected SingleTargetCommandClient(String hostname, int port) {
		this(hostname, port, null);
	}
	
	protected SingleTargetCommandClient(String hostname, int port, String [] args) {
		this.hostname = hostname;
		this.port = port;
		this.args = args;
	}
	
	protected boolean isCommandValid() {
		return true;
	}
	
	protected abstract Properties getCommand();
	
	@Override
	public String sendCommand() throws ClientException {
		SSLSocket socket = null;
		OutputStream os = null;
		BufferedReader br = null;
		
		try {
			if (! isCommandValid()) {
				throw new ClientException("Invalid command received.");
			}
			
			socket = connect(this.hostname, this.port);
			
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			SocketStreamWriter.write(buildCommand(getCommand()), os);
			
			String line = null;
			StringBuilder sb = new StringBuilder();

			while((line = br.readLine()) != null){
				String response = getStatus(line);
				if (ResponseBuilder.TERMINATION.equals(response)) {
					break;
				}
				
				sb.append(line);
			}
			
			return getStatus(sb.toString());
		} catch (Exception ex) {
			logger.error("Failed to run command '" + getCommand() + "'.", ex);
			throw new ClientException("Failed to run command '" + getCommand() + "'.", ex);
		} finally {
			close(br, os, socket);
		}
	}
	
}