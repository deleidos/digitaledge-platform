package com.saic.rtws.commons.cloud.beans.vpc;

public class Subnet {

	private String availabilityZone;
	private int availableIpAddressCount;
	private String cidrBlock;
	private String state;
	private String subnetId;
	private String vpcId;
	
	public Subnet(){
		
	}
	
	public void setAvailabilityZone(String availabilityZone){
		this.availabilityZone = availabilityZone;
	}
	
	public String getAvailabilityZone(){
		return availabilityZone;
	}
	
	public void setAvailableIpAddressCount(int availableIpAddressCount){
		this.availableIpAddressCount = availableIpAddressCount;
	}
	
	public int getAvailableIpAddressCount(){
		return availableIpAddressCount;
	}
	
	public void setCidrBlock(String cidrBlock){
		this.cidrBlock = cidrBlock;
	}
	
	public String getCidrBlock(){
		return cidrBlock;
	}
	
	public void setState(String state){
		this.state = state;
	}
	
	public String getState(){
		return state;
	}
	
	public void setSubnetId(String subnetId){
		this.subnetId = subnetId;
	}
	
	public String getSubnetId(){
		return subnetId;
	}
	
	public void setVpcId(String vpcId){
		this.vpcId = vpcId;
	}
	
	public String getVpcId(){
		return vpcId;
	}
}
