package com.saic.rtws.commons.net.dns.query.dao;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"comment", "changes"})
public class ChangeBatch {
	private String comment;
	private List<Change> changes;
	
	@XmlElement(name="Comment", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@XmlElementWrapper(name="Changes", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	@XmlElement(name="Change", namespace="https://route53.amazonaws.com/doc/2011-05-05/")
	public List<Change> getChanges() {
		return changes;
	}
	
	public void setChanges(List<Change> changes) {
		this.changes = changes;
	}
}
