package com.saic.rtws.commons.repository.tools;

/**
 * Stores the configuration for the ingest content downloader program.
 * 
 * The application uses xml binding to java and expects the configuration file
 * to be of the following form.
 * 
 * <?xml version="1.0" encoding="UTF-8" ?>
 *
 * <ingest-content-downloader 
 *	  xsi:type="java:com.saic.rtws.tools.repository.IngestContentDownloaderConfig" 
 *	  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
 *	
 *  <properties-file>/usr/local/rtws/properties/rtws-common.properties</properties-file>
 *
 *	<!--  
 *	    Retrieve all contents from the tenant's /public/<tenant>/services 
 *	    repository path and drop them in the /usr/local/rtws/ingest/lib directory.
 *	-->
 *	
 *	<directory xsi:type="java:com.saic.rtws.tools.repository.Directory">
 *       <visibility>public</visibility>
 *       <path>services</path>
 *       <destination>/usr/local/rtws/ingest/lib</destination>
 *	</directory>
 *   
 *   <!-- 
 *       Retrieve two scripts from the tenant's /public/<tenant>/scripts
 *       repository path and store them in the /usr/local/rtws/ingest/bin directory. 
 *   -->
 *   
 *   <directory xsi:type="java:com.saic.rtws.tools.repository.Directory">
 *       <visibility>public</visibility>
 *       <path>scripts</path>
 *       <content xsi:type="java:com.saic.rtws.tools.repository.Content">
 *          <file>scriptMethodInfo.groovy</file>
 *	        <file>exif.groovy</file>
 *       </content>
 *	    <destination>/usr/local/rtws/ingest/bin</destination>
 *	</directory>
 *      
 * </ingest-content-downloader>
 */
public class IngestContentDownloaderConfig {
	
	/** The absolute path of the properties file */
	private String propertiesFile;
	
	/** A list of tenant we are interested in retrieving contents. */
	private Directory [] directory;
	
	/**
	 * @return The absolute path of the properties file
	 */
	public String getPropertiesFile() {
		return this.propertiesFile;
	}
	
	/**
	 * Set the absolute path of the properties file.
	 */
	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}
	
	/**
	 * @return A list of tenant content definition.
	 */
	public Directory [] getDirectory() {
		return this.directory;
	}
	
	/**
	 * Set the list of tenant content definition.
	 */
	public void setDirectory(Directory [] directory) {
		this.directory = directory;
	}
	
}