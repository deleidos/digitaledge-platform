package com.saic.rtws.commons.cloud;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class IntrospectionClient {

	private static final String BASE_URL = "http://169.254.169.254/latest/";
	
	public IntrospectionClient() {
		super();
	}
	
	public String getId() throws IOException {
		return retrieveTextContent(buildUrl("meta-data/instance-id"));
	}
	
	private static URL buildUrl(String parameter) throws MalformedURLException {
		return new URL(BASE_URL + parameter);
	}
	
	private String retrieveTextContent(URL url) throws IOException {
		return retrieveTextContent(url, "UTF-8");
	}
	
	private String retrieveTextContent(URL url, String encoding) throws IOException {
		
		URLConnection connection = null;
		InputStream stream = null;
		
		try {
			
			connection = url.openConnection();
			stream = connection.getInputStream();
			
			int length = connection.getContentLength();
			byte[] buffer = new byte[length];
			
			int offset = 0;
			for(int count = 0; count != -1; offset += count) {
				count = stream.read(buffer, offset, length);
			}
			
			return new String(buffer, 0, length, encoding);
			
		} finally {
			try {stream.close();} catch(Exception ignore) { }
		}
	}
	
}
