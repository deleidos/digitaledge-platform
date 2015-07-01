package com.saic.rtws.commons.util.fileset;

import java.io.InputStream;
import java.util.List;

import javax.jcr.Session;

public abstract class AbstractJcrFileSetResource extends AbstractBaseFileSetResource {
	
	/**
	 * Constructor.
	 */
	public AbstractJcrFileSetResource() {
	}
	
	/**
	 * Create the fileset.
	 * Possible Errors: File set already exists, I/O error
	 * @param session the repository session
	 * @param fileSet the identity of the file set
	 */
	public abstract void createFileSet(Session session, FileSetId fileSet, String description)
		throws FileSetResourceException;
	
	/**
	 * Delete the fileset (only the secific version identified in the file set).
	 * Does not raise an error if the file set is missing.
	 * Possible Errors: I/O error
	 * @param session the repository session
	 * @param fileSet the identity of the file set
	 */
	public abstract void deleteFileSet(Session session, FileSetId fileSet) throws FileSetResourceException;

	/**
	 * Store a resource within the fileset. If the file exists, it will be overwritten.
	 * Possible Errors: File set does not exist, I/O error
	 * @param session 	   the repository session
	 * @param fileSet      the identity of the file set
	 * @param data         stream of data to store
	 * @param resourceName name of the resource
	 * @param params       additional parameters if the resource requires it
	 */
	public abstract void store(Session session, FileSetId fileSet, InputStream data, String resourceName, String... params)
		throws FileSetResourceException;
	
	/**
	 * Store an image within the fileset. If the image exists, it will be overwritten.
	 * Possible Errors: File set does not exist, I/O error
	 * @param session the repository session
	 * @param fileSet 		the identity of the file set
	 * @param data			stream of data to store
	 * @param imagename		name of the image
	 * @throws FileSetResourceException
	 */
	
	public abstract void storeImage(Session session, FileSetId fileSet, InputStream data, String imagename)
		throws FileSetResourceException;
	
	
	/**
	 * Retrieve a file from the fileset.  
	 * Possible Errors: File set does not exist, I/O error
	 * @param session the repository session
	 * @param fileSet the identity of the file set
	 * @param resourceName  name of the resource
	 * @param params        additional parameters if the resource requires it
	 * @return InputStream if the file exists, null otherwise
	 */
	public abstract InputStream retrieve(Session session, FileSetId fileSet, String resourceName, String... params)
		throws FileSetResourceException;
	
	/**
	 * Retrieve an image from the fileset.
	 * Possible Errors: File set does not exist, I/O error
	 * @param session     the repository session
	 * @param fileSet     the identity of the file set
	 * @param imagename   The name of the image. 
	 * @return			  InputStream if the file exists, null otherwise
	 */
	public abstract InputStream retrieveImage(Session session, FileSetId fileSet, String imagename);
	
	/**
	 * Return the file set entries available that contain a pattern, sorted by name. 
	 * Possible Errors: File set does not exist, I/O error
	 * @param session the repository session
	 * @param filterPattern only returns those file sets that contain the pattern string, null = all
	 * @return an array of file set entries
	 */
	public abstract List<FileSetEntry> listFileSets(Session session, String filterPattern)
		throws FileSetResourceException;

	/**
	 * Returns all the FileSets (meaning all versions) for the given name, sorted by version number.
	 * @param session the repository session
	 * @param fileSetName name of the file set for which all versions are returned
	 * @return list of file set ids
	 */
	public abstract List<FileSetId> listFileSetVersions(Session session, String fileSetName)
		throws FileSetResourceException;
	
	/**
	 * Returns all the FileSets (meaning all versions and its extension) for the given name, sorted by name, version number, and extension.
	 * @param session the repository session
	 * @param fileSetName name of the file set for which all versions are returned
	 * @return list of file set ids
	 */
	public abstract List<FileSetIdWithExtension> listFileSetVersionsAndExtension(Session session, String fileSetName)
			throws FileSetResourceException;
	
}
