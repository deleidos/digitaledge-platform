package com.saic.rtws.commons.util.repository;

import javax.jcr.SimpleCredentials;

public class RepositoryCredentials {
	
	private SimpleCredentials jcrSimpleCredentials;
	private String            username;
	
	public RepositoryCredentials(String username, String password) {
		jcrSimpleCredentials = new SimpleCredentials(username, password.toCharArray());
		this.username = username;		
	}
	
	public SimpleCredentials getJcrCredentials() {
		return jcrSimpleCredentials;
	}
	
	public String getUsername() {
		return username;
	}	
}
