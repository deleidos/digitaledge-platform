package com.saic.rtws.commons.util.expression.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.expression.Matcher;
import com.saic.rtws.commons.util.expression.primitive.Matchers;

/**
 * Matcher that implements a "query by example" evaluation strategy for testing the key value pairs within a map. Works
 * for simple maps (containing only key value pairs) as well as nested maps (where value can themselves be maps or
 * collections).
 * 
 * For the case where you wish to evaluate a nested map, the SuchThat instance must also be nested, and must have the
 * same structure as the map being evaluated. Thus, for any key in a map that is a primitive value, the SuchThat should
 * have a primitive matcher for the same key; for any key in a map that is a collection, the SuchThat should have an Any
 * or Every matcher with the same key; and for any key in the map that has a map, the SuchThat should have a nested
 * SuchThat for the same key.
 * 
 * When criteria is specified for multiple keys within a map, each is ANDed to determine the overall match against the
 * map; boolean logic is not supported.
 */
@SuppressWarnings("unchecked")
public class SuchThat implements Matcher {

	/** List of fields to be evaluated. */
	private Map<String, Matcher> fields = new HashMap<String, Matcher>();

	/**
	 * Constructor.
	 */
	public SuchThat() {
		super();
	}

	/**
	 * Adds the given matcher to the current list of evaluation criteria.
	 * 
	 * @param field
	 *            The map key to be evaluated.
	 * @param matcher
	 *            The criteria to be matched.
	 */
	public void addField(String field, Matcher matcher) {
		if (matcher == null) {
			fields.remove(field);
		} else {
			fields.put(field, matcher);
		}
	}

	/**
	 * Determines whether the configured criteria match the given object.
	 */
	public boolean matches(Object object, JSONObject fullRecord) {
		if (object == null) {
			return false;
		} else if (object instanceof Map) {
			return matches((Map) object, fullRecord);
		} else {
			return false;
		}
	}

	/**
	 * Determines whether the configured criteria match the given object.
	 */
	public boolean matches(Map map,JSONObject fullRecord) {
		for (String field : fields.keySet()) {
			if (!fields.get(field).matches(map.get(field),fullRecord)) { return false; }
		}
		return true;
	}

	/**
	 * Constructs (recursively) a SuchThat matcher based on the given nested map example. Objects that appear as values
	 * within the example map must be instances of one of the following classes (Map, Collection, String). Any Map
	 * instances are converted into nested SuchThat matchers in the resulting filter, while Collection instances are
	 * converted to Any matchers. String instances are interpreted as textual expressions and are converted to
	 * appropriate matchers by calling Matchers.compile().
	 * 
	 * NOTE: This method is a temporary solution. When constructing a matcher for nested objects it handles all
	 * Collection objects with and Any matcher. This is limited as there are other logical matching algorithms that one
	 * might want to use to express a filter (possible multiple different algorithms for different field in the same
	 * filter. It may be favorable to move this method into some kind of builder class.
	 */
	public static SuchThat suchThat(Map<String, Object> map) {
		SuchThat example = new SuchThat();
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value == null) {
				throw new NullPointerException();
			} else if (value instanceof Map) {
				example.addField(key, suchThat((Map) value));
			} else if (value instanceof Collection) {
				example.addField(key, Any.any((Collection) value));
			} else {
				Matcher m = Matchers.compile(value.toString());
				if(m!=null)
					example.addField(key, m);
			}
		}
		return example;
	}

}
