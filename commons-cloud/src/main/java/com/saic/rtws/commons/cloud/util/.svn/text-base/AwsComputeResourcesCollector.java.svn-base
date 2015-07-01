package com.saic.rtws.commons.cloud.util;

import com.saic.rtws.commons.cloud.environment.monitor.representations.AvailableCloudComputeResources;

public class AwsComputeResourcesCollector implements AvailableCloudComputeResourcesFetcher {

	protected ComputeResourcesFetcher dataFetcher = null;

	public ComputeResourcesFetcher getDataFetcher() {
		return dataFetcher;
	}

	public void setDataFetcher(ComputeResourcesFetcher dataFetcher) {
		this.dataFetcher = dataFetcher;
	}

	@Override
	public AvailableCloudComputeResources fetch() {
		try {

			if (dataFetcher == null)
				dataFetcher = new InMemoryComputeResourcesFetcher();

			AvailableCloudComputeResources resources = new AvailableCloudComputeResources(dataFetcher.compute());
			resources.calculatePercentageUsed();

			return resources;
		} finally {
		}
	}
}