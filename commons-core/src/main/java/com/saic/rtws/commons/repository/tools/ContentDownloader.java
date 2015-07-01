package com.saic.rtws.commons.repository.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.PropertiesUtils;
import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.config.XMLUtils;
import com.saic.rtws.commons.jersey.config.JerseyClientConfig;
import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.target.PassThruCommandClient;
import com.saic.rtws.commons.net.listener.common.BroadcastStatus;
import com.saic.rtws.commons.net.listener.exception.ClientException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * The Class ContentDownloader.
 */
public class ContentDownloader {

	/** The log handler. */
	private static final Logger log = Logger.getLogger(ContentDownloader.class);

	/** The Jersey REST client. */
	private Client client;

	/** The configuration of the downloader. */
	private IngestContentDownloaderConfig config = null;

	/** The connection configuration to the repository. */
	private ConnectionConfig connectionConfig;

	/** The application properties. */
	private Properties properties;

	/** The threshold default. */
	private String THRESHOLD_DEFAULT = "5000";

	/** The threshold. */
	private int threshold;

	/** The retry delay default. */
	private String RETRY_DELAY_DEFAULT = "5000";

	/** The retry delay. */
	private long retryDelay;
	
	/** Keys used for sending commands to the CommandListener */
	private static final String COMMAND_KEY = "command";
	private static final String TOKEN_KEY = "token";
	private static final String STATUS_KEY = "status";

	/**
	 * Instantiates a new content downloader.
	 */
	public ContentDownloader() {

	}

