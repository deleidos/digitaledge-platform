package com.saic.rtws.commons.net.listener.process;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class CassandraNodeProcess {
	
	private static final String SERVICE_NAME = "cassandra";
	
	private Logger logger = LogManager.getLogger(getClass());
	
	public enum CassandraNodeStatus {
		Running, Stopped, Unknown
	}
	
	public static CassandraNodeProcess newInstance() {
		return new CassandraNodeProcess();
	}
	
	public CassandraNodeStatus getStatus() throws ServerException {
		CassandraNodeStatus status = CassandraNodeStatus.Unknown;

		try {
			if (LinuxServiceUtil.statusService(SERVICE_NAME) != 0) {
				status = CassandraNodeStatus.Stopped;
			} else {
				status = CassandraNodeStatus.Running;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return status;
	}
	
	public boolean start() throws ServerException {
		try {
			LinuxServiceUtil.startService(SERVICE_NAME);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} 

		return false;
	}
	
	public boolean stop() throws ServerException {
		try {
			LinuxServiceUtil.stopService(SERVICE_NAME);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} 
		
		return false;
	}
	
	public boolean restart() throws ServerException {
		CassandraNodeStatus status = getStatus();

		if (status == CassandraNodeStatus.Stopped) {
			return start();
		} else if (status == CassandraNodeStatus.Running) {
			return stop() && start();
		} else {
			throw new ServerException("Failed to determine cassandra node status.");
		}
	}
	
}