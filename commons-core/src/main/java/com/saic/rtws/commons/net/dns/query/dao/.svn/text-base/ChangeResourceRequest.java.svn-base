package com.saic.rtws.commons.net.dns.query.dao;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name="ChangeResourceRecordSetsRequest", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
public class ChangeResourceRequest {
	private ChangeBatch changeBatch;
	
	@XmlElement(name="ChangeBatch", namespace="https://route53.amazonaws.com/doc/2011-05-05/" )
	public ChangeBatch getChangeBatch() {
		return changeBatch;
	}

	public void setChangeBatch(ChangeBatch changeBatch) {
		this.changeBatch = changeBatch;
	}

}
