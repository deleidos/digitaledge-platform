package com.saic.rtws.commons.net.dns.query.dao;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"action", "resourceRecordSet"})
public class Change {
	public static enum ActionCommand { CREATE,  DELETE };
	private ActionCommand action;
	private ResourceRecordSet resourceRecordSet;
	
	@XmlElement(name="Action", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public ActionCommand getAction() {
		return action;
	}
	
	public void setAction(ActionCommand action) {
		this.action = action;
	}
	
	@XmlElement(name="ResourceRecordSet", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public ResourceRecordSet getResourceRecordSet() {
		return resourceRecordSet;
	}
	
	public void setResourceRecordSet(ResourceRecordSet resourceRecordSet) {
		this.resourceRecordSet = resourceRecordSet;
	}


}
