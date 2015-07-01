package com.saic.rtws.commons.webapp.rest;

/**
 * Utility class to assist with format of ReST Response.
 */
public class RestResponseFormat {
	
	/** JSON format. */
	public static final String JSON = "json";
	/** XML format. */
	public static final String XML  = "xml";
	
	/** Constructor. */
	protected RestResponseFormat() { }
	
	/**
	 * Determine the format "Content-Type". Default is empty string.
	 * @param format Format to base content type on
	 * @return Return "Content-Type".
	 */
	public static String getFormatContentType(final String format) {
		
		if (JSON.equals(format)) {
			return "text/json";
		} else if (XML.equals(format)) {
			return "text/xml";
		}
		
		return "";
	}
}
