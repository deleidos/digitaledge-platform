package com.saic.rtws.commons.net.dns;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.saic.rtws.commons.net.dns.exception.DNSException;


/**
 * Utility class used to bind new entries to a local DNS servicer.
 */
@XmlJavaTypeAdapter(DnsClient.TypeAdapter.class)
public interface DnsClient {
	
	/**
	 * Binds the given ip address to the given domain name.
	 */
	public void bind(String fqdn, String ip) throws IOException;
	
	/**
	 * Binds the given ip address to the given domain name.
	 */
	public void bind(String host, String domain, String ip) throws IOException;
	
	public void addZone(String domain) throws DNSException;
	
	public void removeZone(String domain) throws DNSException;
	
	public void unbind(String domain, String fqdn) throws DNSException;
	
	public void addForwarder(String domain, String dnsIP, String tmsDomain, String publicIPAddress);
	
	public void removeForwarder(String domain, String dnsIP, String tmsDomain);
	
	public Collection<Map<String, Map<String, String>>> getForwarderStatus() throws IOException;
	
	public Collection<String> getZoneStatus() throws IOException;
	
	static class TypeAdapter extends XmlAdapter<Object, DnsClient> {
		public DnsClient unmarshal(Object element) { return (DnsClient)element; }
		public Object marshal(DnsClient bean) { return bean; }
	}
}
