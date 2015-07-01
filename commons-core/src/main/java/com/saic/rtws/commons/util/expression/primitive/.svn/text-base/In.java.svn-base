package com.saic.rtws.commons.util.expression.primitive;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.JsonPath;
import com.saic.rtws.commons.util.expression.Literal;
import com.saic.rtws.commons.util.expression.Matcher;
import com.saic.rtws.commons.util.expression.Parser;

/**
 * Matcher that evaluates whether a given value is an element of the configured list.
 */
public class In implements Matcher {

	private static final Parser parser = Parser.compile("\\s*[iI][nN]\\s*\\(\\s*(\\L)\\s*\\)\\s*");
	
	private Object[] criteria;
	
	public In(Object... criteria) {
		this.criteria = criteria;
	}
	
	public boolean matches(Object value, JSONObject fullRecord) {
		if(value==null || (value instanceof JSONObject && ((JSONObject)value).isNullObject()))
			return false;
		for(Object element : criteria) {
			Object compTo = element;
			if(compTo instanceof String && ((String)compTo).startsWith("@"))
				compTo = JsonPath.simpleLookup(fullRecord, ((String)compTo).substring(1), false);
			
			if(compTo == null || (compTo instanceof JSONObject && ((JSONObject)compTo).isNullObject())) {
				return false; //value == null;
			} 
			
			if ((compTo instanceof Number) && (value instanceof Number)) {
				if (((Number) value).doubleValue() == ((Number) compTo).doubleValue()) {
					return true;
				}
			} else if(compTo.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return String.format("IN(%s)", Literal.toString(criteria));
	}
	
	public static In valueOf(String expression) {
		return new In(parser.parse(expression).getTermAsList(0));
	}
	
}
