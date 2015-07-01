package com.saic.rtws.commons.net.listener.process;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.exception.ServerException;

public class HiveMetaStoreProcess {

	private Logger logger = LogManager.getLogger(getClass());

	public enum HiveMetaStatus {
		Running, Stopped, Unknown
	}

	public static HiveMetaStoreProcess newInstance() {
		return new HiveMetaStoreProcess();
	}

	public HiveMetaStatus getStatus() throws ServerException {
		HiveMetaStatus status = HiveMetaStatus.Unknown;

		Process process = null;

		try {

			process = new ProcessBuilder("/bin/sh", "-c", "/usr/bin/jps -l| grep -w org.apache.hadoop.util.RunJar|wc -l").start();

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

				if (Integer.valueOf(stdout) >= 1) {
					status = HiveMetaStatus.Running;
				} else {
					status = HiveMetaStatus.Stopped;
				}
				IOUtils.closeQuietly(is);
			}

		} catch (Exception ex) {
			logger.error("Fail to get start the hive metastore.", ex);
		}

		return status;
	}

	public boolean start() throws ServerException {
		boolean successful = false;

		Process process = null;

		try {

			process = new ProcessBuilder("/bin/sh", "-c", "/usr/local/rtws/commons-core/bin/boot/hivemetastore_start.sh").start();

			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}

			int exitValue = process.exitValue();

			if (exitValue == 0)
				successful = true;

		} catch (Exception ex) {
			logger.error("Fail to get start the hive metastore.", ex);
		}

		return successful;
	}

	public boolean stop() throws ServerException {
		boolean successful = false;
		Process process = null;

		try {
			process = new ProcessBuilder("/bin/sh", "-c", "/usr/local/rtws/commons-core/bin/boot/hivemetastore_stop.sh").start();

			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}

			int exitValue = process.exitValue();

			if (exitValue == 0)
				successful = true;

		} catch (Exception ex) {
			logger.error("Fail to get stop the hive metastore.", ex);
		}
		return successful;
	}
}
