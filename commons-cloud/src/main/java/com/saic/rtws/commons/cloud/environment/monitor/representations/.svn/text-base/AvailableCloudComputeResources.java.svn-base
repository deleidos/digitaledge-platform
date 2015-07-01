package com.saic.rtws.commons.cloud.environment.monitor.representations;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.saic.rtws.commons.cloud.CloudProvider;
import com.saic.rtws.commons.cloud.environment.monitor.representations.Instance;
import com.saic.rtws.commons.cloud.util.CloudProviderUtil;

public class AvailableCloudComputeResources {

	@JsonProperty
	private List<Instance> instances = new LinkedList<Instance>();

	@JsonProperty
	private double percentageUsed = 0d;

	public AvailableCloudComputeResources() {
		super();
	}

	public AvailableCloudComputeResources(Instance instance) {
		super();
		this.instances.add(instance);
	}

	public AvailableCloudComputeResources(@JsonProperty("instances") List<Instance> instance) {
		super();
		this.instances.addAll(instance);
	}

	public void calculatePercentageUsed() {
		// Compute Percentage Used
		double used = 0d, max = 0d;

		if (CloudProviderUtil.getCloudProvider() == CloudProvider.AWS) {
			for (Instance instance : instances) {
				used += instance.getUsed();
			}
			// only keep one of the free values because they are all the same in AWS
			max = Double.valueOf(instances.get(0).getMax());
		} else if (CloudProviderUtil.getCloudProvider() == CloudProvider.EUCA) {
			// Old way: Set free based on the amount of large(s) you can run as a measure of the total remaining for a cluster
			/*
			for (Instance instance : instances) {
				if (instance.getType().equals("m1.large")) {
					used = Double.valueOf(instance.getUsed());
					max = Double.valueOf(instance.getMax());
				}
			}
			*/ 
			//new way: calculate number of CPUs used out of a maximum
			
			int minCPU=Integer.MAX_VALUE;
			int usedCPU=0;
			for (Instance instance : instances) {
				int thisTypeTotalAvail = instance.getMax() * instance.getTypeDetails().getCpus();
				usedCPU += instance.getUsed() * instance.getTypeDetails().getCpus();
				if(thisTypeTotalAvail<minCPU)
					minCPU=thisTypeTotalAvail;
			}
			used = usedCPU;
			max = minCPU;//bad names, but it's better to underestimate
			
		}

		setPercentageUsed(Math.ceil((used / max) * 100));
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public double getPercentageUsed() {
		return percentageUsed;
	}

	public void setPercentageUsed(double percentageUsed) {
		this.percentageUsed = percentageUsed;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AvailableCloudComputeResources [instances=").append(instances).append(", percentageUsed=").append(percentageUsed).append("]");
		return builder.toString();
	}

	public AvailableCloudComputeResources withInstance(Instance instance) {
		this.instances.add(instance);
		return this;
	}

}
