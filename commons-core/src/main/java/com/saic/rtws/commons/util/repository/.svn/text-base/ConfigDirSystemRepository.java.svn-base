package com.saic.rtws.commons.util.repository;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigDirSystemRepository extends AbstractSystemRepository {

	private File configPath = null;
	
	/** Configuration Path */
	public void setConfigPath(String configPath) {
		this.configPath = new File(configPath);
	}
	public String getConfigPath() {
		return this.configPath.getPath();
	}
	
	public void initialize() { }
	public void dispose() { }
    
	@Override
	public void setCredentials(RepositoryCredentials credentials) {
		// No credentials for config file based repositories
	}

	@Override
	public void createUserStructures(RepositoryCredentials credentials, String userName) {
		// No users for config based repositories
	}

	@Override
	public void deleteUserStructures(RepositoryCredentials credentials, String userName) {
		// No users for config based repositories
	}

	@Override
	public void createPath(Visibility visibility, String path) {
		File pathFile = new File(configPath, path);
		if (!pathFile.exists()) {
			if (!pathFile.mkdir()) {
				throw new SystemRepositoryException("createPath failed for " + pathFile.getPath());
			}
		}
	}

	@Override
	public void deletePath(Visibility visibility, String path) {
		File pathFile = new File(configPath, path);
		if (pathFile.exists()) {
			if (!pathFile.delete()) {
				throw new SystemRepositoryException("deletePath failed for " + pathFile.getPath());
			}
		}
	}

	@Override
	public void store(Visibility visibility, String path, String filename, InputStream data) {
		try {
			File pathFile = new File(configPath, path);
			File file = new File(pathFile, filename);
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			int c;
			while ((c = data.read()) != -1) {
			    out.write(c);
			}
			data.close();
		    out.close();
		}
		catch (IOException e) {
			throw new SystemRepositoryException("Cannot store " + filename, e);
		}
	}

	@Override
	public InputStream retrieve(Visibility visibility, String path, String filename, String userName) {
		InputStream result = null;
		File pathFile = new File(configPath, path);
		File file = new File(pathFile, filename);
		try {
			result = new FileInputStream(file); 
		}
		catch (FileNotFoundException e) {
			throw new SystemRepositoryException("Cannot retrieve " + file.getPath(), e);
		}
		return result;
	}

	@Override
	public void delete(Visibility visibility, String path, String filename) {
		File pathFile = new File(configPath, path);
		File file = new File(pathFile, filename);
		if (!file.delete()) {
			throw new SystemRepositoryException("delete failed for " + file.getPath());			
		}
	}

	@Override
	public List<RepoFile> listFiles(Visibility visibility, String path, final String filterPattern, String userName) {
		
		// Assume the first pattern has the file set name in it
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if ((filterPattern == null) || (name.contains(filterPattern))) {
					return true;
				}
				return false;
			}
		};

		
		File pathFile = new File(configPath, path);
		File[] files = pathFile.listFiles(filter);

		ArrayList<RepoFile> result = new ArrayList<RepoFile>();
		for (File currFile : files) {
			RepoFile currRepoFile = new RepoFile();
			currRepoFile.setName(currFile.getName());
			currRepoFile.setDirectory(currFile.isDirectory());
			result.add(currRepoFile);
		}
		
		Collections.sort(result, new RepoFileComparator());
		
		return result;
	}

}
