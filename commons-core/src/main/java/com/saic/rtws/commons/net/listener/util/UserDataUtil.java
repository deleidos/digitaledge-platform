package com.saic.rtws.commons.net.listener.util;

import static com.saic.rtws.commons.util.regex.Internet.FQDN_GROUP_HOST;
import static com.saic.rtws.commons.util.regex.Internet.FQDN_PATTERN;

import java.util.regex.Matcher;

import com.saic.rtws.commons.config.UserDataProperties;

public final class UserDataUtil {
	
	public static String getHostFromFqdn() {
		String fqdn = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_FQDN);
		return getHostFromFqdn(fqdn);
	}
	
	/**
	 * Gets the host from a fully qualified domain name.
	 *
	 * @param fqdn the FQDN
	 * @return the host
	 */
	public static String getHostFromFqdn(String fqdn) {
		String host = "";
		if(fqdn == null) {
			// No FQDN might indicate master, verify with which manifest used
			String manifest = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_MANIFEST);
			if(manifest.endsWith("master.ini")) {
				host = "master";
			}
		}
		else {
			Matcher matcher = FQDN_PATTERN.matcher(fqdn);
			if (!matcher.matches()) {
				throw new IllegalArgumentException("Invalid domain name syntax '"
						+ fqdn + "'.");
			}

			host = matcher.group(FQDN_GROUP_HOST);
		}
		
		return host;
	}
	
}