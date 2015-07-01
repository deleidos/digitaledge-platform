package com.saic.rtws.commons.model.response;

import java.util.Properties;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso(Properties.class)
public class PropertiesResponse extends StandardResponse<Properties> {
	
	public PropertiesResponse() { 
		setResponseBody(new Properties());
	}
	
	public Properties getProperties() {
		return getResponseBody();
	}
	
	public void setProperty(String key, String value) {
		body.put(key, value);
	}
	
	public void setProperties(Properties p) {
		setResponseBody(p);
	}
	
}
