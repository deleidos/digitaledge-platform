package com.deleidos.rtws.systemcfg.beans;

import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.deleidos.rtws.systemcfg.beans.externalresource.ExternalResource;
import com.deleidos.rtws.systemcfg.beans.servercluster.ServerCluster;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SystemConfig {
	public static final long DEFAULT_NODE_LAUNCH_TIMEOUT_MILLIS = 90000;
	
	private String name;
	private String version;
	private String domain;
	private Long nodeLaunchTimeoutMillis;
	private Set<DataModel> dataModels;
	private Set<ExternalResource> externalResources;
	private List<ServerCluster> serverClusters;
	
	public SystemConfig() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Long getNodeLaunchTimeoutMillis() {
		return nodeLaunchTimeoutMillis;
	}

	public void setNodeLaunchTimeoutMillis(Long nodeLaunchTimeoutMillis) {
		this.nodeLaunchTimeoutMillis = nodeLaunchTimeoutMillis;
	}
	
	public Set<DataModel> getDataModels() {
		return dataModels;
	}
	
	public void setDataModels(Set<DataModel> dataModels) {
		this.dataModels = dataModels;
	}

	public Set<ExternalResource> getExternalResources() {
		return externalResources;
	}

	public void setExternalResources(Set<ExternalResource> externalResources) {
		this.externalResources = externalResources;
	}

	public List<ServerCluster> getServerClusters() {
		return serverClusters;
	}

	public void setServerClusters(List<ServerCluster> serverClusters) {
		this.serverClusters = serverClusters;
	}
}
