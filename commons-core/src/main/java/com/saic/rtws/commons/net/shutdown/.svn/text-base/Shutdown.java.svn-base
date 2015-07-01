package com.saic.rtws.commons.net.shutdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.net.ssl.SslContext;

/**
 * This class is a poor man's excuse for a management solutions. It should be
 * replaced by a proper JMX implementation that integrates with each individual
 * application rather than an OS init.d script.
 */
@Deprecated
public class Shutdown {

	private static Logger logger = LogManager.getLogger(Shutdown.class);

	public enum MasterStatus {
		Running, Stopping, Stopped, Error, Unknown
	}

	private static int PORT = 5555;
	public static final String MASTER_COMMAND = "MASTER_SHUTDOWN";
	public static final String COMMAND = "SHUTDOWN";
	public static final String SUCCESS = "OK";
	private static boolean isShuttingDown = false;
	public static final String TERMINATION = "OUTPUT_TERMINATION";

	/**
	 * Send master shutdown command.
	 * 
	 * @param host
	 *            the host
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String sendMasterShutdownCommand(String host)
			throws IOException {
		SslContext sslContext = new SslContext(RtwsConfig.getInstance()
				.getString("shutdown.keystore"), RtwsConfig.getInstance()
				.getString("shutdown.keystore.password"), RtwsConfig
				.getInstance().getString("shutdown.truststore"), RtwsConfig
				.getInstance().getString("shutdown.truststore.password"));
		sslContext.initialize();
		SSLSocketFactory factory = sslContext.getSslContext()
				.getSocketFactory();
		// allows for a timeout to be set on the connection
		InetSocketAddress socketAddress = new InetSocketAddress(host, PORT);
		SSLSocket socket = (SSLSocket) factory.createSocket();

		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		try {
			socket.connect(socketAddress, 60 * 1000); // 1 minutes
			socket.startHandshake();
			os = socket.getOutputStream();
			ShutdownUtil.writeToSocketStream(MASTER_COMMAND, os);
			//ShutdownUtil.shutdownOutput(os);
			
			//read response from stream
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			//append data to string until termination comes across
			while((line = br.readLine()) != null){
				if(!Shutdown.TERMINATION.equals(line)){
					sb.append(line);
				}
				else{
					break;
				}
			}
			
			String response = sb.toString();

			if (response.equals(MasterStatus.Stopping.toString())
					|| response.equals(MasterStatus.Stopped.toString())
					|| response.equals(MasterStatus.Error.toString())) {
				return response;
			}
		} finally {
			try {
				//cleanup can't close streams during tls connection, it will close socket
				br.close();
				os.close();
				is.close();
				socket.close();
			} catch (Exception ignore) {
				logger.error(ignore.getMessage(), ignore);
			}
		}

		return MasterStatus.Unknown.toString();
	}

	/**
	 * Send shutdown command.
	 * 
	 * @param host
	 *            the host
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void sendShutdownCommand(String host) throws IOException {
		SslContext sslContext = new SslContext(RtwsConfig.getInstance()
				.getString("shutdown.keystore"), RtwsConfig.getInstance()
				.getString("shutdown.keystore.password"), RtwsConfig
				.getInstance().getString("shutdown.truststore"), RtwsConfig
				.getInstance().getString("shutdown.truststore.password"));
		sslContext.initialize();
		SSLSocketFactory factory = sslContext.getSslContext()
				.getSocketFactory();
		// allows for a timeout to be set on the connection
		InetSocketAddress socketAddress = new InetSocketAddress(host, PORT);
		SSLSocket socket = (SSLSocket) factory.createSocket();

		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		try {
			socket.connect(socketAddress, 60 * 1000); // 1 minutes
			socket.startHandshake();
			os = socket.getOutputStream();
			ShutdownUtil.writeToSocketStream(COMMAND, os);
			//ShutdownUtil.shutdownOutput(os);
			
			//read response from stream
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuilder sb = new StringBuilder();
			//append data to string until termination comes across
			while((line = br.readLine()) != null){
				if(!Shutdown.TERMINATION.equals(line)){
					sb.append(line);
				}
				else{
					break;
				}
			}
			
			String response = sb.toString();
			
			if (!response.equals(SUCCESS)) {
				throw new SocketException(response);
			}
		} finally {
			try {
				//cleanup can't close streams during tls connection, it will close socket
				if ( br != null )
					br.close();
				if ( is != null )
					is.close();
				if ( os != null )
					os.close();
				assert(socket != null );
				if ( !socket.isClosed() )
					socket.close();
			} catch (Exception ignore) {
				logger.error(ignore.getMessage(), ignore);
			}
		}
	}

	/**
	 * Receive shutdown command.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void receiveShutdownCommand() throws IOException {
		SslContext sslContext = new SslContext(RtwsConfig.getInstance()
				.getString("shutdown.keystore"), RtwsConfig.getInstance()
				.getString("shutdown.keystore.password"), RtwsConfig
				.getInstance().getString("shutdown.truststore"), RtwsConfig
				.getInstance().getString("shutdown.truststore.password"));
		sslContext.initialize();
		SSLServerSocketFactory serverFactory = sslContext.getSslContext()
				.getServerSocketFactory();

		ServerSocket server = serverFactory.createServerSocket(PORT);
		try {

			while (true) {
				try {
					logger.info("Accepting client connection at server.");
					(new ShutdownMultiClientThread(server.accept())).start();
				}
				catch(SSLException ssle){
					logger.error("Received ssl exception exiting listener" + ssle.toString(), ssle);
					break;
				}
				catch (IOException e) {
					// Ignore, but log
					logger.error(e.getMessage(), e);
				}
			}
		} finally {
			try {
				server.close();
			} catch (Exception ignore) {
				// Ignore, but log
				logger.error(ignore.getMessage(), ignore);
			}
		}

	}

	/**
	 * Execute master shutdown command.
	 * 
	 * @return the master status
	 * @throws Exception
	 *             the exception
	 */
	public static MasterStatus executeMasterShutdownCommand() throws Exception {
		MasterStatus status = MasterStatus.Error;

		// Shutdown has not been initiated yet.

		if (!isShuttingDown) {
			status = getMasterProcessStatus();

			if (status == MasterStatus.Running) {
				logger.debug("Initiating master shutdown command.");

				int retryCount = 0;

				try {
					retryCount = RtwsConfig.getInstance().getInt(
							"shutdown.process.retry.count");
				} catch (Exception ignore) {
					retryCount = 1; // if no default set one
				}

				for (int i = 1; i <= retryCount; i++) {
					logger.debug("Attempt number "
							+ i
							+ " to stop the processes that started in the manifest.");
					Process process = null;

					try {
						process = new ProcessBuilder(
								"/etc/init.d/rtws_init.sh", "stop").start();
						synchronized (process) {
							process.wait(10000); // Give the script 10 seconds
													// to
													// run before exiting.
						}

						isShuttingDown = true;

						logger.debug("Master shutdown command initiated.");

						return MasterStatus.Stopping;
					} catch (Exception e) {
						logger.error(e.toString(), e);
					}
				}
			}
		} else {
			// Shutdown has been initiated. If we get here, we will just
			// see if the master is running or not.

			logger.debug("Master shutdown already initiated.");

			status = getMasterProcessStatus();

			if (status == MasterStatus.Running) {
				status = MasterStatus.Stopping;
			}
		}

		return status;
	}

