package com.saic.rtws.commons.util.expression.primitive;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.expression.Matcher;
import com.saic.rtws.commons.util.expression.Parser;

/**
 * Matcher that evaluates whether the a given value is not equal to the configured value.
 */
public class IsNull implements Matcher {

	private static String isNullExpression = "isnull";
	
	public IsNull() {
	}
	
	public boolean matches(Object value, JSONObject fullRecord) {
		return value == null || (value instanceof JSONObject && ((JSONObject)value).isNullObject());
	}

	public String toString() {
		return String.format("IsNull");
	}
	
	public static IsNull valueOf(String expression) throws Exception{
		if(expression.toLowerCase().contains(isNullExpression.toLowerCase()))
			return new IsNull();
		throw new Exception("not a match");
	}
	
}
