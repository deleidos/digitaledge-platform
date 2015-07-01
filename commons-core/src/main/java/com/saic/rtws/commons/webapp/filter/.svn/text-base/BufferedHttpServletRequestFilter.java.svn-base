package com.saic.rtws.commons.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.saic.rtws.commons.webapp.request.BufferedHttpServletRequestWrapper;

/**
 * This filter wraps the HttpServletRequest with a BufferedHttpServletRequestWrapper.
 * 
 * @author moserm
 */
public class BufferedHttpServletRequestFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ServletRequest bufferedRequest = request;
		 
 		if (request instanceof HttpServletRequest) {
 			HttpServletRequest httpRequest = (HttpServletRequest) request;
 			bufferedRequest = new BufferedHttpServletRequestWrapper(httpRequest);
 		}

 		chain.doFilter(bufferedRequest, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
