package com.saic.rtws.commons.util;

import java.util.LinkedList;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import com.saic.rtws.commons.net.jmx.JmxConnection;

public class QueryJmxByProcessGroup {

	public int getCount(String hostAndPort, String objectName, String attributeName) {
		int count = 0;

		JmxConnection connection = new JmxConnection();
		connection.setJmxUrl("service:jmx:rmi:///jndi/rmi://" + hostAndPort + "/jmxrmi");

		try {
			MBeanServerConnection jmx = connection.connectToBeanServer();

			Object value = jmx.getAttribute(new ObjectName(objectName), attributeName);
			count = (Integer) value;
		} catch (Exception e) {
			// dumb utility so ignore, but print error
			e.printStackTrace();
		} finally {
			try {
				connection.closeConnection();
			} catch (Exception e) {
				// dumb utility so ignore
				e.printStackTrace();
			}
		}
		return count;
	}
	
	public List<String> getHostnames(String hostAndPort, String objectName) {
		 List<String> hostnames = new LinkedList<String>();
		 
		 return hostnames;
	}

	public static void main(String[] args) {

		String hostAndPort = args[0];
		String objectName = args[1];
		String attributeName = args[2];

		QueryJmxByProcessGroup qg = new QueryJmxByProcessGroup();

		System.out.println(qg.getCount(hostAndPort, objectName, attributeName));
	}
}
