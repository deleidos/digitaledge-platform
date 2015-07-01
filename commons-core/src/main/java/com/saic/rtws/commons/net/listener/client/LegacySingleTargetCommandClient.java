package com.saic.rtws.commons.net.listener.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.net.ssl.SSLSocket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ClientException;
import com.saic.rtws.commons.net.listener.util.SocketStreamWriter;

/**
 * Override this class if you are targeting a specific instance using the legacy
 * shutdown listener command.
 */
public abstract class LegacySingleTargetCommandClient extends SingleTargetCommandClient {
	private Logger logger = LogManager.getLogger(this.getClass());
	
	protected LegacySingleTargetCommandClient(String hostname) {
		this(hostname, DEFAULT_PORT, null);
	}
	
	protected LegacySingleTargetCommandClient(String hostname, String [] args) {
		this(hostname, DEFAULT_PORT, args);
	}
	
	protected LegacySingleTargetCommandClient(String hostname, int port) {
		this(hostname, port, null);
	}
	
	protected LegacySingleTargetCommandClient(String hostname, int port, String [] args) {
		super(hostname, port, args);
	}
	
	@Override
	public String sendCommand() throws ClientException {
		SSLSocket socket = null;
		OutputStream os = null;
		BufferedReader br = null;
		
		try {
			socket = connect(this.hostname, this.port);
			
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			SocketStreamWriter.write(getCommand().getProperty(CommandClient.COMMAND_KEY), os);
			
			String line = null;
			StringBuilder sb = new StringBuilder();

			while((line = br.readLine()) != null){
				if (ResponseBuilder.TERMINATION.equals(line)) {
					break;
				}
				
				sb.append(line);
			}
			
			return sb.toString();
		} catch (Exception ex) {
			throw new ClientException("Fail to run command '" + getCommand() + "'.", ex);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception ex) {
				logger.error("Fail to close buffered reader stream.", ex);
			}
		
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception ex) {
				logger.error("Fail to close output stream.", ex);
			}
			
			try {
				if (socket != null) {
					closeSSLSocket(socket);
				}
			} catch (Exception ex) {
				logger.error("Fail to close ssl socket.", ex);
			}
		}
	}
	
}