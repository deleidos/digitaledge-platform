package com.saic.rtws.commons.net.listener.process;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.listener.exception.ServerException;

public class H2Process {

	private Logger logger = LogManager.getLogger(getClass());

	private String saUser = RtwsConfig.getInstance().getString("h2.sa.connection.user", "SA");
	private String saPassword = RtwsConfig.getInstance().getString("h2.sa.connection.password", "<redacted>");
	private String testSql = "select 1";

	public enum H2Status {
		Running, Stopped, Unknown
	}

	public static H2Process newInstance() {
		return new H2Process();
	}

	public H2Status getStatus() throws ServerException {
		H2Status status = H2Status.Unknown;

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:tcp://127.0.0.1:8161/commondb", saUser, saPassword);
			stmt = conn.createStatement();
			stmt.execute(testSql);

			status = H2Status.Running;
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			status = H2Status.Stopped;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			status = H2Status.Stopped;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}

			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
		}

		return status;
	}

	public boolean start() throws ServerException {
		boolean successful = false;

		Process process = null;

		try {

			// If using the cloudera-hive-metastore.ini, must pass additional param to command
			if (isUsingHiveMetastore()) {
				process = new ProcessBuilder("/bin/bash", "-c", "/usr/local/rtws/commons-core/bin/boot/run_h2.sh /mnt/rdafs true").start();

			} else {
				process = new ProcessBuilder("/bin/bash", "-c", "/usr/local/rtws/commons-core/bin/boot/run_h2.sh /mnt/rdafs").start();

			}

			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}

			int exitValue = process.exitValue();

			if (exitValue == 0) {
				successful = true;
				if (logger.isDebugEnabled()) {
					InputStream is = process.getInputStream();
					String stdout = IOUtils.toString(is);

					logger.debug("stdout:" + stdout);
					IOUtils.closeQuietly(is);
				}

			}

		} catch (Exception ex) {
			logger.error("Fail to get start H2.", ex);
		}

		return successful;
	}

	private boolean isUsingHiveMetastore() throws IOException {
		boolean isMetastoreProcessGroup = false;
		File rtwsrc = new File("/etc/rtwsrc");

		Properties rtwsrcProps = new Properties();

		Reader rtwrcReader = null;
		try {
			rtwrcReader = new FileReader(rtwsrc);
			rtwsrcProps.load(rtwrcReader);
			if (rtwsrcProps.getProperty("RTWS_MANIFEST") != null) {
				// TODO Must keep in sync with manifest used for the Hive metastore
				isMetastoreProcessGroup = rtwsrcProps.getProperty("RTWS_MANIFEST").contains("cloudera-hive-metastore.ini");
				if (!isMetastoreProcessGroup)
					isMetastoreProcessGroup = rtwsrcProps.getProperty("RTWS_MANIFEST").contains("cloudera-hive-metastore-v2.ini");
			}
		} finally {
			IOUtils.closeQuietly(rtwrcReader);
		}

		return isMetastoreProcessGroup;
	}

	public boolean stop() throws ServerException {

		boolean successful = false;

		Process process = null;

		try {

			process = new ProcessBuilder("/bin/bash", "-c", "java -cp /usr/local/rtws/commons-core/lib/h2*.jar org.h2.tools.Server -tcpShutdown tcp://localhost:8161").start();

			synchronized (process) {
				process.waitFor(); // Let the script finish running before we get the exit value.
			}

			int exitValue = process.exitValue();

			if (exitValue == 0) {
				successful = true;

				if (logger.isDebugEnabled()) {
					InputStream is = process.getInputStream();
					String stdout = IOUtils.toString(is);
					logger.debug("stdout:" + stdout);
					IOUtils.closeQuietly(is);
				}

			}

		} catch (Exception ex) {
			logger.error("Fail to get stop H2.", ex);
		}

		return successful;
	}

	public boolean restart() throws ServerException {

		boolean succesful = stop();
		if (succesful) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			succesful = start();
		}
		return succesful;
	}

}
