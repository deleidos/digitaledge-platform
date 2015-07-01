package com.saic.rtws.commons.net.dns.query.dao;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import java.util.List;

@XmlType(propOrder={"name", "type", "identifier", "weight", "ttl", "resourceRecords"})
public class ResourceRecordSet {	
	public static enum RECORDTYPE { CNAME, A };
	private String name;
	private RECORDTYPE type;
	private String identifier;
	private String weight;
	private int ttl;
	private List<ResourceRecord> rescourceRecords;

	
	@XmlElement(name="Name", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="Type", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public RECORDTYPE getType() {
		return type;
	}
	
	public void setType(RECORDTYPE type) {
		this.type = type;
	}
	
	@XmlElement(name="SetIdentifier", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@XmlElement(name="Weight", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public String getWeight() {
		return weight;
	}
	
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	@XmlElement(name="TTL", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public int getTtl() {
		return ttl;
	}
	
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	
	@XmlElementWrapper(name="ResourceRecords", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	@XmlElement(name="ResourceRecord", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public List<ResourceRecord> getResourceRecords() {
		return rescourceRecords;
	}
	
	public void setResourceRecords(List<ResourceRecord> resourceRecords) {
		this.rescourceRecords = resourceRecords;
	}
	
	
	

}
