package com.saic.rtws.commons.model.tmsdb;

public class VpcNetwork {
	
	private String accountId;
	private String vpcId;
	private String publicSubnetId;
	private String internetGatewayId;
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getVpcId() {
		return vpcId;
	}
	
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	
	public String getPublicSubnetId() {
		return publicSubnetId;
	}
	
	public void setPublicSubnetId(String publicSubnetId) {
		this.publicSubnetId = publicSubnetId;
	}
	
	public String getInternetGatewayId() {
		return internetGatewayId;
	}
	
	public void setInternetGatewayId(String internetGatewayId) {
		this.internetGatewayId = internetGatewayId;
	}
	
}