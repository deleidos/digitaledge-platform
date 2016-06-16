package com.deleidos.rtws.webapp.contentapi.servlet.enunciate;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.deleidos.rtws.commons.util.fileset.FileSetId;
import com.deleidos.rtws.commons.util.fileset.JcrFileSetResourceWithCache;
import com.deleidos.rtws.commons.util.fileset.AbstractBaseFileSetResource.FileSetEntry;

/*
 * 
 */

@Path("/content")
public class ContentServiceImpl implements ContentService {

	/** Logger. */
	Logger logger = Logger.getLogger(ContentServiceImpl.class);
	/** Sends back error messages */
	private static final int _BadRequest = 400;
	/** JCR File Sets */
	private JcrFileSetResourceWithCache jcrFileSetResource;
	
	/**
	 * 
	 * Constructor that creates a new Content Service.  It will create a JCR File Set Resource and initialize it.
	 * 
	 * @param servletContext
	 */
	
	public ContentServiceImpl(@Context ServletContext servletContext) {
		jcrFileSetResource = new JcrFileSetResourceWithCache();
		
		jcrFileSetResource.setRepositoryUrl(servletContext.getInitParameter("repositoryURL"));
		jcrFileSetResource.setUserId(servletContext.getInitParameter("userId"));
		jcrFileSetResource.setPassword(servletContext.getInitParameter("password"));
		jcrFileSetResource.setRootName(servletContext.getInitParameter("rootName"));
		
		jcrFileSetResource.initialize();
	}
	
	/**
	 * 
	 * This method will return all the file sets available for a configured root name in JackRabbit.  A root name is specifies a node 
	 * that is one node below the root node.  
	 * 
	 * @param response - sends errors back to the user. 
	 * 
	 * @return - a map containing the FileSetEntry and a list of FileSetID
	 */
	
	public Map<FileSetEntry, List<FileSetId>> directory(HttpServletResponse response) {
		Map<FileSetEntry, List<FileSetId>> fileSetIds = new HashMap<FileSetEntry, List<FileSetId>>();
		
		try {
			Collection<FileSetEntry> fileSetEntries = jcrFileSetResource.listFileSets(null);
			for(FileSetEntry fileSetEntry : fileSetEntries) {
				String name = fileSetEntry.name;
				List<FileSetId> fileSetId = jcrFileSetResource.listFileSetVersions(name);
				fileSetIds.put(fileSetEntry, fileSetId);
			}
		} catch(Exception ex) {
			String message = "Error listing files: " + ex.getMessage();
			logger.error(message, ex);
			writeError(response, message, _BadRequest);
		}
		
		return fileSetIds;
	}
	
	/**
	 *
	 * Loads a file from the Jack Rabbit repository.  It will determine the file name from the model name, the version and the 
	 * reference name.  If the method is unable to load the file, it will send the user a 400 Http Error Message with an explanation
	 * of the problem.  The file will be sent back as an array of bytes so it can be reconstructed at the client. If there is an 
	 * error, there will be no array of bytes returned.
	 *
	 * @param model - name of the model.
	 * @param version - version of the file.
	 * @param referenceName - the type of the file to save.
	 * @param response - sends errors back to the user.
	 * 
	 * @return a byte array containing the file.
	 */
	
	public byte[] load(String model, String version, String referenceName, HttpServletResponse response) {
		byte[] contents = null;
		
		try {
			FileSetId fileSetId = new FileSetId(model, version);
			InputStream is = jcrFileSetResource.retrieve(fileSetId, referenceName);
			contents = IOUtils.toByteArray(is);
		} catch(Exception ex) {
			String message = "Error loading file: " + model + version + " - " + ex.getMessage();
			logger.error(message, ex);
			writeError(response, message, _BadRequest);
		}
		return contents;
	}
	
	/**
	 * 
	 * This method will save multiple files to the jack rabbit repository.  The files can be sent in multiple parts using the 
	 * multi-part form data.  The method will return a string containing the status of the save.
	 * 
	 * @param model - name of the model.
	 * @param version - version of the file.
	 * 
	 * @return A string with the status of the save operation.
	 */
	
	public String save(String model, String version, HttpServletRequest request) {
		String status = "Not Done";
		ServletFileUpload servletFileUpload = new ServletFileUpload();
		FileItemIterator fit;
		
		try {
			fit = servletFileUpload.getItemIterator(request); 
			FileItemStream fis = null;
			while (fit.hasNext()) {
				// get next FileItemStream object
				fis = fit.next();
				FileSetId fileSetId = new FileSetId(model, version);
				jcrFileSetResource.store(fileSetId, fis.openStream(), fis.getFieldName());
			}
			status = "Completed";
		} catch(Exception ex) {
			logger.error("Error saving files: " + ex.getMessage(), ex);
			status = "Error saving files: " + ex.getMessage();
		}
		return status;
	}
	
	/**
	 * 
	 * Sends an error back to the client.
	 * 
	 * @param response - the HttpServletResponse to set with an error message.
	 * @param message - the message to send back to the client.
	 * @param errorCode - the status code of the HTTP message.
	 */
	
	private void writeError(HttpServletResponse response, String message, int errorCode) {
		try {
			response.sendError(errorCode, message);
		} catch(IOException ioe) {
			logger.error("Unable to respond back to client with an error " + ioe.getMessage());
		}
	}
	
}
