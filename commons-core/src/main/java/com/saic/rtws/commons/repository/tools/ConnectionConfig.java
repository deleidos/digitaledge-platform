package com.saic.rtws.commons.repository.tools;

/**
 * Stores the connection configuration to the repository.
 */
public class ConnectionConfig {

	private String userId;

	private String password;

	private String repositoryUrl;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}

	public String getRepositoryUrl() {
		return this.repositoryUrl;
	}

	public String getRetrieveContentUrl() {
		StringBuilder url = new StringBuilder();

		/*
		 * Hook used to support UT/IT testing against the GMR which has a
		 * different path for content retrieval
		 */
		if (Boolean.getBoolean("RTWS_TEST_MODE")) {
			url.append(this.repositoryUrl);
			url.append("/rest/VISIBILITY");
		} else {
			url.append(this.repositoryUrl);
			url.append("/rest/content/retrieve");
		}
		return url.toString();
	}

	public String getListContentUrl() {
		StringBuilder url = new StringBuilder();
		url.append(this.repositoryUrl);
		url.append("/rest/content/list");
		return url.toString();
	}

	public String getCommonContentUrl() {
		StringBuilder url = new StringBuilder();
		url.append(this.repositoryUrl);
		url.append("/rest/common");
		return url.toString();
	}

}