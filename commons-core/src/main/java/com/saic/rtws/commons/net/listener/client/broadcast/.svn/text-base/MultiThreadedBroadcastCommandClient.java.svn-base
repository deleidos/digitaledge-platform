package com.saic.rtws.commons.net.listener.client.broadcast;

import java.util.Arrays;
import java.util.List;

import com.saic.rtws.commons.exception.InvalidParameterException;
import com.saic.rtws.commons.net.listener.client.BroadcastCommandClient;
import com.saic.rtws.commons.net.listener.client.SingleTargetCommandClient;

public class MultiThreadedBroadcastCommandClient extends BroadcastCommandClient {
	
	private static final int DEFAULT_POOL_SIZE = 5;
	
	public static MultiThreadedBroadcastCommandClient newInstance(
			List<String> hosts, 
			Class<SingleTargetCommandClient> clazz) {
		return new MultiThreadedBroadcastCommandClient(hosts, DEFAULT_PORT, null, DEFAULT_POOL_SIZE, clazz);
	}
	
	public static MultiThreadedBroadcastCommandClient newInstance(
			List<String> hosts, 
			String [] args, 
			Class<SingleTargetCommandClient> clazz) {
		return new MultiThreadedBroadcastCommandClient(hosts, DEFAULT_PORT, args, DEFAULT_POOL_SIZE, clazz);
	}
	
	public static MultiThreadedBroadcastCommandClient newInstance(
			List<String> hosts, 
			int poolSize, 
			Class<SingleTargetCommandClient> clazz) {
		return new MultiThreadedBroadcastCommandClient(hosts, DEFAULT_PORT, null, poolSize, clazz);
	}
	
	public static MultiThreadedBroadcastCommandClient newInstance(
			List<String> hosts, 
			String [] args, 
			int poolSize, 
			Class<SingleTargetCommandClient> clazz) {
		return new MultiThreadedBroadcastCommandClient(hosts, DEFAULT_PORT, args, poolSize, clazz);
	}
	
	public static MultiThreadedBroadcastCommandClient newInstance(
			List<String> hosts, 
			int port, 
			int poolSize, 
			Class<SingleTargetCommandClient> clazz) {
		return new MultiThreadedBroadcastCommandClient(hosts, port, null, poolSize, clazz);
	}
	
	public static MultiThreadedBroadcastCommandClient newInstance(
			List<String> hosts, 
			int port, 
			String [] args, 
			int poolSize, 
			Class<SingleTargetCommandClient> clazz) {
		return new MultiThreadedBroadcastCommandClient(hosts, port, args, poolSize, clazz);
	}
	
	protected MultiThreadedBroadcastCommandClient(
			List<String> hosts, 
			int port, 
			String [] args,
			int poolSize, 
			Class<SingleTargetCommandClient> clazz) {
		super(hosts, port, args, poolSize, clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String [] args) {
		
		try {			
			String hostnames = System.getProperty("listener.hostnames", "localhost");
			Integer port = Integer.parseInt(System.getProperty("listener.port", "5555"));
			String className = System.getProperty("listener.classname");
			
			if (className == null) {
				throw new InvalidParameterException("Missing required 'listener.classname' system property.");
			}
			
			List<String> hosts = Arrays.asList(hostnames.split("[,]"));
			Class<SingleTargetCommandClient> clazz = (Class<SingleTargetCommandClient>) Class.forName(className);
			
			MultiThreadedBroadcastCommandClient client = null;
			
			if (args.length == 0) {
				client = MultiThreadedBroadcastCommandClient.newInstance(hosts, port, 5, clazz);
			} else {
				client = MultiThreadedBroadcastCommandClient.newInstance(hosts, port, args, 5, clazz);
			}
			
			String response = client.sendCommand();
			
			System.out.println("Response: " + response);
		} catch (Exception ex) {
			System.err.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
            System.exit(1);
		}
				
	}
	
}