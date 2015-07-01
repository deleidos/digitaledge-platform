package com.saic.rtws.commons.util.fileset;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.saic.rtws.commons.exception.InitializationException;
import com.saic.rtws.commons.util.Initializable;

public abstract class AbstractBaseFileSetResource implements Initializable {
	
	protected HashMap<String, String> patterns;

	protected String rootName = null;
	
	/** Must be set by the subclass prior to initialize() being called */
	protected String implName = null;

	/** Name of the file set plus description (if any) */
	public class FileSetEntry implements Comparable<FileSetEntry> {
		public String name;
		public String description;
		
		public int compareTo(FileSetEntry o) {
			return this.name.compareTo(o.name);
		}
	}
	
	/**
	 * Constructor.
	 */
	public AbstractBaseFileSetResource() {
	}
	
	/**
	 * Initialize the object.
	 */
    public void initialize() throws InitializationException {
    	if (implName == null) {
    		throw new InitializationException(
    			"implName is null - should be set by the implementing subclass");
    	}
		patterns = FilePatternRegistry.get().getPatterns(rootName, implName);    	
    }

    public void dispose() {
    	// Nothing to do
    }
    
    /** simple name identifying the type of fileSets stored (e.g., "models") */
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}
	public String getRootName() {
		return this.rootName;
	}
	
	/**
	 * Computes the filename to store the resource in the fileset with the given optional parameters.
	 * @param fileSet File set ID
	 * @param resourceName Resource name
	 * @param params Parameters
	 * @return the filename
	 */
	protected String computeFilename(FileSetId fileSet, String resourceName, String... params) {
		String result = patterns.get(resourceName);
		if ((result != null) && (result.contains("@"))) {
			result = result.replace("@FSNAME@", fileSet.getName());
			result = result.replace("@VERSION@", fileSet.getVersion().toString());
			for (int i = 0; i < params.length; i++) {
				String paramSpec = "@PARAM" + i + "@";
				result = result.replace(paramSpec, params[i]);
			}
		}
		if (result == null) {
			throw new FileSetResourceException(
				"Resource name '" + resourceName + "' has no filename patterns defined");
		}
		return result;
	}
	
	
	/** 
	 * Convenience function for reading an input stream and converting it to a string.
	 * @param in Input stream
	 * @return String
	 * @throws IOException Could throw an IOException
	 */
	public String inputStream2String(final InputStream in) throws IOException {
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
