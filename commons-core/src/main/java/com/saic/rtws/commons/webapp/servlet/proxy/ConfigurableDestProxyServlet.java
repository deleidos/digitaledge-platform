package com.saic.rtws.commons.webapp.servlet.proxy;

import java.net.MalformedURLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.servlets.ProxyServlet;

import com.saic.rtws.commons.webapp.servlet.proxy.dest.ProxyDestProvider;

/**
 * An HTTP(S) Proxy with a configurable destination provider.  This proxy should
 * be mapped to a url-pattern and will proxy the remaining path info, query string,
 * and request body to the destination determined by the ProxyDestProvider impl.
 *
 * The general concept of the proxy is as follows:
 *   <proxy-server>/<proxy-servlet-path>[<dest-path>[?<dest-query-string>]]
 *   [<dest-request>]
 * 
 * <br />
 * Extenders or Users should set the following information:
 * <br />
 * 
 * <dl>
 * <dt>Proxy Servlet Path</dt>
 * <dd>
 * 	The servlet path of the proxy servlet on the proxy server.  In general, this
 * 	will be very similar to the servlet-mapping of the proxy servlet but should
 * 	not be a pattern, should start with "/", and should not end in "/".  This value
 * 	can be set by extenders with the defaultServletPath property or by users using
 * 	the proxyServletPath init param.
 * 
 * 	[Note:] The property must be set before the init method is called.
 * </dd>
 * 
 * <dt>ProxyDestProvider impl</dt>
 * <dd>
 * 	The ProxyDestProvider interface provides the contract on how to build the
 * 	destination URL of this proxy.  Standard impls can pull the values from
 * 	static init properties, RTWS properties, request params, and header values
 * 	and are configurable.
 * 
 * 	Extenders can set this value at any time via the setDestProvider method call.
 * 	Users can provide a fully qualified java class at servlet init time with the
 * 	destProviderClass init param.
 * 
 * 	[Note:] The property must be set before the init method is called.
 * </dd>
 * </dl>
 * 
 * @author rayju
 */
public class ConfigurableDestProxyServlet extends ProxyServlet {
	protected static final String PROXY_SERVLET_PATH_INIT_PARAM = "proxyServletPath";
	protected static final String DEST_PROVIDER_CLASS_INIT_PARAM = "destProviderClass";
	
	protected String uriPrefix;
	
	protected String defaultServletPath;
	
	private ProxyDestProvider destProvider;
	
	private boolean initComplete = false;
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		boolean isRootContext = false;
		uriPrefix = config.getServletContext().getContextPath();
		if(StringUtils.isEmpty(uriPrefix))
		{
			uriPrefix = "/";
			isRootContext = true;
		}
		
		String proxyServletPathArg = getInitParam(config, PROXY_SERVLET_PATH_INIT_PARAM, defaultServletPath);
		// Empty is allowed, but is only applicable for a complete proxy webapp.  Unlikely in this case, but allowing.
		if(StringUtils.isNotBlank(proxyServletPathArg))
		{
			if(proxyServletPathArg.startsWith("/") && proxyServletPathArg.endsWith("/") == false)
			{
				// Exclude the leading slash if we already added it for the root context
				uriPrefix += proxyServletPathArg.trim().substring(isRootContext ? 1 : 0);
			}
			else
			{
				throw new ServletException("'" + PROXY_SERVLET_PATH_INIT_PARAM + "' should begin with a '/' and not end with a '/'.  Also, no wildcards");
			}
		}
		
		String destProviderClass = config.getInitParameter(DEST_PROVIDER_CLASS_INIT_PARAM);
		if(destProviderClass != null) {
			try {
				setDestProvider((ProxyDestProvider)Class.forName(destProviderClass).newInstance());
			}
			catch (Exception e) {
				throw new ServletException("Failed to instantiate/set the ProxyDestProvider at '" + destProviderClass + "'", e);
			}
		}
		
		if(getDestProvider() != null) {
			getDestProvider().init(config);
		}
		
		super.init(config);
		
		initComplete = true;
	}
	
	public ProxyDestProvider getDestProvider() {
		return destProvider;
	}
	public void setDestProvider(ProxyDestProvider destProvider) {
		if(initComplete) {
			throw new IllegalStateException("Cannot set a ProxyDestProvider after the servlet has been initialized.");
		}
		this.destProvider = destProvider;
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
	
	@Override
	protected HttpURI proxyHttpURI(HttpServletRequest request, String uri) throws MalformedURLException {
		if(destProvider == null) {
			throw new MalformedURLException("Proxy must define a ProxyDestProvider");
		}
		
		String tmpDestProtocol = destProvider.getDestProtocol(request, uri);
		if(tmpDestProtocol == null) {
			throw new MalformedURLException("Proxy failed to specify a dest protocol");
		}
		
		String tmpDestHost = destProvider.getDestHost(request, uri);
		if(tmpDestHost == null) {
			throw new MalformedURLException("Proxy failed to specify a dest host");
		}
		
		int tmpDestPort = destProvider.getDestPort(request, uri);
		if(tmpDestPort < 1) {
			throw new MalformedURLException("Proxy failed to specify a dest port");
		}
		
		String tmpDestPathPrefix = destProvider.getDestPathPrefix(request, uri);
		
		// Base Proxy handles the rest of the URL validation
		return super.proxyHttpURI(tmpDestProtocol, tmpDestHost, tmpDestPort, (tmpDestPathPrefix != null ? tmpDestPathPrefix : "") + uri.substring(uriPrefix.length()));
	}
}
