package com.saic.rtws.commons.net.dns.query.dao;

import javax.xml.bind.annotation.XmlElement;

public class ResourceRecord {
	String value;
	
	@XmlElement(name="Value", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
