package com.saic.rtws.commons.net.dns.query.dao;

import javax.xml.bind.annotation.XmlElement;

public class ChangeInfo {
	private String id;
	private String status;
	private String submittedAt;
	
	@XmlElement(name="Id", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@XmlElement(name="Status", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
	@XmlElement(name="SubmittedAt", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setSubmittedAt(String submittedAt) {
		this.submittedAt = submittedAt;
	}
	
	public String getSubmittedAt() {
		return submittedAt;
	}
	
}
