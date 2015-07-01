package com.saic.rtws.commons.repository.tools;

/**
 * This class holds a list of files to retrieve
 * from the repository.
 */
public class Content {
	
	/** List of files to retrieve. */
	private String [] file;
	
	/**
	 * @return The list of files.
	 */
	public String [] getFile() {
		return this.file;
	}
	
	/**
	 * Sets the list of files.
	 * @param filesList Array of files list.
	 */
	public void setFile(final String [] filesList) {
		this.file = filesList;
	}
	
}