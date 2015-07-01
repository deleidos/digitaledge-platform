package com.saic.rtws.commons.webapp.servlet.proxy.dest;

import java.net.MalformedURLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface ProxyDestProvider {
	
	/**
	 * Allows this provider access to the servlet config object, if needed.
	 * 
	 * @param config
	 */
	public void init(ServletConfig config) throws ServletException;
	
	/**
	 * @return the protocol (no special chars - "://") of the destination resource
	 * or null to indicate a problem and fail the proxy request.
	 */
	public String getDestProtocol(HttpServletRequest request, String uri) throws MalformedURLException;
	
	/**
	 * @return the host of the destination resource or null to indicate a problem
	 * and fail the proxy request.
	 */
	public String getDestHost(HttpServletRequest request, String uri) throws MalformedURLException;
	
	/**
	 * @return the port of the destination resource or null to indicate a problem
	 * and fail the proxy request.
	 */
	public int getDestPort(HttpServletRequest request, String uri) throws MalformedURLException;
	
	/**
	 * The destination path prefix is a way to simplify proxy calls for the caller.
	 * 
	 * Ex:
	 * If the proxy servlet listens on <proxyServer>/proxy/rtws/myApi
	 * and the destiation servlet listens on <destServer>/myApi, a proxy
	 * call would look like <proxyServer>/proxy/rtws/myApi/myApi/<path>/<to>/<resource>?<optionalQueryString>
	 * 
	 * Notice the duplication of the myApi to match the proxy servlet and the servlet path of the destination.
	 * 
	 * Use this method to provide a prefix to the destination and eliminate the need for this duplication.
	 * 
	 * @return "" to indicate no prefix or a path starting with "/" and ending with no "/" otherwise;
	 * 	null to indicate a problem and fail the proxy request.
	 */
	public String getDestPathPrefix(HttpServletRequest request, String uri) throws MalformedURLException;
}
