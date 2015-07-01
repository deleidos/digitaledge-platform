package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class HbaseMasterProcess {

	private Logger logger = LogManager.getLogger(getClass());

	private String serviceNameV1 = "hadoop-hbase-master";

	private String serviceNameV2 = "hbase-master";

	private String serviceName = null;

	protected static final String INIT_LOCATION = "/etc/init.d/";

	public enum HbaseMasterStatus {
		Running, Stopped, Unknown
	}

	public static HbaseMasterProcess newInstance() {
		return new HbaseMasterProcess();
	}

	private HbaseMasterProcess() {

		if (new File(INIT_LOCATION + serviceNameV1).exists()) {

			serviceName = serviceNameV1;
		} else {
			// Assume exists
			serviceName = serviceNameV2;
		}
	}

	public HbaseMasterStatus getStatus() throws ServerException {
		HbaseMasterStatus status = HbaseMasterStatus.Unknown;
		try {

			if (LinuxServiceUtil.statusService(serviceName) != 0) {
				status = HbaseMasterStatus.Stopped;
			} else {
				status = HbaseMasterStatus.Running;
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
			throw new ServerException("HbaseMaster server is not installed on this instance.");
		}

		HbaseMasterStatus status = getStatus();

		if (status == HbaseMasterStatus.Stopped) {
			return start();
		} else if (status == HbaseMasterStatus.Running) {
			return stop() && start();
		} else {
			throw new ServerException("Fail to determine HbaseMaster status.");
		}

	}

}
