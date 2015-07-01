package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.SingleTargetCommandClient;
import com.saic.rtws.commons.net.listener.client.broadcast.MultiThreadedBroadcastCommandClient;
import com.saic.rtws.commons.net.listener.common.BroadcastStatus;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.status.ExecuteResultBuilder;
import com.saic.rtws.commons.net.listener.util.SocketStreamWriter;
import com.saic.rtws.commons.net.listener.util.SoftwareUtil;

public abstract class CommandExecutor {
	
	private static HashMap<String, Broadcaster> broadcasters = new HashMap<String, Broadcaster>();
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	private final Object broadcastLock = new Object();
	
	protected String command = null;
	protected String request = null;
	protected OutputStream os = null;
	
	protected class Broadcaster implements Runnable {
		private static final int TEN_MINUTES = 10 * 60 * 1000;
		
		private String classname;
		private String token;
		private String command;
		private String [] args;
		private List<String> hosts;
		private BroadcastStatusMap status;
		private String result;
		private Timestamp completed;
		private boolean canTerminate = false;
		
		public Broadcaster(
				String classname,
				String token,
				String command,
				List<String> hosts, 
				BroadcastStatusMap status) {
			this(classname, token, command, null, hosts, status);
		}
		
		public Broadcaster(
				String classname,
				String token,
				String command,
				String [] args,
				List<String> hosts, 
				BroadcastStatusMap status) {
			this.classname = classname;
			this.token = token;
			this.command = command;
			this.args = args;
			this.hosts = hosts;
			this.status = status;
		}
		
		public String getToken() { return this.token; }
		public void terminate() { this.canTerminate = true; }
		public String getResult() { return this.result; }
		
		@Override
		public void run() {
			boolean done = false;
			try {
				while (! canTerminate) {
					if (! done) {
						// Broadcast the command to all target hosts and when
						// finished mark it as done to start the expiration clock.
						
						try {
							@SuppressWarnings("unchecked")
							Class<SingleTargetCommandClient> clazz = 
								(Class<SingleTargetCommandClient>) Class.forName(this.classname);
		
							MultiThreadedBroadcastCommandClient client = (this.args == null) ?  
								MultiThreadedBroadcastCommandClient.newInstance(hosts, clazz) :
								MultiThreadedBroadcastCommandClient.newInstance(hosts, args, clazz);

							logger.info(String.format("Broadcasting command %s ...", this.command));	
							this.result = client.sendCommand();
							logger.debug(String.format("Broadcast %s event result: %s", this.command, this.result));
				
							this.status.setStatus(this.command, BroadcastStatus.Finished);
						} catch (Exception ex) {
							logger.error(String.format("Failed to broadcast %s event.", this.command), ex);
							this.status.setStatus(this.command, BroadcastStatus.Error);
						} finally {
							done = true;
							this.completed = new Timestamp(System.currentTimeMillis());
						}
					} else {
						// After ten minutes if the client hasn't retrieved the terminal status
						// of this broadcast command we'll end it so other broadcast command 
						// can be accepted.
						
						Timestamp expiration = new Timestamp(completed.getTime() + TEN_MINUTES);
						Timestamp current = new Timestamp(System.currentTimeMillis());
						
						if (current.after(expiration)) {
							logger.debug(String.format("Terminal status for command %s hasn't been retrieved for some time, expiring this broadcast.", this.command));
							break;
						} else {
							try { Thread.sleep(1000); } catch (Exception ignore) {}
						}
					}
				}
			} finally {
				// Once this thread is done mark it as idle so another commands
				// can be invoked.
				
				logger.info(String.format("Enabling command %s.", this.command));
				this.status.setStatus(this.command, BroadcastStatus.Idle);
			}
		}
	}
	
	public CommandExecutor(String command, String request, OutputStream os) {
		this.command = command;
		this.request = request;
		this.os = os;
	}
	
	public abstract ExecuteResult execute() throws ServerException;

	protected ExecuteResult broadcast(
			String commandClientClassname, 
			BroadcastStatusMap status) throws ServerException {
		return broadcast(commandClientClassname, null, status);
	}
	
