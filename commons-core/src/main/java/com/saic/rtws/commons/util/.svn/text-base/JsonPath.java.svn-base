package com.saic.rtws.commons.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JSON Path utility class.
 */
public class JsonPath {

	/** Constructor. */
	protected JsonPath() { }
	
	/**
	 * Performs a simple JSONPath lookup, where only dot notation and array [] references are supported.
	 * @param json		  JSON object to examine
	 * @param path        string path using dot notation plus array references only
	 * @param indexesZero force all array references to reference element zero only
	 * @return The referenced object or null
	 */
	public static Object simpleLookup(JSONObject json, String path, boolean indexesZero) {
		Object result = null;
		
		// Remove indexing sequences
		// path = path.replaceAll("\\[[0-9]*\\]","");
		if (indexesZero) {
			path = path.replaceAll("\\[[0-9]*\\]", "[0]" );
		}

		// Change index references from [x] to .x
        path = path.replaceAll("\\[(.*?)\\]", ".#$1");
        
		// Split on the dot; resolve an empty path to the root.
		String names[] = new String[0];
		if(path.length() != 0) {
			names = path.split("\\.");
		}
		
		// Go through each name, stepping into objects and arrays as needed
		Object curObj = json;
		Object obj = null;
		int index = 0;
		while ((curObj != null) &&
			   (index < names.length) &&
			   ((curObj instanceof JSONObject) || (curObj instanceof JSONArray))) {
			
			String name = names[index];
			if (name.startsWith("#")) {
				obj = null;
				if (curObj instanceof JSONArray) {
					int arrayIndex = Integer.parseInt(name.substring(1));
					obj = ((JSONArray)curObj).get(arrayIndex);					
				}
			}
			else {
				obj = ((JSONObject)curObj).get(name);				
			}
			
			curObj = obj;
			index++;
		}
		
		if ((index >= names.length) && (curObj != null)) {
			result = curObj;
		}
		
		return result;		
	}

