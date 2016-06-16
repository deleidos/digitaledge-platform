package com.deleidos.rtws.webapp.contentapi.servlet.enunciate;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.deleidos.rtws.commons.model.response.PropertiesResponse;
import com.deleidos.rtws.commons.util.fileset.FileSetId;
import com.deleidos.rtws.commons.util.fileset.JcrFileSetResourceWithCache;
import com.deleidos.rtws.webapp.contentapi.dao.SimpleContentIdGenerator;

@Path("/image")
public class ImageServiceImpl implements ImageService {

	/** Logger. */
	Logger logger = Logger.getLogger(ContentServiceImpl.class);
	/** Sends back error messages */
	private static final int _BadRequest = 400;
	/** JCR File Sets */
	private JcrFileSetResourceWithCache jcrFileSetResource;
	private String contextPath;
	
	/**
	 * 
	 * Constructor that creates a new Image Service.  It will create a JCR File Set Resource and initialize it.
	 * 
	 * @param servletContext
	 */
	public ImageServiceImpl(@Context ServletContext servletContext) {
		jcrFileSetResource = new JcrFileSetResourceWithCache();
		
		jcrFileSetResource.setRepositoryUrl(servletContext.getInitParameter("repositoryURL"));
		jcrFileSetResource.setUserId(servletContext.getInitParameter("userId"));
		jcrFileSetResource.setPassword(servletContext.getInitParameter("password"));
		jcrFileSetResource.setRootName(servletContext.getInitParameter("imageRootName"));
		contextPath = servletContext.getContextPath();
		jcrFileSetResource.initialize();
	}
	
	/**
	 * 
	 * This method will save one image to the jackrabbit repository.  It will return a URL that will 
	 * have the location where the file can be retrieved.
	 * 
	 * @param request - The actual request from the user that contains the image to be uploaded.
	 * 
	 * @return a String containing a URL where the file can be retreived.  If there is an error, then 
	 * an error message will be returned.
	 * 
	 */
	
	public String saveImage(HttpServletRequest request) {
		String responseString = "Not Done";
		ServletFileUpload servletFileUpload = new ServletFileUpload();
		FileItemIterator fit;
		long nextUUID = SimpleContentIdGenerator.getInstance().getNextUUID();
		String uuid = Long.toString(nextUUID);
		JSONObject jsonObj = null;
		
		try {
			fit = servletFileUpload.getItemIterator(request); 
			FileItemStream fis = null;
			int numberOfImages = 0;
			
			if(fit.hasNext()) {
				// get next FileItemStream object (Only one permitted).
				fis = fit.next();
				numberOfImages++;
				// Can only process one file.
				if (numberOfImages > 1) {
					responseString = "Only one image can be uploaded at a time";
				} else { 
					FileSetId fileSetId = new FileSetId(uuid, "1.0");
					jcrFileSetResource.createFileSet(fileSetId, "JPG FILE - " + uuid);
					jcrFileSetResource.storeImage(fileSetId, fis.openStream(), uuid);
				}
			}
			// Return an error if no file is sent.
			if (numberOfImages == 0) {
				responseString = "No images to upload";
			} else { 
				PropertiesResponse r = new PropertiesResponse();
				r.setProperty("contentId", String.valueOf(nextUUID));
				r.setProperty("contentURL", contextPath + "/rest/image/loadImage/" + uuid);
				
				jsonObj = JSONObject.fromObject(r);
				responseString = jsonObj.toString();
			} 
			 
		} catch(Exception ex) {
			logger.error("Error saving files: " + ex.getMessage(), ex);
			responseString = "Error saving files: " + ex.getMessage();
		}
		return responseString;
	
	}
	
	public byte[] loadImage(long contentId, HttpServletResponse response) {
		byte[] contents = null;
		
		try {
			String sContentId = Long.toString(contentId);
			FileSetId fileSetId = new FileSetId(sContentId, "1.0");
			InputStream is = jcrFileSetResource.retrieveImage(fileSetId, sContentId);
			contents = IOUtils.toByteArray(is);
		} catch(Exception ex) {
			String message = "Error loading file: " + contentId + " - " + ex.getMessage();
			logger.error(message, ex);
			try {
				response.sendError(_BadRequest, message);
			} catch (IOException ioe) {
				logger.error("Unable to respond back to client with an error " + ioe.getMessage());
			}
		}
		return contents;
	}
	
}
