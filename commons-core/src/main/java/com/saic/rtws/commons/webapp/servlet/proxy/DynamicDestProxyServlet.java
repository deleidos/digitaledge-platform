package com.saic.rtws.commons.webapp.servlet.proxy;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpURI;

import com.saic.rtws.commons.webapp.servlet.proxy.dest.RequestParamsProxyDestProvider;

/**
 * An HTTP proxy whose destination provider fetches the destination from request
 * parameters and falls back to RTWS properties.  If found, the destination request
 * params are not proxied on to the destination.
 * 
 * By default, the request params are defined in {@link RequestParamsProxyDestProvider}
 * 
 * Note: this proxy servlet REQUIRES a BufferedHttpServletRequestFilter to be in
 * the filter chain before reaching here.  Otherwise, for POST requests, we would
 * read the body twice causing a Jetty EofException.
 */
public class DynamicDestProxyServlet extends ConfigurableDestProxyServlet {
	public DynamicDestProxyServlet() {
		super();
		this.setDestProvider(new RequestParamsProxyDestProvider());
	}
	
	@Override
	protected HttpURI proxyHttpURI(HttpServletRequest request, String uri)
			throws MalformedURLException {
		
		RequestParamsProxyDestProvider tmpDestProvider = null;
		try {
			tmpDestProvider = (RequestParamsProxyDestProvider)getDestProvider();
		}
		catch(Exception e) {
			tmpDestProvider = null;
		}
		
		if(tmpDestProvider == null) {
			throw new MalformedURLException("Invalid Proxy Configuration.  Unknown Request Params for Proxy Destination");
		}
		
		List<String> nonProxiedParams = new ArrayList<String>();
		
		for(String currParamName : Arrays.asList(tmpDestProvider.getDestProtocolParamName(),
			tmpDestProvider.getDestHostParamName(),
			tmpDestProvider.getDestPortParamName(),
			tmpDestProvider.getDestPathPrefixParamName()))
		{
			if(StringUtils.isNotBlank(currParamName)) {
				nonProxiedParams.add(currParamName);
			}
		}
		
		StringBuilder queryStringBuilder = new StringBuilder();
		String origQueryString = request.getQueryString();
		if(origQueryString != null) {
			/*
			 * Only include params on the query string if they previously existed there.
			 * The rest of the params will be passed via the body
			 */
			
			boolean foundFirstParam = false;
			Enumeration paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String currParamName = (String)paramNames.nextElement();
				
				
				/*
				 * NiceToHave This check doesn't handle the case where a param name existed
				 * in the request body and in the value of a query string param.  Too rare a case and the 
				 * full impl below would handle it anyway.
				 * 
				 * This also doesn't handle matrix params, but the only surefire way to do
				 * that would be to wrap the request and return false/empty for the proxy
				 * properties.  This would ensure the request was re-constructed perfectly via
				 * the servlet impl.
				 */
				if(nonProxiedParams.contains(currParamName) == false && origQueryString.contains(currParamName)) {
					
					String[] currParamValues = request.getParameterValues(currParamName);
					
					// Values should always exist by virtue of the parameter existing, but this check ensures it
					if(currParamValues != null && currParamValues.length > 0) {
						
						if(foundFirstParam == false) {
							foundFirstParam = true;
							queryStringBuilder.append("?");
						}
						else {
							queryStringBuilder.append("&");
						}
						
						for(String currParamValue : currParamValues) {
							/* 
							 * cases where the param is specified with no value (param1&param2=blah)
							 * or an empty value (param1=&param2=blah) still return an empty string
							 * value.  The null check is just to be safe.
							 */
							queryStringBuilder.append(currParamName).append("=").append(currParamValue == null ? "" : currParamValue);
						}
					}
				}
			}
		}
			
		StringBuilder uriBuilder = new StringBuilder();
		
		String tmpPrefix = tmpDestProvider.getDestPathPrefix(request, uri);
		if(tmpPrefix != null) {
			uriBuilder.append(tmpPrefix);
		}
		
		int endIndex = uri.length();
		if(StringUtils.isNotBlank(origQueryString)) {
			endIndex = uri.indexOf("?");
		}
		uriBuilder.append(uri.substring(uriPrefix.length(), endIndex));
		
		if(queryStringBuilder.length() > 0) {
			uriBuilder.append(queryStringBuilder.toString());
		}
		
		return super.proxyHttpURI(tmpDestProvider.getDestProtocol(request, uri),
				tmpDestProvider.getDestHost(request, uri),
				tmpDestProvider.getDestPort(request, uri),
				uriBuilder.toString());
	}
}
