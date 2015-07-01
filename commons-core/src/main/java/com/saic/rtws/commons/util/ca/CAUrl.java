package com.saic.rtws.commons.util.ca;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.config.UserDataProperties;

public class CAUrl {

	private static Logger logger = LogManager.getLogger(CAUrl.class);
	private static String certUrl = null;
	private static String certHost = null;
	
	private CAUrl(){
		//do nothing
	}
	
	/**
	 * Gets the CA host name.
	 * 
	 * @return CA host.
	 */
	public static String getCAHost(){
		return certHost;
	}
	
	public static String getCAHostTestUrl(){
		if(StringUtils.isBlank(UserDataProperties.getInstance().getString("RTWS_TMS_DNS"))){
			return String.format("%s%s/", certHost, RtwsConfig.getInstance().getString("webapp.caapi.url.contextPath"));
		}
		else{
			return String.format("%s%s/", certHost, RtwsConfig.getInstance().getString("webapp.caapi.proxy.path"));
		}
	}
	
	/**
	 * Gets the CA base url.
	 * 
	 * @param path  String context
	 * @return CA url.
	 */
	public static String getCaApiUrl(String path){
		if(CAUrl.certUrl == null){
			String certUrl = null;
			String urlPath = RtwsConfig.getInstance().getString("webapp.caapi.url.path");
			String isGateway = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_IS_GATEWAY);
			String gatewayIp = System.getProperty("GATEWAY_IP");
			
			//if user data tms_api is present or if cert host is null
			if(StringUtils.isNotBlank(isGateway) == true || StringUtils.isNotBlank(gatewayIp) == true){
				if(StringUtils.isNotBlank(isGateway)){
					String apiHost = UserDataProperties.getInstance().getString("RTWS_TMS_DNS");
					String contextPath = RtwsConfig.getInstance().getString("webapp.caapi.proxy.path");
					
					if(StringUtils.isNotBlank(apiHost)){
						certUrl = String.format("%s://%s:%s%s/rest/cert/%s", RtwsConfig.getInstance().getString("webapp.caapi.url.scheme"), 
												apiHost, RtwsConfig.getInstance().getString("webapp.caapi.url.port"), contextPath, path);
						CAUrl.certUrl = String.format("%s://%s:%s%s/rest/cert/", RtwsConfig.getInstance().getString("webapp.caapi.url.scheme"), 
												apiHost, RtwsConfig.getInstance().getString("webapp.caapi.url.port"), contextPath);
						CAUrl.certHost = String.format("%s://%s:%s", RtwsConfig.getInstance().getString("webapp.caapi.url.scheme"), 
								apiHost, RtwsConfig.getInstance().getString("webapp.caapi.url.port"));
					}
					else{
						logger.error("RTWS_TMS_DNS is not set in user data, exiting certificate generation.");
						System.exit(-1);
					}
				}
				else if(StringUtils.isNotBlank(gatewayIp)){
					String apiHost = gatewayIp;
					
					certUrl = String.format("%s://%s:%s%s/rest/cert/%s", RtwsConfig.getInstance().getString("webapp.caapi.url.scheme"), 
							apiHost, RtwsConfig.getInstance().getString("webapp.caapi.url.port"), RtwsConfig.getInstance().getString("webapp.caapi.url.contextPath"), path);
					CAUrl.certUrl = String.format("%s://%s:%s%s/rest/cert/", RtwsConfig.getInstance().getString("webapp.caapi.url.scheme"), 
							apiHost, RtwsConfig.getInstance().getString("webapp.caapi.url.port"), RtwsConfig.getInstance().getString("webapp.caapi.url.contextPath"));
					CAUrl.certHost = String.format("%s://%s:%s", RtwsConfig.getInstance().getString("webapp.caapi.url.scheme"), 
							apiHost, RtwsConfig.getInstance().getString("webapp.caapi.url.port"));
				}
				else{
					logger.error("System property RTWS_TMS_API_IP or RTWS_GATEWAY_IP are not defined, exiting certificate generation.");
					System.exit(-1);
				}
			}
			else{
				if(StringUtils.isNotBlank(urlPath)){
					certUrl = String.format("%s/rest/cert/%s", urlPath, path);
					CAUrl.certUrl = String.format("%s/rest/cert/", urlPath);
					CAUrl.certHost = String.format("%s://%s:%s", RtwsConfig.getInstance().getString("webapp.caapi.url.scheme"), 
							RtwsConfig.getInstance().getString("webapp.caapi.url.host"), RtwsConfig.getInstance().getString("webapp.caapi.url.port"));
				}
				else{
					logger.error("Property webapp.caapi.url.pathis not defined, exiting certificate generation.");
					System.exit(-1);
				}
			}
			
			return certUrl;
		}
		else{
			return String.format("%s%s", CAUrl.certUrl, path);
		}
	}
}
