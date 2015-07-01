package com.saic.rtws.commons.webapp.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * This wrapper allows you to add HTTP headers to the request.
 * 
 * @author moserm
 */
public class MoreHeadersHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private Map<String, String> moreHeadersMap;

	public MoreHeadersHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		moreHeadersMap = new HashMap<String, String>();
	}

	public void addHeader(String name, String value) {
		moreHeadersMap.put(name, value);
	}

	@Override
	public String getHeader(String name) {
		String value = null;

		if (moreHeadersMap.containsKey(name)) {
			value = moreHeadersMap.get(name);
		}
		else {
			value = ((HttpServletRequest) this.getRequest()).getHeader(name);
		}

		return value;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaderNames() {
		HttpServletRequest request = (HttpServletRequest) this.getRequest();

		List<String> returnList = new ArrayList<String>();

		// get headers from original request
		Enumeration e = request.getHeaderNames();
		while (e.hasMoreElements()) {
			returnList.add(e.nextElement().toString());
		}

		// add more headers
		for (String key : moreHeadersMap.keySet()) {
			returnList.add(key);
		}
		
		return Collections.enumeration(returnList);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaders(String name) {
		HttpServletRequest request = (HttpServletRequest) this.getRequest();

		List<String> returnList = new ArrayList<String>();

		// get headers matching this name from original request
		Enumeration e = request.getHeaders(name);
		while (e.hasMoreElements()) {
			returnList.add(e.nextElement().toString());
		}

		// add more headers matching this name
		if (moreHeadersMap.containsKey(name)) {
			returnList.add(moreHeadersMap.get(name)); 
		}

		return Collections.enumeration(returnList);
	}

}
