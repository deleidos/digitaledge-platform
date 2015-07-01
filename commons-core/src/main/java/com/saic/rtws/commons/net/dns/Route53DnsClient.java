package com.saic.rtws.commons.net.dns;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.PropertiesUtils;
import com.saic.rtws.commons.net.dns.exception.QueryException;
import com.saic.rtws.commons.net.dns.query.Route53DNSQuery;
import com.saic.rtws.commons.net.dns.query.dao.Change;
import com.saic.rtws.commons.net.dns.query.dao.ChangeBatch;
import com.saic.rtws.commons.net.dns.query.dao.ChangeInfo;
import com.saic.rtws.commons.net.dns.query.dao.ResourceRecord;
import com.saic.rtws.commons.net.dns.query.dao.ResourceRecordSet;
import com.saic.rtws.commons.net.dns.query.dao.ResourceRecordSet.RECORDTYPE;


/**
 * Utility class used to bind new entries to a Route53 DNS Server. This allows outside users to access servers on the Amazon Web Service.
 */
public class Route53DnsClient implements InternetDnsClient {
	
	private static final Logger log = Logger.getLogger(Route53DnsClient.class); 
	
	private String propFile;
	
	private String queryUrl;
	
	private String changeUrl;
	
	private Route53DNSQuery route53DNSQuery;
	
	/**
	 * Create a Route53DnsClient to communicate with the Amazon Route 53 Server.
	 */
	public Route53DnsClient()  {
		super();
		route53DNSQuery = new Route53DNSQuery(); 
	}
	
	/**
	 * Initialize the Route 53 client by loading the properites file.
	 * 
	 * @throws QueryException
	 * @throws IOException
	 */
	public void init() {
		try {
			route53DNSQuery.setPropFile(new File(propFile));
			
			Properties properties = PropertiesUtils.loadProperties(propFile);
			route53DNSQuery.setAccessKeyId(properties.getProperty("accessKey"));
			String keyString = properties.getProperty("secretKey");
			route53DNSQuery.setKey(keyString.getBytes());
			
			route53DNSQuery.init();
		} catch (QueryException qe) {
			log.error("Error initializing Route 53 Query: " + qe.getMessage());
		} catch (IOException ioe) {
			log.error("Error initializing Route 53 Client: " + ioe.getMessage());
		} catch (Exception ex) {
			log.error("Error initializing Route 53 Client: " + ex.getMessage());
		}
	}
	
	public void setPropFile(String propFile) {
		this.propFile = propFile;
	}
	
	public String getPropFile() {
		return propFile;
	}
	
	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
	
	public String getQueryUrl() {
		return this.queryUrl;
	}
	
	public void setChangeUrl(String changeUrl) {
		this.changeUrl = changeUrl;
	}
	
	public String getChangeUrl() {
		return this.changeUrl;
	}
	
		 
	private List<Change> findEntriesToRemove(String fqdn, ResourceRecordSet.RECORDTYPE recordType) {
		List<Change> changes = new ArrayList<Change>();
		
		// Find old record and remove it.
		List<ResourceRecord> resourceRecords = route53DNSQuery.queryFQDNByRecordType(this.getChangeUrl(), fqdn, recordType);
		for(ResourceRecord resourceRecord :  resourceRecords) {
			String oldAddress = resourceRecord.getValue();
			changes.add(createDNSEntry(fqdn, oldAddress, recordType, Change.ActionCommand.DELETE));
		}
		
		return changes;
	}
	
	
	private void sendBatch(List<Change> changes, String comment) throws IOException {
		try {
						
			ChangeBatch changeBatch = new ChangeBatch();
			changeBatch.setChanges(changes);
			changeBatch.setComment(comment);
		
			ChangeInfo changeInfo = route53DNSQuery.postChange(changeBatch, this.getChangeUrl());
			log.info("Change " + changeInfo.getId() + " was submitted " + changeInfo.getSubmittedAt() + " " + changeInfo.getStatus());
		} catch (Exception ex) {
			throw new IOException("Exception creating DNS entry: " + ex.getMessage(), ex);
		}
	}
	
	
	/**
	 * First, this method will find old entries by searching for the fully-qualified domain name (fqdn) and for all known record types. Then the
	 * new record will be created.  All changes will be sent as a batch.  It is important that the batch contain the delete records first
	 * and then do the create. The cloud service processes the records in the order they are received.
	 * 
	 * 
	 * @param fqdn - Fully Qualified Domain Name
	 * @param address - ip address of the server
	 * @throws IOException
	 */
	public void bindAndReplaceAll(String fqdn, String address, ResourceRecordSet.RECORDTYPE newRecordType ) throws IOException {
		List<Change> changes = new ArrayList<Change>();
		ResourceRecordSet.RECORDTYPE[] knownRecordTypes = { ResourceRecordSet.RECORDTYPE.CNAME, ResourceRecordSet.RECORDTYPE.A };
		
		// Remove old entries.
		for(RECORDTYPE recordType : knownRecordTypes) {
			changes.addAll(findEntriesToRemove(fqdn, recordType));
		}
		
		// Add new entry.
		changes.add(createDNSEntry(fqdn, address, newRecordType, Change.ActionCommand.CREATE));
		String comment = "binding " + fqdn + " to address " + address;
		sendBatch(changes, comment);
		
		log.info("Bound address " + fqdn + " to " + address);
	}
	
