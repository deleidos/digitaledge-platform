package com.saic.rtws.commons.model.user;

public class EmailFilter extends WatchListFilter {

	private String username;
	private String password;
	private String email_list;
	
	public EmailFilter() {
		super();
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmailList() {
		return email_list;
	}
	
	public void setEmailList(String email_list) {
		this.email_list = email_list;
	}
	
	
}
