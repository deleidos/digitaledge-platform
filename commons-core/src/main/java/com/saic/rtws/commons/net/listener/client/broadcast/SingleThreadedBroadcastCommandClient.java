package com.saic.rtws.commons.net.listener.client.broadcast;

import java.util.Arrays;
import java.util.List;

import com.saic.rtws.commons.exception.InvalidParameterException;
import com.saic.rtws.commons.net.listener.client.BroadcastCommandClient;
import com.saic.rtws.commons.net.listener.client.SingleTargetCommandClient;

public class SingleThreadedBroadcastCommandClient extends BroadcastCommandClient {
	
	private static final int DEFAULT_POOL_SIZE = 1;
	
	public static SingleThreadedBroadcastCommandClient newInstance(List<String> hosts, Class<SingleTargetCommandClient> clazz) {
		return new SingleThreadedBroadcastCommandClient(hosts, DEFAULT_PORT, null, clazz);
	}
	
	public static SingleThreadedBroadcastCommandClient newInstance(List<String> hosts, int port, Class<SingleTargetCommandClient> clazz) {
		return new SingleThreadedBroadcastCommandClient(hosts, port, null, clazz);
	}
	
	public static SingleThreadedBroadcastCommandClient newInstance(List<String> hosts, int port, String [] args, Class<SingleTargetCommandClient> clazz) {
		return new SingleThreadedBroadcastCommandClient(hosts, port, args, clazz);
	}
	
	protected SingleThreadedBroadcastCommandClient(List<String> hosts, int port, String [] args, Class<SingleTargetCommandClient> clazz) {
		super(hosts, port, args, DEFAULT_POOL_SIZE, clazz);
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
			
			SingleThreadedBroadcastCommandClient client = null;
			
			if (args.length == 0) {
				client = SingleThreadedBroadcastCommandClient.newInstance(hosts, port, clazz);
			} else {
				client = SingleThreadedBroadcastCommandClient.newInstance(hosts, port, args, clazz);
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