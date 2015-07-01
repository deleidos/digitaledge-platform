package com.saic.rtws.commons.util.expression.primitive;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.expression.Matcher;
import com.saic.rtws.commons.util.expression.Parser;

/**
 * Matcher that evaluates whether the a given value is not equal to the configured value.
 */
public class NotNull implements Matcher {

	private static String notNullExpression = "notnull";
	
	public NotNull() {
	}
	
	public boolean matches(Object value, JSONObject fullRecord) {
		if(value==null || (value instanceof JSONObject && ((JSONObject)value).isNullObject()))
			return false;
		return true;
		//return value!=null;
	}

	public String toString() {
		return String.format("notNull");
	}
	
	public static NotNull valueOf(String expression) throws Exception{
		if(expression.toLowerCase().contains(notNullExpression.toLowerCase()))
			return new NotNull();
		throw new Exception("not a match");
	}
	
}
