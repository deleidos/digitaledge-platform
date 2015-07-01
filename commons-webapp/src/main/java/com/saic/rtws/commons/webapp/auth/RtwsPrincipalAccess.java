package com.saic.rtws.commons.webapp.auth;

/**
 * A singleton used to access the RtwsPrincipal consistently regardless of security module.
 * Each webapp should provide a principal locator to dictate how the user should be accessed.
 * This is typically done via DI configuration (e.g. spring).
 */
public enum RtwsPrincipalAccess {
	INSTANCE;
	
	private RtwsPrincipalLocator locator;
	
	public void setPrincipalLocator(RtwsPrincipalLocator locator) {
		this.locator = locator;
	}
	
	public RtwsPrincipal getUserPrincipal() {
		if(locator == null) {
			throw new IllegalStateException(RtwsPrincipalLocator.class.getCanonicalName() + " impl has not been provided");
		}
		
		return locator.getUserPrincipal();
	}
}

