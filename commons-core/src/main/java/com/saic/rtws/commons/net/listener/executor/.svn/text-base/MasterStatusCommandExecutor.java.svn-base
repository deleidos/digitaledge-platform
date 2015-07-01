package com.saic.rtws.commons.net.listener.executor;

import java.io.IOException;
import java.io.OutputStream;

import javax.management.ObjectName;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.jmx.JmxConnectionFactory;
import com.saic.rtws.commons.net.jmx.JmxConnectionFactoryImpl;
import com.saic.rtws.commons.net.jmx.JmxMBeanServerConnection;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.MasterProcess;
import com.saic.rtws.commons.net.listener.process.MasterProcess.MasterStatus;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class MasterStatusCommandExecutor extends CommandExecutor {

	private static final String ERROR_MESSAGE = "Failed to determine master status.";
	
	public static MasterStatusCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new MasterStatusCommandExecutor(command, request, os);
	}
	
	public MasterStatusCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {
		
		MasterStatus status = MasterProcess.newInstance().getStatus();
		
		if (MasterStatus.Running == status) {
			return buildTerminateResult(retrieveMasterProcessStatus());
		}
		
		if (MasterStatus.Stopped == status) {
			return buildTerminateResult(ResponseBuilder.buildCustomResponse(status.toString(), null));
		}
		
		return buildTerminateResult(ResponseBuilder.buildCustomResponse(status.toString(), ERROR_MESSAGE));
		
	}
	
	private String retrieveMasterProcessStatus() throws ServerException {
		
		JmxMBeanServerConnection connection = null;
		
		try {
			String name = String.format("rtws.saic.com:name=%s,type=MasterProcess,group=master", 
				RtwsConfig.getInstance().getString("master.host.name"));
			
			connection = createDefaultConnectionFactory().getConnection();
			ObjectName objName = ObjectName.getInstance(name);
			
			Object statusObj = connection.getAttribute(objName, "Status");
			Object errorMessageObj = connection.getAttribute(objName, "ErrorMessage");
			
			String status = (statusObj != null && statusObj instanceof String) ? (String) statusObj : "Unknown";
			String errorMessage = (errorMessageObj != null && errorMessageObj instanceof String) ? (String) errorMessageObj : null;
			
			return ResponseBuilder.buildCustomResponse(status, errorMessage);
		} catch (Exception ex) {
			return ResponseBuilder.buildCustomResponse(MasterStatus.Error.toString(), ERROR_MESSAGE);
		} finally {
			try { connection.close(); } catch (Exception ignore) { }
		}
		
	}
	
	private JmxConnectionFactory createDefaultConnectionFactory() throws IOException {
		
		JmxConnectionFactoryImpl jmx;
		
		try {
			jmx = new JmxConnectionFactoryImpl();
			jmx.setHost(RtwsConfig.getInstance().getString("master.host.name"));
			jmx.setPort(RtwsConfig.getInstance().getInt("master.jmx.port"));
			jmx.setConnector(RtwsConfig.getInstance().getString("master.jmx.connector"));
		} catch (Exception e) {
			throw new IOException("Unable to create JmxConnectionFactory.", e);
		}
		
		return jmx;
		
	}
	
}