package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class HiveProcess {

	private Logger logger = LogManager.getLogger(getClass());

	private String serviceNameV2 = "hive-server2";

	private boolean useServiceScript = false;

	protected static final String INIT_LOCATION = "/etc/init.d/";

	private HiveProcess() {
		if (new File(INIT_LOCATION + serviceNameV2).exists())
			useServiceScript = true;

	}

	public enum HiveStatus {
		Running, Stopped, Unknown
	}

	public static HiveProcess newInstance() {
		return new HiveProcess();
	}

	public HiveStatus getStatus() throws ServerException {
		HiveStatus status = HiveStatus.Unknown;

		try {
			if (useServiceScript) {

				if (LinuxServiceUtil.statusService(serviceNameV2) != 0) {
					status = HiveStatus.Stopped;
				} else {
					status = HiveStatus.Running;
				}

			} else {
				/*
				 * TODO refactor the hiveserver startup and monitor to use something a bit more durable
				 */
				File pidFile = new File("/usr/local/rtws/ingest/logs/hiveserver.pid");
				if (pidFile.exists()) {
					status = HiveStatus.Running;
				} else {
					status = HiveStatus.Stopped;
				}
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
			if (useServiceScript) {

				LinuxServiceUtil.startService(serviceNameV2);
				successful = true;

			} else {
				String command = "su root -l -s /bin/bash -c service " + serviceNameV2 +" start";
				Process process = null;

				if (logger.isDebugEnabled())
					logger.debug("Executing command:" + command);

				process = new ProcessBuilder(command).start();

				synchronized (process) {
					process.waitFor(); // Let the script finish running before we get the exit value.
				}

				int exitValue = process.exitValue();

				if (exitValue == 0)
					successful = true;
			}
		} catch (Exception ex) {
			logger.error("Fail to get start the hiveserver.", ex);
		}

		return successful;
	}

	public boolean stop() throws ServerException {
		boolean successful = false;

		try {
			if (useServiceScript) {

				LinuxServiceUtil.stopService(serviceNameV2);
				successful = true;

			} else {
				String command = "su root -l -s /bin/bash -c service " + serviceNameV2 +" stop";
				Process process = null;

				if (logger.isDebugEnabled())
					logger.debug("Executing command:" + command);

				process = new ProcessBuilder(command).start();

				synchronized (process) {
					process.waitFor(); // Let the script finish running before we get the exit value.
				}

				int exitValue = process.exitValue();

				if (exitValue == 0)
					successful = true;
			}
		} catch (Exception ex) {
			logger.error("Fail to get stop the hiveserver.", ex);
		}

		return successful;
	}

}
