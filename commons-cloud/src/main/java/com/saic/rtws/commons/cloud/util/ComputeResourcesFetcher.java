package com.saic.rtws.commons.cloud.util;

import java.util.List;

import com.saic.rtws.commons.cloud.environment.monitor.representations.Instance;

public interface ComputeResourcesFetcher {
	
	public List<Instance> compute();

}
