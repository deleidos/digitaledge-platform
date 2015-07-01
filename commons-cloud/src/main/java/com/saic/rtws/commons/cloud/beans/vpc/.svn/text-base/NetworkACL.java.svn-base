package com.saic.rtws.commons.cloud.beans.vpc;

import java.util.ArrayList;
import java.util.List;

public class NetworkACL {

	private String networkACLId;
	private String vpcId;
	private List<NetworkACLEntry> networkACLEntries;
	
	public NetworkACL(){
		
	}
	
	public void setNetworkACLId(String networkACLId){
		this.networkACLId = networkACLId;
	}
	
	public String getNetworkACLId(){
		return networkACLId;
	}
	
	public void setVpcId(String vpcId){
		this.vpcId = vpcId;
	}
	
	public String getVpcId(){
		return vpcId;
	}
	
	public void setNetworkACLEntries(List<NetworkACLEntry> networkACLEntries){
		this.networkACLEntries = networkACLEntries;
	}
	
	public List<NetworkACLEntry> getNEtworkACLEntries(){
		return networkACLEntries;
	}
	
	public void addNetworkACLEntry(NetworkACLEntry networkACLEntry){
		if(networkACLEntries == null){
			networkACLEntries = new ArrayList<NetworkACLEntry>();
		}
		
		networkACLEntries.add(networkACLEntry);
	}
}
