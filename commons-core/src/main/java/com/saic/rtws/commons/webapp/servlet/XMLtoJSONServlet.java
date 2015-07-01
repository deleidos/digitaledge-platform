package com.saic.rtws.commons.webapp.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
// import org.apache.lucene.queryParser.ParseException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.saic.rtws.commons.exception.InvalidParameterException;

/**
 * XMLtoJSON servlet.
 */
public class XMLtoJSONServlet extends com.saic.rtws.commons.webapp.rest.BaseRestServlet {

	/** Logger. */
	private Logger logger = Logger.getLogger(XMLtoJSONServlet.class);
	
	/** Serial version UID. */
	private static final long serialVersionUID = -1119289005837847708L;
	
	/** Constructor. */
	public XMLtoJSONServlet() { }
	
	/**
	 * GET method.
	 * @param request HttpServletRequest parameter
	 * @param response HttpServletResponse parameter
	 * @throws ServletException Could throw a ServletException
	 * @throws IOException Could throw a IOException
	 */
	public final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		try {
			// output response
			doXMLtoJSONResponse(request, response);
			
		} catch (Exception e) {
			try { this.printErrorResponse(response, parseFormat(request), e); 
			} catch (Exception e2) { logger.error(e2); } 
		}
	}
	
	/**
	 * Method to render output for GET methods.
	 * @param request HttpServletRequest parameter
	 * @param response HttpServletResponse parameter
	 * @throws IOException Could throw a IOException
	 * @throws MarshalException Could throw a MarshalException
	 * @throws ValidationException Could throw a ValidationException
	 * @throws InvalidParameterException 
	 * @throws ParseException 
	 */
	protected void doXMLtoJSONResponse(final HttpServletRequest request, final HttpServletResponse response) 
	throws IOException, MarshalException, ValidationException, InvalidParameterException// , ParseException 
	{
		String url = "";
		GetMethod get = null;
		InputStream is = null;
		java.io.BufferedWriter bos = new java.io.BufferedWriter(new java.io.OutputStreamWriter(response.getOutputStream()));
		HttpClient client = null;
		
		try {
			url = buildProxyURL(request) + "?" + request.getQueryString();
			
			final int timeout = 30000;
			
			client = new HttpClient(new MultiThreadedHttpConnectionManager());
			client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			
			// Proxy
			get = new GetMethod(url);
			int httpResultCode = client.executeMethod(get);
			
			if ((HttpServletResponse.SC_OK <= httpResultCode) && (httpResultCode < HttpServletResponse.SC_MULTIPLE_CHOICES)) {
				//
				is = get.getResponseBodyAsStream();
				//String xml = get.getResponseBodyAsString();
				//System.out.println("XML = " + xml);
				
				// Setup XML to JSON conversion
	            XMLSerializer xmlSerializer = new XMLSerializer();
	            JSON json = xmlSerializer.readFromStream(is);
	            //JSON json = xmlSerializer.read( xml );  
	            //System.out.println("JSON = " + json.toString(2) );
	            
	            // Write JSON to output
		    	setContentType(response);
	            bos.write(json.toString(2));
			}
		} finally {
			try { is.close(); } catch (Exception e) { }
			try { get.releaseConnection(); } catch (Exception e) { }
			try { bos.close(); } catch (Exception e) { }
		}
	}
	
	/**
	 * Build the proxy URL.
	 * @param request HttpServletRequest object
	 * @return Return url for proxy
	 */
	protected String buildProxyURL(final HttpServletRequest request) {
		StringBuffer buf = new StringBuffer();
		
		String path = request.getServletPath();
		if (path != null) {
			path = path.replaceFirst("/json", "");
		}
		
		buf.append(request.getScheme())
			.append("://")
			.append(request.getServerName())
			.append(":")
			.append(request.getServerPort())
			.append(request.getContextPath())
			.append(path);
		
		return buf.toString();
	}
	
	/**
	 * Override parseFormat to always return "json".
	 * @param request HttpServletRequest object
	 * @return Return "json" as format
	 */
	public final String parseFormat(final HttpServletRequest request) {
		return "json";
	}
	
	/**
	 * Set the "Content-Type" to "application/json".
	 * @param response HttpServletResponse object
	 */
	public void setContentType(final HttpServletResponse response) {
    	response.setContentType("application/json");
	}
	
}