	/**
	 * Execute shutdown command.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static void executeShutdownCommand() throws Exception {
		int retryCount = 0;
		try {
			retryCount = RtwsConfig.getInstance().getInt(
					"shutdown.process.retry.count");
		} catch (Exception ignore) {
			retryCount = 1; // if no default set one
		}

		int timeOut = 0;
		try {
			timeOut = RtwsConfig.getInstance().getInt(
					"shutdown.process.timeout.value");
			timeOut = timeOut * 60 * 1000; // min * 60 seconds * 1000, gives
											// milliseconds
		} catch (Exception ignore) {
			timeOut = 5 * 60 * 1000; // 5 min * 60 seconds * 1000, gives
										// milliseconds
		}

		for (int i = 1; i <= retryCount; i++) {
			logger.debug("Attempt number " + i
					+ " to stop the processes that started in the manifest.");
			Process process = null;
			try {
				process = new ProcessBuilder("/etc/init.d/rtws_init.sh", "stop")
						.start();
				synchronized (process) {
					process.wait(timeOut); // either process completes or times
											// out after interval
				}
			} catch (Exception e) {
				logger.error(e.toString(), e);
				if (i == retryCount) {
					throw new Exception(
							"Unable to complete shutdown of manifest processes after maximum number of retry attempts:  "
									+ i, e);
				}
			} finally {
				try {
					process.destroy();
				} catch (Exception e) {
					logger.error(e.toString(), e);
				}
			}
		}
	}

	/**
	 * Gets the master process status.
	 * 
	 * @return the master process status
	 */
	private static MasterStatus getMasterProcessStatus() {
		MasterStatus status = MasterStatus.Error;

		String command = "/usr/bin/jps | grep ClusterManager";
		Process process = null;

		try {
			logger.debug("Executing check master process command '/bin/bash -c "
					+ command + "'");

			process = new ProcessBuilder("/bin/bash", "-c", command).start();
			synchronized (process) {
				process.waitFor(); // Let the script finish running before we
									// get the exit value.
			}

			int exitValue = process.exitValue();

			logger.debug("Check master process completed with exit value "
					+ exitValue);

			if (exitValue == 0) {
				status = MasterStatus.Running;
			} else {
				status = MasterStatus.Stopped;
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}

		return status;
	}

	public static boolean getShutdownStatus() {
		return isShuttingDown;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		String port = System.getProperty("shutdown.port");

		if (port != null) {
			try {
				Shutdown.PORT = Integer.parseInt(port);
			} catch (Exception ex) {
				System.err.println("Error: Invalid shutdown port number.");
				System.exit(1);
			}
		}

		receiveShutdownCommand();
	}

}
