package com.saic.rtws.commons.net.listener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.executor.CommandExecutorFactory;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.status.ExecuteState;
import com.saic.rtws.commons.net.listener.util.SocketStreamWriter;

/**
 * This class represents a client connection to the command listener. This
 * thread is expecting a command in json form and will construct the
 * appropriate executor to service the command.
 * 
 * { "command" : "<COMMMAND>", ... }
 * 
 * It does support the legacy 'MASTER_SHUTDOWN' and 'SHUTDOWN' commands 
 * that is not in json format (they are simply plain text commands).
 */
public class ClientConnection extends Thread {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	private Socket socket = null;
	
	public ClientConnection(Socket socket) {
		super();
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		logger.info("Connection received from  " + this.socket.getInetAddress().getHostAddress());
		
		BufferedReader br = null;
		OutputStream os  = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = socket.getOutputStream();
			
			ExecuteResult result = null;
			String request = null;
			
			// This client connection will service one command per a read line request.
			
			while ((request = br.readLine()) != null) {
				logger.debug("Received request: " + request);
				
				// The output stream will be provided to the executor to write
				// back to the client. This thread will handle closing of the
				// output stream and socket abstracting away the need for the
				// executor to perform these cleanup tasks.
				
				result = CommandExecutorFactory.createExecutor(request, os).execute();
				if (result.getState() != ExecuteState.Continue) {
					break;
				}
			}
			
			processExecuteResult(result, os);
		} catch (Exception ex) {
			logger.error("Failed to execute command request.", ex);
			
			if (os != null) {
				String response = ResponseBuilder.buildCustomResponse(ResponseBuilder.ERROR, 
						String.format("Internal Server Error. Message: %s", ex.getMessage()));
				
				SocketStreamWriter.write(response, os);
				SocketStreamWriter.write(ResponseBuilder.buildTerminationResponse(), os);
			}
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception ex) {
				logger.debug("Error closing output stream.", ex);
			}
			
			try {
				if (socket != null && ! socket.isClosed()) {
					socket.close();
				}
			} catch (Exception ex) {
				logger.debug("Error closing socket.", ex);
			}
		}
		
	}
	
	private void processExecuteResult(ExecuteResult result, OutputStream os) throws ServerException {

		if (result.getState() == ExecuteState.LegacyTerminate) {
			SocketStreamWriter.write(ResponseBuilder.TERMINATION, os);
		} else if (result.getState() == ExecuteState.Terminate) {
			SocketStreamWriter.write(ResponseBuilder.buildTerminationResponse(), os);
		} else {
			throw new ServerException("Execute result '" + result.getState() + "' is not recognized.");
		}
		
	}
	
}