	/**
	 * Download.
	 * 
	 * @param directory
	 *            the directory
	 * @param file
	 *            the file
	 * @return true, if successful
	 */
	private boolean download(Directory directory, String file) {
		boolean rt = false;
		InputStream is = null;
		FileOutputStream out = null;
		WebResource resource = null;
		try {

			if (Boolean.getBoolean("RTWS_TEST_MODE")) {
				resource = this.client
						.resource(
								this.connectionConfig.getRetrieveContentUrl()
										.replaceAll("VISIBILITY", "common"))
						.path(file)
						.path(directory.getPath())
						.queryParam("userId", this.connectionConfig.getUserId());
			} else {
				resource = this.client
						.resource(this.connectionConfig.getRetrieveContentUrl())
						.path(file)
						.path(directory.getVisibility())
						.path(directory.getPath())
						.queryParam("userId", this.connectionConfig.getUserId());
			}

			String destPath = directory.getDestination() + '/' + file;

			log.info("Downloading file " + file);

			is = resource.get(InputStream.class);
			out = new FileOutputStream(new File(destPath));

			byte buffer[] = new byte[1024];
			int len;

			while ((len = is.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}

			out.flush();

			log.info("Downloaded file to " + destPath);
			rt = true;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("downloadContent - Exception: " + e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(out);
		}

		return rt;
	}

	/**
	 * Invokes the retrieve REST service and to download the contents from the
	 * given repository directory into the configured destination.
	 * 
	 * @param directory
	 *            the directory
	 * @param files
	 *            the files
	 */
	public void downloadContent(Directory directory, List<String> files) {

		for (String file : files) {

			boolean isSuccessful = false;

			int ct = 0;
			while (ct < threshold) {
				isSuccessful = download(directory, file);

				if (!isSuccessful) {
					// delay before retry
					try {
						Thread.sleep(retryDelay);
						ct++;
					} catch (InterruptedException e) {
						log.error(e);
					}
				} else {
					break;
				}
			}
		}

	}

	/**
	 * Driver method to download files from the repository when the repository
	 * is available.
	 * 
	 * @param configFile
	 *            the config file
	 * @param retryInterval
	 *            the retry interval
	 */

	public void downloadRepositoryFiles(String configFile, Integer retryInterval) {
		try {

			config = XMLUtils.loadXML(configFile,
					IngestContentDownloaderConfig.class);

			while (!initialize()) {
				// The goal is to block indefinitely to alert of this system
				// problem
				log.error("Failed to initialize properly, retrying...");
				Thread.sleep(20000);
			}

			// Do not proceed unless the repository is available.

			int retryInSecs = retryInterval / 1000;
			while (!isRepositoryAvailable()) {
				if (retryInterval == null) {
					log.error("Unable to contact the repository ...exiting!");
					return;
				}

				log.error("Contacting the repository failed, will retry in "
						+ retryInSecs + " seconds");
				Thread.sleep(retryInterval);
			}

			// Depending on how the user configured the input configuration file
			// we either go straight to downloading the contents or we list all
			// the content out first and download everything from that
			// directory.

			for (Directory directory : config.getDirectory()) {
				if (directory.getContent() != null
						&& directory.getContent().getFile() != null
						&& directory.getContent().getFile().length > 0) {

					// A set of files are defined, these are the files the user
					// wants to download from this directory.

					downloadContent(directory,
							Arrays.asList(directory.getContent().getFile()));
				} else {

					// No file set was given in the configuration file but the
					// path
					// was given. The user wants to download all the content
					// from
					// the configured path.

					downloadContent(directory, listFiles(directory));
				}
			}
		} catch (Exception e) {
			log.error("Error downloading the files:" + e.getMessage(), e);
		}
		
		// Now that the content should be finished downloading, we want to
		// launch the ProcessGroupMonitor to begin monitoring the system
		// processes. This is done by sending a command to the command
		// listener
		startInstanceProcessGroupMonitor();
	}
	
	private void startInstanceProcessGroupMonitor(){
		String fqdn = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_FQDN);
		String jsonDef = buildStartProcessGroupMonitorCommand(null);
		PassThruCommandClient client = PassThruCommandClient.newInstance(fqdn, jsonDef);
		
		try {
			String response = client.sendCommand();
			JSONObject jsonResponse = JSONObject.fromObject(response);
			
			String status = getCommandResponseStatus(jsonResponse);
			if (status == null || status.equals(BroadcastStatus.Error.toString())) {
				throw new ClientException("Start Proecess Group Monitor command resulted in an unknown error.");
			}
		} catch (Exception e) {
			log.error("Start Proecess Group Monitor failed. Message: " + e.getMessage(), e);
		}
	}
	
	private String buildStartProcessGroupMonitorCommand(String token) {
		JSONObject jsonCmd = new JSONObject();
		jsonCmd.put(COMMAND_KEY, Command.START_PROCESS_GROUP_MONITOR);
		if (token != null) jsonCmd.put(TOKEN_KEY, token);
		return jsonCmd.toString();
	}
	
	private String getCommandResponseStatus(JSONObject jsonResponse) {
		String status = null;
		if (jsonResponse.has(STATUS_KEY)) {
			status = jsonResponse.getString(STATUS_KEY);
		}
		return status;
	}

	/**
	 * Gets the config.
	 * 
	 * @return the config
	 */
	public IngestContentDownloaderConfig getConfig() {
		return config;
	}

	/**
	 * Gets the connection config.
	 * 
	 * @return the connection config
	 */
	public ConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Initialize the application.
	 * 
	 * @return true, if successful
	 */
	private boolean initialize() {

		try {
			this.properties = PropertiesUtils.loadProperties(this.config
					.getPropertiesFile());

			String tenantId = System.getProperty("RTWS_TENANT_ID");
			String tenantPassword = (String) this.properties
					.get("webapp.repository.tenant.password");
			String repositoryUrl = (String) this.properties
					.get("webapp.repository.url.path");

			this.threshold = Integer.valueOf(this.properties.getProperty(
					"content.downloader.retry.threshold", THRESHOLD_DEFAULT));

			this.retryDelay = Long.valueOf(this.properties.getProperty(
					"content.downloader.retry.delay", RETRY_DELAY_DEFAULT));

			this.connectionConfig = new ConnectionConfig();
			this.connectionConfig.setUserId(tenantId);
			this.connectionConfig.setPassword(tenantPassword);
			this.connectionConfig.setRepositoryUrl(repositoryUrl);

			this.client = Client.create(JerseyClientConfig.getInstance().getInternalConfig());
		} catch (Exception e) {
			log.fatal("Unable to load application properties file.", e);
			return false;
		}
		return true;
	}

	/**
	 * Check whether the content repository is up or not.
	 * 
	 * @return true if the repository is up, else false
	 */
	public boolean isRepositoryAvailable() {

		String url = this.connectionConfig.getRepositoryUrl();

		try {
			WebResource resource = this.client.resource(url);
			resource.get(String.class);

			return true;
		} catch (Exception ex) {
			log.info("Repository is not available [" + url + "]. Message: " + ex.getMessage());
		}

		return false;

	}

	/**
	 * Invokes the list REST service and retrieve a list of content stored in
	 * the given repository directory.
	 * 
	 * @param directory
	 *            the directory
	 * @return a list of content from the repository
	 */
	public List<String> listFiles(Directory directory) {

		WebResource resource = this.client.resource(this.connectionConfig
				.getListContentUrl());
		resource = resource.path(directory.getVisibility())
				.path(directory.getPath())
				.queryParam("userId", this.connectionConfig.getUserId());

		try {
			String response = resource.accept(MediaType.APPLICATION_JSON).get(
					String.class);

			JSONObject json = JSONObject.fromObject(response);

			List<String> files = new ArrayList<String>();

			if (json != null && !json.isNullObject()) {
				try {
					@SuppressWarnings("rawtypes")
					Iterator nodeit = json.getJSONArray("file").iterator();
					while (nodeit.hasNext()) {
						String file = (String) nodeit.next();
						files.add(file);
					}
				} catch (Exception ex1) {
					files.add(json.getString("file"));
				}
			}

			return files;
		} catch (Exception ex2) {
			log.error("listContent - Exception: " + ex2.getMessage(), ex2);
		}

		return Collections.emptyList();

	}

	/**
	 * Sets the config.
	 * 
	 * @param config
	 *            the new config
	 */
	public void setConfig(IngestContentDownloaderConfig config) {
		this.config = config;
	}

	/**
	 * Sets the connection config.
	 * 
	 * @param connectionConfig
	 *            the new connection config
	 */
	public void setConnectionConfig(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

	/**
	 * Sets the properties.
	 * 
	 * @param properties
	 *            the new properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
