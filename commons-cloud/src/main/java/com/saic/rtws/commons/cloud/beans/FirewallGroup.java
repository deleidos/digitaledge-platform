package com.saic.rtws.commons.cloud.beans;

import java.util.ArrayList;
import java.util.List;

public class FirewallGroup {
	
	private String id;
	
	private String name;
	
	private String description;
	
	private String assignedGroups;
	
	private String vpcId;
	
	private ArrayList<FirewallRule> rules = new ArrayList<FirewallRule>();
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAssignedGroups() {
		return this.assignedGroups;
	}
	
	public void setAssignedGroups(String assignedGroups) {
		this.assignedGroups = assignedGroups;
	}
	
	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public List<FirewallRule> getRules() {
		return this.rules;
	}
	
	public void addRule(FirewallRule rule) {
		this.rules.add(rule);
	}

	@Override
	public String toString() {
		return "FirewallGroup [id=" + id + ", name=" + name + ", description=" + description + ", assignedGroups=" + assignedGroups
				+ ", vpcId=" + vpcId + ", rules=" + rules + "]";
	}
	
}