package com.saic.rtws.commons.net.dns.query.dao;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ErrorResponse", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
public class ErrorResponse {
	private Error 	error;
	private String 	requestId;

	@XmlElement(name="Error", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setError(Error error) {
		this.error = error;
	}
	
	public Error getError() {
		return error;
	}
	
	@XmlElement(name="RequestId", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getRequestId() {
		return requestId;
	}

}
