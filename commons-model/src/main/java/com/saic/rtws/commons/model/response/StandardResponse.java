package com.saic.rtws.commons.model.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StandardResponse<BodyType> {
	
	protected StandardHeader head = new StandardHeader();
	protected BodyType body = null;
	
	public StandardResponse() { }

	public StandardHeader getStandardHeader() {
		return head;
	}
	
	public void setStandardHeaderCode(int code) {
		head.setCode(code);
	}

	public void setStandardHeaderVersion(String version) {
		head.setVersion(version);
	}
	
	public BodyType getResponseBody() {
		return body;
	}
	
	public void setResponseBody(BodyType b) {
		body = b;
	}
}
