package com.saic.rtws.security;

public class RestrictedFirewallGroup
{
	private String groupName;
	
	private String groupDescription;
	
	private RestrictedFirewallRule [] firewallRule;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getGroupDescription() {
		return groupDescription;
	}
	
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public RestrictedFirewallRule [] getFirewallRule() {
		return firewallRule;
	}

	public void setFirewallRule(RestrictedFirewallRule [] firewallRule) {
		this.firewallRule = firewallRule;
	}
	
	public RestrictedFirewallRule find(String protocol, String fromPort, String toPort, String source) {
		if (firewallRule != null) {
			for (RestrictedFirewallRule rule : firewallRule) {
				if (rule.getProtocol().equals(protocol) && rule.getFromPort().equals(fromPort) &&
					rule.getToPort().equals(toPort) && rule.find(source) != null){
					return rule;
				}
			}
		}
		
		return null;
	}
	
}