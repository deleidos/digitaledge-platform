package com.deleidos.rtws.webapp.contentapi.dao;

import com.deleidos.rtws.commons.config.PropertiesUtils;
import com.deleidos.rtws.commons.config.autogen.RTWSCommonProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

/**
 * DAO for accessing a simple content storage system.
 */
public class SimpleContentStorageDAO extends com.deleidos.rtws.commons.dao.BaseRestDAO {
	
	/** Base path for content storage. */
	// private String basePath = "/usr/local/rtws/data";
	private String basePath;
	
	// Number of directory buckets. This value is used to compute the subdirectory
	// where the file is stored as a modulo of the random id. A value of 101 keeps
	// the total number of files less than 10000 for 1M files. This value cannot
	// be changed on an existing store unless the store is empty or rebalanced.
	private long numDirBuckets = 101; 
	
	/** 
	 * Constructor.
	 * @param userId ID of user 
	 */
	public SimpleContentStorageDAO(final int userId) { 
		super(userId);
		try {
			this.basePath = RTWSCommonProperties.getWebappContentapiStoreDir();
			// this.basePath = "/usr/local/rtws/data";
		}
		catch (Exception e) {
			this.basePath = null;
		}
	}
	
	/**
	 * Getter for the base path.
	 * @return Returns the base path
	 */
	public final String getBasePath() {
		return basePath;
	}
	
	/**
	 * Setter for the base path.
	 * @param path Base path
	 */
	public final void setBasePath(final String path) {
		basePath = path;
	}
	
	/**
	 * Helper function to create a path string to the directory of the content.
	 * @param contentId ID of content
	 * @return Path to content directory
	 */
	public final String getContentDir(final long contentId) {
		StringBuffer buf = new StringBuffer(basePath);
		long subdir_id = contentId % numDirBuckets;
		buf.append("/" + subdir_id);
		return buf.toString();
	}
	
	/**
	 * Helper function to determine the path to the content object.
	 * @param contentId ID of content
	 * @return Returns path to content object
	 */
	public final String getContentObjectPath(final long contentId) {
		return getContentDir(contentId) + "/" + contentId;
	}
	
	/**
	 * Opens InputStream to object of content.
	 * @param contentId ID of content
	 * @return Returns input stream
	 * @throws FileNotFoundException FileNotFoundException could be thrown
	 */
	public final InputStream getContentObjectStream(final long contentId) throws FileNotFoundException {
		String path = getContentObjectPath(contentId);
		return new FileInputStream(new File(path));
	}
	
	/**
	 * Saves content object to the file system and returns a content id.
	 * @param fileName Name of file
	 * @param contentType Content type of file
	 * @param is InputStream containing content of file
	 * @return Returns assigned id of content
	 * @throws java.io.IOException Could thrown an IOException
	 */
	public final long saveContentObject(final String fileName, final String contentType, final InputStream is) throws java.io.IOException {
		long contentId = getNextContentId();
		saveContentObject(contentId, fileName, contentType, is);
		return contentId;
	}
	
	/**
	 * Saves content object to the file system.
	 * @param contentId ID of content
	 * @param fileName Name of file
	 * @param contentType Content type of file
	 * @param is InputStream containing content of file
	 * @throws java.io.IOException Could thrown an IOException
	 */
	public final void saveContentObject(final long contentId, final String fileName, final String contentType, final InputStream is) 
	throws java.io.IOException 
	{
		String path = getContentDir(contentId);
		File f = new File(path);
		
		// make sure directory exists 
		if (!f.exists()) {
			f.mkdirs();
		}
		
		/* Save content object */
		
		path = getContentObjectPath(contentId);
		f = new File(path);
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
			IOUtils.copyLarge(is, os);
		} finally {
			try { os.close(); } catch (Exception e) { }
		}
		
		/* Save content object properties */
		
		Properties p = new Properties();
		p.put("Filename", fileName);
		p.put("Content-Type", contentType);
		
		saveContentProperties(contentId, p);
	}
	
	/**
	 * Return a Properties object for the content.
	 * @param contentId ID of content
	 * @return Returns Properties object
	 * @throws IOException 
	 */
	public final Properties getContentProperties(final long contentId) throws IOException {
		String path = getContentPropertiesPath(contentId);
		return PropertiesUtils.loadProperties(path);
	}
	
	/**
	 * Return a path to the content properties file.
	 * @param contentId ID of content
	 * @return Path string
	 */
	public final String getContentPropertiesPath(final long contentId) {
		return getContentDir(contentId) + "/" + contentId + ".properties";
	}
	
	/**
	 * Save the "content" properties object.
	 * @param contentId ID of content
	 * @param props Properties object
	 * @throws IOException IOException could be thrown
	 */
	public final void saveContentProperties(final long contentId, final Properties props) throws IOException {
		String path = getContentPropertiesPath(contentId);
		OutputStream os = null;
		
		try {
			os = new FileOutputStream(new File(path));
			props.store(os, "");
		} finally {
			try { os.close(); } catch (Exception e) { }
		}
	}
	
	/**
	 * Get the next content id.
	 * @return Returns a long value for content id.
	 */
	public final long getNextContentId() {
		return SimpleContentIdGenerator.getInstance().getNextUUID();
	}
}
