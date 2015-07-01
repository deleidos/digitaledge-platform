package com.saic.rtws.commons.cloud.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.saic.rtws.commons.cloud.CloudProvider;
import com.saic.rtws.commons.cloud.environment.monitor.representations.AvailableCloudComputeResources;
import com.saic.rtws.commons.cloud.exception.UnsupportedCloudProviderExecutionException;
import com.saic.rtws.commons.cloud.util.AvailableCloudComputeResourcesFetcher;

public enum AvailableCloudComputeResourcesCache {

	instance;

	public Cache<CloudProvider, AvailableCloudComputeResources> cache = CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(5l, TimeUnit.MINUTES).build();

	private AvailableCloudComputeResourcesFetcher fetcher;

	private AvailableCloudComputeResourcesCache() {
	}

	public AvailableCloudComputeResourcesFetcher getFetcher() {
		return fetcher;
	}

	public void setFetcher(AvailableCloudComputeResourcesFetcher fetcher) {
		this.fetcher = fetcher;
	}

	public AvailableCloudComputeResources get(final CloudProvider key) throws Exception {
		try {

			// If the key wasn't in the "easy to compute" group, we need to
			// do things the hard way.
			return cache.get(key, new Callable<AvailableCloudComputeResources>() {
				@Override
				public AvailableCloudComputeResources call() throws Exception {
					Logger logger = LoggerFactory.getLogger(getClass());
					logger.info("Fetching {} provider data.", key);
					return populate(key);
				}

				private AvailableCloudComputeResources populate(CloudProvider key) throws UnsupportedCloudProviderExecutionException {

					if (fetcher == null)
						throw new UnsupportedCloudProviderExecutionException(String.format("CloudProvider %s not supported.", key), new Throwable("e"));

					return fetcher.fetch();
				}
			});
		} catch (ExecutionException e) {
			throw new Exception(e.getCause());
		}

	}
}
