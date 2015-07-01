package com.saic.rtws.commons.net.listener.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.listener.exception.ClientException;
import com.saic.rtws.commons.net.ssl.SslContext;

public abstract class CommandClient {
	
	protected static final String COMMAND_KEY = "command";
	protected static final String TOKEN_KEY = "token";
	protected static final int DEFAULT_PORT = 5555;
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	protected String hostname;
	protected int port;
	protected String [] args; // Additional arguments for the command client
	
	public abstract String sendCommand() throws ClientException;
	
	protected SslContext getSslContext() {
		SslContext sslContext = new SslContext(
				RtwsConfig.getInstance().getString("listener.keystore"), 
				RtwsConfig.getInstance().getString("listener.keystore.password"), 
				RtwsConfig.getInstance().getString("listener.truststore"), 
				RtwsConfig.getInstance().getString("listener.truststore.password"));
		sslContext.initialize();
		
		return sslContext;
	}
	
	protected SSLSocket connect(String hostname, int port) throws IOException {
		InetSocketAddress socketAddress = new InetSocketAddress(hostname, port);
		
		SSLSocket socket = createSSLSocket();
		
		socket.connect(socketAddress, 30 * 1000); // 30 seconds
		socket.startHandshake();
		
		return socket;
	}
	
	protected void closeSSLSocket(Socket socket) throws IOException {
		if (socket != null && ! socket.isClosed()) {
			socket.close();
		}
	}
	
	protected String getStatus(String response) {
		try {
			JSONObject jsonResponse = JSONObject.fromObject(response);
			if (jsonResponse.has("status")) {
				return jsonResponse.getString("status");
			}
		} catch(JSONException e) {
			// not a json file
			return response;
		}
		
		return null;
	}
	
	protected String buildCommand(Properties commandProperties) {
		JSONObject jsonCmd = new JSONObject();
		
		for(Object key : commandProperties.keySet()){
			jsonCmd.put(key, commandProperties.get(key));
		}
		
		return jsonCmd.toString();
	}
	
	private SSLSocket createSSLSocket() throws IOException {
		SSLSocketFactory factory = getSslContext().getSslContext().getSocketFactory();
		return (SSLSocket) factory.createSocket();
	}
	
	protected void close(BufferedReader br, OutputStream os, Socket socket) {
		
		try {
			if (br != null) {
				br.close();
			}
		} catch (Exception ex) {
			logger.error("Failed to close buffered reader stream.", ex);
		}
	
		try {
			if (os != null) {
				os.close();
			}
		} catch (Exception ex) {
			logger.error("Failed to close output stream.", ex);
		}
		
		try {
			if (socket != null) {
				closeSSLSocket(socket);
			}
		} catch (Exception ex) {
			logger.error("Failed to close ssl socket.", ex);
		}
		
	}
	
}