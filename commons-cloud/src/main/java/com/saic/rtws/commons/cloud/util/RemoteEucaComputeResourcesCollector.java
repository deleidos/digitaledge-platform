package com.saic.rtws.commons.cloud.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

public class RemoteEucaComputeResourcesCollector extends AwsComputeResourcesCollector {

	private Logger logger = Logger.getLogger(getClass());

	public RemoteEucaComputeResourcesCollector() {
		try {
			// TODO Revisit when service endpoint is not constructed like so
			// <service-endpoint>http://192.168.32.2:8773/services/Eucalyptus</service-endpoint>
			String endpoint = InterfaceConfig.getInstance().getServiceInterface().retrieveServiceEndpoint();
			dataFetcher = new RemoteEucaComputeResourcesFetcher(new URL(String.format("http://%s:%s", endpoint.split("/")[2].split(":")[0], "9080")));
		} catch (MalformedURLException e) {
			logger.error(e);
			try {
				dataFetcher = new RemoteEucaComputeResourcesFetcher(new URL("http://unknownhost:9080"));
			} catch (MalformedURLException e1) {
				logger.error(e);
			}
		} finally {
		}
	}

}