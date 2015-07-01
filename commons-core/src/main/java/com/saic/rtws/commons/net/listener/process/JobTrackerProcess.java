package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.util.LinuxServiceUtil;

public class JobTrackerProcess {

	private Logger logger = LogManager.getLogger(getClass());

	private String serviceNameV1 = "hadoop-0.20-jobtracker";
	private String serviceNameV2 = "hadoop-0.20-mapreduce-jobtracker";

	private String serviceName = null;

	protected static final String INIT_LOCATION = "/etc/init.d/";

	private JobTrackerProcess() {

		if (new File(INIT_LOCATION + serviceNameV1).exists()) {

			serviceName = serviceNameV1;
		} else {
			// Assume exists
			serviceName = serviceNameV2;
		}
	}

	public enum JobTrackerStatus {
		Running, Stopped, Unknown
	}

	public static JobTrackerProcess newInstance() {
		return new JobTrackerProcess();
	}

	public JobTrackerStatus getStatus() throws ServerException {

		JobTrackerStatus status = JobTrackerStatus.Unknown;

		Process process = null;

		try {

			process = new ProcessBuilder("/bin/sh", "-c", "/usr/bin/jps -l| grep -w JobTracker|wc -l").start();

			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}

			int exitValue = process.exitValue();
			InputStream is = process.getInputStream();
			String stdout = IOUtils.toString(is).trim();

			if (logger.isDebugEnabled()) {
				logger.debug("stdout:" + stdout);

				InputStream es = process.getErrorStream();
				String stderr = IOUtils.toString(es);
				logger.debug("stderr:" + stderr);
			}

			if (exitValue == 0) {

				if (Integer.valueOf(stdout) == 1) {
					status = JobTrackerStatus.Running;
				} else {
					status = JobTrackerStatus.Stopped;
				}
				IOUtils.closeQuietly(is);
			}

		} catch (Exception ex) {
			logger.error("Fail to get start the Jobtracker Daemon.", ex);
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
			throw new ServerException("JobTracker server is not installed on this instance.");
		}

		JobTrackerStatus status = getStatus();

		if (status == JobTrackerStatus.Stopped) {
			return start();
		} else if (status == JobTrackerStatus.Running) {
			return stop() && start();
		} else {
			throw new ServerException("Fail to determine JobTracker status.");
		}

	}

}