	/**
	 * Convenience  method to call bindAndReplace using CNAME entries.
	 *
	 */
	
	public void bindAndReplace(String fqdn, String address ) throws IOException { 
		bindAndReplaceAll(fqdn, address, ResourceRecordSet.RECORDTYPE.CNAME);
	}
	
	/**
	 * Same as bindAndReplace, but does not remove old entries. 
	 * 
	 */
	
	public void bindNew(String fqdn, String address, ResourceRecordSet.RECORDTYPE recordType) throws IOException {
		List<Change> changes = new ArrayList<Change>();
		
		
		// Add new entry.
		changes.add(createDNSEntry(fqdn, address, recordType, Change.ActionCommand.CREATE));
		String comment = "binding " + fqdn + " to address " + address;
		sendBatch(changes, comment);
		
		log.info("Bound address " + fqdn + " to " + address);
	}
	
	/**
	 * 
	 * Convenience  method to call bindNew using CNAME entries.
	 *
	 */
	
	public void bindNew(String fqdn, String address ) throws IOException { 
		bindNew(fqdn, address, ResourceRecordSet.RECORDTYPE.CNAME);
	}
	
	public void unbind(String fqdn, String address, ResourceRecordSet.RECORDTYPE recordType) throws IOException {
		
		if (!(route53DNSQuery.queryRecordExists(this.getChangeUrl(), fqdn, recordType))) { 
			log.warn("Node " + fqdn + " of type " + recordType + " does not exist and cannot be deleted");
			return;
		}
		
		List<Change> changes = new ArrayList<Change>();
		changes.add(createDNSEntry(fqdn, address, recordType, Change.ActionCommand.DELETE));
		String comment = "unbinding " + fqdn + " from address " + address;
		sendBatch(changes, comment);
		
		log.info("Address " + fqdn + " was removed");
	}
	
	public void unbindAllForFQDN(String fqdn) throws IOException {
		ResourceRecordSet resourceRecordSet = route53DNSQuery.queryFQDN(this.getChangeUrl(), fqdn);
		if (resourceRecordSet.getResourceRecords().size() == 0) { 
			log.warn("Node " + fqdn + " does not exist and cannot be deleted");
			return;
		}
		
		StringBuffer comment = new StringBuffer();
		List<Change> changes = new ArrayList<Change>();
		
		for(ResourceRecord resourceRecord : resourceRecordSet.getResourceRecords()) {
			RECORDTYPE recordType = resourceRecordSet.getType();
			String address = resourceRecord.getValue();
			comment.append("unbinding " + fqdn + " from address " + address + " of type " + recordType);
			changes.add(createDNSEntry(fqdn, address, recordType, Change.ActionCommand.DELETE));
		}
		
		sendBatch(changes, comment.toString());
		log.info("Address " + fqdn + " was removed");
	}
	
	public Change createDNSEntry(String fqdn, String address, ResourceRecordSet.RECORDTYPE recordType, Change.ActionCommand operation) {
		return createWeightedDNSEntry(fqdn, address, null, null, recordType, operation);
	}
	
	public Change createWeightedDNSEntry(String fqdn, String address, String identifier, String weight,  
			ResourceRecordSet.RECORDTYPE recordType, Change.ActionCommand operation) {
		
		Change change = new Change();
		ResourceRecordSet resourceRecordSet = new ResourceRecordSet();
		
		resourceRecordSet.setName(fqdn);
		resourceRecordSet.setType(recordType);
		List<ResourceRecord> resourceRecords = new ArrayList<ResourceRecord>();
		ResourceRecord resourceRecord = new ResourceRecord();
		resourceRecord.setValue(address);
		resourceRecords.add(resourceRecord);
		resourceRecordSet.setResourceRecords(resourceRecords);
		resourceRecordSet.setIdentifier(identifier);
		resourceRecordSet.setWeight(weight);
		
		change.setAction(operation);
		change.setResourceRecordSet(resourceRecordSet);
		
		return change;
	
	}
	
	public boolean queryRecordExists(String fqdn, ResourceRecordSet.RECORDTYPE type) {
		return route53DNSQuery.queryRecordExists(this.getChangeUrl(), fqdn, type);
	}
}
