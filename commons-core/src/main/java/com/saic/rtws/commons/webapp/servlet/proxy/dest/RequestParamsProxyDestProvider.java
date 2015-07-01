package com.saic.rtws.commons.webapp.servlet.proxy.dest;

import java.net.MalformedURLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * Fetches proxy destination information from the inbound request with the option to
 * fall back to RTWS properties.  Extenders/Users can customize the param names used
 * by extending this class or passing servlet init params.  Also, the default RTWS property names
 * can still be customized by servlet init params as described in {@link RtwsPropProxyDestProvider}
 * 
 * [IMPORTANT USAGE NOTE:] Because this proxy reads from the request, any proxy utilizing this ProxyDestProvider
 * must be fronted with a {@link com.saic.rtws.commons.webapp.filter.BufferedHttpServletRequestFilter}
 * to ensure the request body can be proxied.  Because of this, this type of proxy is not recommended
 * for any streaming resources.
 * 
 * See Also:<br />
 * @see RtwsPropProxyDestProvider
 * @see com.saic.rtws.commons.webapp.filter.BufferedHttpServletRequestFilter
 * 
 * @author rayju
 */
public class RequestParamsProxyDestProvider extends RtwsPropProxyDestProvider {
	// Servlet Param Properties to override the defaults
	private static final String DEST_PROTOCOL_PARAM_NAME_INIT_PARAM = "destProtocolParamName";
	private static final String DEST_HOST_PARAM_NAME_INIT_PARAM = "destHostParamName";
	private static final String DEST_PORT_PARAM_NAME_INIT_PARAM = "destPortParamName";
	private static final String DEST_PATH_PREFIX_PARAM_NAME_INIT_PARAM = "destPathPrefixParamName";
	
	// Default values for this instance.  Can be set by extenders or overridden by servlet params.  Null/Empty allowed.
	protected String defaultDestProtocolParamName = "destprotocol";
	protected String defaultDestHostParamName = "desthostname";
	protected String defaultDestPortParamName = "destport";
	protected String defaultDestPathPrefixParamName = "destpathprefix";
	
	// Internal variables to hold the final configured param names
	private String destProtocolParamName, destHostParamName, destPortParamName, destPathPrefixParamName;
	
	public RequestParamsProxyDestProvider() {
		super();
		this.setForceCompleteDestProps(false);
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		destProtocolParamName = getConfiguredRequestParamName(config, DEST_PROTOCOL_PARAM_NAME_INIT_PARAM, defaultDestProtocolParamName);
		destHostParamName = getConfiguredRequestParamName(config, DEST_HOST_PARAM_NAME_INIT_PARAM, defaultDestHostParamName);
		destPortParamName = getConfiguredRequestParamName(config, DEST_PORT_PARAM_NAME_INIT_PARAM, defaultDestPortParamName);
		destPathPrefixParamName = getConfiguredRequestParamName(config, DEST_PATH_PREFIX_PARAM_NAME_INIT_PARAM, defaultDestPathPrefixParamName);
	}
	
	public String getDestProtocolParamName() {
		return destProtocolParamName;
	}

	public String getDestHostParamName() {
		return destHostParamName;
	}

	public String getDestPortParamName() {
		return destPortParamName;
	}

	public String getDestPathPrefixParamName() {
		return destPathPrefixParamName;
	}
	
	@Override
	public String getDestProtocol(HttpServletRequest request, String uri) throws MalformedURLException {
		
		String result = getDestComponentRequestParam(request, getDestProtocolParamName());
		
		if(result == null) {
			result = super.getDestProtocol(request, uri);
		}
		
		return result;
	}

	@Override
	public String getDestHost(HttpServletRequest request, String uri) throws MalformedURLException {
		
		String result = getDestComponentRequestParam(request, getDestHostParamName());
		
		if(result == null) {
			result = super.getDestHost(request, uri);
		}
		
		return result;
	}

	@Override
	public int getDestPort(HttpServletRequest request, String uri) throws MalformedURLException {
		int result = -1;
		
		String portString = getDestComponentRequestParam(request, getDestPortParamName());
		
		if(portString == null) {
			result = super.getDestPort(request, uri);
		}
		else {
			try {
				result = Integer.parseInt(portString);
			}
			catch (NumberFormatException e) {
				throw new MalformedURLException("Invalid port specified at '" + getDestPortParamName() + "'");
			}
		}
		
		return result;
	}

	@Override
	public String getDestPathPrefix(HttpServletRequest request, String uri)
			throws MalformedURLException {
		
		String result = getDestComponentRequestParam(request, getDestPathPrefixParamName());
		
		if(result == null) {
			result = super.getDestPathPrefix(request, uri);
		}
		
		return result;
	}

	private String getDestComponentRequestParam(HttpServletRequest request, String componentParamName) throws MalformedURLException {
		String result = null;
		
		if(StringUtils.isNotBlank(componentParamName)) {
			
			String[] paramValues = request.getParameterValues(componentParamName);
			
			if(paramValues != null && paramValues.length > 0) {
				if(paramValues.length == 1) {
					result = paramValues[0];
					if(StringUtils.isBlank(result)) {
						throw new MalformedURLException("Blank proxy destination specified at param '" + componentParamName + "'");
					}
				}
				else {
					throw new MalformedURLException("Multiple proxy destination values specified at param '" + componentParamName + "'");
				}
			}
			
		}
		
		return result;
	}
	
	private String getConfiguredRequestParamName(ServletConfig config, String initParam, String defaultParamName) {
		String result = null;
		
		String initParamValue = config.getInitParameter(initParam);
		if(StringUtils.isBlank(initParamValue)) {
			if(initParamValue == null) {
				// Use the default if a servlet param was not provided.
				result = defaultParamName;
			}
			else {
				// Allowing the user to specify an empty value to indicate that the param should
				// not be used when determining the destination
				result = null;
			}
		}
		
		return result;
	}
}
