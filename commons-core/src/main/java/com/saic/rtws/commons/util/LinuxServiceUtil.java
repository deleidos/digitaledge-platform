package com.saic.rtws.commons.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LinuxServiceUtil {

	public final static Logger logger = LogManager.getLogger(LinuxServiceUtil.class);

	private static int serviceDaemon(String name, String action) throws IOException {
		int rt = 1;
		logger.info(String.format("Executing: %s %s %s", "service", name, action));
		Process p = new ProcessBuilder("service", name, action).start();
		try {
			rt = p.waitFor();
			if (rt != 0) {
				logger.error(IOUtils.toString(p.getInputStream()));
				logger.error(IOUtils.toString(p.getErrorStream()));
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		return rt;
	}

	public static void startService(String serviceName) throws IOException {
		logger.info(String.format("Starting service: %s", serviceName));
		serviceDaemon(serviceName, "start");
	}

	public static void stopService(String serviceName) throws IOException {
		logger.info(String.format("Stopping service: %s", serviceName));
		serviceDaemon(serviceName, "stop");
	}

	public static void restartService(String serviceName) throws IOException {
		logger.info(String.format("Restarting service: %s", serviceName));
		serviceDaemon(serviceName, "restart");
	}

	public static int statusService(String serviceName) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug(String.format("Checking service: %s", serviceName));
		return serviceDaemon(serviceName, "status");
	}

}
