package com.saic.rtws.commons.webapp.servlet.proxy.dest;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class InitParamProxyDestProvider implements ProxyDestProvider {
	private static final String DEST_URL_INIT_PARAM = "destUrl";
	private static final String DEST_PROTOCOL_INIT_PARAM = "destProtocol";
	private static final String DEST_HOST_INIT_PARAM = "destHost";
	private static final String DEST_PORT_INIT_PARAM = "destPort";
	private static final String DEST_PATH_PREFIX_INIT_PARAM = "destPathPrefix";
	
	private String destProtocol, destHost, destPathPrefix;
	private int destPort;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		URL destUrl = null;
		
		try {
			String destUrlArg = config.getInitParameter(DEST_URL_INIT_PARAM);
			if(destUrlArg != null) {
				destUrl = new URL(destUrlArg);
			}
			else {
				
				String destProtocolArg = config.getInitParameter(DEST_PROTOCOL_INIT_PARAM);
				if(StringUtils.isBlank(destProtocolArg)) {
					throw new ServletException("'" + DEST_PROTOCOL_INIT_PARAM + "' is required.");
				}
				
				String destHostArg = config.getInitParameter(DEST_HOST_INIT_PARAM);
				if(StringUtils.isBlank(destHostArg)) {
					throw new ServletException("'" + DEST_HOST_INIT_PARAM + "' is required.");
				}
				
				String destPortArg = config.getInitParameter(DEST_PORT_INIT_PARAM);
				if(StringUtils.isBlank(destPortArg)) {
					throw new ServletException("'" + DEST_PORT_INIT_PARAM + "' is required.  Use -1 to use protocol-default value.");
				}
				int destPortInt = 0;
				try {
					destPortInt = Integer.parseInt(destPortArg);
				}
				catch(NumberFormatException e) {
					throw new ServletException("'" + DEST_PORT_INIT_PARAM + "' must be an int.  Use -1 to use protocol-default value.");
				}
				
				String destPathPrefixArg = config.getInitParameter(DEST_PATH_PREFIX_INIT_PARAM);
				if(destPathPrefixArg == null) {
					destPathPrefixArg = "";
				}
				else if(!destPathPrefixArg.startsWith("/") || destPathPrefixArg.endsWith("/")) {
					throw new ServletException("'" + DEST_PATH_PREFIX_INIT_PARAM + "' should begin with a '/' and not end with a '/'");
				}
				
				destUrl = new URL(destProtocolArg, destHostArg, destPortInt, destPathPrefixArg);
			}
			
			this.destProtocol = destUrl.getProtocol();
			this.destHost = destUrl.getHost();
			this.destPort = destUrl.getPort();
			this.destPathPrefix = destUrl.getPath();
			
		} catch (MalformedURLException malformedURLException) {
			throw new ServletException("Invalid destination configuration.  Produces invalid URL");
		}
	}

	@Override
	public String getDestProtocol(HttpServletRequest request, String uri)
			throws MalformedURLException {
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

}
