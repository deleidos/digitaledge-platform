package com.saic.rtws.commons.util.env;

import org.apache.commons.lang.StringUtils;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.config.UserDataProperties;

public final class SystemEnvUtil {
	
	public final static String RTWS_BUCKET_KEY = "RTWS_BUCKET_NAME";
	
	public final static String RTWS_DOMAIN_KEY = "RTWS_DOMAIN";
	
	private SystemEnvUtil() {
		// prevent instantiation
	}
	
	public static boolean checkShadowCopyEnvExist() {
		if (System.getProperty(RTWS_BUCKET_KEY) == null || System.getProperty(RTWS_DOMAIN_KEY) == null) {
			return false;
		}
		
		return true;
	}
	
	public static String getDomain() {
		return System.getProperty(RTWS_DOMAIN_KEY);
	}
	
	public static String getShadowCopyBucket() {
		return System.getProperty(RTWS_BUCKET_KEY);
	}
	
	/**
	 * 
	 * Will build the file key for the procceses.xml file.  It will take the domain and the file name and merge them together
	 * to create a key.  This key can be used to store and retrieve the file from a cloud data store.
	 * 
	 * @param domain - name of the domain of the system
	 * @param filename - name of the file to be stored or retrieved
	 * 
	 * @return - A processes file key.
	 */
	
	public static String buildProcessesFileKey(String domain, String filename) {
		final String PROCESS_FILE_KEY = "process";
		final char BUCKET_SEPERATOR_CHARACTER = '/';
		 
				
		// Append all the process file key, domain and filename into a string, with a forward slash
		
		StringBuilder sb = new StringBuilder();
		sb.append(PROCESS_FILE_KEY).append(BUCKET_SEPERATOR_CHARACTER)
		  .append(domain).append(BUCKET_SEPERATOR_CHARACTER)
		  .append(filename);
		
		// Return the string
		return sb.toString();
	}
	
	public static boolean isNatConfigured(){
		boolean systemHasNat = false;
		
		if(SystemEnvUtil.isVpcEnabled() == true && StringUtils.isBlank(UserDataProperties.getInstance().getString("RTWS_ROOT_CA"))){
			systemHasNat = true;
		}
		
		return systemHasNat;
	}
	
	public static boolean isVpcEnabled() {
		
		//tms check
		String vpcEnabled = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_VPC_ENABLED);
		if (! StringUtils.isBlank(vpcEnabled) && (vpcEnabled.equalsIgnoreCase("Y") || vpcEnabled.equalsIgnoreCase("true"))) {
			return true;
		}
		
		//tenant system check
		vpcEnabled = RtwsConfig.getInstance().getString("rtws.vpc.enabled");
		if (! StringUtils.isBlank(vpcEnabled) && (vpcEnabled.equalsIgnoreCase("Y") || vpcEnabled.equalsIgnoreCase("true"))) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isTmsSystem() {
		String isTms = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_IS_TMS);
		if(StringUtils.isNotBlank(isTms)){
			if(Boolean.parseBoolean(isTms) == true){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	public static String getTmsTenantId(){
		if(isTmsSystem() == true){
			return UserDataProperties.getInstance().getString(UserDataProperties.RTWS_TENANT_ID);
		}
		else{
			return RtwsConfig.getInstance().getString("rtws.tmsTenantId");
		}
	}
}