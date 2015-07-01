package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class ZookeeperProcess {

	private Logger logger = LogManager.getLogger(getClass());

	private String serviceNameV1 = "hadoop-zookeeper-server";
	private String serviceNameV2 = "zookeeper-server";
	private String serviceName = null;

	protected static final String INIT_LOCATION = "/etc/init.d/";

	private ZookeeperProcess() {
		if (new File(INIT_LOCATION + serviceNameV1).exists()) {
			serviceName = serviceNameV1;
		} else {
			serviceName = serviceNameV2; // Assume exists
		}
	}
	
	public enum ZookeeperStatus {
		Running, Stopped, Unknown
	}

	public static ZookeeperProcess newInstance() {
		return new ZookeeperProcess();
	}

	public ZookeeperStatus getStatus() {
		try {
			if (LinuxServiceUtil.statusService(serviceName) != 0) {
				return ZookeeperStatus.Stopped;
			} else {
				return ZookeeperStatus.Running;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} 
		return ZookeeperStatus.Unknown;
	}

	public boolean start() {
		try {
			LinuxServiceUtil.startService(serviceName);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} 
		return false;
	}

	public boolean stop() {
		try {
			LinuxServiceUtil.stopService(serviceName);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public boolean restart() throws ServerException {
		if (! new File(INIT_LOCATION + serviceName).exists()) {
			throw new ServerException("Zookeeper server is not installed on this instance.");
		}

		ZookeeperStatus status = getStatus();
		if (status == ZookeeperStatus.Stopped) {
			return start();
		} else if (status == ZookeeperStatus.Running) {
			return stop() && start();
		} else {
			throw new ServerException("Failed to determine Zookeeper status.");
		}
	}

}
