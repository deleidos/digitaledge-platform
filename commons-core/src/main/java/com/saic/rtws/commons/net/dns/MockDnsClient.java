package com.saic.rtws.commons.net.dns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MockDnsClient implements DnsClient {

	public MockDnsClient() {
		super();
	}
	
	public void bind(String fqdn, String ip) {
		// Pretend everything worked fine.
	}

	public void bind(String host, String domain, String ip) throws IOException {
		// Pretend everything worked fine.
	}

	@Override
	public void addZone(String domain) {
		// Pretend everything worked fine.
	}

	@Override
	public void removeZone(String domain) {
		// Pretend everything worked fine.
	}

	@Override
	public void unbind(String domain, String fqdn) {
		// Pretend everything worked fine.
	}

	@Override
	public void addForwarder(String domain, String dnsIP, String tmsDomain, String publicIPAddress) {
		// Pretend everything worked fine.
	}

	@Override
	public void removeForwarder(String domain, String dnsIp, String tmsDomain) {
		// Pretend everything worked fine.
	}

	@Override
	public Collection<Map<String, Map<String, String>>> getForwarderStatus() {
		// Pretend everything worked fine.
		Collection<Map<String, Map<String, String>>> forwarders = new ArrayList<Map<String, Map<String, String>>>();
		
		return forwarders;
	}

	@Override
	public Collection<String> getZoneStatus() {
		// Pretend everything worked fine.
		Collection<String> collection = new ArrayList<String>();
		
		return collection;
	}

}
