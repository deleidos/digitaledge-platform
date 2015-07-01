package com.saic.rtws.commons.net.listener;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.net.listener.common.CommandListenerMonitorAccess;
import com.saic.rtws.commons.net.listener.model.ScheduleClient;
import com.saic.rtws.commons.net.listener.util.FileUtil;
import com.saic.rtws.commons.net.listener.util.ParameterUtil;
import com.saic.rtws.commons.net.ssl.SslContext;

public class CommandListener {
	
	private static Logger logger = LogManager.getLogger(CommandListener.class);
	
	private static int getServerPort() {
		
		try {
			String configPort = System.getProperty("listener.port");
			
			if (configPort != null) {
				return Integer.parseInt(configPort);
			}
		} catch (Exception ex) {
			logger.warn("Property 'listener.port' was not found or value is invalid, defaulting to 5555.", ex);
		}
		
		return 5555;
		
	}
	
	private static int getPoolSize() {
		
		try {
			String poolSize = System.getProperty("listener.poolSize");
			
			if (poolSize != null) {
				return Integer.parseInt(poolSize);
			}
		} catch (Exception ex) {
			logger.warn("Property 'listener.poolSize' was not found or value is invalid, defaulting to 10.", ex);
		}
		
		return 10;
		
	}
	
	private static ServerSocketFactory getServerSocketFactory() {
		
		SslContext sslContext = getSslContext();
		sslContext.initialize();
		
		return sslContext.getSslContext().getServerSocketFactory();
		
	}
	
	private static SslContext getSslContext() {
		
		String keyStore = null;
		String keyStorePwd = null;
		String trustStore = null;
		String trustStorePwd = null;
		
		while (true) {
			// Load the listener keystore and truststore properties
			
			if (ParameterUtil.anyNull(keyStore, keyStorePwd, trustStore, trustStorePwd)) {
				keyStore = RtwsConfig.getInstance().getString("listener.keystore");
				keyStorePwd = RtwsConfig.getInstance().getString("listener.keystore.password");
				trustStore = RtwsConfig.getInstance().getString("listener.truststore");
				trustStorePwd = RtwsConfig.getInstance().getString("listener.truststore.password");
			}
			
			// If the new listener keystore and truststore properties cannot be found. Search for the
			// legacy shutdown keystore and truststure properties. This is for backward compatiability.
		
			if (ParameterUtil.anyNull(keyStore, keyStorePwd, trustStore, trustStorePwd)) {
				keyStore = RtwsConfig.getInstance().getString("shutdown.keystore");
				keyStorePwd = RtwsConfig.getInstance().getString("shutdown.keystore.password");
				trustStore = RtwsConfig.getInstance().getString("shutdown.truststore");
				trustStorePwd = RtwsConfig.getInstance().getString("shutdown.truststore.password");
			}
			
			if (! ParameterUtil.anyNull(keyStore, keyStorePwd, trustStore, trustStorePwd) && FileUtil.exists(keyStore, trustStore)) {
				 break;
			}
			
			logger.info("Cannot find the keystore and truststore to start the command listener. Will wait 5 seconds and retry ...");
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ie) {
				logger.error("Fail to wait 5 seconds before attempting to load keystore and truststore.", ie);
			}
		}
		
		return new SslContext(keyStore, keyStorePwd, trustStore, trustStorePwd);
		
	}
	
	private static ServerSocket createServetSocket(ServerSocketFactory factory, int port) {
		
		try {
            return factory.createServerSocket(port);
        } catch (IOException ioe) {
        	String errorMsg = "Could not listen on port: " + port + ".";
        	
        	logger.error(errorMsg);
        	
            System.err.println("ERROR: " + errorMsg);
            System.exit(1);
        }
		
		return null;
		
	}
	
	public static void startServer(int port, int poolSize) {
		
		logger.info("Starting command listener on port " + port + " with pool size " + poolSize + ".");
		
		ServerSocket server = createServetSocket(getServerSocketFactory(), port);
		
		ExecutorService pool = Executors.newFixedThreadPool(poolSize);

		try {
			while (true) {
				try {
					logger.info("Waiting for connection ...");
					
					pool.execute(new ClientConnection(server.accept()));
				} catch (SSLException ssle) {
					logger.error("Received ssl exception, exiting listener.", ssle);
					break;
				} catch (IOException ioe) {
					logger.error("Error accepting client connection", ioe);
				}
			}
		} finally {
			try {
				server.close();
			} catch (Exception ex) {
				logger.error("Fail to close server socket.", ex);
			}
		}
		
	}
	
	private static void startMonitors() {
		CommandListenerMonitorAccess.getInstance().startSystemStatusMonitor();
		
		// ProcessGroupMonitor needs to be started differently depending on the instance.
		// For instances that require content to be downloaded(i.e. datasinks and transports),
		// the monitors need to wait for the content to download, and so, they will not start 
		// the monitors here. Instead we will wait for the Command.START_PROCESS_GROUP_MONITOR
		// command to launch the PGM. However, for systems that don't need to wait for content
		// to download, the PGM should be started here.
		if(!instanceRequiresContentDownload()) {
			CommandListenerMonitorAccess.getInstance().startProcessGroupMonitor();	
		}
	}
	
	private static boolean instanceRequiresContentDownload() {
		// To determine if this instance requires a content download, look for the
		// folders that the content downloader exists in.  Currently, this is: 
		// 		/usr/local/rtws/transport
		// 		/usr/local/rtws/ingest
		
		final String fs = System.getProperty("file.separator");
		
		final String[] contentDownloaderLocations = new String[] {
				fs + "usr" + fs + "local" + fs + "rtws" + fs + "transport",
				fs + "usr" + fs + "local" + fs + "rtws" + fs + "ingest" };
		
		for(String location : contentDownloaderLocations) {
			if((new File(location)).exists()) {
				return true;
			}
		}
		
		return false;
	}
	
	private static void startScheduleClient() {
		String nat = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_IS_NAT);
		if(nat==null || !nat.equalsIgnoreCase("true")){//do not run if a nat instance
			new ScheduleClientStarter().start();
		}
	}
	
	public static void main(String [] args) {
		
		startMonitors();
		startScheduleClient();
		startServer(getServerPort(), getPoolSize());
		
	}
	
	private static class ScheduleClientStarter extends Thread{
		@Override
		public void run(){
			try {
				if (ScheduleClient.INSTANCE.startup()) {
					logger.info("Schedule client started.");
				}
			} catch(Exception ex){
				logger.error("Failed to start schedule service. Schedule commands will not be accepted.", ex);
			}
		}
	}
	
}