package com.saic.rtws.commons.util.fileset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.saic.rtws.commons.util.DataModelBasedNames;

public class FilePatternRegistry {
	
	private HashMap<String, HashMap<String, String>> registry;
	
	protected FilePatternRegistry() {
		registry = new HashMap<String, HashMap<String, String>>();
	}

	/** Return the patterns associated with rootName and implName */
	public HashMap<String, String> getPatterns(String rootName, String implName) {
		return registry.get(rootName + "." + implName);
	}

	/**
	 * Return a hash map of patterns. The input strings must be a set of resource name+
	 * filename pattern strings. 
	 * @param strings 
	 * @return hash map
	 */
	public HashMap<String, String> makePatterns( String...strings) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i=0; i<strings.length; i+=2) {
			map.put(strings[i], strings[i+1]);
		}
		return map;
	}

	/**
	 * Return a list of regular expression patterns that match the filename patterns
	 * for the given pattern map.
	 * @param patterns pattern map
	 * @return list of regular expressions
	 */
	public List<String> fileRegexPatterns( HashMap<String,String> patterns ) {
		ArrayList<String> result = new ArrayList<String>();
		Collection<String> filenamePatterns = patterns.values();
		for (String filenamePattern : filenamePatterns) {
			String parts[] = filenamePattern.split("@");
			String regex = "";
			for (int i=0; i<parts.length; i++) {
				if ((i % 2) == 0) {
					regex = regex + parts[i];
				}
				else {
					regex = regex + ".*";
				}
			}
			result.add(regex);
		}
		return result;
	}
		
	static FilePatternRegistry instance = null;
	public static FilePatternRegistry get() {
		return instance;
	}
	
	static {
		instance = new FilePatternRegistry();

		instance.registry.put("models." + ConfigFileSetResource.NAME,
			instance.makePatterns(
				DataModelBasedNames.CANONICAL, "canonical_@FSNAME@_v@VERSION@.json",
				DataModelBasedNames.ENRICHED,  "enriched_@FSNAME@_v@VERSION@.json",
				DataModelBasedNames.TRANSLATE, "xlate_@PARAM0@_to_@FSNAME@_v@VERSION@.json" ));

		instance.registry.put("models." + JcrFileSetResource.NAME,
			instance.makePatterns(
				DataModelBasedNames.CANONICAL, "canonical.json",
				DataModelBasedNames.ENRICHED,  "enriched.json",
				DataModelBasedNames.TRANSLATE, "xlate_@PARAM0@.json" ));
		
		instance.registry.put("repository." + JcrFileSetResource.NAME,
				instance.makePatterns("content", "@FSNAME@-@VERSION@.@PARAM0@"));
	}
}