package com.saic.rtws.commons.net.listener.client;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.common.ResponseBuilder;

public abstract class BroadcastCommandClient extends CommandClient {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	private class RunnableCommandClient implements Runnable {
		private static final String NEW_INSTANCE_METHOD = "newInstance";
		
		private final Logger logger = LogManager.getLogger(this.getClass());
		
		private String hostname;
		private int port;
		private String [] args;
		private Class<SingleTargetCommandClient> clazz;
		
		public RunnableCommandClient(
				String hostname, 
				int port, 
				String [] args, 
				Class<SingleTargetCommandClient> clazz) {
			this.hostname = hostname;
			this.port = port;
			this.args = args;
			this.clazz = clazz;
		}

		@Override
		public void run() {
			String response = ResponseBuilder.UNKNOWN;
			
			try {
				SingleTargetCommandClient client = null;

				int numParams = (args == null || args.length == 0) ? 2 : 3;
				Method [] methods = this.clazz.getMethods();
				
				for (Method method : methods) {
					if (Modifier.isStatic(method.getModifiers()) && 
						method.getName().equals(NEW_INSTANCE_METHOD) && 
						method.getParameterTypes().length == numParams) {
						if (numParams == 2) {
							client = (SingleTargetCommandClient) method.invoke(this.clazz, this.hostname, this.port);
							break;
						} else {
							client = (SingleTargetCommandClient) method.invoke(this.clazz, this.hostname, this.port, this.args);
							break;
						}
					}
				}
				
				if (client != null) {
					logger.debug(String.format("Sending command %s to host %s.", client.getCommand(), this.hostname));
					response = client.sendCommand();
				} else {
					throw new NullPointerException("Failed to create '" + this.clazz.getSimpleName() + "' client instance.");
				}
			} catch (Exception ex) {
				logger.error(String.format("Failed to execute runnable command client %s.", this.clazz.getSimpleName()), ex);
				response = ResponseBuilder.ERROR;
			}
			
			BroadcastCommandClient.this.results.put(this.hostname, response);
		}
	}
	
	private List<String> hosts;
	private Class<SingleTargetCommandClient> clazz; 
	private int poolSize;
	private HashMap<String, String> results;
	
	protected BroadcastCommandClient(
			List<String> hosts, 
			int port, 
			String [] args, 
			int poolSize, 
			Class<SingleTargetCommandClient> clazz) {
		this.hosts = hosts;
		this.port = port;
		this.args = args;
		this.poolSize = poolSize;
		this.clazz = clazz;
	}
	
	@Override
	public String sendCommand() {
		ExecutorService pool = null;
		
		try {
			this.results = new HashMap<String, String>();
		
			pool = Executors.newFixedThreadPool(this.poolSize);
		
			@SuppressWarnings("rawtypes")
			ArrayList<Future> futures = new ArrayList<Future>();
		
			for (String host : this.hosts) {
				Future<?> future = pool.submit(new FutureTask<Boolean>(
						new RunnableCommandClient(host, this.port, this.args, this.clazz), true));
				futures.add(future);
			}
		
			waitForClientToComplete(futures);
		
			return buildResponse();
		} finally {
			if (pool != null && ! pool.isShutdown()) {
				pool.shutdown();
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected void waitForClientToComplete(ArrayList<Future> futures) {
		while (true) {
			int counter = 0;
			for (Future<?> future : futures) {
				if (future.isDone()) {
					counter++;
				}
			}
			
			if (counter == futures.size()) {
				break;
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException ie) {
				logger.error("Fail to sleep when waiting for single target command clients to complete.");
			}
		}
	}
	
	protected String buildResponse() {
		JSONObject response = new JSONObject();
		
		for (Entry<String, String> entry : this.results.entrySet()) {
			response.put(entry.getKey(), entry.getValue());
		}
		
		return response.toString();
	}
	
}