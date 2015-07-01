package com.saic.rtws.commons.util.expression.primitive;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.JsonPath;
import com.saic.rtws.commons.util.expression.Literal;
import com.saic.rtws.commons.util.expression.Matcher;
import com.saic.rtws.commons.util.expression.Parser;

/**
 * Matcher that evaluates whether a given value is equal to the configured value.
 */
public class IsEqual implements Matcher {

	private static Parser parser = Parser.compile("\\s*=\\s*(\\l)\\s*");
	
	private Object criteria;
	
	public IsEqual(Object criteria) {
		this.criteria = criteria;
	}
	
	public boolean matches(Object value, JSONObject fullRecord) {
		Object compTo = criteria;
        if(compTo == null || value ==null || (compTo instanceof JSONObject && ((JSONObject)compTo).isNullObject()) || (value instanceof JSONObject && ((JSONObject)value).isNullObject()))
            return false;

		if(compTo instanceof String && ((String)compTo).startsWith("@")) {
			compTo = JsonPath.simpleLookup(fullRecord, ((String)compTo).substring(1), false);
		} 
		
		if ((compTo instanceof Number) && (value instanceof Number)) {
			return ((Number) value).doubleValue() == ((Number) compTo).doubleValue();
		} else {
			return compTo.equals(value);
		}
	}

	public String toString() {
		return String.format("=%s", Literal.toString(criteria));
	}
	
	public static IsEqual valueOf(String expression) {
		return new IsEqual(parser.parse(expression).getTermAsLiteral(0));
	}
	
}
