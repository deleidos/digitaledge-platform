package com.saic.rtws.commons.util.expression.collection;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.expression.Matcher;

/**
 * Matcher that evaluates whether the every element of the given list matches the configured criteria. The configured
 * criteria can be a primitive expression or a complex expression as long is it implements the Matcher interface.
 * Typically a single criteria is specified and matches() is called for a list of possible values. The list is
 * considered a match if at all of the elements match the configured criteria. It is also possible to configure a set of
 * criteria, where the values from the list are matched against the configured criteria by ordinal position.
 */
public class Every implements Matcher {

	private Matcher[] criteria;

	public Every(Matcher... criteria) {
		this.criteria = criteria;
	}

	public boolean matches(Object values,JSONObject fullRecord) {
		if (values == null) {
			return false;
		} else if (values instanceof Object[]) {
			return matches((Object[]) values, fullRecord);
		} else if (values instanceof Iterable) {
			return matches((Iterable) values, fullRecord);
		} else {
			return false;
		}
	}

	public boolean matches(Object[] values,JSONObject fullRecord) {
		int index = 0;
		for (Object value : values) {
			if (!criteria[index].matches(value, fullRecord)) { return false; }
			if (criteria.length != 1) {
				index++;
			}
		}
		return true;
	}

	public boolean matches(Iterable values,JSONObject fullRecord) {
		int index = 0;
		for (Object value : values) {
			if (!criteria[index].matches(value,fullRecord)) { return false; }
			if (criteria.length != 1) {
				index++;
			}
		}
		return true;
	}

}
