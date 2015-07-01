package com.saic.rtws.commons.repository.tools;


/**
 * This class defines a particular directory in the repository
 * to retrieve contents.
 */
public class Directory {
	
	/** The public/private area of the tenant. */
	private String visibility;
	
	/** Relative path starting from the user home path. */
	private String path;
	
	/** Holds the content to retrieve */
	private Content content;
	
	/** The destination of the retrieved content. */
	private String destination;
	
	/**
	 * @return The visibility of the repository.
	 */
	public String getVisibility() {
		return this.visibility;
	}
	
	/**
	 * Sets the visibility of the repository.
	 */
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * @return The relative path from the user's home path.
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Sets the relative path from the user's home path.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @return The content.
	 */
	public Content getContent() {
		return this.content;
	}
	
	/**
	 * Sets the content.
	 */
	public void setContent(Content content) {
		this.content = content;
	}
	
	/**
	 * @return The destination.
	 */
	public String getDestination() {
		return this.destination;
	}
	
	/**
	 * Sets the destination.
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
}