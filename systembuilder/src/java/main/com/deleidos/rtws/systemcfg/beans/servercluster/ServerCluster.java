package com.deleidos.rtws.systemcfg.beans.servercluster;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.deleidos.rtws.systemcfg.beans.UserAddressable;
import com.deleidos.rtws.systemcfg.beans.servercluster.jms.JmsServerCluster;
import com.deleidos.rtws.systemcfg.beans.servercluster.policy.AllocationPolicy;
import com.deleidos.rtws.systemcfg.beans.servercluster.serverconfig.IaasServerConfig;

@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,  
	include = JsonTypeInfo.As.PROPERTY,  
	property = "type")  
@JsonSubTypes({
	@Type(name = "jms", value = JmsServerCluster.class)
	}
)
public interface ServerCluster extends UserAddressable {
	// TODO Add allocationPolicy, security, and storage
	public String getName();
	public void setName(String name);
	
	public String getClusterSubDomain();
	public void setClusterSubDomain(String clusterSubDomain);
	
	public Boolean isPermIpNeeded();
	public void setPermIpNeeded(Boolean isPermIpNeeded);
	
	public IaasServerConfig getIaasServerConfig();
	public void setIaasServerConfig(IaasServerConfig serverConfig);
	
	public AllocationPolicy getAllocationPolicy();
	public void setAllocationPolicy(AllocationPolicy allocationPolicy);
}
