package com.saic.rtws.commons.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Utility class for accessing and validating parameters from java properties files.
 * 
 * NOTE: All of the checkOptionalXXX() methods provided here do not function in a
 * logically sound way. They always return a value, even when the underlying
 * parameter is missing from the properties object. However, there is no way to
 * distinguish whether the method is returning a meaningless default, or a successfully
 * parser parameter. Please avoid using these methods until they are re-factored. 
 */
public final class PropertiesUtils {
    /** logger. */
    private static Logger logger = Logger.getLogger(PropertiesUtils.class);
    
    /** Constructor. */
    protected PropertiesUtils() { }
    
    /**
     * Load the given properties file from the specified path.
     * @param file File object to convert to Properties object
     * @return Properties object
     * @throws IOException Could throw an IOException
     */
    public static Properties loadProperties(final File file) throws IOException { 
        FileInputStream stream = null;
        Properties config = new Properties(System.getProperties());
        try {
            stream = new FileInputStream(file);
            config.load(stream);
            return config;
        } finally {
            try { stream.close(); } catch (Exception ignore) { }
        }
    }
    
    /**
     * Loads the given properties file from the system resource path.
     * @param filename Name of file to convert to Properties object
     * @return Properties object
     * @throws IOException Could throw an IOException
     */
    public static Properties loadProperties(final String filename) throws IOException {
        Properties properties = new Properties();
        InputStream stream = loadResource(filename);
        if (stream != null) { 
        	try {
            	properties.load(stream);
        	} finally {
            	try { stream.close(); } catch (Exception ignore) { }
        	} 
        } else {
            throw new FileNotFoundException(filename);
        }
        return properties;
    }
    
    /**
     * Loads a resource from the system resource path.
     * @param filename Name of file to get InputStream from
     * @return InputStream of file
     * @throws IOException Could throw an IOException
     */
    public static InputStream loadResource(final String filename) throws IOException { 
        return PropertiesLoader.getInputStream(filename);
    }
    
