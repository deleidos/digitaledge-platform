package com.saic.rtws.commons.webapp.servlet.proxy.dest;

import java.net.MalformedURLException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * Fetches proxy destination information from the inbound request headers with the option to
 * fall back to RTWS properties.  Extenders/Users can customize the header names used
 * by extending this class or passing servlet init params.  Also, the default RTWS property names
 * can still be customized by servlet init params as described in {@link RtwsPropProxyDestProvider}
 * 
 * See Also:<br />
 * @see RtwsPropProxyDestProvider
 * 
 * @author rayju
 */
public class HeaderProxyDestProvider extends RtwsPropProxyDestProvider {
	// Servlet Param Properties to override the defaults
	private static final String DEST_PROTOCOL_HEADER_NAME_INIT_PARAM = "destProtocolParamName";
	private static final String DEST_HOST_HEADER_NAME_INIT_PARAM = "destHostParamName";
	private static final String DEST_PORT_HEADER_NAME_INIT_PARAM = "destPortParamName";
	private static final String DEST_PATH_PREFIX_HEADER_NAME_INIT_PARAM = "destPathPrefixParamName";
	
	// Default values for this instance.  Can be set by extenders or overridden by servlet params.  Null/Empty allowed.
	protected String defaultDestProtocolHeaderName = "X-Forwarded-Proto";
	protected String defaultDestHostHeaderName = "X-Forwarded-To";
	protected String defaultDestPortHeaderName = "X-Forwarded-Port";
	protected String defaultDestPathPrefixHeaderName = "X-Forwarded-Path";
	
	// Internal variables to hold the final configured param names
	private String destProtocolHeaderName, destHostHeaderName, destPortHeaderName, destPathPrefixHeaderName;
	
	public HeaderProxyDestProvider() {
		super();
		this.setForceCompleteDestProps(false);
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		destProtocolHeaderName = getConfiguredHeaderName(config, DEST_PROTOCOL_HEADER_NAME_INIT_PARAM, defaultDestProtocolHeaderName);
		destHostHeaderName = getConfiguredHeaderName(config, DEST_HOST_HEADER_NAME_INIT_PARAM, defaultDestHostHeaderName);
		destPortHeaderName = getConfiguredHeaderName(config, DEST_PORT_HEADER_NAME_INIT_PARAM, defaultDestPortHeaderName);
		destPathPrefixHeaderName = getConfiguredHeaderName(config, DEST_PATH_PREFIX_HEADER_NAME_INIT_PARAM, defaultDestPathPrefixHeaderName);
	}
	
	public String getDestProtocolHeaderName() {
		return destProtocolHeaderName;
	}

	public String getDestHostHeaderName() {
		return destHostHeaderName;
	}

	public String getDestPortHeaderName() {
		return destPortHeaderName;
	}

	public String getDestPathPrefixHeaderName() {
		return destPathPrefixHeaderName;
	}
	
	@Override
	public String getDestProtocol(HttpServletRequest request, String uri) throws MalformedURLException {
		
		String result = getDestComponentHeaderValue(request, getDestProtocolHeaderName());
		
		if(result == null) {
			result = super.getDestProtocol(request, uri);
		}
		
		return result;
	}

	@Override
	public String getDestHost(HttpServletRequest request, String uri) throws MalformedURLException {
		
		String result = getDestComponentHeaderValue(request, getDestHostHeaderName());
		
		if(result == null) {
			result = super.getDestHost(request, uri);
		}
		
		return result;
	}

	@Override
	public int getDestPort(HttpServletRequest request, String uri) throws MalformedURLException {
		int result = -1;
		
		String portString = getDestComponentHeaderValue(request, getDestPortHeaderName());
		
		if(portString == null) {
			result = super.getDestPort(request, uri);
		}
		else {
			try {
				result = Integer.parseInt(portString);
			}
			catch (NumberFormatException e) {
				throw new MalformedURLException("Invalid port specified at '" + getDestPortHeaderName() + "'");
			}
		}
		
		return result;
	}

	@Override
	public String getDestPathPrefix(HttpServletRequest request, String uri)
			throws MalformedURLException {
		
		String result = getDestComponentHeaderValue(request, getDestPathPrefixHeaderName());
		
		if(result == null) {
			result = super.getDestPathPrefix(request, uri);
		}
		
		return result;
	}

	private String getDestComponentHeaderValue(HttpServletRequest request, String componentHeaderName) throws MalformedURLException {
		String result = null;
		
		if(StringUtils.isNotBlank(componentHeaderName)) {
			
			Enumeration headerValueEnum = request.getHeaders(componentHeaderName);
			if(headerValueEnum != null) {
				while(headerValueEnum.hasMoreElements()) {
					Object currHeaderValue = headerValueEnum.nextElement();
					
					if(currHeaderValue == null || ((currHeaderValue instanceof String) == false)) {
						throw new MalformedURLException("Encountered invalid header type at header '" + componentHeaderName + "'");
					}
					
					if(result == null) {
						result = (String)currHeaderValue;
						
						if(StringUtils.isBlank(result)) {
							throw new MalformedURLException("Blank proxy destination specified at header '" + componentHeaderName + "'");
						}
					}
					else {
						throw new MalformedURLException("Multiple header values specified at header '" + componentHeaderName + "'");
					}
				}
			}
		}
		
		return result;
	}
	
	private String getConfiguredHeaderName(ServletConfig config, String initParam, String defaultHeaderName) {
		String result = null;
		
		String initParamValue = config.getInitParameter(initParam);
		if(StringUtils.isBlank(initParamValue)) {
			if(initParamValue == null) {
				// Use the default if a servlet param was not provided.
				result = defaultHeaderName;
			}
			else {
				// Allowing the user to specify an empty value to indicate that the header should
				// not be used when determining the destination
				result = null;
			}
		}
		
		return result;
	}
}
