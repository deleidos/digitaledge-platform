package com.saic.rtws.commons.webapp.rest;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.saic.rtws.commons.exception.InvalidParameterException;
import com.saic.rtws.commons.model.response.ErrorResponse;
import com.saic.rtws.commons.webapp.BaseHttpServlet;

/**
 * Convenience base class for the HttpServlet. 
 */
public class BaseRestServlet extends BaseHttpServlet implements java.io.Serializable {
	
	/** Random serial version. */
	private static final long serialVersionUID = 546146281347L;

	/** Constructor. */
	public BaseRestServlet() { }
	
	/**
	 * Parse the response "format" from the HttpServletRequest object.
	 * @param request HttpServletRequest object
	 * @return Return format
	 */
	public String parseFormat(final HttpServletRequest request) {
		String format = RestResponseFormat.JSON;
		
		if (request.getServletPath().indexOf("/json") == 0) {
			format = RestResponseFormat.JSON;
		} else if (request.getServletPath().indexOf("/xml") == 0) {
			format = RestResponseFormat.XML;
		}
		
		return format;
	}
	
	/**
	 * Parse the URL parameter by its position.
	 * @param request HttpServletRequest object
	 * @param pos Position
	 * @return Return parameter as String
	 * @throws InvalidParameterException Could throw an InvalidParameterException
	 */
	public final String parseUrlParameter(final HttpServletRequest request, final int pos) throws InvalidParameterException {
		String parameterValue = "";
		
		try {
			StringTokenizer tokens = new StringTokenizer(request.getPathInfo(), "/");
			String token = "";
			int countNonBlankValues = 0;
			
			// 
			while ((tokens.hasMoreTokens()) && (countNonBlankValues <= pos)) { 
				token = tokens.nextToken();
				if (token != null && !"".equals(token)) {
					parameterValue = token;
					countNonBlankValues++;
				}
			}
		} catch (Exception e) {
			throw new InvalidParameterException("Please enter a valid parameter", e);
		}
		
		return parameterValue;
	}
	
	/**
	 * Parse the URL parameter by its position.
	 * @param request HttpServletRequest object
	 * @param pos Position
	 * @return Return parameter as int
	 * @throws InvalidParameterException Could throw an InvalidParameterException
	 */
	public final int parseUrlParameterAsInteger(final HttpServletRequest request, final int pos) throws InvalidParameterException {
		int value = 0;
		String str = parseUrlParameter(request, pos);
		
		try {
			value = Integer.valueOf(str);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Could not convert parameter to a integer.", e);
		} catch (Exception e) {
			throw new InvalidParameterException("Please enter a valid integer parameter " + printUrlParameterPosition(request, pos), e);
		}
		
		return value;
	}
	
	/**
	 * Parse the URL parameter by its position.
	 * @param request HttpServletRequest object
	 * @param pos Position
	 * @return Return parameter as long
	 * @throws InvalidParameterException Could throw an InvalidParameterException
	 */
	public final long parseUrlParameterAsLong(final HttpServletRequest request, final int pos) throws InvalidParameterException {
		long value = 0;
		String str = parseUrlParameter(request, pos);
		
		try {
			value = Long.valueOf(str);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Could not convert parameter to a long.", e);
		} catch (Exception e) {
			throw new InvalidParameterException("Please enter a valid long parameter " + printUrlParameterPosition(request, pos), e);
		}
		
		return value;
	}
	
	/**
	 * Print an error response.
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @param e Exception object
	 * @throws MarshalException Could throw a MarshalException
	 * @throws ValidationException Could throw a ValidationException
	 * @throws IOException Could throw an IOException
	 */
	public final void printErrorResponse(final HttpServletRequest request, final HttpServletResponse response, final Exception e) 
	throws MarshalException, ValidationException, IOException 
	{
		printErrorResponse(response, parseFormat(request), e);
	}

	/**
	 * Print an error response.
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @param message Exception message
	 * @param e Exception object
	 * @throws MarshalException Could throw a MarshalException
	 * @throws ValidationException Could throw a ValidationException
	 * @throws IOException Could throw an IOException
	 */
	public final void printErrorResponse(final HttpServletRequest request, final HttpServletResponse response, final String message, final Exception e) 
	throws MarshalException, ValidationException, IOException 
	{
		printErrorResponse(response, parseFormat(request), message, e);
	}

	/** 
	 * Print an error response.
	 * @param response HttpServletResponse object
	 * @param responseFormat Format of response
	 * @param e Exception object
	 * @throws MarshalException Could throw a MarshalException
	 * @throws ValidationException Could throw a ValidationException
	 * @throws IOException Could throw an IOException
	 */
	public final void printErrorResponse(final HttpServletResponse response, final String responseFormat, final Exception e) 
	throws MarshalException, ValidationException, IOException 
	{
		Logger logger = Logger.getLogger(getClass());
		logger.error(e.getMessage(), e);
		
		ErrorResponse r = new ErrorResponse();
		r.setStandardHeaderCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		r.setMessage(e.getMessage());
		
		RestResponseWriter.write(response, responseFormat, r);
	}

	/** 
	 * Print an error response.
	 * @param response HttpServletResponse object
	 * @param responseFormat Format of response
	 * @param message Exception message
	 * @param e Exception object
	 * @throws MarshalException Could throw a MarshalException
	 * @throws ValidationException Could throw a ValidationException
	 * @throws IOException Could throw an IOException
	 */
	public final void printErrorResponse(final HttpServletResponse response, final String responseFormat, final String message, final Exception e) 
	throws MarshalException, ValidationException, IOException 
	{
		Logger logger = Logger.getLogger(getClass());
		logger.error(message, e);
		
		ErrorResponse r = new ErrorResponse();
		r.setStandardHeaderCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		r.setMessage(message);
		
		RestResponseWriter.write(response, responseFormat, r);
	}
	
	/**
	 * Parse and return a string from a url as determined by its position delimited by the forward-slashes.
	 * Uses "parameter" as the default parameter name
	 * @param request HttpServletRequest object
	 * @param pos Position in "path info" (non-servlet) part of URL. Zero-based position.
	 * @return String
	 */
	public final String printUrlParameterPosition(final HttpServletRequest request, final int pos) {
		return printUrlParameterPosition(request, pos, "parameter");
	}
	
	/**
	 * Parse and return a string from a url as determined by its position delimited by the forward-slashes.
	 * @param request HttpServletRequest object
	 * @param pos Position in "path info" (non-servlet) part of URL. Zero-based position.
	 * @param parameterName Name of parameter
	 * @return String
	 */
	public final String printUrlParameterPosition(final HttpServletRequest request, final int pos, final String parameterName) {
		StringBuffer buf = new StringBuffer(request.getServletPath().substring(0, request.getServletPath().indexOf(request.getPathInfo())));
		
		int i = 0;
		String token = "";
		StringTokenizer tokens = new StringTokenizer(request.getPathInfo(), "/");
		
		// loop through pieces of url
		while (tokens.hasMoreTokens()) { 
			token = tokens.nextToken();
			if (!"".equals(token)) {
				buf.append("/");
				if (i == pos) {
					buf.append("{").append(parameterName).append("}");
				} else {
					buf.append(token);
				}
				i++;
			}
		}
		
		return buf.toString();
	}
}
