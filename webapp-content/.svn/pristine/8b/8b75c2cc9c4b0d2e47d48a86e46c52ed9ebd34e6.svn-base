package com.deleidos.rtws.webapp.contentapi.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.deleidos.rtws.commons.exception.InvalidParameterException;
import com.deleidos.rtws.commons.model.response.PropertiesResponse;
import com.deleidos.rtws.commons.webapp.WebappUrlFactory;
import com.deleidos.rtws.commons.webapp.rest.RestResponseWriter;
import com.deleidos.rtws.webapp.contentapi.dao.SimpleContentStorageDAO;

/**
 * Content object servlet.
 */
public class ContentObjectServlet extends com.deleidos.rtws.commons.webapp.rest.BaseRestServlet {
	/** Logger. */
	private Logger logger = Logger.getLogger(ContentObjectServlet.class);
	
	/** Serial version UID. */
	private static final long serialVersionUID = 54731564L;

	/** Constructor. */
	public ContentObjectServlet() { }
	
	/**
	 * GET method.
	 * @param request HttpServletRequest parameter
	 * @param response HttpServletResponse parameter
	 * @throws ServletException Could throw a ServletException
	 * @throws IOException Could throw a IOException
	 */
	public final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		String format = "json";
		long contentId = 0L;
		int userId = 0;
		
