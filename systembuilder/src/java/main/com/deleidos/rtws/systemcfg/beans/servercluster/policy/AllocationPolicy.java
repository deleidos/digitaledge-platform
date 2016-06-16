package com.deleidos.rtws.systemcfg.beans.servercluster.policy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AllocationPolicy {
	private int minServers;
	private int maxServers;
	
	public int getMinServers() {
		return minServers;
	}
	
	public void setMinServers(int minServers) {
		this.minServers = minServers;
	}
	
	public int getMaxServers() {
		return maxServers;
	}
	
	public void setMaxServers(int maxServers) {
		this.maxServers = maxServers;
	}

}
