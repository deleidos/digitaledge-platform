package com.saic.rtws.commons.util.expression.primitive;

import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.JsonPath;
import com.saic.rtws.commons.util.expression.Literal;
import com.saic.rtws.commons.util.expression.Matcher;
import com.saic.rtws.commons.util.expression.Parser;
import com.saic.rtws.commons.util.regex.PatternUtils;

/**
 * Matcher that evaluates whether a given value matches the configured wildcard expression.
 */
public class Like implements Matcher {

	private static Parser parser = Parser.compile("\\s*[lL][iI][kK][eE]\\s*(\\l)\\s*");
	
	private String original;
	private Pattern criteria;
	
	public Like(Object criteria) {
		if(criteria!=null){
		this.original = criteria.toString();
		this.criteria = Pattern.compile(PatternUtils.convertUnixWildcardToRegex(criteria.toString()));
		}else{
			this.original=null;
			this.criteria=null;
		}
	}
	
	public boolean matches(Object value, JSONObject fullRecord) {
        if(original == null || value ==null || (value instanceof JSONObject && ((JSONObject)value).isNullObject()))
            return false;

		Pattern compPattern = criteria;
		if(original.startsWith("@"))
			 compPattern = Pattern.compile(PatternUtils.convertUnixWildcardToRegex((String)JsonPath.simpleLookup(fullRecord, original.substring(1), false)));
		
		return compPattern.matcher(value.toString()).matches();
		
	}

	public String toString() {
		return String.format("LIKE %s", Literal.toString(original));
	}
	
	public static Like valueOf(String expression) {
		return new Like(parser.parse(expression).getTermAsLiteral(0));
	}
	
}
