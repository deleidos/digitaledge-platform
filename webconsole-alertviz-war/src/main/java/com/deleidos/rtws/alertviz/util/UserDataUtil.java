package com.deleidos.rtws.alertviz.util;

import com.deleidos.rtws.commons.config.UserDataProperties;

public final class UserDataUtil {
	
	private static UserDataProperties userData = UserDataProperties.getInstance();
	
	public static String getAccessKey() {
		return userData.getString(UserDataProperties.RTWS_ACCESS_KEY);
	}
	
	public static String getSecretKey() {
		return userData.getString(UserDataProperties.RTWS_SECRET_KEY);
	}
	
	public static String getTenantId() {
		return userData.getString(UserDataProperties.RTWS_TENANT_ID);
	}
	
	public static String getDomainSuffix() {
		return userData.getString(UserDataProperties.RTWS_DOMAIN_SUFFIX);
	}
	
	public static String getServiceEndpoint() {
		return userData.getString(UserDataProperties.RTWS_SERVICE_ENDPOINT);
	}
	
	public static String getStorageEndpoint() {
		return userData.getString(UserDataProperties.RTWS_STORAGE_ENDPOINT);
	}
	
	public static boolean isGateway() {
		return Boolean.valueOf(userData.getString(UserDataProperties.RTWS_IS_GATEWAY));
	}
	
	public static boolean isVpcEnabled() {
		// should never need this... only use in euca.. / dont use VPC?
		String vpcEnabled = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_VPC_ENABLED);
		if ((vpcEnabled.equalsIgnoreCase("Y") || vpcEnabled.equalsIgnoreCase("true"))) {
			return true;
		}
		
		return false;
		
	}
	
}
