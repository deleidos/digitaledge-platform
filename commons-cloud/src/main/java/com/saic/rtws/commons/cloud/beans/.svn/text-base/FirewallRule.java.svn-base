package com.saic.rtws.commons.cloud.beans;

import java.util.ArrayList;
import java.util.Collection;

public class FirewallRule {

	private String protocol;
	
	private Integer fromPort;
	
	private Integer toPort;
	
	private ArrayList<FirewallIpRangeSource> ipRanges = new ArrayList<FirewallIpRangeSource>();
	
	private ArrayList<FirewallGroupSource> firewallGroupSources = new ArrayList<FirewallGroupSource>();
	
	public String getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public Integer getFromPort() {
		return this.fromPort;
	}
	
	public void setFromPort(Integer fromPort) {
		this.fromPort = fromPort;
	}
	
	public Integer getToPort() {
		return this.toPort;
	}
	
	public void setToPort(Integer toPort) {
		this.toPort = toPort;
	}
	
	public Collection<FirewallIpRangeSource> getIpRanges() {
		return this.ipRanges;
	}
	
	public void addIpRange(FirewallIpRangeSource ipRange) {
		this.ipRanges.add(ipRange);
	}
	
	public Collection<FirewallGroupSource> getFirewallGroupSources() {
		return this.firewallGroupSources;
	}
	
	public void addFirewallGroupSource(FirewallGroupSource source) {
		this.firewallGroupSources.add(source);
	}

	@Override
	public String toString() {
		return "FirewallRule [protocol=" + protocol + ", fromPort=" + fromPort
				+ ", toPort=" + toPort + ", ipRanges=" + ipRanges
				+ ", firewallGroupSources=" + firewallGroupSources + "]";
	}
	
}