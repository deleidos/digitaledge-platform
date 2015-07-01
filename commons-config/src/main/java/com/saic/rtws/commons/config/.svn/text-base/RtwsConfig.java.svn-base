package com.saic.rtws.commons.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.filesystem.ConfigLoadPrecedence;
import com.saic.rtws.commons.config.filesystem.DirRelativePatternBasedLoadPrecedence;
import com.saic.rtws.commons.config.filesystem.DirectoryProcessingMode;
import com.saic.rtws.commons.config.filesystem.FileSystemConfigLoader;

public class RtwsConfig {
	private static final Logger LOGGER = Logger.getLogger(RtwsConfig.class);
	
	private static final String RTWS_CONFIG_DIR_PROP_NAME = "RTWS_CONFIG_DIR";
	// This is the default for the target environment.  Use an environment or java var in development environments.
	private static final String DEFAULT_RTWS_CONFIG_DIR = "/usr/local/rtws/properties";
	private static final String RTWS_COMMON_PROPERTIES_FILENAME = "rtws-common.properties";
	private static final List<String> ORDERED_INCLUSION_PATTERNS = new ArrayList<String>();
	private static final List<String> EXCLUSION_PATTERNS = null;

	static
	{
		ORDERED_INCLUSION_PATTERNS.add(RTWS_COMMON_PROPERTIES_FILENAME);
		ORDERED_INCLUSION_PATTERNS.add("*/" + RTWS_COMMON_PROPERTIES_FILENAME);
		ORDERED_INCLUSION_PATTERNS.add("*.properties");
	}
	
	static class SingletonHolder{
      static final RtwsConfig INSTANCE = new RtwsConfig();
	}
	
	/**
	 * Returns an instance of RtwsConfig containing all the properties files
	 * found in the directory identified in the RTWS_CONFIG_DIR system or
	 * environment variable.  To add properties to this configuration, simply
	 * copy the properties to the configuration directory in your node's manifest.
	 * 
	 * If no system or environment variable RTWS_CONFIG_DIR is provided, the
	 * default, /usr/local/rtws/properties, will be used.
	 * 
	 * @return RtwsConfig instance containing all configuration (e.g. properties files).
	 */
	public static RtwsConfig getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private CombinedConfiguration combinedConfiguration;
	
	/**
	 * This method has been made protected for unit testing ... This class should not
	 * need to be extended for normal use.
	 */
	protected RtwsConfig()
	{
		super();
		String configRootDirPath = getInitProperty(RTWS_CONFIG_DIR_PROP_NAME, DEFAULT_RTWS_CONFIG_DIR);
		if(StringUtils.isBlank(configRootDirPath))
		{
			String errorMsg = null;
			if(StringUtils.isEmpty(configRootDirPath))
			{
				errorMsg = "An empty value";
			}
			else if(configRootDirPath == null)
			{
				// Should not happen ... a default is provided
				errorMsg = "No value";
			}
			errorMsg += " was provided for '" + RTWS_CONFIG_DIR_PROP_NAME + //
					"'.  This value should be set as a System or Environmental " + //
					" property and should not be blank (null or empty).";
			LOGGER.fatal(errorMsg);
			throw new RuntimeException(errorMsg);
		}
		
		File systemConfigRootDir = new File(configRootDirPath);
		if(systemConfigRootDir == null || !systemConfigRootDir.isDirectory() || !systemConfigRootDir.canRead())
		{
			String errorMsg = "Invalid or Inaccessible configuration directory specified.";
			if(systemConfigRootDir != null)
			{
				errorMsg += "  Specified Path: '" + systemConfigRootDir.getAbsolutePath() + "'";
			}
			LOGGER.fatal(errorMsg);
			throw new RuntimeException(errorMsg);
		}
		else
		{
			try {
				ConfigLoadPrecedence loadPrecedence = new DirRelativePatternBasedLoadPrecedence(systemConfigRootDir, ORDERED_INCLUSION_PATTERNS, EXCLUSION_PATTERNS, DirectoryProcessingMode.BREADTH_FIRST);
				FileSystemConfigLoader configLoader = new FileSystemConfigLoader(loadPrecedence);
				combinedConfiguration = configLoader.getCombinedConfiguration();
				if(combinedConfiguration == null)
				{
					String errorMsg = "Failed to bootstrap configuration.";
					LOGGER.fatal(errorMsg);
					throw new RuntimeException(errorMsg);
				}
			} catch (ConfigurationException configException) {
				String errorMsg = "Failed to load Configuration.";
				LOGGER.fatal(errorMsg, configException);
				throw new RuntimeException(errorMsg, configException);
			}
		}
	}
	
	public Configuration getConfiguration() {
		return combinedConfiguration;
	}
	
