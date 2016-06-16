package com.deleidos.rtws.alertviz.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Convenience class to help parse Amazon and Euca endPoint URL Strings (storage, service, etc.)
 * This class takes a full endPoint URL String in the form of {protocol}://{endpoint}:{port}/{virtualpath}
 * and provides methods to retrieve the individual elements
 */
public class EndPointURL {
	
	private URL url;
	private String urlString;
	
	public EndPointURL() {
		super();
	}
	
	public EndPointURL(String urlString) throws MalformedURLException {
		this.url = new URL(urlString);
		this.urlString = urlString;
	}

	public String getURLString() {
		return urlString;
	}

	public void setURLString(String urlString) throws MalformedURLException {
		this.urlString = urlString;
		url = new URL(urlString);
	}

	public String getProtocol() {
		return url.getProtocol();
	}
	
	public String getEndpoint() {
		return url.getHost();
	}

	public int getPort() {
		return url.getPort();
	}

	public String getVirtualpath() {
		return url.getPath();
	}
	
	public String getProtocolAndEndpoint() {
		return String.format("%s://%s", url.getProtocol(), url.getHost());
	}

}
