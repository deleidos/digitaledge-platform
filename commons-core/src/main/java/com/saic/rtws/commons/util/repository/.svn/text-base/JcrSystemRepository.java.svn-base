package com.saic.rtws.commons.util.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.nodetype.NodeType;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.core.security.principal.EveryonePrincipal;

public class JcrSystemRepository extends AbstractSystemRepository {
	
	/**
	 * Stores the access control entry. The entry contains the
	 * principal (user/group), the privileges, and a flag
	 * indicating if the privileges are to be allowed or denied.
	 */
	public static class ACLEntry {
		
		public Principal principal;
		public String [] privileges;
		public boolean isAllow;

		public ACLEntry(Principal principal, String [] privileges, boolean isAllow) {
			this.principal = principal;
			this.privileges = privileges;
			this.isAllow = isAllow;
		}
		
	}
	
	private RepositoryCredentials credentials = null;
	private String username = null;
	private String password = null;

	/**
	 * Set the repository URL.
	 * @param url
	 */
	public void setRepositoryUrl(String url) {
		JcrSessionCache.get().setRepositoryURL(url);
	}
	
	/**
	 * Set the repository.
	 * @param repository
	 */
	public void setRepository(Repository repository) {
		JcrSessionCache.get().setRepository(repository);
	}

	/** Set username */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/** Set password */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Initialize access to the repository.
	 */
	public void initialize() {
		// If username and password are set, override the credentials
		if ((username != null) && (password != null)) {
			this.credentials = new RepositoryCredentials(username, password);
		}
		JcrSessionCache.get().initialize();
	}
	
	public void dispose() {
		// Nothing to do
	}

	/**
	 * Set the current access credentials.
	 */
	@Override
	public void setCredentials(RepositoryCredentials credentials) {
		
		this.credentials = credentials;
	}

	public boolean commonExists() {
		try {
			Node commonNode = getCommonNode();

			return (commonNode != null);
		}
		catch (RepositoryException e) {
			// fall through and return false;
		}

		return false;
	}

	public Node getCommonNode()
			throws RepositoryException {
		Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

		synchronized (session) {

			Node root = session.getRootNode(); // JCR root

			String commonPath = Common;
			Node commonNode = root.getNode(commonPath);
			if (commonNode == null) {
				throw new RepositoryException("Common content data path does not exist");
			}

			return commonNode;
		}
	}

