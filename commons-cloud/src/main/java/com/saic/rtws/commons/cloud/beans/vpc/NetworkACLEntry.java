package com.saic.rtws.commons.cloud.beans.vpc;

public class NetworkACLEntry {

	private String cidrBlock;
	private Boolean egress;
	private PortRange portRange;
	private String protocol;
	private String ruleAction;
	private int ruleNumber;
	
	public NetworkACLEntry(){
		
	}
	
	public void setCidrBlock(String cidrBlock){
		this.cidrBlock = cidrBlock;
	}
	
	public String getCidrBlock(){
		return cidrBlock;
	}
	
	public void setEgress(Boolean egress){
		this.egress = egress;
	}
	
	public Boolean getEgress(){
		return egress;
	}
	
	public void setPortRange(PortRange portRange){
		this.portRange = portRange;
	}
	
	public PortRange getPortRange(){
		return portRange;
	}
	
	public void setProtocol(String protocol){
		this.protocol = protocol;
	}
	
	public String getProtocol(){
		return protocol;
	}
	
	public void setRuleAction(String ruleAction){
		this.ruleAction = ruleAction;
	}
	
	public String getRuleAction(){
		return ruleAction;
	}
	
	public void setRuleNumber(int ruleNumber){
		this.ruleNumber = ruleNumber;
	}
	
	public int getRuleNumber(){
		return ruleNumber;
	}
}
