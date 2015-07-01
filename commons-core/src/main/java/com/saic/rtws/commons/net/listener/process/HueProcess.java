package com.saic.rtws.commons.net.listener.process;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class HueProcess {
	private Logger logger = LogManager.getLogger(getClass());

	private String serviceName = "hue";

	protected static final String INIT_LOCATION = "/etc/init.d/";

	private HueProcess() {
	}

	public enum HiveStatus {
		Running, Stopped, Unknown
	}

	public static HueProcess newInstance() {
		return new HueProcess();
	}

	public HiveStatus getStatus() throws ServerException {
		HiveStatus status = HiveStatus.Unknown;

		try {

			if (LinuxServiceUtil.statusService(serviceName) != 0) {
				status = HiveStatus.Stopped;
			} else {
				status = HiveStatus.Running;
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

		} catch (Exception ex) {
			logger.error("Fail to start hue.", ex);
		}

		return successful;
	}

	public boolean stop() throws ServerException {
		boolean successful = false;

		try {
			LinuxServiceUtil.stopService(serviceName);
			successful = true;

		} catch (Exception ex) {
			logger.error("Fail to stop hue.", ex);
		}

		return successful;
	}
}