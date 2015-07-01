package com.saic.rtws.commons.net.dns.rest;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.jersey.config.JerseyClientConfig;
import com.saic.rtws.commons.net.dns.exception.DNSException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;


public class DNSRestClient {
		
	private static final Logger logger = LogManager.getLogger(DNSRestClient.class);
	private String dnsUrl;
	private String lastError;
	private Client client;
	
	
	public DNSRestClient(){
		client = Client.create(JerseyClientConfig.getInstance().getInternalConfig());
		
		dnsUrl = RtwsConfig.getInstance().getString("webapp.dnsapi.url.path");
	}
	
	/**
	 * Binds a ip address to a dns name under the domain zone.
	 * 
	 * @return boolean successful dns bind
	 */
	public boolean bindRestAddEntry(String dnsEntry, String ipAddress, boolean createZone){
		boolean success = true;
		try{
			if(dnsUrl != null){
				//only call once make sure zone exists
				if(createZone == true){
					//system.root.domain, domain = root.domain
					String subDomain = dnsEntry.substring(dnsEntry.indexOf(".") + 1);
					bindRestAddZone(subDomain);
				}
				
				logger.debug(String.format("Attempting to bind %s to %s.", dnsEntry, ipAddress));
				
				WebResource resource = this.client.resource(String.format("%s/rest/dns/add/dnsentry", dnsUrl));
				
				Form form = new Form();
				form.add("ipAddress", ipAddress);
				form.add("dnsEntry", dnsEntry);
				logger.info(resource.getURI().toString());
				
				String response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
						.post(String.class, form);
				
				JSONObject json = JSONObject.fromObject(response);
				JSONObject standardHeader = json.getJSONObject("standardHeader");
				
				if (standardHeader.getInt("code") != 200) {
					lastError = json.getString("responseBody");
					logger.debug("Unable to bind public dns entry: " + lastError);
					success = false;
				}
			}
			else{
				logger.warn("Unable to bind public dns entry no dnsUrl in properties, webapp.dnsapi.url.path");
			}
		}
		catch(Exception e){
			success = false;  //retry dns bind, should retry if connection problem to anchor/gateway only
			logger.warn(String.format("Retrying binding of dns entry:  DNSEntry:  %s, IP:  %s, Error:  %s", dnsEntry, ipAddress, e.toString()));
			try{lastError = Arrays.toString(e.getStackTrace());}catch(Exception ignore){/*ignore*/}
		}
		
		return success;
	}
	
	/**
	 * Add a new zone to dns, if it already exists then nothing changes in dns.
	 * 
	 * @param domain
	 * @throws ServiceException
	 */
	public void bindRestAddZone(String domain) throws DNSException{
		if(dnsUrl != null){
			WebResource resource = this.client.resource(String.format("%s/rest/dns/add/zone", dnsUrl));
			
			Form form = new Form();
			form.add("domain", domain);
			
			logger.info(resource.getURI().toString());
			
			ClientResponse response = resource.post(ClientResponse.class, form);
			logger.debug(response.getEntity(String.class));
		}
		else{
			throw new DNSException("Unable to bind public dns entry no dnsUrl in properties, webapp.dnsapi.url.path");
		}
	}
	
	public String getLastError(){
		return lastError;
	}
}
