package com.deleidos.rtws.systemcfg.beans.servercluster;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.deleidos.rtws.systemcfg.beans.UserAddressableImpl;
import com.deleidos.rtws.systemcfg.beans.servercluster.policy.AllocationPolicy;
import com.deleidos.rtws.systemcfg.beans.servercluster.serverconfig.IaasServerConfig;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerClusterImpl extends UserAddressableImpl implements ServerCluster {
	private String name;
	private String clusterSubDomain;
	private Boolean isPermIpNeeded;
	private IaasServerConfig iaasServerConfig;
	private AllocationPolicy allocationPolicy;
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClusterSubDomain() {
		return clusterSubDomain;
	}
	
	public void setClusterSubDomain(String clusterSubDomain) {
		this.clusterSubDomain = clusterSubDomain;
	}
	
	public Boolean isPermIpNeeded() {
		return isPermIpNeeded;
	}

	public void setPermIpNeeded(Boolean isPermIpNeeded) {
		this.isPermIpNeeded = isPermIpNeeded;
	}

	public IaasServerConfig getIaasServerConfig() {
		return iaasServerConfig;
	}

	public void setIaasServerConfig(IaasServerConfig iaasServerConfig) {
		this.iaasServerConfig = iaasServerConfig;
	}
	
	public AllocationPolicy getAllocationPolicy() {
		return allocationPolicy;
	}
	
	public void setAllocationPolicy(AllocationPolicy allocationPolicy) {
		this.allocationPolicy = allocationPolicy;
	}
}
