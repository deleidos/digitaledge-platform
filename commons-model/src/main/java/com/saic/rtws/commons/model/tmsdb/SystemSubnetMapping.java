package com.saic.rtws.commons.model.tmsdb;

public class SystemSubnetMapping {

	private String systemDomain;
	private String vpcId;
	private String internetGatewayId;
	private String publicSubnetId;
	private String privateSubnetId;
	private String routeTableId;
	private String cidrBlock;
	private Integer numHosts;
	private String natInstanceId;
	private String natUserData;
	
	public String getSystemDomain() { return systemDomain; }
	public String getVpcId() { return vpcId; }
	public String getInternetGatewayId() { return internetGatewayId; }
	public String getPublicSubnetId() { return publicSubnetId; }
	public String getPrivateSubnetId() { return privateSubnetId; }
	public String getRouteTableId() { return routeTableId; }
	public String getCidrBlock() { return cidrBlock; }
	public Integer getNumHosts() { return numHosts; }
	public String getNatInstanceId() { return natInstanceId; }
	public String getNatUserData() { return natUserData; }
	
	public void setSystemDomain(String systemDomain) { this.systemDomain = systemDomain; }
	public void setVpcId(String vpcId) { this.vpcId = vpcId; }
	public void setInternetGatewayId(String internetGatewayId) { this.internetGatewayId = internetGatewayId; }
	public void setPublicSubnetId(String publicSubnetId) { this.publicSubnetId = publicSubnetId; }
	public void setPrivateSubnetId(String privateSubnetId) { this.privateSubnetId = privateSubnetId; }
	public void setRouteTableId(String routeTableId) { this.routeTableId = routeTableId; }
	public void setCidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; }
	public void setNumHosts(Integer numHosts) { this.numHosts = numHosts; }
	public void setNatInstanceId(String natInstanceId) { this.natInstanceId = natInstanceId; }
	public void setNatUserData(String natUserData) { this.natUserData = natUserData; }

	
}
