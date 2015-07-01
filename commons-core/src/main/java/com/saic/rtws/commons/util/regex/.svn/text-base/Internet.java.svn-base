package com.saic.rtws.commons.util.regex;

import java.util.regex.Pattern;

public class Internet {

	/////
	// Host name patterns.
	
	private static final String LABEL = "[a-zA-Z0-9](?:[\\-a-zA-Z0-9]*[a-zA-Z0-9])?";
	
	public static final String HOST_NAME = LABEL;
	public static final String DOMAIN_NAME = LABEL + "(?:\\." + LABEL + ")*";
	public static final String FQDN = "(" + HOST_NAME + ")\\.(" + DOMAIN_NAME + ")";
	
	public static final Pattern HOST_NAME_PATTERN = Pattern.compile(HOST_NAME);
	public static final Pattern DOMAIN_NAME_PATTERN = Pattern.compile(DOMAIN_NAME);
	public static final Pattern FQDN_PATTERN = Pattern.compile(FQDN);
	
	public static final int FQDN_GROUP_HOST = 1;
	public static final int FQDN_GROUP_DOMAIN = 2;
	
	
	/////
	// Email address patterns.
	
	public static final String EMAIL_ADDRESS = "(" + DOMAIN_NAME + ")@(" + DOMAIN_NAME + ")";
	
	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(EMAIL_ADDRESS);
	
	public static final int EMAIL_ADDRESS_GROUP_USER = 1;
	public static final int EMAIL_ADDRESS_GROUP_DOMAIN = 2;
	
	
	/////
	// IP address patterns.
	
	private static final String IP_OCTET = "[0-9]|[0-9][0-9]|[0-1][0-9][0-9]|2[0-4][0-9]|25[0-5]";
	
	public static final String IP_ADDRESS = "(" + IP_OCTET + ")\\.(" + IP_OCTET + ")\\.(" + IP_OCTET + ")\\.(" + IP_OCTET + ")";
	public static final String NETWORK_MASK = "(" + IP_ADDRESS + ")/([0-9]|[0-2][0-9]|3[0-2])";
	
	public static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(IP_ADDRESS);
	public static final Pattern NETWORK_MASK_PATTERN = Pattern.compile(NETWORK_MASK);
	
	public static final int IP_ADDRESS_GROUP_OCTET1 = 1;
	public static final int IP_ADDRESS_GROUP_OCTET2 = 2;
	public static final int IP_ADDRESS_GROUP_OCTET3 = 3;
	public static final int IP_ADDRESS_GROUP_OCTET4 = 4;
	
	public static final int NETWORK_MASK_GROUP_ADDRESS = 1;
	public static final int NETWORK_MASK_GROUP_BITS = 6;
	
}
