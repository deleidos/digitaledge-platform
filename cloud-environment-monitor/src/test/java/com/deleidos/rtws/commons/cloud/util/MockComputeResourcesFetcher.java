/**
 * LEIDOS CONFIDENTIAL
 * __________________
 *
 * (C)[2007]-[2014] Leidos
 * Unpublished - All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the exclusive property of Leidos and its suppliers, if any.
 * The intellectual and technical concepts contained
 * herein are proprietary to Leidos and its suppliers
 * and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Leidos.
 */
package com.deleidos.rtws.commons.cloud.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.deleidos.rtws.commons.cloud.beans.State;
import com.deleidos.rtws.commons.cloud.environment.monitor.representations.AvailabilityZoneDescription;
import com.deleidos.rtws.commons.cloud.environment.monitor.representations.AvailableCloudComputeResources;
import com.deleidos.rtws.commons.cloud.environment.monitor.representations.Instance;
import com.deleidos.rtws.commons.cloud.environment.monitor.representations.InstanceDescription;
import com.deleidos.rtws.commons.cloud.environment.monitor.tasks.EucaDescribeAvailabilityZones;
import com.deleidos.rtws.commons.cloud.environment.monitor.tasks.EucaDescribeInstances;
import com.deleidos.rtws.commons.cloud.environment.monitor.tasks.MockEucaDescribeAvailabilityZones;
import com.deleidos.rtws.commons.cloud.environment.monitor.tasks.MockEucaDescribeInstances;

public class MockComputeResourcesFetcher implements ComputeResourcesFetcher, AvailableCloudComputeResourcesFetcher {

	@Override
	public List<Instance> compute() {
		EucaDescribeInstances describeInstancesCommand = new EucaDescribeInstances(new MockEucaDescribeInstances());
		List<InstanceDescription> instanceDescriptions = describeInstancesCommand.fetch();
		
		EucaDescribeAvailabilityZones describeAZCommand = new EucaDescribeAvailabilityZones(new MockEucaDescribeAvailabilityZones());
		List<AvailabilityZoneDescription> azDescriptions = describeAZCommand.fetch();
		
		return buildInstances(azDescriptions, instanceDescriptions);
	}

	@Override
	public AvailableCloudComputeResources fetch() {
		List<Instance> instances = compute();

		AvailableCloudComputeResources resources = new AvailableCloudComputeResources(instances);
		resources.calculatePercentageUsed();

		return resources;
	}
	
	private List<Instance> buildInstances(List<AvailabilityZoneDescription> azDescriptions, List<InstanceDescription> instanceDescriptions) {
		List<Instance> instances = new LinkedList<Instance>();
		
		Map<String, Integer> instanceCounts = countInstanceTypes(instanceDescriptions);
		
		for(AvailabilityZoneDescription azDescription : azDescriptions) {
			String instanceType = azDescription.getType();
			if(instanceCounts.containsKey(instanceType)) {
				Instance instance = new Instance(instanceType, azDescription.getFree(), instanceCounts.get(instanceType), azDescription.getMax());
				instance.setTypeDetails(azDescription.getTypeDetails());

				instances.add(instance);
			}
		}
		
		return instances;
	}
	
	private Map<String, Integer> countInstanceTypes(List<InstanceDescription> instanceDescriptions) {
		HashMap<String, Integer> instanceTypeCounts = new HashMap<String, Integer>();
		
		for(InstanceDescription instanceDescription : instanceDescriptions) {
			if( instanceDescription.getState().equals(State.Running) ||
				instanceDescription.getState().equals(State.Pending) ||
				instanceDescription.getState().equals(State.Stopped)	) {
				
				String type = instanceDescription.getType();
				if(instanceTypeCounts.containsKey(type)) {
					instanceTypeCounts.put(type, instanceTypeCounts.get(type) + 1);
				}
				else {
					instanceTypeCounts.put(type, 1);
				}
			}
		}
		
		return instanceTypeCounts;
	}
}
