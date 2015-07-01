package com.saic.rtws.commons.webapp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/**
 * Convenience base class for the HttpServlet. 
 */
public class BaseHttpServlet extends HttpServlet implements java.io.Serializable {
	
	/** Random serial version. */
	private static final long serialVersionUID = 546181347L;

	/** Constructor. */
	public BaseHttpServlet() { }
	
	/** 
	 * DELETE Method.
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException Could throw a ServletException
	 * @throws IOException Could throw an IOException
	 */
	public void doDelete(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "DELETE Method for " + request.getPathInfo() + " not implemented.");
	}
	
	/** 
	 * GET Method.
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException Could throw a ServletException
	 * @throws IOException Could throw an IOException
	 */
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "GET Method for " + request.getPathInfo() + " not implemented.");
	}
	
	/** 
	 * POST Method.
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException Could throw a ServletException
	 * @throws IOException Could throw an IOException
	 */
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "POST Method for " + request.getPathInfo() + " not implemented.");
	}
	
	/** 
	 * PUT Method.
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException Could throw a ServletException
	 * @throws IOException Could throw an IOException
	 */
	public void doPut(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "PUT Method for " + request.getPathInfo() + " not implemented.");
	}
	
	/** Print. 
	 * @throws IOException */
	public void printErrorResponse(HttpServletResponse response, Exception e) throws IOException {
		Logger logger = Logger.getLogger(getClass());
		logger.error(e.getMessage(), e);
		
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	}

	/** Print. 
	 * @throws IOException 
	 * @throws ValidationException 
	 * @throws MarshalException */
	public void printErrorResponse(HttpServletResponse response, String message, Exception e) throws IOException, MarshalException, ValidationException {
		Logger logger = Logger.getLogger(getClass());
		logger.error(message, e);
		
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
	}
}
