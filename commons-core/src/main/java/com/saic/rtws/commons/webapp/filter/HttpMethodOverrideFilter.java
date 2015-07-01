package com.saic.rtws.commons.webapp.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.saic.rtws.commons.webapp.request.HttpMethodOverrideRequestWrapper;

public class HttpMethodOverrideFilter implements Filter 
{

	public static final String HTTP_METHOD_OVERRIDE_PARAM = "X-HTTP-Method-Override";
	private static final String FORCE_BODY_PARAMETER = "forceBodyInclude";
	private static final ArrayList<String> IGNORABLE_PARAMETERS = new ArrayList<String>();
 
 	@Override
	public void init(FilterConfig arg0) throws ServletException {
 		IGNORABLE_PARAMETERS.add(FORCE_BODY_PARAMETER);
	}
 	
 	@Override
    public void doFilter(ServletRequest request, ServletResponse response,
    		FilterChain chain) throws IOException, ServletException 
    {
 		ServletRequest filteredRequest = request;
 
 		if (request instanceof HttpServletRequest) {
 			HttpServletRequest httpRequest = (HttpServletRequest) request;
 			filteredRequest = new HttpMethodOverrideRequestWrapper(httpRequest,
 									HTTP_METHOD_OVERRIDE_PARAM, IGNORABLE_PARAMETERS);
 		}
 
 		chain.doFilter(filteredRequest, response);
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}