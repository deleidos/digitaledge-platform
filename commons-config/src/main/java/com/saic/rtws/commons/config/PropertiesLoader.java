package com.saic.rtws.commons.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Helper utility to load a properties file into a Properties object.
 *
 */
public class PropertiesLoader {
	
	/** Constructor. */
	protected PropertiesLoader() { }
	
	/**
	 * Get a File object for a specified file path.
	 * @param propertiesFilePath Path to the properties file
	 * @return Return a Properties object
	 * @throws IOException Could throw an IOException
	 */
	public static final java.io.File getFile(final String propertiesFilePath) throws IOException {
		// Attempt #1
		File f = null;
		try {
			java.net.URL url = ClassLoader.getSystemResource(propertiesFilePath);
			if (url != null) {
				f = new File(url.getFile());
			}
		} catch (Exception e) {
			// ignore
		}
		
		if (f != null) {
			return f;
		}
		// Attempt #2
		return new File(propertiesFilePath);
	}
	
	/**
	 * Get an InputStream object for a specified file path.
	 * @param propertiesFilePath Path to the properties file
	 * @return Returns an InputStream object
	 * @throws IOException Could throw an IOException
	 */
	public static final java.io.InputStream getInputStream(final String propertiesFilePath) throws IOException {
        InputStream stream = null;
        if (stream == null) {
            stream = PropertiesUtils.class.getResourceAsStream(propertiesFilePath);
        }
        if (stream == null) {
            stream = ClassLoader.getSystemResourceAsStream(propertiesFilePath);
        }
        if (stream == null) {
            stream = PropertiesUtils.class.getResourceAsStream("/" + propertiesFilePath);
        }
        if (stream == null) {
            stream = ClassLoader.getSystemResourceAsStream("/" + propertiesFilePath);
        }
        if (stream == null) {
            stream = PropertiesLoader.class.getClassLoader().getResourceAsStream(propertiesFilePath);
        }
        if (stream == null) {
            stream = getInputStream(getFile(propertiesFilePath));
        }
        if (stream == null) {
            throw new FileNotFoundException(propertiesFilePath);
        } else {
            return stream;
        }
	}
	
	/**
	 * Get an InputStream object from the File object.
	 * @param propertiesFile File object
	 * @return Return an InputStream
	 * @throws IOException Could throw an IOException
	 */
	public static final java.io.InputStream getInputStream(final File propertiesFile) throws IOException {
		return new FileInputStream(propertiesFile);
	}
	
    /**
     * Looks up a resource named 'name' in the classpath. The resource must map
     * to a file with .properties extention. The name is assumed to be absolute
     * and can use either "/" or "." for package segment separation with an
     * optional leading "/" and optional ".properties" suffix. Thus, the
     * following names refer to the same resource:
     * <pre>
     * some.pkg.Resource
     * some.pkg.Resource.properties
     * some/pkg/Resource
     * some/pkg/Resource.properties
     * /some/pkg/Resource
     * /some/pkg/Resource.properties
     * </pre>
     * 
     * @param name classpath resource name [may not be null]
     * @param loader classloader through which to load the resource [null
     * is equivalent to the application loader]
     * 
     * @return resource converted to java.util.Properties [may be null if the
     * resource was not found and throwOnLoadFailure is false]
     */
    public static Properties loadProperties(final String name, final ClassLoader loader)
    {
    	String resourceName = name;
    	ClassLoader classLoader = loader;
    	
        if (resourceName == null) {
			throw new IllegalArgumentException("null input: name");
		}
        
        if (resourceName.startsWith("/")) {
        	resourceName = resourceName.substring(1);
		}
            
        if (resourceName.endsWith(SUFFIX)) {
        	resourceName = resourceName.substring(0, resourceName.length() - SUFFIX.length());
		}
        
        Properties result = null;
        
        InputStream in = null;
        try {
            if (classLoader == null) {
            	classLoader = ClassLoader.getSystemClassLoader();
			}
            
            if (loadAsResourceBundle) {    
            	resourceName = resourceName.replace('/', '.');
                // Throws MissingResourceException on lookup failures:
                final ResourceBundle rb = ResourceBundle.getBundle(resourceName,
                    Locale.getDefault(), loader);
                
                result = new Properties();
                for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();)
                {
                    final String key = keys.nextElement();
                    final String value = rb.getString(key);
                    
                    result.put(key, value);
                } 
            } else {
            	resourceName = resourceName.replace('.', '/');
                
                if (!resourceName.endsWith(SUFFIX)) {
                	resourceName = resourceName.concat(SUFFIX);
				}
                                
                // Returns null on lookup failures:
                in = getInputStream(resourceName);
                if (in != null) {
                    result = new Properties();
                    result.load(in); // Can throw IOException
                }
            }
        } catch (Exception e) {
            result = null;
        } finally {
            if (in != null) {
				try { in.close(); } catch (Throwable ignore) { }
			}
        }
        
        if (throwOnLoadFailure && (result == null)) {
        	String msg = (loadAsResourceBundle ? "a resource bundle" : "a classloader resource");
            throw new IllegalArgumentException("could not load [" + resourceName + "] as " + msg);
        }
        
        return result;
    }
    
    /**
     * A convenience overload of {@link #loadProperties(String, ClassLoader)}
     * that uses the current thread's context classloader.
     * @param name classpath resource name [may not be null]
     * @return resource converted to java.util.Properties [may be null if the
     *         resource was not found and throwOnLoadFailure is false]
     */
    public static Properties loadProperties(final String name) {
        return loadProperties(name,
            Thread.currentThread().getContextClassLoader());
    }
    
    /**
     * Load the "System" properties into a Properties object.
     * @return Return a Properties object populated with the System properties
     */
    public static Properties loadSystemProperties() {
    	return new Properties(System.getProperties());
    }
    
    /** Toggle on whether or not to throw exception on load failure. */
    private static boolean throwOnLoadFailure = true;
    /** Toggle on whether or not to load as a resource bundle. */
    private static boolean loadAsResourceBundle = false;
    /** Suffix for properties file. */
    private static final String SUFFIX = ".properties";
} // End of class
