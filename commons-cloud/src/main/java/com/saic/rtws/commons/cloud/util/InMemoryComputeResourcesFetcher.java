package com.saic.rtws.commons.cloud.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.saic.rtws.commons.cloud.environment.monitor.representations.Instance;
import com.saic.rtws.commons.cloud.platform.ServiceInterface;

public class InMemoryComputeResourcesFetcher implements ComputeResourcesFetcher {

	private ServiceInterface service = null;

	public InMemoryComputeResourcesFetcher() {
		service = InterfaceConfig.getInstance().getServiceInterface();
	}

	public InMemoryComputeResourcesFetcher(ServiceInterface service) {
		super();
		this.service = service;
	}

	@Override
	public List<Instance> compute() {
		List<Instance> instanceInfo = new LinkedList<Instance>();

		Map<String, Integer> awsInstances = new HashMap<String, Integer>();

		try {
			List<com.amazonaws.services.ec2.model.Instance> runningInstances = service.listAllRunningInstances();
			for (com.amazonaws.services.ec2.model.Instance instance : runningInstances) {

				if (!awsInstances.containsKey(instance.getInstanceType())) {
					awsInstances.put(instance.getInstanceType(), new Integer(1));
				} else {
					awsInstances.put(instance.getInstanceType(), new Integer(awsInstances.get(instance.getInstanceType()).intValue() + 1));
				}
			}

			Map<String, Instance> instanceInfoMap = new HashMap<String, Instance>();
			Iterator<String> iterator = awsInstances.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();

				// Explicitly setting the free and max to 0 since that will be calculated based on data in the DB
				// Also because AWS currently does not provide the ability to query your account limits like 
				// number of instances you can run
				if (!instanceInfoMap.containsKey(key.intern()))
					instanceInfoMap.put(key.intern(), new Instance(key, 0, awsInstances.get(key), 0));

			}

			instanceInfo.addAll(instanceInfoMap.values());

		} catch (Exception e) {

		}
		return instanceInfo;
	}
}
