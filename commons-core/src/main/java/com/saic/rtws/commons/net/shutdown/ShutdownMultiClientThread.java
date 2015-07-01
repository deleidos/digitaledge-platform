package com.saic.rtws.commons.net.shutdown;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.shutdown.Shutdown.MasterStatus;

public class ShutdownMultiClientThread extends Thread {

	private Logger logger = LogManager
			.getLogger(ShutdownMultiClientThread.class);
	private Socket socket = null;

	public ShutdownMultiClientThread(Socket socket) {
		super();
		this.socket = socket;
	}

	public void run() {
		logger.info("Processing client socket.");
		// InputStream is = null;
		OutputStream os = null;
		boolean handlingLogRequest = false;

		try {
			String request = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			//continually read data from connection, server tells client when its done processing request
			//sends termination command to socket to notify that it can not close
			while ((request = br.readLine()) != null) {

				logger.info("Received request: " + request);

				if (request.equals(Shutdown.MASTER_COMMAND)) {
					try {
						MasterStatus status = Shutdown
								.executeMasterShutdownCommand();
						os = socket.getOutputStream();
						ShutdownUtil.writeToSocketStream(status.toString(), os);
						ShutdownUtil.shutdownOutput(os);
						if (status == MasterStatus.Stopped) {
							break;
						}
					} catch (Exception e) {
						String error = ExceptionUtils.getFullStackTrace(e);
						os = socket.getOutputStream();
						ShutdownUtil.writeToSocketStream(error, os);
						ShutdownUtil.shutdownOutput(os);
					}
				}
				else if (!Shutdown.getShutdownStatus()
						&& request.equals(Shutdown.COMMAND)) {
					try {
						Shutdown.executeShutdownCommand();
						os = socket.getOutputStream();
						ShutdownUtil.writeToSocketStream(Shutdown.SUCCESS, os);
						ShutdownUtil.shutdownOutput(os);
						break;
					} catch (Exception e) {
						String error = ExceptionUtils.getFullStackTrace(e);
						os = socket.getOutputStream();
						ShutdownUtil.writeToSocketStream(error, os);
						ShutdownUtil.shutdownOutput(os);
					}
				}
				else{
					/**
					 * Start of interpretation of request in Json format
					 */
					try {
						JSONObject jsonReq = JSONObject.fromObject(request);
						ShutdownCommandJsonParser jsonParser = new ShutdownCommandJsonParser();
						jsonParser.setClient(socket);
						jsonParser.setJsonReq(jsonReq);
						if (jsonParser.isValidRequest()) {
							handlingLogRequest = true;
	
							jsonParser.run();
						}
	
					} catch (JSONException e) {
						ShutdownUtil
								.writeToSocketStream(
										"Request was not in a recognized json format , ignoring....",
										socket.getOutputStream());
						logger.warn(e);
						ShutdownUtil.shutdownOutput(socket.getOutputStream());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			try {

				if (!handlingLogRequest) {
					if (os != null) {
						os.close();
					}
					if (socket != null) {
						socket.close();
					}
				}
			} catch (Exception ignore) {
				// Ignore, but log
				logger.error(ignore.getMessage(), ignore);
			}
		}

	}
}
