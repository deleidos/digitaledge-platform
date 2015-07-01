package com.saic.rtws.security;

public class FirewallConfigRestriction
{
	private RestrictedFirewallGroup [] firewallGroup;

	public RestrictedFirewallGroup [] getFirewallGroup() {
		return firewallGroup;
	}

	public void setFirewallGroup(RestrictedFirewallGroup [] firewallGroup) {
		this.firewallGroup = firewallGroup;
	}
	
	public RestrictedFirewallGroup find(String groupName) {
		for (RestrictedFirewallGroup group : firewallGroup) {
			if (group.getGroupName().equals(groupName)) {
				return group;
			}
		}
		
		return null;
	}
}