		InputStream is = null;
		OutputStream os = null;
		try {
			try { format	= parseFormat(request); } catch (Exception e) { printErrorResponse(response, format, e); return; }
			try { contentId = parseParameterContentId(request); } catch (InvalidParameterException e) { printErrorResponse(response, format, e); return; }
			
			SimpleContentStorageDAO simpleContentStorageDAO = new SimpleContentStorageDAO(userId);
			Properties p = simpleContentStorageDAO.getContentProperties(contentId);
			String originalFilename = p.getProperty("Filename", "");
			String contentType = p.getProperty("Content-Type", "application/octet-stream");
			String filePath = simpleContentStorageDAO.getContentObjectPath(contentId);
			File file = new File(filePath);
			Long fileLength = file.length(); 
			
			// Manipulate return ContentType
			if (contentType.indexOf("application/octet-stream") >= 0) {
				// Try to figure out content type
				contentType = getMimeTypeName(file);
				
				// If it's still octet-stream and clearly a jpg file, then mark it as such
				if (contentType.indexOf("application/octet-stream") >= 0 && originalFilename.trim().toLowerCase().endsWith(".jpg")) {
					contentType = "image/jpeg";
				}
			}
			
			response.setContentType(contentType);
			if (fileLength < Integer.MAX_VALUE) { response.setContentLength(new Long(file.length()).intValue());	}
			//logger.info("Output " + filePath + " as " + contentType);
			
			// open streams
			is = simpleContentStorageDAO.getContentObjectStream(contentId);
			os = response.getOutputStream();
			
			// copy the streams
			IOUtils.copyLarge(is, os);
			
		} catch (FileNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Could not find file associated with id: " + contentId);
		} catch (Exception e) {
			try { printErrorResponse(response, format, e); } catch (Exception e2) { logger.error(e2); }
		} finally {
			try { is.close(); } catch (Exception e) { }
			try { os.close(); } catch (Exception e) { }
		}
		return;
	}
	
	/**
	 * POST method.
	 * @param request HttpServletRequest parameter
	 * @param response HttpServletResponse parameter
	 * @throws ServletException Could throw a ServletException
	 * @throws IOException Could throw a IOException
	 */
	public final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		// trap bad method
		if (!ServletFileUpload.isMultipartContent(request)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request must contain multi-part content.");
			return;
		}
		
		int userId = 0;
		
		SimpleContentStorageDAO simpleContentStorageDAO = new SimpleContentStorageDAO(userId);
		HashMap<String, Long> ids = new HashMap<String, Long>();
		long contentId = 0L;
		InputStream is = null;
		ServletFileUpload servletFileUpload = new ServletFileUpload();
		String fileName = "";
		FileItemIterator fit;
		try {
			fit = servletFileUpload.getItemIterator(request);
			FileItemStream fis = null;
			while (fit.hasNext()) {
				// get next FileItemStream object
				fis = fit.next();
				
				// make sure it's a file
				if (!fis.isFormField()) {
					try {
						// open stream
						is = fis.openStream();
						fileName = fis.getName();
						
						// save file
						contentId = simpleContentStorageDAO.saveContentObject(fileName, fis.getContentType(), is);
						
						// add id
						ids.put(fileName, contentId);
					} catch (Exception e) {
						logger.error("Could not save " + fileName, e);
					} finally {
						try { is.close(); } catch (Exception e) { }
					}
				}
			}
			
			// output response
			doPostPutResponse(request, response, contentId);
			
		} catch (FileUploadException e) {
			try { this.printErrorResponse(response, parseFormat(request), "Error while receiving file: " + e.getMessage(), e); 
			} catch (Exception e2) { logger.error(e2); } 
		} catch (Exception e) {
			try { this.printErrorResponse(response, parseFormat(request), e); 
			} catch (Exception e2) { logger.error(e2); } 
		} finally {
			try { is.close(); 
			} catch (Exception e) { }
		}
	}
	
	/**
	 * PUT method.
	 * @param request HttpServletRequest parameter
	 * @param response HttpServletResponse parameter
	 * @throws ServletException Could throw a ServletException
	 * @throws IOException Could throw a IOException
	 */
	public final void doPut(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		int userId = 0;
		String filename = null;
		
		SimpleContentStorageDAO simpleContentStorageDAO = new SimpleContentStorageDAO(userId);
		long contentId = 0L;
		InputStream is = null;
		
		try {
			try { filename = parseParameterFilename(request); 
			} catch (Exception e) { printErrorResponse(request, response, "Could not parse filename", e); return; }
			
			// grab input stream
			is = request.getInputStream();
			// save file
			contentId = simpleContentStorageDAO.saveContentObject(filename, request.getContentType(), is);
			
			// output response
			doPostPutResponse(request, response, contentId);
			
		} catch (Exception e) {
			try { this.printErrorResponse(response, parseFormat(request), e); } catch (Exception e2) { logger.error(e2); } 
		} finally {
			try { is.close(); } catch (Exception e) { }
		}
	}
	
	/**
	 * Method to produce output for either POST or PUT methods.
	 * @param request HttpServletRequest parameter
	 * @param response HttpServletResponse parameter
	 * @param contentId ID of content
	 * @throws IOException Could throw a IOException
	 * @throws MarshalException Could throw a MarshalException
	 * @throws ValidationException Could throw a ValidationException
	 */
	public final void doPostPutResponse(final HttpServletRequest request, final HttpServletResponse response, final long contentId) 
	throws IOException, MarshalException, ValidationException 
	{
		String responseFormat = parseFormat(request);
		
		PropertiesResponse r = new PropertiesResponse();
		r.setProperty("contentId", String.valueOf(contentId));
		r.setProperty("contentURL", WebappUrlFactory.getContentApiRelativeUrl(responseFormat, contentId));
		
		RestResponseWriter.write(request, response, r);
	}
	
	protected String getMimeTypeName(File f) {
		MimeTypes mimeTypes = new MimeTypes();
		MimeType  mimeType  = mimeTypes.getMimeType(f);
		return mimeType.getName();
	}
	
	/**
	 * Parse the "content id" parameter from the url.
	 * @param request HttpServletRequest object
	 * @return Return the content id
	 * @throws InvalidParameterException Could throw InvalidParameterException
	 */
	protected final long parseParameterContentId(final HttpServletRequest request) throws InvalidParameterException {
		
		int pos = 0;
		
		try {
			return parseUrlParameterAsLong(request, pos);
		} catch (InvalidParameterException e) {
			throw new InvalidParameterException("Please enter a valid parameter for content id: " + printUrlParameterPosition(request, pos, "content-id"), e);
		}
		
	}
	
	/**
	 * Parse the "filename" parameter from the url.
	 * @param request HttpServletRequest object
	 * @return Return the filename
	 * @throws InvalidParameterException Could throw InvalidParameterException
	 */
	protected final String parseParameterFilename(final HttpServletRequest request) throws InvalidParameterException {
		
		int pos = 0;
		
		try {
			return parseUrlParameter(request, pos);
		} catch (InvalidParameterException e) {
			throw new InvalidParameterException("Please enter a valid parameter for filename: " + printUrlParameterPosition(request, pos, "filename"), e);
		}
		
	}
}
