package com.saic.rtws.commons.net.dns.query.dao;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ListResourceRecordSetsResponse", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
public class ListResourceRecordSetsResponse {

	List<ResourceRecordSet> resourceRecordSets;
	
	@XmlElementWrapper(name="ResourceRecordSets", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	@XmlElement(name="ResourceRecordSet", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public List<ResourceRecordSet> getResouruceRecordSets() {
		return resourceRecordSets; 
	}
	
	public void setResouruceRecordSets(List<ResourceRecordSet> resourceRecordSets) {
		this.resourceRecordSets = resourceRecordSets;
	}
}
