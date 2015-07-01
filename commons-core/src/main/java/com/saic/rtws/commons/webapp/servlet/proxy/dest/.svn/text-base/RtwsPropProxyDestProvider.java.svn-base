package com.saic.rtws.commons.webapp.servlet.proxy.dest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.lang.StringUtils;

import com.saic.rtws.commons.config.RtwsConfig;

/**
 * This ProxyDestProvider reads the destination from RTWS Properties.  The names of
 * the properties can be specified via Java properties or Servlet Init Params.  Init
 * Params will override any default property names provided in the class
 * 
 * @author rayju
 */
public class RtwsPropProxyDestProvider implements ProxyDestProvider {

	private static final String DEST_URL_PROP_NAME_INIT_PARAM = "destUrlProp";
	private static final String DEST_PROTOCOL_PROP_NAME_INIT_PARAM = "destProtocolProp";
	private static final String DEST_HOST_PROP_NAME_INIT_PARAM = "destHostProp";
	private static final String DEST_PORT_PROP_NAME_INIT_PARAM = "destPortProp";
	private static final String DEST_PATH_PREFIX_PROP_NAME_INIT_PARAM = "destPathPrefixProp";
	
	protected String defaultDestUrlPropName = null;
	protected String defaultDestProtocolPropName = null;
	protected String defaultDestHostPropName = null;
	protected String defaultDestPortPropName = null;
	protected String defaultDestPathPrefixPropName = null;
	
	private boolean forceCompleteDestProps = true;
	private String destProtocol, destHost, destPathPrefix;
	private int destPort;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			Configuration systemConfig = RtwsConfig.getInstance().getConfiguration();
			
			URL tmpURL = null;
			
			String destUrlPropName = getInitParam(config, DEST_URL_PROP_NAME_INIT_PARAM, defaultDestUrlPropName);
			if(destUrlPropName != null) {
				tmpURL = new URL(systemConfig.getString(destUrlPropName));
			}
			else {
				
				String destProtocolPropName = getInitParam(config, DEST_PROTOCOL_PROP_NAME_INIT_PARAM, defaultDestProtocolPropName);
				if(StringUtils.isNotBlank(destProtocolPropName)) {
					destProtocol = systemConfig.getString(destProtocolPropName);
				}
				
				String destHostPropName = getInitParam(config, DEST_HOST_PROP_NAME_INIT_PARAM, defaultDestHostPropName);
				if(StringUtils.isNotBlank(destHostPropName)) {
					destHost = systemConfig.getString(destHostPropName);
				}
				
				String destPortPropName = getInitParam(config, DEST_PORT_PROP_NAME_INIT_PARAM, defaultDestPortPropName);
				if(StringUtils.isNotBlank(destPortPropName)) {
					destPort = systemConfig.getInt(destPortPropName);
				}
				
				String destPathPrefixPropName = getInitParam(config, DEST_PATH_PREFIX_PROP_NAME_INIT_PARAM, defaultDestPathPrefixPropName);
				if(StringUtils.isNotBlank(destPathPrefixPropName)) {
					destPathPrefix = systemConfig.getString(destPathPrefixPropName);
				}
				
				if(forceCompleteDestProps) {
					tmpURL = new URL(destProtocol, destHost, destPort, destPathPrefix != null ? destPathPrefix : "");
				}
			}
			
			if(tmpURL != null) {
				destProtocol = tmpURL.getProtocol();
				destHost = tmpURL.getHost();
				destPort = tmpURL.getPort();
				destPathPrefix = tmpURL.getPath();
			}
		}
		catch(MalformedURLException malformedUrlException) {
			throw new ServletException("Invalid Destination URL ... Check servlet config/default property names and value of said properties");
		}
		catch(ConversionException conversionException) {
			throw new ServletException("Invalid Dest Port.", conversionException);
		}
		catch(NoSuchElementException missingPropException) {
			throw new ServletException("Invalid Dest Property", missingPropException);
		}
		catch(Exception e) {
			throw new ServletException("Invalid Configuration." + e.getMessage());
		}
	}
	
	/**
	 * Sets whether the initialization will force a complete and valid set of
	 * destination defining properties. This is useful for extenders who wish to
	 * default their dest lookup with a set of RTWS properties.
	 * 
	 * @param forceCompleteDestProps whether or not to force a complete and valid set of destination
	 * properties on initialization
	 */
	public void setForceCompleteDestProps(boolean forceCompleteDestProps) {
		this.forceCompleteDestProps = forceCompleteDestProps;
	}

	@Override
	public String getDestProtocol(HttpServletRequest request, String uri) throws MalformedURLException {
		return destProtocol;
	}

	@Override
	public String getDestHost(HttpServletRequest request, String uri) throws MalformedURLException {
		return destHost;
	}

	@Override
	public int getDestPort(HttpServletRequest request, String uri) throws MalformedURLException {
		return destPort;
	}

	@Override
	public String getDestPathPrefix(HttpServletRequest request, String uri)
			throws MalformedURLException {
		return destPathPrefix;
	}
	
	protected final String getInitParam(ServletConfig config, String paramName, String defaultValue)
	{
		String result = defaultValue;
		
		String paramValue = config.getInitParameter(paramName);
		if(paramValue != null)
		{
			result = paramValue;
		}
		
		return result;
	}
}
