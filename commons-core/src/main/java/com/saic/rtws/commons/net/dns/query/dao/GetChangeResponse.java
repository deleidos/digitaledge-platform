package com.saic.rtws.commons.net.dns.query.dao;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="GetChangeResponse", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
public class GetChangeResponse {
	private ChangeInfo changeInfo;
	
	@XmlElement(name="ChangeInfo", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public void setChangeInfo(ChangeInfo changeInfo) {
		this.changeInfo = changeInfo;
	}
	
	public ChangeInfo getChangeInfo() {
		return changeInfo;
	}
}
