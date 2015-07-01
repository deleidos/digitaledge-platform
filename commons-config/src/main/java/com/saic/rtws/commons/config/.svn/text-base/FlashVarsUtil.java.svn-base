package com.saic.rtws.commons.config;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class.
 */
public class FlashVarsUtil {
	
	/** Constructor. */
	public FlashVarsUtil() { }
	
	/**
	 * Create a class id.
	 * @return Return the classid
	 */
	public static String getClassID() {
		
		Random rand = new Random();
		
		int i = rand.nextInt();
		while (i < 0) {
			i = rand.nextInt();
		}
		
		return "D27CDB6E-AE6D-11cf-96B8-00" + i;
	}
	
	/**
	 * Builds a string of javascript code that will set the set the flashvars javascript variable to the
	 * looked up property values when run within an existing javascript block.
	 * 
	 * Details:
	 * <ul>
	 *   <li>
	 *   	The property keys will be converted to a bean-spec format before
	 *   	being set on the flashvarsvariable.  (This is to support legacy
	 *   	users of this utility.)
	 *   </li>
	 *   <li>
	 *   	Empty keys, invalid keys, and null values will be silently ignored
	 *   	(Flex converts param values of null to "null" so this utility ends that silliness.)
	 *   </li>
	 *   <li>
	 *   	The generated code will not be in a script block.  It should be written to the document within such a block.
	 *   </li>
	 *   <li>The generated code will assume that a variable named <flashVarsVariableName> will already be declared</li>
	 * 
	 * @param propertyKeysCsv A comma-separated list of property keys to look up
	 * @param flashVarsVariableName The flashvars javascript variable name previously declared within the page in which this
	 * 	javascript string will eventually be run.
	 * 
	 * @return a String of javascript code setting the specified property key/value pairs on the provided javascript dynamic object. 
	 */
	public static String buildPropertiesSetFlashVarsJavascript(final String propertyKeysCsv, final String flashVarsVariableName)
	{
		return buildSetFlashVarsJavascript(buildPropertiesKeyValueMap(propertyKeysCsv), flashVarsVariableName);
	}
	
	/**
	 * Builds a flashvars querystring representation of the requested property keys and their looked-up values.
	 * 
	 * Details:
	 * <ul>
	 *   <li>
	 *   	The property keys will be converted to a bean-spec format before
	 *   	being placed on the querystring.  (This is to support legacy
	 *   	users of this utility.)
	 *   </li>
	 *   <li>Keys and values will be url-encoded using UTF-8</li>
	 *   <li>
	 *   	Empty keys, invalid keys, and null values will be silently ignored
	 *   </li>
	 *   <li>
	 *   	Key/Value pairs will be separated by &amp; characters but no leading/trailing separates will be included.
	 *   </li>
	 * 
	 * @param propertyKeysCsv A comma-separated list of property keys to look up
	 * 
	 * @return an &amp separated list of url-encoded key-value pairs. 
	 */
	public static String buildPropertiesFlashVarsQueryString(final String propertyKeysCsv)
	{
		return buildFlashVarsQueryString(buildPropertiesKeyValueMap(propertyKeysCsv));
	}
	
	public static Map<String, String> buildPropertiesKeyValueMap(final String propertyKeysCsv)
	{
		Map<String, String> result = new HashMap<String, String>();
		
		if(StringUtils.isNotBlank(propertyKeysCsv))
		{
			StringTokenizer tokenizer = new StringTokenizer(propertyKeysCsv, ",");
			String currToken = null;
			String currGetter = null;
			String currValue = null;
			while(tokenizer.hasMoreTokens())
			{
				currToken = tokenizer.nextToken();
				
				if(StringUtils.isNotBlank(currToken))
				{
					try {
						currGetter = PropertiesUtils.formatKeyIntoCamelCase(currToken);
						currValue = RtwsConfig.getInstance().getString(currToken);
						
						if(StringUtils.isNotBlank(currGetter))
						{
							result.put(currGetter, currValue);
						}
					}
					catch (Exception ignored)
					{
						// Invalid key
					}
				}
			}
		}
		
		return result;
	}
	
	public static Map<String, String> buildMap(final String keySetStr)
	{
		return buildMap(keySetStr, ",", null);
	}
	
	public static Map<String, String> buildMap(final String keySetStr, String delim, String defaultValue)
	{
		Map<String, String> result = new HashMap<String, String>();
		
		if(StringUtils.isNotBlank(keySetStr))
		{
			StringTokenizer tokenizer = new StringTokenizer(keySetStr, delim);
			String currToken = null;
			while(tokenizer.hasMoreTokens())
			{
				currToken = tokenizer.nextToken();
				if(StringUtils.isNotBlank(currToken))
				{
					result.put(currToken, defaultValue);
				}
			}
		}
		
		return result;
	}
	
	
	public static String buildSetFlashVarsJavascript(Map<String, String> keyValueMap)
	{
		return buildSetFlashVarsJavascript(keyValueMap, "flashvars");
	}
	
	public static String buildSetFlashVarsJavascript(Map<String, String> keyValueMap, String flashVarsVariableName)
	{
		StringBuilder resultBuilder = new StringBuilder();
		if(keyValueMap != null)
		{
			String currValue = null;
			int validKeyCount = 0;
			for(String currKey : keyValueMap.keySet())
			{
				currValue = keyValueMap.get(currKey);
				if(StringUtils.isNotBlank(currKey) && currValue != null)
				{
					if(validKeyCount > 0)
					{
						resultBuilder.append("\n");
					}
					
					resultBuilder.append(flashVarsVariableName).append("[\"").append(currKey).append("\"] = ").append("\"").append(currValue).append("\"").append(";");
					
					validKeyCount++;
				}
			}
		}
		
		return resultBuilder.toString();
	}
	
	public static String buildFlashVarsQueryString(Map<String, String> keyValueMap)
	{
		StringBuilder resultBuilder = new StringBuilder();
		if(keyValueMap != null)
		{
			String currQueryStringParam = null;
			String currValue = null;
			int validParamCount = 0;
			for(String currKey : keyValueMap.keySet())
			{
				currValue = keyValueMap.get(currKey);
				if(StringUtils.isNotBlank(currKey) && currValue != null)
				{
					if(validParamCount > 0)
					{
						resultBuilder.append("&");
					}
					
					try
					{
						currQueryStringParam = URLEncoder.encode(currKey, "UTF-8");
						currQueryStringParam += "=";
						currQueryStringParam += URLEncoder.encode(currValue, "UTF-8");
						resultBuilder.append(currQueryStringParam);
						validParamCount++;
					}
					catch(UnsupportedEncodingException ignored)
					{
						// Should not occur
					}
				}
			}
		}
		
		return resultBuilder.toString();
	}
	
	/**
	 * Create the "flash vars" in javascript notation.
	 * @param propertyKeys Keys of properties to include in flash vars
	 * @return Javascript notation of flash vars
	 * 
	 * @deprecated Use buildPropertiesSetFlashVarsJavascript(java.lang.String, java.lang.String) instead.
	 */
	public static String getJavascriptFlashVars(final String propertyKeys) {
		return buildPropertiesSetFlashVarsJavascript(propertyKeys, "flashvars");
	}
	
	/**
	 * Create the "flash vars" in querystring notation.
	 * @param propertyKeys Keys of properties to include in flash vars
	 * @return Querystring notation of flash vars
	 * 
	 * @deprecated Use buildPropertiesFlashVarsQueryString(java.lang.String) instead.
	 */
	public static String getQuerystringFlashVars(final String propertyKeys) {
		return buildPropertiesFlashVarsQueryString(propertyKeys);
	}
}
