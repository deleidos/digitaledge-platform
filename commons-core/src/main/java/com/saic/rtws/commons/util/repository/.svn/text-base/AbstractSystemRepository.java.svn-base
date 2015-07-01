package com.saic.rtws.commons.util.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.saic.rtws.commons.util.Initializable;

public abstract class AbstractSystemRepository implements Initializable {
	
	public enum Visibility 
	{ 
		Public, Private;
		
		public static Visibility get(String vis) {
			if (Public.name().equalsIgnoreCase(vis)) {
				return Visibility.Public;
			}
			
			if (Private.name().equalsIgnoreCase(vis)) {
				return Visibility.Private;
			}
			
			return null;
		}
	};
	
	// System domain repository, not based on a tenant
	public final static String Systems = "systems";

	// Common visibility, provided for all tenants
	public final static String Common = "common";

	// Sets the user credentials for future operations
	public abstract void setCredentials(RepositoryCredentials credentials);

	// Creates the user's structures, and sets the credentials if successful
	public abstract void createUserStructures(RepositoryCredentials credentials, String userName);
	
	// Deletes the user's structures, and nulls the credentials
	public abstract void deleteUserStructures(RepositoryCredentials credentials, String userName);
	
	// Creates a path for the current user and specified visibility
	public abstract void createPath(Visibility visibility, String path);
	
	// Deletes a path for the current user and specified visibility
	public abstract void deletePath(Visibility visibility, String path);
	
	// Stores the data for the user, visibility, path and filename
	public abstract void store(Visibility visibility, String path, String filename, InputStream data);

	// Retrieves the data for the user, visibility, path, and filename.
	public abstract InputStream retrieve(Visibility visibility, String path, String filename, String userName);

	// Deletes the file for the user, visibility, path, and filename
	public abstract void delete(Visibility visibility, String path, String filename);

	// Lists the files for the user, visibility, path, and file pattern
	public abstract List<RepoFile> listFiles(Visibility visibility, String path, String filterPattern, String userName);
	
	/** 
	 * Convenience function for reading an input stream and converting it to a string.
	 * @param in Input stream
	 * @return String
	 * @throws IOException Could throw an IOException
	 */
	public static String inputStream2String(final InputStream in) throws IOException {
		final int bufferSize = 4096;
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[bufferSize];
	    for (int n; (n = in.read(b)) != -1; ) {
	        out.append(new String(b, 0, n));
	    }
	    in.close();
	    return out.toString();
	}
}