	/**
	 * Convenience function to avoid the call to getConfiguration().  Same as #getConfiguration().getInt(java.lang.String)
	 */
	public int getInt(String key) {
		return combinedConfiguration.getInt(key);
	}
	
	/**
	 * Convenience function to avoid the call to getConfiguration().  Same as #getConfiguration().getInt(java.lang.String, int)
	 */
	public int getInt(String key, int defaultValue) {
		return combinedConfiguration.getInt(key, defaultValue);
	}

	/**
	 * Convenience function to avoid the call to getConfiguration().  Same as #getConfiguration().getLong(java.lang.String)
	 */
	public long getLong(String key) {
		return combinedConfiguration.getLong(key);
	}
	
	/**
	 * Convenience function to avoid the call to getConfiguration().  Same as #getConfiguration().getLong(java.lang.String, long)
	 */
	public long getLong(String key, long defaultValue) {
		return combinedConfiguration.getLong(key, defaultValue);
	}

	/**
	 * Convenience function to avoid the call to getConfiguration().  Same as #getConfiguration().getString(java.lang.String)
	 * @param key property name
	 * 
	 * @return property value; or null if no property is found
	 */
	public String getString(String key) throws ConversionException {
		return combinedConfiguration.getString(key);
	}
	
	/**
	 * Convenience function to avoid the call to getConfiguration().  Same as #getConfiguration().getString(java.lang.String, java.lang.String);
	 * @param key property name
	 * @param defaultValue default value to use if the property is not found
	 * 
	 * @return property value; or the provided defaultValue if no property is found
	 */
	public String getString(String key, String defaultValue) throws ConversionException {
		return combinedConfiguration.getString(key, defaultValue);
	}
	
	/**
	 * @deprecated with no replacement.  This method only exists to support runtime manipulation of the 
	 * properties and backfill the autogen properties interface.  The prop value will not be persisted
	 * and, as such, this method should not be used.
	 */
	public void setProperty(String propKey, String propValue) throws ConfigurationRuntimeException
	{
		if(StringUtils.isBlank(propKey))
		{
			throw new IllegalArgumentException("propKey argument cannot be empty");
		}
		
		if(combinedConfiguration == null)
		{
			return;
		}
		
		int setPropCount = 0;
		PropertiesConfiguration currConfiguration = null;
		for(Object currObj : combinedConfiguration.getConfigurations())
		{
			if(currObj != null && currObj instanceof PropertiesConfiguration)
			{
				currConfiguration = ((PropertiesConfiguration)currObj);
				Object existingPropVal = currConfiguration.getProperty(propKey);
				if(existingPropVal != null && existingPropVal instanceof String && !isPropertyInterpolated(currConfiguration, propKey))
				{
					((PropertiesConfiguration)currConfiguration).setProperty(propKey, propValue);
					setPropCount++;
				}
			}
		}
		
		if(setPropCount == 0)
		{
			throw new ConfigurationRuntimeException("'" + propValue + "' could not be updated.  It either doesn't exist or is interpolated.");
		}
	}
	
	private boolean isPropertyInterpolated(Configuration configuration, String propKey)
	{
		Object propObj = configuration.getProperty(propKey);
		return (propObj != null && propObj instanceof String && !propObj.toString().equals(configuration.getString(propKey)));
	}
	
	private String getInitProperty(String propName)
	{
		return getInitProperty(propName, null);
	}
	
	private String getInitProperty(String propName, String defaultValue)
	{
		String result = null;
		
		try
		{
			result = System.getProperty(propName);
		}
		catch(SecurityException securityException)
		{
			result = null;
		}
		
		if(result == null)
		{
			try
			{
				result = System.getenv(propName);
			}
			catch(SecurityException securityException)
			{
				result = null;
			}
		}
		
		if(result == null)
		{
			result = defaultValue;
			if(defaultValue != null)
			{
				LOGGER.info("'" + propName + "' was not found in environmental or system properties.  Using '" + defaultValue + "' as a default value.");
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) { 

		final int EXIT_CODE_SUCCESS 	  = 0,
				  EXIT_CODE_INVALID_USAGE = 1,
				  EXIT_CODE_EXCEPTION 	  = 2;
		
		if ( args.length != 2 )
		{
			System.err.println( "Usage: RtwsConfig property_name default_propery_value");
			System.exit(EXIT_CODE_INVALID_USAGE);
		} 
		String propertyName  = args[0];
		String propertyDefaultValue = args[1];
		try {
			String propertyValue = RtwsConfig.getInstance().getString(propertyName, propertyDefaultValue);
			System.out.println(propertyValue);
			System.exit(EXIT_CODE_SUCCESS);
		} catch (Exception ex) {
			System.err.println( "Caught exception while getting value for property " + propertyName + "':");
			System.err.println( ex);
			System.exit(EXIT_CODE_EXCEPTION);
		}
	}
}
