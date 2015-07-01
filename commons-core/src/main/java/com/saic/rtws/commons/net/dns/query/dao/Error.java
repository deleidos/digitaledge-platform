package com.saic.rtws.commons.net.dns.query.dao;

import javax.xml.bind.annotation.XmlElement;

public class Error {
	private String type;
	private String code;
	private String message;
	
	@XmlElement(name="Type", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	@XmlElement(name="Code", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	@XmlElement(name="Message", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
