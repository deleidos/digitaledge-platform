package com.deleidos.rtws.commons.net.listener.client.target;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.net.ssl.SSLSocket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.deleidos.rtws.commons.net.listener.client.CommandClient;
import com.deleidos.rtws.commons.net.listener.common.ResponseBuilder;
import com.deleidos.rtws.commons.net.listener.exception.ClientException;
import com.deleidos.rtws.commons.net.listener.util.SocketStreamWriter;

/**
 * Takes a command in json format and sends it to the command listener.
 */
public class PassThruCommandClient extends CommandClient {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	private String jsonDef;
	
	public static PassThruCommandClient newInstance(String hostname, String jsonDef) {
		return new PassThruCommandClient(hostname, DEFAULT_PORT, jsonDef);
	}
	
	public static PassThruCommandClient newInstance(String hostname, int port, String jsonDef) {
		return new PassThruCommandClient(hostname, port, jsonDef);
	}
	
	private PassThruCommandClient(String hostname, int port, String jsonDef) {
		this.hostname = hostname;
		this.port = port;
		this.jsonDef = jsonDef;
	}

	/**
	 * Passes a command in json format to the command listener and
	 * returns back the result whatever the listener gives back.
	 */
	@Override
	public String sendCommand() throws ClientException {
		SSLSocket socket = null;
		OutputStream os = null;
		BufferedReader br = null;
		
		try {
			if (jsonDef == null) {
				throw new ClientException("Command cannot be null.");
			}
			
			socket = connect(this.hostname, this.port);
			
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			SocketStreamWriter.write(jsonDef, os);
			
			String line = null;
			StringBuilder sb = new StringBuilder();

			while((line = br.readLine()) != null){
				String response = getStatus(line);
				if (ResponseBuilder.TERMINATION.equals(response)) {
					break;
				}
				
				sb.append(line);
			}
			
			return sb.toString();
		} catch (Exception ex) {
			logger.error(String.format("Failed to send command %s using pass thru client.", jsonDef), ex);
			throw new ClientException(String.format("Failed to send command %s using pass thru client.", jsonDef), ex);
		} finally {
			close(br, os, socket);
		}
	}
	
	public static void main(String [] args) {
		
		try {
			if (args == null || args.length != 1) {
				System.out.println("Usage: PassThruCommandClient <json-format-command>");
				System.exit(1);
			}
			
			String hostname = System.getProperty("listener.hostname", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));

			PassThruCommandClient client = PassThruCommandClient.newInstance(hostname, port, args[0]);
			System.out.println("Response: " + client.sendCommand());
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
	
}