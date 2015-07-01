package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class NameNodeProcess {

	private Logger logger = LogManager.getLogger(getClass());

	private String serviceNameV1 = "hadoop-0.20-namenode";

	private String serviceNameV2 = "hadoop-hdfs-namenode";
	private String serviceName = null;

	protected static final String INIT_LOCATION = "/etc/init.d/";

	private NameNodeProcess() {

		if (new File(INIT_LOCATION + serviceNameV1).exists()) {

			serviceName = serviceNameV1;
		} else {
			// Assume exists
			serviceName = serviceNameV2;
		}
	}

	public enum NameNodeStatus {
		Running, Stopped, Unknown
	}

	public static NameNodeProcess newInstance() {
		return new NameNodeProcess();
	}

	public NameNodeStatus getStatus() throws ServerException {
		NameNodeStatus status = NameNodeStatus.Unknown;
		try {

			if (LinuxServiceUtil.statusService(serviceName) != 0) {
				status = NameNodeStatus.Stopped;
			} else {
				status = NameNodeStatus.Running;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {

		}

		return status;
	}

	public boolean start() throws ServerException {
		boolean successful = false;

		try {
			LinuxServiceUtil.startService(serviceName);
			successful = true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {

		}

		return successful;
	}

	public boolean stop() throws ServerException {
		boolean successful = false;
		try {
			LinuxServiceUtil.stopService(serviceName);
			successful = true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {

		}
		return successful;
	}

	public boolean restart() throws ServerException {

		if (!new File(INIT_LOCATION + serviceName).exists()) {
			throw new ServerException("NameNode server is not installed on this instance.");
		}

		NameNodeStatus status = getStatus();

		if (status == NameNodeStatus.Stopped) {
			return start();
		} else if (status == NameNodeStatus.Running) {
			return stop() && start();
		} else {
			throw new ServerException("Fail to determine NameNode status.");
		}

	}
}
