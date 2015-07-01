package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class RegionServerProcess {

	private Logger logger = LogManager.getLogger(getClass());

	private String serviceNameV1 = "hadoop-hbase-regionserver";
	private String serviceNameV2 = "hbase-regionserver";

	private String serviceName = null;

	protected static final String INIT_LOCATION = "/etc/init.d/";

	private RegionServerProcess() {

		if (new File(INIT_LOCATION + serviceNameV1).exists()) {

			serviceName = serviceNameV1;
		} else {
			// Assume exists
			serviceName = serviceNameV2;
		}
	}

	public enum RegionServerStatus {
		Running, Stopped, Unknown
	}

	public static RegionServerProcess newInstance() {
		return new RegionServerProcess();
	}

	public RegionServerStatus getStatus() throws ServerException {
		RegionServerStatus status = RegionServerStatus.Unknown;
		try {

			if (LinuxServiceUtil.statusService(serviceName) != 0) {
				status = RegionServerStatus.Stopped;
			} else {
				status = RegionServerStatus.Running;
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
			throw new ServerException("RegionServer server is not installed on this instance.");
		}

		RegionServerStatus status = getStatus();

		if (status == RegionServerStatus.Stopped) {
			return start();
		} else if (status == RegionServerStatus.Running) {
			return stop() && start();
		} else {
			throw new ServerException("Fail to determine RegionServer status.");
		}

	}

}