	/**
	 * Creates the common content area.
	 */
	public void createCommonStructure(RepositoryCredentials credentials)
	{
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {

				Node root = session.getRootNode(); // JCR root

				// Ensure the common node is there
				if (!root.hasNode(Common)) {
					root.addNode(Common);
					session.save();
				}

				this.credentials = credentials;
			}
		}
		catch (RepositoryException e) {
			credentials = null;
			throw new SystemRepositoryException("createCommonStructure failed", e);
		}
	}

	/**
	 * Creates the user structures for a given user specified by the passed credentials.
	 */
	public void createUserStructures(RepositoryCredentials credentials, String userName)
	{
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				
				Node root = session.getRootNode(); // JCR root
				
				// Add user to each visibility node
				for (Visibility v : Visibility.values()) {
					// Ensure the visibility node is there first
					Node visNode = null;
					String visName = v.toString().toLowerCase();
					if (root.hasNode(visName)) {
						visNode = root.getNode(visName);
					}
					else {
						visNode = root.addNode(visName);
				        session.save();
					}					
					
					// Now add a user node under the visibility node

					if (!visNode.hasNode(userName)) {
						visNode.addNode(userName);
				        session.save();
					}
				}
						        
				this.credentials = credentials;
			}
		}
		catch (RepositoryException e) {
			credentials = null;
			throw new SystemRepositoryException("createUserStructures failed", e);
		}				
	}

	/**
	 * Creates the systems structures for a given domain specified by the passed credentials.
	 */
	public void createSystemDomainStructures(RepositoryCredentials credentials, String domainName)
	{
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {

				Node root = session.getRootNode(); // JCR root

				// Ensure the systems node is there first
				Node sysNode = null;
				if (root.hasNode(Systems)) {
					sysNode = root.getNode(Systems);
				}
				else {
					sysNode = root.addNode(Systems);
					session.save();

					// set ACL on the Systems node
					String [] allPrivilege = { Privilege.JCR_ALL };
					List<ACLEntry> aclEntries = new ArrayList<ACLEntry>();
					aclEntries.add(new ACLEntry(EveryonePrincipal.getInstance(), allPrivilege, true));
					setAccessControlPolicy(sysNode, aclEntries);
				}

				// Now add a domain node under the systems node
				if (!sysNode.hasNode(domainName)) {
					sysNode.addNode(domainName);
					session.save();
				}

				this.credentials = credentials;
			}
		}
		catch (RepositoryException e) {
			credentials = null;
			throw new SystemRepositoryException("createSystemDomainStructures failed", e);
		}
	}

	/**
	 * Removes all user data from the repository.
	 */
	public void deleteUserStructures(RepositoryCredentials credentials, String userName)
	{
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				
				Node root = session.getRootNode(); // JCR root
				
				// Delete user from each visibility node
				for (Visibility v : Visibility.values()) {
					String visName = v.toString().toLowerCase();
					if (root.hasNode(visName)) {
						Node visNode = root.getNode(visName);
						if (visNode.hasNode(userName)) {
							Node userNode = visNode.getNode(userName);
							userNode.remove();
						}
					}
				}
				
		        session.save();
		        
		        this.credentials = null;
			}
		}
		catch (RepositoryException e) {
			this.credentials = null;
			throw new SystemRepositoryException("deleteUserStructures failed", e);
		}		
	}
	
	/**
	 * Removes all system data from the repository for one domain.  If path is not null, then just remove
	 * the path node and all of its children.
	 */
	public void deleteSystemDomainStructures(RepositoryCredentials credentials, String domainName, String path)
	{
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {

				Node root = session.getRootNode(); // JCR root

				// Delete domain from systems node
				if (root.hasNode(Systems)) {
					Node sysNode = root.getNode(Systems);
					if (sysNode.hasNode(domainName)) {
						Node domainNode = sysNode.getNode(domainName);
						Node pathNode = domainNode;
						if (StringUtils.isNotBlank(path)) {
							pathNode = domainNode.getNode(path);
						}
						
						pathNode.remove();
					}
				}

				session.save();

		        credentials = null;
			}
		}
		catch (RepositoryException e) {
			credentials = null;
			throw new SystemRepositoryException("deleteSystemDomainStructures failed", e);
		}
	}
	
	public List<String> listElasticAddresses(RepositoryCredentials credentials, String domainName, String path) {
		ArrayList<String> persistentIpAddresses = new ArrayList<String>();

		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {

				Node root = session.getRootNode(); // JCR root

				// Find the domain in the systems node
				if (root.hasNode(Systems)) {
					Node sysNode = root.getNode(Systems);
					if (sysNode.hasNode(domainName)) {
						Node domainNode = sysNode.getNode(domainName);
						Node pathNode = domainNode;
						if (StringUtils.isNotBlank(path)) {
							pathNode = domainNode.getNode(path);
						}
						
						persistentIpAddresses.addAll(listElasticAddresses(pathNode));
					}
				}
				
				session.save();

		        credentials = null;
			}
		}
		catch (RepositoryException e) {
			credentials = null;
			throw new SystemRepositoryException("retrieveElasticAddresses failed", e);
		}
		
		return persistentIpAddresses;
	}
	
	public List<String> listElasticAddresses(Node pathNode) {
		ArrayList<String> persistentIpAddresses = new ArrayList<String>();
		
		try {
			if(pathNode.getPath().endsWith("/userConfig.json")) {
				// Retrieve the config file
				InputStream is = retrieve(pathNode);
				
				// Parse the file contents for persistentIpAddresses
				if (is != null) {
					
					try {
						String userConfigJsonText = inputStream2String(is);
						JSONObject userConfig = JSONObject.fromObject(userConfigJsonText);
						
						JSONArray processGroups = userConfig.optJSONArray("processGroups");
						if(processGroups != null) {
							for(int i = 0; i < processGroups.size(); i++) {
								JSONObject processGroup = (JSONObject)processGroups.get(i);
								String persistentIPAddress = processGroup.getString("persistentIPAddress");
								if(!persistentIPAddress.equals("none")) {
									persistentIpAddresses.add(persistentIPAddress);
								}
							}
						}
					} catch (IOException doNothing) {
						// Failed to read contents of the InputStream into a String, there is nothing
						// to be done about this, carry on
					}
				}
			}
			else {
				if(pathNode.hasNodes()) {
					// This node has child nodes, inspect them
					try {
						NodeIterator nodeIt = pathNode.getNodes();
						
						while(nodeIt.hasNext()) {
							Node child = (Node)nodeIt.next();
							
							persistentIpAddresses.addAll(listElasticAddresses(child));
						}
						
					} catch (RepositoryException doNothing) {
						// Failed to get the node's children, there is nothing
						// to be done about this, carry on
					}
				}
			}
		}
		catch (RepositoryException e) {
			credentials = null;
			throw new SystemRepositoryException("retrieveElasticAddresses failed", e);
		}
		
		return persistentIpAddresses;
	}
	
	public boolean systemDomainExists(String domainName)
	{
		try {
			Node domainNode = getSystemDomainNode(domainName);

			return (domainNode != null);
		}
		catch (RepositoryException e) {
			// fall through and return false;
		}

		return false;
	}

	public Node getSystemDomainNode(String domainName) throws RepositoryException
	{
		Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

		synchronized (session) {

			Node root = session.getRootNode(); // JCR root

			String domainPath = Systems + "/" + domainName;
			Node domainNode = root.getNode(domainPath);
			if (domainNode == null) {
				throw new RepositoryException("System Domain data path " + domainName + " does not exist");
			}

			return domainNode;
		}
	}

	/**
	 * Returns an array of the JCR nodes for the current user, one for each level of visibility
	 * as defined in the enumeration Visibility
	 * @return an array of JCR nodes
	 */
	public Node[] getUsersNodes() {
		
		return getUsersNodes(credentials.getUsername());
		
	}
	
	/**
	 * Returns an array of the JCR nodes for the given user, one for each level of visibility
	 * as defined in the enumeration Visibility
	 * @return an array of JCR nodes
	 */
	public Node[] getUsersNodes(String userName) {
		
		Node[] result = null;
		
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				
				Node root = session.getRootNode(); // JCR root
		
				Visibility visibilities[] = Visibility.values();
				result = new Node[visibilities.length];
				for (Visibility v : visibilities) {
					String visName = v.toString().toLowerCase();
					String path = visName + "/" + userName;
					result[v.ordinal()] = root.getNode(path);
				}
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("getUsersNodes failed", e);
		}
		
		return result;
		
	}
	
	/**
	 * Set an access policy for a given node.
	 * 
	 * @param node The node to set the policy upon
	 * @param entries A list of access control entries to apply to a node
	 */
	public void setAccessControlPolicy(Node node, List<ACLEntry> entries) { 
		
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
	
			synchronized(session) {
				AccessControlManager acm = session.getAccessControlManager();
				AccessControlPolicyIterator it = acm.getApplicablePolicies(node.getPath());
			
				JackrabbitAccessControlList acl = null;
				while (it.hasNext() && acl == null) {
					acl = (JackrabbitAccessControlList) it.nextAccessControlPolicy();
				}
			
				for (ACLEntry entry : entries) {
					Privilege [] privileges = new Privilege[entry.privileges.length];
					
					for (int i = 0; i < entry.privileges.length; i++) {
						privileges[i] = acm.privilegeFromName(entry.privileges[i]);
					}
					
					acl.addEntry(entry.principal, privileges, entry.isAllow);
				}
	
				acm.setPolicy(node.getPath(), acl);
			
				session.save();
			}
		}
		catch (Exception e) {
			throw new SystemRepositoryException("setAccessControlPolicy failed", e);
		}
		
	}

	/**
	 * Creates a named path under the specified node.
	 * Path can have multiple nodes separated by a '/'
	 * ACLs are inherited from the parent node.
	 */
	public void createPath(Node node, String path) {
		if (StringUtils.isBlank(path)) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				// File Set root - add it if it does not exist
				String [] directories = path.split("/");
				Node current = node;
				for (String dir : directories) {
					if (!current.hasNode(dir)) {
						current.addNode(dir);
					}
					current = current.getNode(dir);
				}
		        session.save();
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("createPath failed", e);
		}
	}

	/**
	 * Creates an named path for the user within the specified visibility zone.
	 */
	@Override
	public void createPath(Visibility visibility, String path) {
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node userNode = getUserNode(session, visibility);
				createPath(userNode, path);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("createPath failed", e);
		}
		
	}
	
	public void createCommonPath(String path) {

		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node commonNode = getCommonNode();
				createPath(commonNode, path);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("createPath failed", e);
		}
	}

	@Override
	public void deletePath(Visibility visibility, String path) {
		
		if (path == null || path.trim().length() == 0) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				
				Node userNode = getUserNode(session, visibility);

				String [] directories = path.split("/");
				Node current = userNode;
				for (String dir : directories) {
					if (! current.hasNode(dir)) {
						throw new SystemRepositoryException("Path '" + path + "' does not exist.");
					}
					
					current = current.getNode(dir);
				}
				
				current.remove();
				
		        session.save();
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("deletePath failed", e);
		}

	}

	/*
	 * Core function to store data into a JCR Node
	 */
	public void store(Node node, String filename, InputStream data)
			throws RepositoryException {

		if (node == null) {
			throw new SystemRepositoryException("Cannot save a file to a repository Node that doesn't exist.");
		}
		if (StringUtils.isBlank(filename)) {
			throw new SystemRepositoryException("Must specify a filename to store data in the repository.");
		}

		Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

		synchronized (session) {
			if (node.hasNode(filename)) {
				node.getNode(filename).remove();
			}

			Node fileNode = node.addNode(filename, "nt:file");
			Node contentNode = fileNode.addNode("jcr:content", "nt:resource");

			ValueFactory valueFactory = session.getValueFactory();
			Binary contents = valueFactory.createBinary(data);
			contentNode.setProperty("jcr:data", contents);

			Calendar lastModified = Calendar.getInstance();
			lastModified.setTimeInMillis(System.currentTimeMillis());
			contentNode.setProperty("jcr:lastModified", lastModified);

			session.save();
		}
	}

	@Override
	public void store(Visibility visibility, String path, String filename, InputStream data) {
		
		if (StringUtils.isBlank(path)) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node userNode = getUserNode(session, visibility);
				createPath(userNode, path);
				Node pathNode = userNode.getNode(path);
				store(pathNode, filename, data);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("store failed", e);
		}
		
	}

	public void storeInCommon(String path, String filename, InputStream data) {

		if (StringUtils.isBlank(path)) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node commonNode = getCommonNode();
				createPath(commonNode, path);
				Node pathNode = commonNode.getNode(path);
				store(pathNode, filename, data);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("common area store failed", e);
		}
	}

	public void storeInSystemDomain(String domainName, String path, String filename, InputStream data) {

		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {
				Node domainNode = getSystemDomainNode(domainName);
				Node pathNode = domainNode;
				if (StringUtils.isNotBlank(path)) {
					createPath(domainNode, path);
					pathNode = domainNode.getNode(path);
				}
				store(pathNode, filename, data);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("system domain store failed", e);
		}
	}

	/*
	 * Core function to retrieve data described by a JCR Node
	 */
	public InputStream retrieve(Node node)
			throws RepositoryException {

		if (node == null) {
			throw new SystemRepositoryException("Cannot retrieve a file from a repository Node that doesn't exist.");
		}

		InputStream result = null;

		Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

		synchronized (session) {
			Node contentNode = node.getNode("jcr:content");
	        javax.jcr.Property prop = contentNode.getProperty("jcr:data");
	        Binary contents = prop.getBinary();
	        result = contents.getStream();
		}

		return result;
	}

	@Override
	public InputStream retrieve(Visibility visibility, String path, String filename, String userName) {
		
		if (StringUtils.isBlank(path)) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}
		
		InputStream result = null;
		
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			synchronized (session) {
				Node userNode = getUserNode(session, visibility, userName);
				Node pathNode = userNode.getNode(path);				
				Node fileNode = pathNode.getNode(filename);
				result = retrieve(fileNode);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("retrieve failed", e);
	
		}
		
		return result;
	}

	public InputStream retrieveFromCommon(String path, String filename) {

		InputStream result = null;

		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {
				Node commonNode = getCommonNode();
				Node pathNode = commonNode.getNode(path);
				Node fileNode = pathNode.getNode(filename);
				result = retrieve(fileNode);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("common area retrieve failed", e);
		}

		return result;
	}

	public InputStream retrieveFromSystemDomain(String domainName, String path, String filename) {

		InputStream result = null;

		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {
				Node domainNode = getSystemDomainNode(domainName);
				Node pathNode = domainNode;
				if (StringUtils.isNotBlank(path)) {
					pathNode = domainNode.getNode(path);
				}
				Node fileNode = pathNode.getNode(filename);
				result = retrieve(fileNode);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("system domain retrieve failed", e);
		}

		return result;
	}

	/*
	 * Core function to delete data described by a JCR Node
	 */
	public void delete(Node node)
			throws RepositoryException {

		if (node == null) {
			throw new SystemRepositoryException("Cannot retrieve a file from a repository Node that doesn't exist.");
		}

		Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

		synchronized (session) {
			node.remove();
			session.save();
		}
	}

	@Override
	public void delete(Visibility visibility, String path, String filename) {
		
		if (StringUtils.isBlank(path)) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {			
				Node userNode = getUserNode(session, visibility);
				Node pathNode = userNode.getNode(path);
				if (pathNode.hasNode(filename)) {
					Node fileNode = pathNode.getNode(filename);
					delete(fileNode);
				}
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("delete failed", e);
		}
		
	}

	public void deleteFromCommon(String path, String filename) {

		if (StringUtils.isBlank(path)) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}

		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node commonNode = getCommonNode();
				Node pathNode = commonNode.getNode(path);
				if (pathNode.hasNode(filename)) {
					Node fileNode = pathNode.getNode(filename);
					delete(fileNode);
				}
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("common area delete failed", e);
		}
	}

	public void deleteFromSystemDomain(String domainName, String path, String filename) {
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node domainNode = getSystemDomainNode(domainName);
				Node pathNode = domainNode;
				if (StringUtils.isNotBlank(path)) {
					pathNode = domainNode.getNode(path);
				}
				if (pathNode.hasNode(filename)) {
					Node fileNode = pathNode.getNode(filename);
					delete(fileNode);
				}
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("system domain delete failed", e);
		}
	}

	/*
	 * Core function to list described by a JCR Node
	 */
	public List<RepoFile> listFiles(Node node, String filterPattern) {

		if (node == null) {
			throw new SystemRepositoryException("Cannot list files in a repository Node that doesn't exist.");
		}

		List<RepoFile> result = new ArrayList<RepoFile>();

		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				NodeIterator ni = node.getNodes();
				while (ni.hasNext()) {
					Node n = ni.nextNode();
					String name = n.getName();
					
					// Don't return version node because they don't
					// have the jcr:description property
					// also skip built-in nodes like rep:policy
					if ( !name.startsWith("jcr:") && !name.startsWith("rep:") &&
							((filterPattern == null) || (name.contains(filterPattern))) ) {
						
						RepoFile currRepoFile = new RepoFile();
						currRepoFile.setName(name);
						currRepoFile.setDirectory((n.getPrimaryNodeType() != null && !n.getPrimaryNodeType().isNodeType(NodeType.NT_FILE)));
						result.add(currRepoFile);
					}
				}
				Collections.sort(result, new RepoFileComparator());
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("listFiles failed", e);
		}
		
		return result;
	}

	@Override
	public List<RepoFile> listFiles(Visibility visibility, String path, String filterPattern, String userName) {
		
		if (StringUtils.isBlank(path)) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}
		
		List<RepoFile> result = null;
		
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node userNode = getUserNode(session, visibility, userName);
				Node pathNode = userNode.getNode(path);
				
				result = listFiles(pathNode, filterPattern);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("listFiles failed", e);
		}
		
		return result;
		
	}
	
	public List<RepoFile> listFilesInCommon(String path, String filterPattern) {
		if (StringUtils.isBlank(path)) {
			throw new SystemRepositoryException("Parameter 'path' cannot be null or a empty string.");
		}

		List<RepoFile> result = null;

		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {
				Node commonNode = getCommonNode();
				Node pathNode = commonNode.getNode(path);
				result = listFiles(pathNode, filterPattern);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("common area listFiles failed", e);
		}

		return result;
	}
	
	public List<RepoFile> listFilesInSystemDomain(String domainName, String path, String filterPattern) {
		List<RepoFile> result = null;

		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());

			synchronized (session) {
				Node domainNode = getSystemDomainNode(domainName);

				Node startNode = null;
				if (StringUtils.isNotBlank(path)) {
					startNode = domainNode.getNode(path);
				}
				else {
					startNode = domainNode;
				}

				result = listFiles(startNode, filterPattern);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("system domain listFiles failed", e);
		}

		return result;
	}

	public List<String> greedyListFilesWithPath(Visibility visibility, String path, String filterPattern, String userName) {
				
		ArrayList<String> result = new ArrayList<String>();
		
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node userNode = getUserNode(session, visibility, userName);
				
				Node startNode = null;
				if (path != null) {
					startNode = userNode.getNode(path);
				}
				else {
					startNode = userNode;
				}
				
				result.addAll(findNode(startNode, filterPattern));
				
				Collections.sort(result);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("greedyListFilesWithPath failed", e);
		}
		
		return result;
		
	}
	
	public List<String> greedyListFilesInCommon(String path, String filterPattern) {
		ArrayList<String> result = new ArrayList<String>();
		
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node commonNode = this.getCommonNode();
				
				Node startNode = null;
				if (path != null) {
					startNode = commonNode.getNode(path);
				}
				else {
					startNode = commonNode;
				}
				
				result.addAll(findNode(startNode, filterPattern));
				
				Collections.sort(result);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("greedyListFilesInCommon failed", e);
		}
		
		return result;
	}

	public List<String> greedyListFilesInSystemDomain(String domainName, String path, String filterPattern) {
		ArrayList<String> result = new ArrayList<String>();
		
		try {		
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
			
			synchronized (session) {
				Node domainNode = getSystemDomainNode(domainName);
				
				Node startNode = null;
				if (StringUtils.isNotBlank(path)) {
					startNode = domainNode.getNode(path);
				}
				else {
					startNode = domainNode;
				}

				result.addAll(findNode(startNode, filterPattern));
				
				Collections.sort(result);
			}
		}
		catch (RepositoryException e) {
			throw new SystemRepositoryException("greedyListFilesInSystemDomain failed", e);
		}
		
		return result;		
	}
	
	// ----- Start User Management functionality -----
	
	public User createUser(String userId, String password) {
	
		User user = null;
	
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
	
			synchronized(session) {
				JackrabbitSession js = (JackrabbitSession) session;
	
				UserManager um = js.getUserManager();
	
				Authorizable authorizable = um.getAuthorizable(userId);
	
				if (authorizable != null) {
					throw new SystemRepositoryException(userId + " already exist in the repository");
				}
	
				user = um.createUser(userId, password);
			}
		} catch(Exception e) {
			throw new SystemRepositoryException("createUser failed", e);
		}
		
		return user;
	}
	
	public User findUser(String userId) {
		
		User user = null;
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
		
			synchronized(session) {
				JackrabbitSession js = (JackrabbitSession) session;
		
				UserManager um = js.getUserManager();
		
				Authorizable authorizable = um.getAuthorizable(userId);
		
				if (authorizable != null) {
					if (! (authorizable instanceof User)) {
						throw new SystemRepositoryException(userId + " is not a valid user");
					} 
			
					user = (User) authorizable;
				}
			}
		} catch(Exception e) {
			throw new SystemRepositoryException("findUser failed", e);
		}
		
		return user;
		
	}
	
	public void deleteUser(String userId) {
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
		
			synchronized(session) {
				User user = findUser(userId);
				user.remove();
			}
		} catch(Exception e) {
			throw new SystemRepositoryException("findUser failed", e);
		}

	}
	
	public void changeUserPassword(String userId, String password) {
		
		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
		
			synchronized(session) {
				User user = findUser(userId);
				user.changePassword(password);
			}
		} catch(Exception e) {
			throw new SystemRepositoryException("changeUserPassword failed", e);
		}
		
	}
	
	public List<String> retrieveAllUserIds() {
		
		List<String> userIds = new ArrayList<String>();

		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
		
			synchronized(session) {
				JackrabbitSession js = (JackrabbitSession) session;
		
				UserManager um = js.getUserManager();
    
				Iterator<Authorizable> auths = um.findAuthorizables("rep:principalName", null, UserManager.SEARCH_TYPE_USER);
	        
				while (auths.hasNext()) {
					User user = (User) auths.next();
					userIds.add(user.getID());
				}
			}
		} catch(Exception e) {
			throw new SystemRepositoryException("retrieveAllUserIds failed", e);
		}

	    return userIds;
	    
	}
	
	public List<User> retrieveAllUsers() {
		
		List<User> users = new ArrayList<User>();

		try {
			Session session = JcrSessionCache.get().getSession(credentials.getJcrCredentials());
		
			synchronized(session) {
				JackrabbitSession js = (JackrabbitSession) session;
		
				UserManager um = js.getUserManager();
    
				Iterator<Authorizable> auths = um.findAuthorizables("rep:principalName", null, UserManager.SEARCH_TYPE_USER);
	        
				while (auths.hasNext()) {
					User user = (User) auths.next();
					users.add(user);
				}
			}
		} catch(Exception e) {
			throw new SystemRepositoryException("retrieveAllUsers failed", e);
		}

	    return users;
	    
	}
	
	// ----- End User Management functionality -----
	
	// ----- Start Private Methods -----
	
	private Node getUserNode(Session session, Visibility visibility) throws RepositoryException {
		return getUserNode(session, visibility, null);
	}
	
	private Node getUserNode(Session session, Visibility visibility, String userName) throws RepositoryException 
	{
		if (userName == null) {
			userName = credentials.getUsername();
		}
		
		Node root = session.getRootNode(); // JCR root
		String visibilityName = visibility.toString().toLowerCase();
		String userPath = visibilityName + "/" + userName;
		Node userNode = root.getNode(userPath);
		if (userNode == null) {
			throw new RepositoryException("User data path " + userPath + " does not exist");
		}
		return userNode;
	}
	
	/**
	 * Recursively iterate down the given node tree structure looking for
	 * a particular node type and return a list of absolute path of the
	 * node that matches.
	 */
	private List<String> findNode(Node node, String filterPattern) throws RepositoryException {
		
		List<String> paths = new ArrayList<String>();
		
		findNodeHelper(node, filterPattern, paths);
		
		return paths;
	}
	
	/**
	 * The helper method for the findNode method.
	 */
	private void findNodeHelper(Node node, String filterPattern, List<String> paths) throws RepositoryException {
		
		NodeIterator it = node.getNodes();
		
		while (it.hasNext()) {
			Node curr = it.nextNode();
			
			if (filterPattern == null || curr.getName().contains(filterPattern)) {
				paths.add(curr.getPath());
			}
			
			findNodeHelper(curr, filterPattern, paths);
		}
		
	}
	
	// ----- End Private Methods -----
	
	public static void main(String args[]) {
 		
		if (args.length != 3) {
			System.out.println("Parameters: <url> <userId> <password>");
			System.exit(1);
		}
		
		String url = args[0];
		String userId = args[1];
		String password = args[2];
		
		RepositoryCredentials credentials = new RepositoryCredentials(userId, password);
		try {
			final String testSales1 = "testsales_v1.0";
			final String testSales2 = "testsales_v2.0";
			
			JcrSystemRepository jcr = new JcrSystemRepository();
			jcr.setRepositoryUrl(url);
			jcr.setCredentials(credentials);
			jcr.initialize();
			
			System.out.println("Create user structures for " + userId);
			jcr.createUserStructures(credentials, userId);
			
			System.out.println("Create models folder...");
			jcr.createPath(Visibility.Public, "models");
			
			System.out.println("Delete file sets if they exist");
			jcr.delete(Visibility.Public, "models", testSales1);
			jcr.delete(Visibility.Public, "models", testSales2);
			
			System.out.println("Retrieve non-existant file set");
			try {
				@SuppressWarnings("unused")
				InputStream is = jcr.retrieve(Visibility.Public, "models", testSales1, null);
			}
			catch (SystemRepositoryException e) {
				System.out.println(e);
				System.out.println("Exception correctly raised.");
			}
			
			System.out.println("Create files...");
			ByteArrayInputStream is1 = new ByteArrayInputStream("Test Sales 1.0".getBytes());
			jcr.store(Visibility.Public, "models", testSales1, is1);
			
			ByteArrayInputStream is2 = new ByteArrayInputStream("Test Sales 2.0".getBytes());
			jcr.store(Visibility.Public, "models", testSales2, is2);			
			
			System.out.println("Test listFileSets(null):");
			List<RepoFile> repoFiles = jcr.listFiles(Visibility.Public, "models", null, null);
			for (RepoFile currRepoFile : repoFiles) {
				System.out.println(currRepoFile.getName() + " [" + (currRepoFile.isDirectory() ? "Directory" : "File") + "]");
			}
			
			System.out.println("Retrieving files...");
			InputStream is = jcr.retrieve(Visibility.Public, "models", testSales1, null);
			System.out.println(JcrSystemRepository.inputStream2String(is));
			
			is = jcr.retrieve(Visibility.Public, "models", testSales2, null);
			System.out.println(JcrSystemRepository.inputStream2String(is));
			
			System.out.println("Delete files...");
			jcr.delete(Visibility.Public, "models", testSales1);
			jcr.delete(Visibility.Public, "models", testSales2);
			
			System.out.println("Delete models folder...");
			jcr.deletePath(Visibility.Public, "models");
		}
		catch (Exception e) {
			e.printStackTrace();
		} 		
	}
}
