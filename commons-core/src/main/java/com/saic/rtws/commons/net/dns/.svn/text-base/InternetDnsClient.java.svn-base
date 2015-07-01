package com.saic.rtws.commons.net.dns;

import java.io.IOException;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.saic.rtws.commons.net.dns.query.dao.ResourceRecordSet;

/**
* Utility class used to bind new entries to a local DNS servicer.
*/
@XmlJavaTypeAdapter(InternetDnsClient.TypeAdapter.class)
public interface InternetDnsClient {

	/**
	 * Initialize the InternetDnsClient
	 */
	public void init();
	
	/**
	 * Binds the given ip address to the given domain name and removes an old entry.
	 */
	public void bindAndReplaceAll(String fqdn, String address, ResourceRecordSet.RECORDTYPE newRecordType) throws IOException;
	
	
	/**
	 * Does not try to remove old entries, just attempts to bind a new address.
	 */
	public void bindNew(String fqdn, String address, ResourceRecordSet.RECORDTYPE recordType) throws IOException;
	
	/**
	 * Unbinds the given ip address to the given domain name.
	 */
	public void unbind(String fqdn, String address, ResourceRecordSet.RECORDTYPE recordType) throws IOException;
	
	static class TypeAdapter extends XmlAdapter<Object, InternetDnsClient> {
		public InternetDnsClient unmarshal(Object element) { return (InternetDnsClient)element; }
		public Object marshal(InternetDnsClient bean) { return bean; }
	}
}
