package com.saic.rtws.commons.cloud.beans.vpc;

public class Route {

	private String destinationCidrBlock;
	private String gatewayId;
	private String instanceId;
	private String state;
	
	public Route(){
		
	}
	
	public void setDestinationCidrBlock(String destinationCidrBlock){
		this.destinationCidrBlock = destinationCidrBlock;
	}
	
	public String getDestinationCidrBlock(){
		return destinationCidrBlock;
	}
	
	public void setGatewayId(String gatewayId){
		this.gatewayId = gatewayId;
	}
	
	public String getGatewayId(){
		return gatewayId;
	}
	
	public void setInstanceId(String instanceId){
		this.instanceId = instanceId;
	}
	
	public String getInstanceId(){
		return instanceId;
	}
	
	public void setState(String state){
		this.state = state;
	}
	
	public String getState(){
		return state;
	}
}
