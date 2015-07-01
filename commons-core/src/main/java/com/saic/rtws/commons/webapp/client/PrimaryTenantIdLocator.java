package com.saic.rtws.commons.webapp.client;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.configuration.ConversionException;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.jersey.config.JerseyClientConfig;
import com.saic.rtws.commons.jersey.filter.JerseyRetryFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * This client is used to locate the primary tenant id given
 * a primary/secondary tenant id. It does this going calling
 * a tenantapi rest service.
 */
public class PrimaryTenantIdLocator {

	private static final Object LOCK = new Object();
	private static AtomicBoolean initialized = new AtomicBoolean(false);
	
	private static String DEFAULT_WEBAPPS_SCHEME;
	private static String DEFAULT_WEBAPPS_PORT;
	private static String TENANT_API_HOST;
	private static Client REST_CLIENT_INSTANCE;	
	private static String PATH = "/tenantapi/";
	
	private Logger logger = Logger.getLogger(PrimaryTenantIdLocator.class);

	public static PrimaryTenantIdLocator newInstance() {
		return new PrimaryTenantIdLocator();
	}
	
	private PrimaryTenantIdLocator() {
		initialize();
	}

	public String getId(String tenantId) {
		WebResource resource = getClient().resource(buildBaseUrl(TENANT_API_HOST, PATH));
		resource = resource.path("json/account/link").path(tenantId);
		String linkId = resource.get(String.class);
		return linkId;
	}
	
	private void initialize() {
		if (! initialized.get()) {
			// Lazily initialize these static properties
			
			synchronized(LOCK) {
				if (DEFAULT_WEBAPPS_SCHEME == null) {
					try {
						DEFAULT_WEBAPPS_SCHEME = RtwsConfig.getInstance().getString("rtws.internal.webapp.scheme");
					} catch (ConversionException e) {
						logger.fatal("Load property [rtws.internal.webapp.scheme] failed. Message: " + e.getMessage());
					}
				}
				
				if (DEFAULT_WEBAPPS_PORT == null) {
					try {
						DEFAULT_WEBAPPS_PORT = RtwsConfig.getInstance().getString("rtws.internal.webapp.port");
					} catch (ConversionException e) {
						logger.fatal("Load property [rtws.internal.webapp.port] failed. Message: " + e.getMessage());
					}
				}
				
				if (TENANT_API_HOST == null) {
					try {
						TENANT_API_HOST = RtwsConfig.getInstance().getString("webapp.tenantapi.url.host");
					} catch (ConversionException e) {
						logger.fatal("Load file property [webapp.tenantapi.url.host] failed. Message: " + e.getMessage());
					}
				}
				
				REST_CLIENT_INSTANCE = Client.create(JerseyClientConfig.getInstance().getInternalConfig());
				REST_CLIENT_INSTANCE.addFilter(new JerseyRetryFilter(120, 30000));
			}
			// flip the initialized flag
			initialized.set(true);
		}
	}
	
	private Client getClient() {
		return REST_CLIENT_INSTANCE;
	}
	
	private String buildBaseUrl(String hostname, String path) {
		StringBuilder baseUrl = new StringBuilder();
		
		baseUrl.append(DEFAULT_WEBAPPS_SCHEME);
		baseUrl.append("://");
		baseUrl.append(hostname);
		baseUrl.append(String.format(":%s", DEFAULT_WEBAPPS_PORT));
		baseUrl.append(path);
		
		return baseUrl.toString();
	}

}