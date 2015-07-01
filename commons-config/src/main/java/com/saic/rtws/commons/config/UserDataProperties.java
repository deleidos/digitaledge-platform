package com.saic.rtws.commons.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class UserDataProperties {

	private static final Logger logger = Logger.getLogger(UserDataProperties.class);
	
	private static final String USER_DATA_FILE_ABS_PATH_ENV_VAR_NAME = "RTWS_USER_DATA_FILE_ABS_PATH";
	private static final String USER_DATA_DEFAULT_LOC = "/etc/rtwsrc";
	
	public static final String RTWS_ACCESS_KEY = "RTWS_ACCESS_KEY";
	public static final String RTWS_SECRET_KEY = "RTWS_SECRET_KEY";
	public static final String RTWS_RAID_PATH = "RTWS_RAID_PATH";
	public static final String RTWS_MOUNT_DEVICE = "RTWS_MOUNT_DEVICE";
	public static final String RTWS_MANIFEST = "RTWS_MANIFEST";
	public static final String RTWS_BUCKET_NAME = "RTWS_BUCKET_NAME";
	public static final String RTWS_TENANT_ID = "RTWS_TENANT_ID";
	public static final String RTWS_SAIC_LOG_LEVEL = "RTWS_SAIC_LOG_LEVEL";
	public static final String RTWS_DNS_ADDRESS = "RTWS_DNS_ADDRESS";
	public static final String RTWS_ROOT_LOG_LEVEL = "RTWS_ROOT_LOG_LEVEL";
	public static final String RTWS_FQDN = "RTWS_FQDN";
	public static final String RTWS_DOMAIN = "RTWS_DOMAIN";
	public static final String RTWS_DOMAIN_SUFFIX = "RTWS_TENANT_DOMAIN_SUFFIX";
	public static final String RTWS_STORAGE_ENDPOINT = "RTWS_STORAGE_ENDPOINT";
	public static final String RTWS_SERVICE_ENDPOINT = "RTWS_SERVICE_ENDPOINT";
	public static final String RTWS_MOUNT_MODE = "RTWS_MOUNT_MODE";
	public static final String RTWS_SW_VERSION = "RTWS_SW_VERSION";
	public static final String RTWS_TMS_DNS = "RTWS_TMS_DNS";
	public static final String RTWS_TMS_API_IP = "RTWS_TMS_API_IP";
	public static final String RTWS_GATEWAY_IP = "RTWS_GATEWAY_IP";
	public static final String RTWS_TMS_DEFAULT_IP = "RTWS_TMS_DEFAULT_IP";
	public static final String RTWS_ACCT_ID = "RTWS_ACCT_ID";
	public static final String RTWS_IAAS_REGION = "RTWS_IAAS_REGION";
	public static final String RTWS_RUN_ATTACHER = "RTWS_RUN_ATTACHER";
	public static final String RTWS_SETUP_KEY = "RTWS_SETUP_KEY";
	public static final String RTWS_RAID_DEVICES = "RTWS_RAID_DEVICES";
	public static final String RTWS_HOSTED_ZONE = "RTWS_HOSTED_ZONE";
	public static final String RTWS_IS_GATEWAY = "RTWS_IS_GATEWAY";
	public static final String RTWS_ZOOKEEPER_QUORUM_SERVERS = "RTWS_ZOOKEEPER_QUORUM_SERVERS";
	public static final String RTWS_VPC_ENABLED = "RTWS_VPC_ENABLED";
	public static final String RTWS_INGEST_CONFIG  = "RTWS_INGEST_CONFIG";
	public static final String RTWS_PROCESS_GROUP = "RTWS_PROCESS_GROUP";
	public static final String RTWS_IS_NAT = "RTWS_IS_NAT";
	public static final String RTWS_IS_TMS = "RTWS_IS_TMS";
	
	private static class UserDataPropertiesSingletonHolder { 
		static final UserDataProperties instance = new UserDataProperties();
	}

	public static UserDataProperties getInstance() {
		return UserDataPropertiesSingletonHolder.instance;
	}
	
	Properties userData = new Properties();
	
	private UserDataProperties() {
		String userDataFileAbsPath = null;
		InputStream is = null;
		try {
			try {
				userDataFileAbsPath = System.getenv(USER_DATA_FILE_ABS_PATH_ENV_VAR_NAME);
				
				if (userDataFileAbsPath == null)
					userDataFileAbsPath = System.getProperty(USER_DATA_FILE_ABS_PATH_ENV_VAR_NAME);
				
				if(userDataFileAbsPath != null) {
					if(StringUtils.isBlank(userDataFileAbsPath)) {
						logger.error("The value specified for '" + USER_DATA_FILE_ABS_PATH_ENV_VAR_NAME + "' is empty.  Falling back to default location.");
						userDataFileAbsPath = null;
						}
					else {
						File rtwsUserData = new File(userDataFileAbsPath);
						if(rtwsUserData.exists() == false || rtwsUserData.isFile() == false || rtwsUserData.canRead() == false) {
							logger.error("The value specified for '" + USER_DATA_FILE_ABS_PATH_ENV_VAR_NAME + "', '" + userDataFileAbsPath + "' cannot be read.  Falling back to default location.");
							userDataFileAbsPath = null;
						}
					}
				}
			}
			catch(SecurityException securityException) {
				userDataFileAbsPath = null;
			}
			
			if(userDataFileAbsPath == null) {
				logger.debug("Using default value '" + USER_DATA_DEFAULT_LOC + "' for User Data location");
				userDataFileAbsPath = USER_DATA_DEFAULT_LOC;
			}
			
			is = new FileInputStream(userDataFileAbsPath);
			userData.load(is);
		} catch (IOException e) {
			logger.error("Failed to load user data from '" + userDataFileAbsPath + "'", e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e);
				}
		}
	}
	
	public String getString(String propertyName) {
		String value = userData.getProperty(propertyName);
		if (value == null || value.trim().length() == 0) {
			
			if (logger.isDebugEnabled())
				logger.debug("Property Name:" + propertyName + " not found in UserData");
		}
		return value;
	}
}