	protected ExecuteResult broadcast(
			String commandClientClassname, 
			String [] args, 
			BroadcastStatusMap status) throws ServerException {
		ExecuteResult result = null;
		
		// Lock this section of the code to make it thread-safe. Prevent
		// multiple broadcaster from spawning and to read the status
		// value only after the switch statement has been executed.
		
		synchronized (broadcastLock) {
			// Only allow one broadcast command to run at a time because executing a broadcast command is a 2-step
			// process. First you start it and than the client polls for the status until it receives a finished
			// or error broadcast status.
			
			if (status.isAnyCommandInProgress() && ! status.isCommandInProgress(this.command)) {
				throw new ServerException(String.format("Another broadcast command is in progress, ignoring received command %s.", this.command));
			}
			
			BroadcastStatus currStatus = status.getStatus(this.command);	
			logger.debug(String.format("Broadcast %s current status %s.", this.command, currStatus.toString()));
		
			switch (currStatus) {
				case Idle :
					List<String> hosts = parseHosts();
					if (hosts.size() == 0) throw new ServerException(String.format("Failed to broadcast %s event because no hosts were found.", this.command));
					status.setStatus(this.command, BroadcastStatus.Working);
					String token = UUID.randomUUID().toString();
					Broadcaster broadcaster = new Broadcaster(commandClientClassname, token, this.command, args, hosts, status);
					new Thread(broadcaster).start();
					broadcasters.put(this.command, broadcaster);
					result = buildTerminateResult(ResponseBuilder.buildCustomResponse(BroadcastStatus.Working.toString(), token));
					break;
				case Working :
					validateToken();
					result = buildTerminateResult(ResponseBuilder.buildCustomResponse(BroadcastStatus.Working.toString(), null));
					break;
				case Finished :
					validateToken();
					result = buildTerminateResult(ResponseBuilder.buildCustomResponse(BroadcastStatus.Finished.toString(), getBroadcastResult()));
					terminateBroadcaster();
					break;
				case Error :
					validateToken();
					result = buildTerminateResult(ResponseBuilder.buildCustomResponse(BroadcastStatus.Error.toString(), getBroadcastResult()));
					terminateBroadcaster();
					break;
				default:
					throw new ServerException(String.format("Invalid broadcast status %s.", status));
			}
		}
		
		return result;
	}
	
	private String getBroadcastResult() {
		Broadcaster broadcaster = broadcasters.get(this.command);
		if (broadcaster != null) {
			return broadcaster.getResult();
		}
		return null;
	}
	
	private void terminateBroadcaster() {
		Broadcaster broadcaster = broadcasters.get(this.command);
		if (broadcaster != null) {
			broadcaster.terminate();
			broadcasters.remove(this.command);
		}
	}
	
	protected boolean canBroadcast() throws ServerException {
		if (SoftwareUtil.isMasterInstalled() && (parseHosts().size() > 0 || getToken() != null)) {
			return true;
		}
		
		return false;
	}
	
	protected void validateToken() throws ServerException {
		String requestToken = getToken();
		if (requestToken == null) {
			throw new ServerException(String.format("Failed to retrieve status for command %s because request token was not found.", this.command));
		}
		
		String storedToken = (broadcasters.get(this.command) != null) ? broadcasters.get(this.command).getToken() : null;
		if (storedToken == null) {
			throw new ServerException(String.format("Failed to retrieve status for command %s because stored token was not found.", this.command));
		}
		
		if (! requestToken.equals(storedToken)) {
			throw new ServerException(String.format("Failed to retrieve status for command %s because token mismatch.", this.command));
		}
	}
	
	/**
	 * Extract the token from the incoming request.
	 * 
	 * { 
	 *   "command" : "<command>",
	 *   "token" : "<token>",
	 *   ...
	 * }
	 */
	protected String getToken() throws ServerException {
		try {
			JSONObject jsonObj = JSONObject.fromObject(this.request);
			if (jsonObj.has("token")) {
				return jsonObj.getString("token");
			}
		} catch (Exception ex) {
			throw new ServerException("Failed to parse token from request.", ex);
		}
		
		return null;
	}
	
	/**
	 * Extract the hosts from the incoming request.
	 * 
	 * { 
	 *   "command" : "<command>",
	 *   "hosts"   : [ "<host1>", "<host2>", ... ],
	 *   ...
	 * }
	 */
	protected List<String> parseHosts() throws ServerException {
		ArrayList<String> hosts = new ArrayList<String>();
		
		try {
			JSONObject jsonObj = JSONObject.fromObject(this.request);
			if (jsonObj.has("hosts")) {
				JSONArray jsonArr = jsonObj.getJSONArray("hosts");
				for (int i = 0; i < jsonArr.size(); i++) {
					hosts.add(jsonArr.getString(i));
				}
			} 
		} catch (Exception ex) {
			throw new ServerException("Failed to parse hostnames from request.", ex);
		}
		
		return hosts;
	}
	
	protected boolean isLegacyCommand() {
		return Command.isLegacyCommand(this.request);
	}
	
	protected void writeToSocketStream(String message) {
		logger.debug("Writing response to socket: " + message);
		SocketStreamWriter.write(message, this.os);
	}
	
	/**
	 * @param response The last response sent to the caller before the terminate response is sent.
	 * @return The execute terminate result for the client connection thread to process.
	 */
	protected ExecuteResult buildTerminateResult(String response) {
		writeToSocketStream(response);
		return ExecuteResultBuilder.buildTerminateResult(this.command);
	}
	
	/**
	 * @param response The last response sent to the caller before the legacy terminate response is sent.
	 * @return The legacy execute terminate result for the client connection thread to process.
	 */
	protected ExecuteResult buildLegacyTerminateResult(String response) {
		writeToSocketStream(response);
		return ExecuteResultBuilder.buildLegacyTerminateResult(this.command);
	}
	
}