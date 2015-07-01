package com.saic.rtws.commons.util.fileset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements a file set resource for a flat configuration directory. The anchor
 * is the configuration directory path. Does not support write operations such as
 * delete and store.
 *
 */
public class ConfigFileSetResource extends AbstractFileSetResource {
	
	public static final String NAME = "config";
	
	private File configPath = null;
	
	public ConfigFileSetResource() {
		super.implName = NAME;
	}
	
	/** Configuration Path */
	public void setConfigPath(String configPath) {
		this.configPath = new File(configPath);
	}
	public String getConfigPath() {
		return this.configPath.getPath();
	}
    
	public void createFileSet(FileSetId fileSet, String description)
			throws FileSetResourceException {
		return; // Do nothing - the configuration path is flat and descriptions are not supported.
	}

	public void deleteFileSet(FileSetId fileSet)
			throws FileSetResourceException {
		throw new FileSetResourceException("deleteFileSet not supported");
	}

	@Override
	public List<FileSetEntry> listFileSets(final String filterPattern)
		throws FileSetResourceException {
		
		List<String> regexList = FilePatternRegistry.get().fileRegexPatterns(patterns);
		
		// Assume the first pattern has the file set name in it
		final String regex = regexList.get(0);
		
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.matches(regex)) {
					if ((filterPattern == null) || (name.contains(filterPattern))) {
						return true;
					}
				}
				return false;
			}
		};

		String filenames[] = configPath.list(filter);
		ArrayList<FileSetEntry> result = new ArrayList<FileSetEntry>(); 
		for (String filename : filenames) {
			FileSetEntry entry = new FileSetEntry();
			entry.name = filename;
			entry.description = "";
			result.add(entry);
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	@Override
	public List<FileSetId> listFileSetVersions(String fileSetName)
		throws FileSetResourceException {
		
		throw new FileSetResourceException("listFileSetVersions not supported");
	}

	@Override
	public InputStream retrieve(FileSetId fileSet, String resourceName, String... params)
			throws FileSetResourceException {
		
		InputStream result = null;
		String filename = computeFilename(fileSet, resourceName, params);
		File file = new File(configPath, filename);
		try {
			result = new FileInputStream(file); 
		}
		catch (FileNotFoundException e) {
			throw new FileSetResourceException("File \"" + filename + "\" not found in config path: " + configPath.getAbsolutePath(), e);
		}
		return result;
	}

	@Override
	public InputStream retrieveImage(FileSetId fileSet, String imagename)
		throws FileSetResourceException {
		throw new FileSetResourceException("retrieve image not supported");
	}
	
	@Override
	public void store(FileSetId fileSet, InputStream data, String resourceName, String... params)
		throws FileSetResourceException {
		throw new FileSetResourceException("store not supported");
	}
	
	@Override
	public void storeImage(FileSetId fileSet, InputStream data, String imagename) 
		throws FileSetResourceException {
			throw new FileSetResourceException("store image not supported");
	}
}
