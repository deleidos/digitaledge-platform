package com.saic.rtws.commons.webapp.request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.HttpMethod;

public class HttpMethodOverrideRequestWrapper extends HttpServletRequestWrapper 
{
	private String httpMethodOverrideHeader;
	private ArrayList<String> ignorableParameters;
	
	public HttpMethodOverrideRequestWrapper(HttpServletRequest request, String httpMethodOverrideHeader, 
											ArrayList<String> ignorableParameneters) {
		super(request);
		
		this.httpMethodOverrideHeader = httpMethodOverrideHeader;
		this.ignorableParameters = ignorableParameneters;
	}

	@Override
	public String getMethod() 
	{
		String headerValue = getHeader(httpMethodOverrideHeader);
		
		if (headerValue != null) {
			return headerValue;
		}
		
		return super.getMethod();
	}

	@Override
	public String getQueryString() {
		
		String headerValue = getHeader(httpMethodOverrideHeader);
		
		if (headerValue != null && headerValue.equalsIgnoreCase(HttpMethod.GET)) {
			StringBuilder queryStringBuilder = new StringBuilder("");
			
			// Include the original query string
			if(super.getQueryString() != null) {
				queryStringBuilder.append(super.getQueryString());
			}
			
			// Move all the additional parameters to the query string
			@SuppressWarnings("unchecked")
			Map<String, String[]> parameterMap = super.getParameterMap();
			Iterator<Entry<String, String[]>> mapIt = parameterMap.entrySet().iterator();
			while(mapIt.hasNext()) {
				Entry<String, String[]> parameterPair = mapIt.next();
				
				String parameterName = parameterPair.getKey();
				if(!ignorableParameters.contains(parameterName)) {
					for(String parameterValue : parameterPair.getValue()) {
						if(!queryStringBuilder.toString().isEmpty()) {
							queryStringBuilder.append("&");
						}
						queryStringBuilder.append(parameterName);
						queryStringBuilder.append("=");
						queryStringBuilder.append(parameterValue);
					}
				}
			}
			
			return queryStringBuilder.toString();
		}
		
		return super.getQueryString();
	}
}