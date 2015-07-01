package com.saic.rtws.commons.jersey.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 * An extension of the Jersey ClientFilter to allow for the retrying of requests.
 * Add this filter to your Jersey client to automatically retry all IOExceptions
 * and any HTTP errors > 500 the given number of times.
 */
public class JerseyRetryFilter extends ClientFilter {

	private static final Logger logger = Logger.getLogger(JerseyRetryFilter.class);

	private int maxRetries = 0;
	private int retryDelay = 0;
	
	public JerseyRetryFilter(int maxRetries, int retryDelay) {
		this.maxRetries = maxRetries;
		this.retryDelay = retryDelay;
	}

	@Override
	public ClientResponse handle(ClientRequest clientrequest)
			throws ClientHandlerException {

		int attempt = 0;
		ClientResponse response = null;
		RuntimeException rtException = null;
		boolean caughtException = false;
		while (true) {
			try {
				caughtException = false;
				response = getNext().handle(clientrequest);
				// return for statuses less than 500
				if (response.getStatus() < HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
					return response;
				}
			} catch (RuntimeException rte) {
				caughtException = true;
				rtException = rte;
				if (rte instanceof UniformInterfaceException) {
					UniformInterfaceException ue = (UniformInterfaceException)rte;
					ClientResponse cr = ue.getResponse();
					if (cr.getStatus() < HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
						throw rte;
					}
				}
				
				if (rte instanceof ClientHandlerException) {
					Throwable chCause = rte.getCause();
					if (!(chCause instanceof IOException)) {
						throw rte;
					}
				}
			}

			attempt++;
			logger.warn("Attempt " + attempt + " out of " + maxRetries + " failed for: " + clientrequest.getURI());
			if (caughtException) {
				logger.warn("Exception: " + rtException);
			} else {
				logger.warn("HTTP Status: " + response.getStatus());
			}
			
			if (attempt >= maxRetries) {
				logger.warn("Maximum retry attempts reached!");
				if (caughtException) {
					throw rtException;
				} else {
					return response;
				}
			}
			
			try { Thread.sleep(retryDelay); } catch (InterruptedException ignore) {}
		}
	}

}