    /**
     * Returns the local hostname.
     * @return String containing local hostname
     */
    public static String getLocalHostname() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException x) {
            return "localhost";
        }
    }
    
    /**
     * Get the given named parameter from the given properties object and
     * verify that it is a non-empty string. If configuration parameter is 
     * missing or invalid, then system will exit fatally.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return Property value for the parameter key
     */
    public static String checkString(final Properties config, final String param) {
        String value = config.getProperty(param);
        if (value == null || value.matches("\\s*")) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
            System.exit(1);
        } else {
            value = value.trim();
        }
        return value;
    }

    /**
     * Get the given named parameter from the given properties object, if it exists.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return Property value for the parameter key
     */
    public static String checkOptionalString(final Properties config, final String param) {
        String value = config.getProperty(param);
        if (value == null || value.matches("\\s*")) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
        } else {
            value = value.trim();
        }
        return value;
    }
    
    /**
     * Get the given named parameter from the given properties object and
     * verify that it is a valid integer.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return Property value for the parameter key
     */
    public static int checkInt(final Properties config, final String param) {
        int value = -1;
        try {
            value = Integer.parseInt(config.getProperty(param));
        } catch (Exception e) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
            System.exit(1);
        }
        return value;
    }

    /**
     * Get the given named parameter from the given properties object, if it exists.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return Property value for the parameter key
     */
    public static int checkOptionalInt(final Properties config, final String param) {
        int value = -1;
        try {
            value = Integer.parseInt(config.getProperty(param));
        } catch (Exception e) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
        }
        return value;
    }    

    /**
     * Get the given named parameter from the given properties object and
     * verify that it is a valid boolean.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return Property value for the parameter key
     */
    public static boolean checkBoolean(final Properties config, final String param) {
        String value = config.getProperty(param);
        if (value == null || value.matches("\\s*")) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
            System.exit(1);
        } else {
            return value.equalsIgnoreCase("true");
        }
        return false;
    }

    /**
     * Get the given named parameter from the given properties, if it exists.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return Property value for the parameter key
     */
    public static boolean checkOptionalBoolean(final Properties config, final String param) {
        String value = config.getProperty(param);
        if (value == null || value.matches("\\s*")) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
        } else {
            return value.equalsIgnoreCase("true");
        }
        return false;
    }
   
    /**
     * Get the given named parameter from the given properties object and
     * verify that it is valid a file that exists on the system.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return File object of the property value for the parameter key
     */
    public static File checkFile(final Properties config, final String param) {
        File file = new File(checkString(config, param));
        if (!file.exists()) {
            logger.error("Configuration parameter '" + param + "' refers to a file that does not exist.");
            System.exit(1);
        }
        return file;
    }
    
    /**
     * Get the given named parameter from the given properties object, if it exists.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return File object of the property value for the parameter key
     */
    public static File checkOptionalFile(final Properties config, final String param) {
        File file = new File(checkString(config, param));
        if (!file.exists()) {
            logger.error("Configuration parameter '" + param + "' refers to a file that does not exist.");
        }
        return file;
    }
 
    /**
     * Get the given named parameter from the given properties object and
     * verify that it is a valid CSV list and put it in a HashSet.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return HashSet object of the property values for the parameter key
     */
    public static HashSet<String> checkCSVList(final Properties config, final String param) {
    	String[] elements;
    	HashSet<String> elementSet = new HashSet<String>();
    	String value = config.getProperty(param);
    	if (value == null) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
            System.exit(1);
        } else if (!value.contains(",")) { // assume single value list
        	elementSet.add(value);
        } else {
            elements = value.split(",");
            for (int i = 0; i < elements.length; i++) {
            	elementSet.add(elements[i]);
            } 
        }       
        return (elementSet);
    }    

    /**
     * Get the given named parameter from the given properties object and
     * if it exists put it in a HashSet.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @return HashSet object of the property values for the parameter key
     */
    public static HashSet<String> checkOptionalCSVList(final Properties config, final String param) {
    	String[] elements;
    	HashSet<String> elementSet = new HashSet<String>();
    	String value = config.getProperty(param);
    	if (value == null) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
        } else if (!value.contains(",")) { // assume single value list
        	elementSet.add(value);
        } else {
            elements = value.split(",");
            for (int i = 0; i < elements.length; i++) {
            	elementSet.add(elements[i]);
            } 
        }       
        return (elementSet);
    } 

    /**
     * Get the given named parameter from the given properties object and
     * verify that it is a valid enumeration element.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @param cls Class to be used to load enumeration
     * @param <E> Generic class type for enumeration
     * @return Enumeration object of the property values for the parameter key
     */
    public static <E extends Enum<E>> E checkEnum(final Properties config, final String param, final Class<E> cls) {
        E element = null;
        String value = config.getProperty(param);
        if (value == null || value.matches("\\s*")) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
            System.exit(1);
        } else {
        	try {
            	element = Enum.valueOf(cls, value.trim());
        	} catch (Exception e) {
            	logger.error("Configuration parameter '" + param + "' is not a valid enumeration element of '" + cls.getName() + "'.");
            	System.exit(1);
        	}
        }
        return element; 
    }
    
    /**
     * Get the given named parameter from the given properties object, if it exists.
     * @param config Properties object
     * @param param Parameter key to check in Properties object
     * @param cls Class to be used to load enumeration
     * @param <E> Generic class type for enumeration
     * @return Enumeration object of the property values for the parameter key
     */
    public static <E extends Enum<E>> E checkOptionalEnum(final Properties config, final String param, final Class<E> cls) {
        E element = null;
        String value = config.getProperty(param);
        if (value == null || value.matches("\\s*")) {
            logger.error("Configuration parameter '" + param + "' is missing or invalid.");
        } else {
        	try {
        		element = Enum.valueOf(cls, value.trim());
        	} catch (Exception e) {
        		logger.error("Configuration parameter '" + param + "' is not a valid enumeration element of '" + cls.getName() + "'.");
        	}
        }
        return element; 
    }
	
	/**
	 * Format a Properties key into a java CamelCase function name.
	 * @param key Properties key
	 * @return Java CamelCase function name
	 */
	public static String formatKeyIntoCamelCase(String key) {
		if (key == null) {
			return "";
		}
		
		StringBuffer formattedKey = new StringBuffer();
		
		boolean capitalizeNextChar = true;
		char[] chars = key.toCharArray();
		for (int i=0; i<chars.length; i++) {
			if (i == 0) {
				formattedKey.append(String.valueOf(chars[i]).toLowerCase());
				capitalizeNextChar = false;
			}
			else if (chars[i] == ' ' || chars[i] == '.' || chars[i] == '_' || chars[i] == '-') {
				capitalizeNextChar = true;
			}
			else if (capitalizeNextChar) {
				formattedKey.append(String.valueOf(chars[i]).toUpperCase());
				capitalizeNextChar = false;
			}
			else {
				formattedKey.append(chars[i]);
				capitalizeNextChar = false;
			}
		}
		
		return formattedKey.toString();
	}
	
	/**
	 * Format a Properties key into a java getter function name.
	 * @param key Properties key
	 * @return Java "Getter" function name
	 */
	public static String formatKeyIntoGetter(String key) {
		StringBuffer formattedKey = new StringBuffer();
		
		boolean capitalizeNextChar = true;
		char[] chars = key.toCharArray();
		for (int i=0; i<chars.length; i++) {
			if (chars[i] == ' ' || chars[i] == '.' || chars[i] == '_' || chars[i] == '-') {
				capitalizeNextChar = true;
			}
			else if (capitalizeNextChar) {
				formattedKey.append(String.valueOf(chars[i]).toUpperCase());
				capitalizeNextChar = false;
			}
			else {
				formattedKey.append(chars[i]);
				capitalizeNextChar = false;
			}
		}
		
		return formattedKey.toString();
	}
	
}
