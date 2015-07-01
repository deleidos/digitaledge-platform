package com.saic.rtws.commons.model.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorResponse extends StandardResponse<String> {
	
	/** Constructor. */
	public ErrorResponse() {
		// set default message
		setResponseBody("An error has occurred");
	}
	
	public String getMessage() {
		return getResponseBody();
	}

	public void setMessage(String message) {
		setResponseBody(message);
	}
	
}