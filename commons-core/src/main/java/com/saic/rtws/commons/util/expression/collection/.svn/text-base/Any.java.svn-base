package com.saic.rtws.commons.util.expression.collection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.expression.Matcher;
import com.saic.rtws.commons.util.expression.primitive.Matchers;

/**
 * Matcher that evaluates whether the any element of a given list match the configured criteria. The configured criteria
 * can be a primitive expression or a complex expression as long is it implements the Matcher interface. Typically a
 * single criteria is specified and matches() is called for a list of possible values. The list is considered a match if
 * at least one of the elements matches the configured criteria. It is also possible to configure a set of criteria,
 * where the criteria are evaluated as a compound OR expression.
 */
public class Any implements Matcher {

	private Matcher[] criteria;

	public Any(Matcher... criteria) {
		this.criteria = criteria;
	}

	public boolean matches(Object values,JSONObject fullRecord) {
		if (values == null) {
			return false;
		} else if (values instanceof Object[]) {
			return matches((Object[]) values,fullRecord);
		} else if (values instanceof Iterable) {
			return matches((Iterable) values,fullRecord);
		} else {
			for (Matcher option : criteria)
				if (option.matches(values,fullRecord)) { return true; }
			
			return false;
		}
	}

	public boolean matches(Object[] values,JSONObject fullRecord) {
		for (Object value : values) {
			for (Matcher option : criteria) {
				if (option.matches(value,fullRecord)) { return true; }
			}
		}
		return false;
	}

	public boolean matches(Iterable values,JSONObject fullRecord) {
		for (Object value : values) {
			for (Matcher option : criteria) {
				if (option.matches(value,fullRecord)) { return true; }
			}
		}
		return false;
	}

	public static Matcher any(Collection<Object> collection) {
		LinkedList<Matcher> list = new LinkedList<Matcher>();
		for (Object value : collection) {
			if (value instanceof Map) {
				list.add(SuchThat.suchThat((Map) value));
			} else if (value instanceof Collection) {
				list.add(any((Collection) value));
			} else {
				Matcher m = Matchers.compile(value.toString());
				if(m!=null)
					list.add(m);
			}
		}
		return new Any(list.toArray(new Matcher[list.size()]));
	}

}
