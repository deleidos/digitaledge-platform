package com.saic.rtws.commons.util.fileset;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import com.saic.rtws.commons.exception.InitializationException;

/**
 * A wrapper class around the JcrFileSetResource providing logic
 * to cache remote session and reusing them when performing repository
 * operations.
 */
public class JcrFileSetResourceWithCache extends JcrFileSetResource {
	
	public static final String NAME = "jcr";
	
	private JcrSessionCache   sessionCache;
    private SimpleCredentials credentials;

	private String userId;
	private String password;
	private String repositoryUrl;
	
	public JcrFileSetResourceWithCache() {
		super.implName = NAME;
	}
	
	/** Repository URL */
	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}
	public String getRepositoryUrl() {
		return repositoryUrl;
	}
	
	/** User Id */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	
	/** Password */
	public void setPassword(String password) {
		this.password = password;
	}

    public void initialize() throws InitializationException {
    	super.initialize();
    	sessionCache = JcrSessionCache.get();
    	sessionCache.setRepositoryURL(repositoryUrl);
    	sessionCache.initialize();
        credentials = new SimpleCredentials(userId, password.toCharArray());
    }
    
    public void dispose() {
    	super.dispose();
    	sessionCache.dispose();
    }
    
	public void createFileSet(FileSetId fileSet, String description)
			throws FileSetResourceException {
		
		try {
			Session session = sessionCache.getSession(credentials);
			super.createFileSet(session, fileSet, description);
		} 
		catch (LoginException e) {
			throw new FileSetResourceException("createFileSet failed", e);
		} 
		catch (RepositoryException e) {
			throw new FileSetResourceException("createFileSet failed", e);
		}
		
	}

	public void deleteFileSet(FileSetId fileSet)
			throws FileSetResourceException {
		
		try {
			Session session = sessionCache.getSession(credentials);
			super.deleteFileSet(session, fileSet);
		}
		catch (LoginException e) {
			throw new FileSetResourceException("deleteFileSet failed", e);
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("deleteFileSet failed", e);
		}
		
	}

	public List<FileSetEntry> listFileSets(String filterPattern)
		throws FileSetResourceException {
		
		try {
			Session session = sessionCache.getSession(credentials);	
			return super.listFileSets(session, filterPattern);
		}
		catch (LoginException e) {
			throw new FileSetResourceException("listFileSets failed", e);
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("listFileSets failed", e);
	
		}
		
	}

	public List<FileSetId> listFileSetVersions(String fileSetName)
			throws FileSetResourceException {

		try {
			Session session = sessionCache.getSession(credentials);			
			return super.listFileSetVersions(session, fileSetName);
		}
		catch (LoginException e) {
			throw new FileSetResourceException("listFileSetVersions failed", e);
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("listFileSetVersions failed", e);
	
		}
		
	}
	
	public List<FileSetIdWithExtension> listFileSetVersionsAndExtension(String fileSetName)
		throws FileSetResourceException {
		
		try {
			Session session = sessionCache.getSession(credentials);
			return super.listFileSetVersionsAndExtension(session, fileSetName);
		} 
		catch (LoginException e) {
			throw new FileSetResourceException("listFileSetVersions failed", e);
		} 
		catch (RepositoryException e) {
			throw new FileSetResourceException("listFileSetVersions failed", e);
		}
		
	}

	
	public InputStream retrieve(FileSetId fileSet, String resourceName,
			String... params) throws FileSetResourceException {
		
		try {
			Session session = sessionCache.getSession(credentials);			
			return super.retrieve(session, fileSet, resourceName, params);
		}
		catch (LoginException e) {
			throw new FileSetResourceException("retrieve failed", e);
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("retrieve failed", e);
		}	
		
	}

	public InputStream retrieveImage(FileSetId fileSet, String imagename) {

		try {
			Session session = sessionCache.getSession(credentials);	
			return super.retrieveImage(session, fileSet, imagename);
		}
		catch (LoginException e) {
			throw new FileSetResourceException("retrieveImage failed", e);
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("retrieveImage failed", e);
	
		}		

	}
	
	public void store(FileSetId fileSet, InputStream data, String resourceName,
			String... params) throws FileSetResourceException {
		
		try {
			Session session = sessionCache.getSession(credentials);
			super.store(session, fileSet, data, resourceName, params);
		}
		catch (LoginException e) {
			throw new FileSetResourceException("store failed", e);
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("store failed", e);
		}
		
	}
	
	public void storeImage(FileSetId fileSet, InputStream data, String imagename)
		throws FileSetResourceException {
		
		try {
			Session session = sessionCache.getSession(credentials);	
			super.storeImage(session, fileSet, data, imagename);
		}
		catch (LoginException e) {
			throw new FileSetResourceException("storeImage failed", e);
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("storeImage failed", e);
		}
		
	}
	
	public static void main(String args[]) {
 		
		if (args.length != 3) {
			System.out.println("Parameters: <url> <userId> <password>");
			System.exit(1);
		}
		String url = args[0];
		String userId = args[1];
		String password = args[2];
		
		try {
			final String resName = "canonical";
			
			JcrFileSetResourceWithCache jcr = new JcrFileSetResourceWithCache();
			jcr.setRepositoryUrl(url);
			jcr.setUserId(userId);
			jcr.setPassword(password);
			jcr.setRootName("models");
			jcr.initialize();
			
			FileSetId sales1 = new FileSetId("testsales", "1.0");
			FileSetId sales2 = new FileSetId("testsales", "2.0");
			System.out.println("Delete file sets if they exist");
			jcr.deleteFileSet(sales1);
			jcr.deleteFileSet(sales2);

			System.out.println("Retrieve non-existant file set");
			try {
				@SuppressWarnings("unused")
				InputStream is = jcr.retrieve(sales1, resName);
			}
			catch (FileSetResourceException e) {
				System.out.println(e);
				System.out.println("Exception correctly raised.");
			}
			
			System.out.println("Create file sets...");
			jcr.createFileSet(sales1, "This is a data model based on the TPCH data set");
			jcr.createFileSet(sales2, null);
			
			System.out.println("Test listFileSets(null):");
			List<FileSetEntry> fileSetEntries = jcr.listFileSets(null);
			for (FileSetEntry e : fileSetEntries) {
				System.out.println("name=" + e.name + " desc=" + e.description );
			}
			
			System.out.println("Test listFileSetVersions(name):");
			List<FileSetId> fileSets = jcr.listFileSetVersions(sales1.getName());
			for (FileSetId fs : fileSets) {
				System.out.println(fs.toString());
			}
			
			System.out.println("Storing files...");
			ByteArrayInputStream is1 = new ByteArrayInputStream("Canonical 1.0".getBytes());
			jcr.store(sales1, is1, resName);
			
			ByteArrayInputStream is2 = new ByteArrayInputStream("Canonical 2.0".getBytes());
			jcr.store(sales2, is2, resName);
			
			System.out.println("Retrieving files...");
			InputStream is = jcr.retrieve(sales1, resName);
			System.out.println(jcr.inputStream2String(is));
			
			is = jcr.retrieve(sales2, resName);
			System.out.println(jcr.inputStream2String(is));
			
			System.out.println("Delete file sets");
			jcr.deleteFileSet(sales1);
			jcr.deleteFileSet(sales2); 
		}
		catch (Exception e) {
			e.printStackTrace();
		} 		
	}
}
