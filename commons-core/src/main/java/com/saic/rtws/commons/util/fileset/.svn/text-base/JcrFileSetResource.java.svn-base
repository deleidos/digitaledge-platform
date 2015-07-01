package com.saic.rtws.commons.util.fileset;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

/**
 * Implements the AbstractFileSetResource using a Java Content Repository (JCR). To setup the class,
 * instantiate an instance and call setRepositoryUrl, setUserId, and setPassword. Then call
 * initialize. At this point you can call the other methods. Note that the session to the JCR
 * repository will not be established until it is needed, i.e., the first call is made.
 */
public class JcrFileSetResource extends AbstractJcrFileSetResource {
	
	public static final String NAME = "jcr";
	
	public JcrFileSetResource() {
		super.implName = NAME;
	}
    
	@Override
	public void createFileSet(Session session, FileSetId fileSet, String description)
			throws FileSetResourceException {

		try {		
			synchronized (session) {
				Node root = session.getRootNode(); // JCR root
				
				// File Set root - add it if it does not exist
				
				Node parent = null;
				if (rootName.equals("/")) {
					parent = root;
				}
				else if (root.hasNode(rootName)) {
					parent = root.getNode(rootName);
				}
				else {
		        	parent = root.addNode(rootName);
		        }
		        
		        // Add the file set name if it does not already exist
				Node fileSetNode = null;
				if (parent.hasNode(fileSet.getName())) {
			        fileSetNode = parent.getNode(fileSet.getName());
				}
				else {
			        fileSetNode = parent.addNode(fileSet.getName());
		        }

		        if (description != null) {
			        // Add a description property to the file set node
			        fileSetNode.setProperty("jcr:description", description);
		        }
		        
		        // Now add the version node if it does not exist
		        if ( ! fileSetNode.hasNode(fileSet.getVersion().toString())) {
		        	fileSetNode.addNode(fileSet.getVersion().toString());
		        }
		        
		        session.save();
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("createFileSet failed", e);
		}
	}

	@Override
	public void deleteFileSet(Session session, FileSetId fileSet)
			throws FileSetResourceException {
		
		try {
			synchronized (session) {			
				Node root = session.getRootNode();
				
				Node parent = null;
				if (rootName.equals("/")) {
					parent = root;
				} else if (root.hasNode(rootName)) {
					parent = root.getNode(rootName);
				}
				
				if (parent.hasNode(fileSet.getName())) {
			        Node fileSetNode = parent.getNode(fileSet.getName());

			        if (fileSetNode.hasNode(fileSet.getVersion().toString())) {
			        	Node versionNode = fileSetNode.getNode(fileSet.getVersion().toString());
		        		versionNode.remove();							
					}
			        	
					// If there are no versions of this file set left, delete the fileSetNode
			        boolean doesVersionNodeExist = false;
					NodeIterator ni = fileSetNode.getNodes();
					while(ni.hasNext()) {
						Node node = ni.nextNode();
						Matcher m = FileSetId.VERSION_PATTERN.matcher(node.getName());
						if (m.find()) {
							doesVersionNodeExist = true;
						}
					}
					
					if (! doesVersionNodeExist) {
						fileSetNode.remove();
					}
					
					session.save();
				}
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("deleteFileSet failed", e);
		}
	}

	/** Convenience method to return the file set parent node */
	private Node getParentNode(Session session) throws RepositoryException {
		Node root = session.getRootNode();
		if ( ! root.hasNode(rootName)) {
        	throw new FileSetResourceException("Root path '" + rootName + "' does not exist"); 			
		}
        return (root.getNode(rootName));
	}
	
	/** Convenience method to return the parent node for all the versions of a file set */
	private Node getFileSetNode(Node parent, String name) throws RepositoryException {
		if ( ! parent.hasNode(name)) {
        	throw new FileSetResourceException("File set " + name + " does not exist");			
		}
        return (parent.getNode(name));
	}

	/** Convenience method to return the node for a specific file set (name and version) */
	private Node getFileSetVersionNode(Session session, FileSetId fileSet) throws RepositoryException {
        Node parent = getParentNode(session);
        Node fileSetNode = getFileSetNode(parent, fileSet.getName());
        if ( ! fileSetNode.hasNode(fileSet.getVersion().toString())) {
        	throw new FileSetResourceException(
            		"Version " + fileSet.getVersion() + " of file set " + fileSet.getName() + " does not exist");        	
        }
        return (fileSetNode.getNode(fileSet.getVersion().toString()));
	}

	@Override
	public List<FileSetEntry> listFileSets(Session session, String filterPattern)
		throws FileSetResourceException {
		
		ArrayList<FileSetEntry> result = new ArrayList<FileSetEntry>();
		
		try {		
			synchronized (session) {
				Node parentNode = getParentNode(session);
				NodeIterator ni = parentNode.getNodes();
				while (ni.hasNext()) {
					Node n = ni.nextNode();
					String name = n.getName();
					
					// Don't return version node because they don't
					// have the jcr:description property
					
		        	Matcher m = FileSetId.VERSION_PATTERN.matcher(name);
					if (! m.find()) {
						if ((filterPattern == null) || (name.contains(filterPattern))) {
							FileSetEntry entry = new FileSetEntry();
							entry.name = name;
							
							try {
								Property property = n.getProperty("jcr:description");
								entry.description = property.getString();
							} catch (PathNotFoundException pnfe) {
								entry.description = null;
							}
							
							result.add(entry);
						}
					}
				}
				Collections.sort(result);
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("listFileSets failed", e);
		}
		
		return result;
	}

	@Override
	public List<FileSetId> listFileSetVersions(Session session, String fileSetName)
			throws FileSetResourceException {
		
		ArrayList<FileSetId> result = new ArrayList<FileSetId>();
		
		try {		
			synchronized (session) {
				Node parentNode = getParentNode(session);
		        Node fileSetNode = getFileSetNode(parentNode, fileSetName);
		        NodeIterator ni = fileSetNode.getNodes();
		        while (ni.hasNext()) {
		        	Node n = ni.nextNode();
		        	
		        	Matcher m = FileSetId.VERSION_PATTERN.matcher(n.getName());
					if (m.find()) {
			        	result.add(new FileSetId(fileSetName, n.getName()));
					}
		        }
				Collections.sort(result);
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("listFileSetVersions failed", e);
		}
		
		return result;
	}
	
	@Override
	public List<FileSetIdWithExtension> listFileSetVersionsAndExtension(Session session, String fileSetName)
			throws FileSetResourceException {
		
		ArrayList<FileSetIdWithExtension> result = new ArrayList<FileSetIdWithExtension>();
		
		try {		
			synchronized (session) {
				Node parentNode = getParentNode(session);
		        Node fileSetNode = getFileSetNode(parentNode, fileSetName);
		        NodeIterator ni = fileSetNode.getNodes();
		        
		        while (ni.hasNext()) {
		        	Node n = ni.nextNode();
		        	Matcher m = FileSetId.VERSION_PATTERN.matcher(n.getName());
		        	
					if (m.find()) {
						String version = n.getName();
						NodeIterator ni2 = n.getNodes();
						
						while (ni2.hasNext()) {
							Node n2 = ni2.nextNode();
							
							if (n2.getName().contains(fileSetName) && n2.getName().contains(version)) {
								String extension =  n2.getName().substring(n2.getName().lastIndexOf(".") + 1);
								result.add(new FileSetIdWithExtension(fileSetName, version, extension));
							}
						}
					}
		        }
		        
				Collections.sort(result);
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("listFileSetVersions failed", e);
		}
		
		return result;
	}
	
	@Override
	public InputStream retrieve(Session session, FileSetId fileSet, String resourceName,
			String... params) throws FileSetResourceException {
		
		InputStream result = null;
		String filename = computeFilename(fileSet, resourceName, params);
		try {		
			synchronized (session) {
				Node fileSetVersionNode = getFileSetVersionNode(session, fileSet);
				Node fileNode = fileSetVersionNode.getNode(filename);
		        Node contentNode = fileNode.getNode("jcr:content");
		        javax.jcr.Property prop = contentNode.getProperty("jcr:data");
		        Binary contents = prop.getBinary();
		        result = contents.getStream();
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("getDescription failed", e);
	
		}		
		return result;
	}

	@Override
	public InputStream retrieveImage(Session session, FileSetId fileSet, String imagename) {
		InputStream result = null;
		try {		
			synchronized (session) {
				Node parent = getParentNode(session);
				System.out.println("parent Node Name: " + parent.getName());
				Node fileNode =  getFileSetNode(parent, fileSet.getName());
				System.out.println("file Node Name: " + fileNode.getName());
				Node contentNode = fileNode.getNode("jcr:content");
		        System.out.println("Content Node Name: " + contentNode.getName());
		        javax.jcr.PropertyIterator pi = contentNode.getProperties();
		        while(pi.hasNext()) {
		        	javax.jcr.Property property = pi.nextProperty();
		        	System.out.println(property.getName() + " " + property.getValue());
		        }
		        javax.jcr.Property prop = contentNode.getProperty("jcr:data");
		        Binary contents = prop.getBinary();
		        result = contents.getStream();
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("Failed to retreive image: " + e.getMessage(), e);
	
		}		
		
		return result;
	}
	
	@Override
	public void store(Session session, FileSetId fileSet, InputStream data, String resourceName,
			String... params) throws FileSetResourceException {
		
		String filename = computeFilename(fileSet, resourceName, params);
		
		try {
			ValueFactory valueFactory = session.getValueFactory();
			synchronized (session) {
				Node fileSetVersionNode = getFileSetVersionNode(session, fileSet);
				if (fileSetVersionNode.hasNode(filename)) {
					fileSetVersionNode.getNode(filename).remove();
				}
		        Node fileNode = fileSetVersionNode.addNode(filename, "nt:file");
		        Node contentNode = fileNode.addNode("jcr:content", "nt:resource");
		        Binary contents = valueFactory.createBinary(data);
		        contentNode.setProperty("jcr:data", contents);
		        Calendar lastModified = Calendar.getInstance();
		        lastModified.setTimeInMillis(System.currentTimeMillis());
		        contentNode.setProperty("jcr:lastModified", lastModified);
		        session.save();
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("store failed", e);
		}
	}
	
	@Override
	public void storeImage(Session session, FileSetId fileSet, InputStream data, String imagename)
		throws FileSetResourceException {
		try {
			ValueFactory valueFactory = session.getValueFactory();
			synchronized (session) {
				Node parent = getParentNode(session);
				Node fileSetNode = getFileSetNode(parent, fileSet.getName());
				if (fileSetNode.hasNode(imagename)) {
					fileSetNode.getNode(imagename).remove();
				}
		        //Node fileNode = fileSetNode.addNode(imagename, "nt:file");
		        Node contentNode = fileSetNode.addNode("jcr:content", "nt:resource");
		        System.out.println("Path: " + fileSetNode.getPath());
		        Binary contents = valueFactory.createBinary(data);
		        contentNode.setProperty("jcr:data", contents);
		        Calendar lastModified = Calendar.getInstance();
		        lastModified.setTimeInMillis(System.currentTimeMillis());
		        contentNode.setProperty("jcr:lastModified", lastModified);
		        session.save();
			}
		}
		catch (RepositoryException e) {
			throw new FileSetResourceException("storeImage failed", e);
	
		}
	}
}
