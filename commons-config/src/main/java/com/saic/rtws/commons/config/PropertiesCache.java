package com.saic.rtws.commons.config;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

/**
 * PropertiesCache is intended to cache Properties objects for config files that have
 * already be read off the file system, and to also provide convenience methods to the
 * Properties methods.
 *
 */
public final class PropertiesCache {
	/** Cache of property file properties. */
    private Hashtable<String, Properties> cache = null;

	/** Constructor for PropertiesCache. */
	protected PropertiesCache() {
	}
	
    /**
     * PropertiesCacheHolder is loaded on the first execution of PropertiesCache.getInstance() 
     * or the first access to PropertiesCacheHolder, not before.
     */
	protected static final class PropertiesCacheHolder {
		/** Instance of PropertiesCache. */
		protected static final PropertiesCache INSTANCE = new PropertiesCache();
	    
	    /** Private constructor. */
		protected PropertiesCacheHolder() {
	    }
	}

	
	/**
	 * Return a thread-safe singleton instance of PropertiesCache.
	 * @return Returns singleton instance of PropertiesCache
	 */
	public static PropertiesCache getInstance() {
		return PropertiesCacheHolder.INSTANCE;
	}
	
	/**
	 * Get the loaded Properties object residing at the specified file path location.
	 * @param propertiesFilePath Path to properties file on the file system
	 * @return Returns the Properties object of the specified properties file.
	 * @throws IOException Throws IOException
	 */
	public Properties getProperties(final String propertiesFilePath) throws IOException {
		return getProperties(propertiesFilePath, false);
	}
	
	/**
	 * Get the loaded Properties object residing at the specified file path location.
	 * @param propertiesFilePath Path to properties file on the file system
	 * @param reload Set to true in order to force reload of Properties from file system
	 * @return Returns the Properties object of the specified properties file.
	 * @throws IOException Throws IOException
	 */
	public Properties getProperties(final String propertiesFilePath, final boolean reload) throws IOException {
		// get the thread-safe instance
		PropertiesCache instance = getInstance();
		
		// check to make sure the cache exists
		if (instance.cache == null) {
			instance.cache = new Hashtable<String, Properties>();
		}
		
		// check to see if the properties need to be loaded/reloaded
		if ((!instance.cache.containsKey(propertiesFilePath)) || (reload)) {
			Properties props = null;
			try {
				props = PropertiesLoader.loadProperties(propertiesFilePath);
			} catch (NullPointerException npe) {
				throw new java.io.IOException("Could not load properties \"" + propertiesFilePath + "\"");
			}
			instance.cache.put(propertiesFilePath, props);
		}
		
		// return the properties object
		return instance.cache.get(propertiesFilePath);
	}
	
	/**
	 * Convenience method to return the property value of the specified key within the 
	 * specified properties file.
	 * @param propertiesFilePath Path to properties file on the file system
	 * @param propertyKey Key of property to be returned
	 * @return Returns string value for specified property in specified properties file
	 * @throws IOException Throws an IOException
	 */
	public String getProperty(final String propertiesFilePath, final String propertyKey) throws IOException {
		PropertiesCache instance = getInstance();
		return instance.getProperties(propertiesFilePath).getProperty(propertyKey);
	}
	
	/**
	 * Convenience method to return the property value of the specified key within the 
	 * specified properties file.
	 * @param propertiesFilePath Path to properties file on the file system
	 * @param propertyKey Key of property to be returned
	 * @param defaultPropertyValue Default value in case property is not found
	 * @return Returns string value for specified property in specified properties file. If no
	 *         return value is not found in properties file, then default value is returned
	 * @throws IOException Throws an IOException
	 */
	public String getProperty(final String propertiesFilePath, final String propertyKey, final String defaultPropertyValue) throws IOException {
		PropertiesCache instance = getInstance();
		return instance.getProperties(propertiesFilePath).getProperty(propertyKey, defaultPropertyValue);
	}
	
	/**
	 * Convenience method to set the property value (manually) of the specified key for the specified properties file.
	 * @param propertiesFilePath Path to properties file on the file system
	 * @param propertyKey Key of property to be returned
	 * @param propertyValue Value of the property to be returned
	 * @throws IOException Throws an IOException
	 */
	public void setProperty(final String propertiesFilePath, final String propertyKey, final String propertyValue) throws IOException {
		PropertiesCache instance = getInstance();
		instance.getProperties(propertiesFilePath).setProperty(propertyKey, propertyValue);
	}
}
