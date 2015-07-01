package com.saic.rtws.commons.dao;

public class BaseDAO {
	
	String additionalAuditLogInfo = "";
	int userId;
	String username;
	
	public BaseDAO(int userId) {
		this.userId = userId;
	}
	
	public BaseDAO(int userId, String additionalAuditLogInfo) {
		this.userId = userId;
		this.additionalAuditLogInfo = additionalAuditLogInfo;
	}
	
	public BaseDAO(String username) {
		this.username = username;
	}
	
	public BaseDAO(String username, String additionalAuditLogInfo) {
		this.username = username;
		this.additionalAuditLogInfo = additionalAuditLogInfo;
	}
	
	public String getAdditionalAuditLogInfo() {
		return additionalAuditLogInfo;
	}

	public void setAdditionalAuditLogInfo(String additionalAuditLogInfo) {
		this.additionalAuditLogInfo = additionalAuditLogInfo;
	}

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int id) {
		this.userId = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUserName(String username) {
		this.username = username;
	}
	
}