	/**
	 * Iterate over a JSONObject or JSONArray.
	 * 
	 * @param obj		Current json object
	 * @param curIndex	Current index within the elemnts array
	 * @param elements	Array of element references
	 * @param results	Final results array
	 * @param recurse	true if recursion is enabled
	 */
	private static void walk(Object obj, int curIndex,  ArrayList<String> elements, ArrayList<Object> results, boolean recurse) {
		if (obj instanceof JSONArray) {			
			JSONArray jsonArray = (JSONArray)obj;
			for (Object nextObj : jsonArray) {
				trace( nextObj, curIndex, elements, results, recurse) ;
			}
		}
		else if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject)obj;
			for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
			    String nextKey = (String)it.next();
			    trace( jsonObject.get(nextKey), curIndex, elements, results, recurse);
			}			
		}
	}
	
	/**
	 * Slice an array reference.
	 * @param obj		Current json object
	 * @param curIndex	Current index within the elemnts array
	 * @param elements	Array of element references
	 * @param results	Final results array
	 */
	private static void sliceArray(Object obj, int curIndex,  ArrayList<String> elements, ArrayList<Object> results ) {
		JSONArray jsonArray = (JSONArray)obj;
		String curElement = elements.get(curIndex);
		String indexExpr = curElement.split(" ")[1];
		
		try {
			if (indexExpr.equals("*")) {
				// All array elements
				walk( obj, ++curIndex, elements, results, false );
			}
			else if (indexExpr.matches("[0-9]+(,[0-9]+)*")) {
				// Only those referenced using a comma delimited list
				for (String indexString : indexExpr.split(",")) {
					int index = Integer.parseInt(indexString);
					trace(jsonArray.get(index), ++curIndex, elements, results, false); 
				}
			}
			else if (indexExpr.matches("[0-9]+:[0-9]+:[0-9]+")) {
				// start:end:step
				String params[] = indexExpr.split(":");
				int start = Integer.parseInt(params[0]);
				int end   = Integer.parseInt(params[1]);
				int step  = Integer.parseInt(params[2]);
				for (int i=start; i<end; i+=step) {
					trace(jsonArray.get(i), ++curIndex, elements, results, false);
				}
			}
		}
		catch (NumberFormatException e) {
			// Ignore
		}
	}

	// $.store.book[*].author		store; book; #ARRAY *; author
	// $..author					#RECURSE author
	// $.store.*					store *
	// $.store..price				store #RECURSE price
	// $..book[2]					#RECURSE book #ARRAY 2
	// $..book[1,3]					#RECURSE book #ARRAY 1,3
	// $..book[1:3:1]				#RECURSE book #ARRAY 1:3:1
	// $..*							#RECURSE *

	/**
	 * Trace a JSONPath reference.
	 * 
	 * @param obj		Current json object
	 * @param curIndex	Current index within the elemnts array
	 * @param elements	Array of element references
	 * @param results	Final results array
	 * @param recurse	true if recursion is enabled
	 */
	private static void trace(Object obj, int curIndex, ArrayList<String> elements, ArrayList<Object> results, boolean recurse) {
		if (curIndex >= elements.size()) {
			results.add(obj);
		}
		else {
			String curElement = elements.get(curIndex);
			if (curElement.equals("#RECURSE")) {
				trace(obj, ++curIndex, elements, results, true );
			}
			else if (curElement.startsWith("#ARRAY")) {
				if (obj instanceof JSONArray) {
					sliceArray( obj, curIndex, elements, results );
				}
			}
			else if (curElement.equals("*")) {
				if (obj instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject)obj;
					for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
					    String nextKey = (String)it.next();
					    trace(jsonObject.get(nextKey), ++curIndex, elements, results, recurse);
					}	
				}
			}
			else if (obj instanceof JSONObject){
				Object nextObj = ((JSONObject)obj).get(curElement);
				if (nextObj != null) {
					trace( nextObj, ++curIndex, elements, results, false );
				}
				else if (recurse) {
					// Didn't find name, so walk through all items and keep looking
					walk( obj, curIndex, elements, results, recurse );					 
				}
			}
			else if ((obj instanceof JSONArray) && recurse) {
				walk( obj, curIndex, elements, results, recurse );					 
			}
		}
	}

	/**
	 * Query a json object using an expression. The following expressions are supported:
	 * $ 			- References the root
	 * .<name>		- Move to child object <name>
	 * ..<name>		- Recursively find <name>
	 * *			- If a name equals this, it matches all names
	 * [*]			- All elements of an array
	 * [n]			- The nth element of an array
	 * [m,n,...]	- Each specific referenced elements of the array
	 * [start:end:step] - Iterate over the array using start, end, and step
	 */
	public static Object[] query(JSONObject json, String expression) {
		
        // Remove trailing dots
        String normalized = expression.replaceFirst("[\\.]+$", "");

        // Recursion operator
        normalized = normalized.replaceAll("\\.\\.", ".#RECURSE." );
        
        // Array references
        normalized = normalized.replaceAll("\\[(.*?)\\]", ".#ARRAY $1");

        // Remove root reference
        normalized = normalized.replaceFirst("^\\$\\.", "");

        ArrayList<String> elements = new ArrayList<String>(Arrays.asList(normalized.split("\\.")));
        ArrayList<Object> results = new ArrayList<Object>();
        
        trace( json, 0, elements, results, false );
        
        return results.toArray();
	}
	
	public static String parent(String path) {
		int index = path.lastIndexOf(".");
		return (index == -1) ? "" : path.substring(0, index);
	}
	
	public static String child(String path) {
		int index = path.lastIndexOf(".");
		return path.substring(index + 1);
	}
	
	/**
	 * Simple JSON path helpers
	 */
	
	/**
	 *  Simple JSON path where only dot notation and array [] references are supported.
	 *  The regular expression below is a bit more restrictive than JSON specification allows: space, dot, comma, and brackets are not allowed within the path name.
	 *  Original string: ^\s*([^\]\[.\\\/\s\x22]+\s*(\[\s*\d+\s*\])?)(\s*[.]\s*([^\]\[.\\\/\s\x22]+(\s*\[\s*\d+\s*])?))*\s*$
	 *  Note that \x22 is quote character. The code version is used here so the string below could be used in RegExpValidator that chokes on quotes.
	 */
	public static final String SIMPLE_JSON_PATH_VALIDATOR_REGEX = "^\\s*([^\\]\\[.\\\\\\/\\s\\x22,]+\\s*(\\[\\s*\\d+\\s*\\])?)(\\s*[.]\\s*([^\\]\\[.\\\\\\/\\s\\x22,]+(\\s*\\[\\s*\\d+\\s*])?))*\\s*$";
	
	/**
	 * Comma separated list of simple JSON path(s).
	 */
	public static final String SIMPLE_JSON_PATH_LIST_VALIDATOR_REGEX = "^\\s*([^\\]\\[.\\\\\\/\\s\\x22,]+\\s*(\\[\\s*\\d+\\s*\\])?)(\\s*[.,]\\s*([^\\]\\[.\\\\\\/\\s\\x22,]+(\\s*\\[\\s*\\d+\\s*])?))*\\s*$";

	/**
	 * Validates a simple path
	 * @param path Simple path to validate
	 * @param allowNullOrEmpty Determines whether an null or empty path is considered valid
	 * @return True if the path is valid and false otherwise
	 */
	public static boolean isValidSimpleJsonPath(String path, boolean allowNullOrEmpty)
	{
		return path == null || (path=path.trim()).isEmpty() ? allowNullOrEmpty : path.matches(SIMPLE_JSON_PATH_VALIDATOR_REGEX);
	}
	
	/**
	 * Validates comma separated list of simple paths
	 * @param pathList List of simple paths to validate
	 * @param allowNullOrEmpty Determines whether an null or empty list is considered valid
	 * @return True if the list of paths is valid and false otherwise
	 */
	public static boolean isValidSimpleJsonPathList(String pathList, boolean allowNullOrEmpty)
	{
		return pathList == null || (pathList=pathList.trim()).isEmpty() ? allowNullOrEmpty : pathList.matches(SIMPLE_JSON_PATH_LIST_VALIDATOR_REGEX);
	}
	
	/**
	 * Normalizes the provided simple path by removing all white spaces
	 * @param path Simple path to normalize
	 * @return Normalized path
	 * @throws IllegalArgumentException If the path is not a valid simple path
	 */
	public static String simpleNormalizePath(String path)  throws IllegalArgumentException
	{
		if ( path == null || (path=path.trim()).isEmpty() )
			return null;
		if ( path.matches(SIMPLE_JSON_PATH_VALIDATOR_REGEX) )
			return path.replaceAll("\\s+", "");
		throw new IllegalArgumentException(String.format("String '%s' is not a supported simple JSON path", path));
	}
	
	/**
	 * Normalizes the provided list of simple paths by removing all white spaces and splitting on commas
	 * @param pathList List of simple paths to validate
	 * @return Normalized list of paths
	 * @throws IllegalArgumentException
	 */
	public static String[] simpleNormalizePathList(String pathList)  throws IllegalArgumentException
	{
		if ( pathList == null || (pathList=pathList.trim()).isEmpty() )
			return null;
		if ( pathList.matches(SIMPLE_JSON_PATH_LIST_VALIDATOR_REGEX) )
		{
			pathList = pathList.replaceAll("\\s+", "");
			return pathList.split(",");
		}
		throw new IllegalArgumentException(String.format("String '%s' is not a supported list of simple JSON paths", pathList));
	}
	
	/**
	 * Finds or creates a JSONObject specified by a simple path in string format. 
	 * If the object does not exist it will be created along with 
	 * all intermediate objects, arrays, and array indexes that do not exist.
	 * @param json Parent JSONObject to search and optionally update
	 * @param path Simple path to find or create
	 * @return Parent object if the path is empty, created object or null if the object can not be created, for example, due to path part type conflict.
	 */
	public static JSONObject simpleGetOrCreate(JSONObject json, String path)
	{
		if ( !isValidSimpleJsonPath(path,false) )
			return json;
		return simpleGetOrCreate(json, path.replaceAll("\\s+", "").split("\\."));
	}
	
	/**
	 * Helper method that adds missing elements up to specified index in a JSON array
	 * @param array JSON array to add elements to
	 * @param index Desired element index
	 * @return Object that corresponds to the newly created element at specified index 
	 */
	private static Object addArrayElement(JSONArray array, int index)
	{
		int size = array.size();
		if ( size <= index )
		{
			for( int i = size; i <= index; i++ )
				array.add(i, new JSONObject());
		}
		assert( array.size() > index );
		return array.get(index);
	}
	
	/**
	 * Finds or creates a JSONObject specified by a simple path provided as a string array of path names.
	 * @param json Parent JSONObject to search and optionally update
	 * @param path Simple path to find or create
	 * @return Parent object if the path is empty, created object or null if the object can not be created, for example, due to path part type conflict.
	 */
	public static JSONObject simpleGetOrCreate(JSONObject json, String[] path)
	{
		if ( path == null || path.length == 0 )
			return json;
		Pattern indexPattern = Pattern.compile("\\[(.*?)\\]");

		boolean mustCreate = false;
		Object obj = null;
		Object curObj = json;
		for( String name : path)
		{
			if ( !(curObj instanceof JSONObject || curObj instanceof JSONArray ) )
				break;
			Matcher matcher = indexPattern.matcher(name);
			int index = -1;
			if ( matcher.find() ) {
				int groupCount = matcher.groupCount();
				assert(groupCount == 1);
				index = Integer.parseInt(name.substring(matcher.start(1),matcher.end(1)));
				name  = matcher.replaceFirst("");
			}

			if ( !mustCreate )
			{
				obj = ((JSONObject)curObj).get(name);
				if ( obj != null )
				{
					if ( index != -1  && obj instanceof JSONArray )
						obj = addArrayElement((JSONArray)obj,index);
					
					curObj = obj;
					continue;
				}
				mustCreate = true;
			}
			if ( index == -1 ) {
				((JSONObject)curObj).put(name, new JSONObject());
				curObj = ((JSONObject)curObj).get(name);
			} else {
				((JSONObject)curObj).put(name, new JSONArray());
				obj = ((JSONObject)curObj).get(name);
				curObj = addArrayElement((JSONArray)obj,index);
			}
		}
		return  curObj instanceof JSONObject ?  (JSONObject)curObj : null;
	}
}