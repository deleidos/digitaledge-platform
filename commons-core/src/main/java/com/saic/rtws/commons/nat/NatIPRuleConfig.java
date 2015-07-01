package com.saic.rtws.commons.nat;

import java.util.ArrayList;
import java.util.List;

public class NatIPRuleConfig {

	private List<NatIPGroupRule> natIPRules;
	
	public NatIPRuleConfig(){
		
	}

	public List<NatIPGroupRule> getNatIPRules() {
		if(natIPRules == null){
			natIPRules = new ArrayList<NatIPGroupRule>();
		}
		return natIPRules;
	}

	public void setNatIPRules(List<NatIPGroupRule> natIPRules) {
		this.natIPRules = natIPRules;
	}
	
	public void addNatIPRule(NatIPGroupRule natIPRule){
		if(natIPRules == null){
			natIPRules = new ArrayList<NatIPGroupRule>();
		}
		
		natIPRules.add(natIPRule);
	}
	
	public NatIPGroupRule findRule(String groupName){
		for(NatIPGroupRule ruleConfig : natIPRules){
			if(ruleConfig.getGroupName().equals(groupName)){
				return  ruleConfig;
			}
		}
		
		return null;
	}
	
}
