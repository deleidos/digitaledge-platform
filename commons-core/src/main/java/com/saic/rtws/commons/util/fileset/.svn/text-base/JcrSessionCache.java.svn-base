package com.saic.rtws.commons.util.fileset;

import java.util.Collection;
import java.util.HashMap;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import com.saic.rtws.commons.exception.InitializationException;
import com.saic.rtws.commons.util.Initializable;

/**
 * Maintains a map of a userid to a JCR session.
 */
public class JcrSessionCache implements Initializable {

	/** Map of userid to a session */
	private HashMap<String, Session> sessionCache;
	
	private Repository repository = null;
	private String url;

	/** Private because this is a singleton */
	private JcrSessionCache() {
		sessionCache = new HashMap<String, Session>();
	}

	/** Return the singleton instance for this class */
	private static JcrSessionCache instance;	
	public static JcrSessionCache get() {
		if (instance == null) {
			instance = new JcrSessionCache();
		}
		return instance;
	}

	/**
	 * Set and get the repository URL.
	 */
	public void setRepositoryURL(String url) {
		this.url = url;
	}
	public String getRepositoryURL() {
		return this.url;
	}

	/**
	 * Initialize the class - this will connect to the repository
	 */
	public synchronized void initialize() throws InitializationException {
		if (repository == null) {
			try {
				repository = new URLRemoteRepository(url);					
			}
			catch (Exception e) {
				throw new InitializationException("Can't get the JCR Repository", e);
			}
		}
	}

	/**
	 * Get the session for the given credentials
	 * @param cred credentials for the user
	 * @return a session
	 * @throws RepositoryException 
	 * @throws LoginException 
	 */
	public synchronized Session getSession(SimpleCredentials cred) throws LoginException, RepositoryException {
		Session session = sessionCache.get(cred.getUserID());
		if (session == null) {
			session = repository.login(cred);
			sessionCache.put(cred.getUserID(), session);
		}
        return session;
	}
	
	/**
	 * Logout of all sessions
	 */
	public void dispose() {
		Collection<Session> sessions = sessionCache.values();
		for (Session session : sessions) {
			session.logout();			
		}
	}

}
