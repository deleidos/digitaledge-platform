package com.saic.rtws.commons.util.expression.primitive;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.util.JsonPath;
import com.saic.rtws.commons.util.expression.Literal;
import com.saic.rtws.commons.util.expression.Matcher;
import com.saic.rtws.commons.util.expression.Parser;
import com.saic.rtws.commons.util.expression.TermList;

/**
 * Matcher that evaluates whether a given value is within a configured range.
 */
public class Between implements Matcher {

	private static Parser parser = Parser.compile("\\s*[bB][eE][tT][wW][eE][eE][nN]\\s*([\\[\\(])\\s*(\\l)\\s*,\\s*(\\l)\\s*([\\]\\)])\\s*");
	
	private Object low;
	private Object high;
	private boolean leftInclusive;
	private boolean rightInclusive;
	
	public Between(Object low, Object high) {
		this(low, high, true);
	}
	
	public Between(Object low, Object high, boolean inclusive) {
		this(low, high, inclusive, inclusive);
	}
	
	public Between(Object low, Object high, boolean leftInclusive, boolean rightInclusive) {
		this.low = low;
		this.high = high;
		this.leftInclusive = leftInclusive;
		this.rightInclusive = rightInclusive;
	}
	
	public boolean matches(Object value,JSONObject fullRecord) {
		if(value instanceof Comparable) {
			return matches((Comparable)value,fullRecord);
		} else {
			return false;
		}
	}

	public boolean matches(Comparable value,JSONObject fullRecord) {
		Object compToLow = low;
		Object compToHigh = high;
        if(compToLow == null || compToHigh == null || value == null || (compToLow instanceof JSONObject && ((JSONObject)compToLow).isNullObject()) || (compToHigh instanceof JSONObject && ((JSONObject)compToHigh).isNullObject()) || (value instanceof JSONObject && ((JSONObject)value).isNullObject()))
            return false;

		if(compToLow instanceof String && ((String)compToLow).startsWith("@"))
			compToLow = JsonPath.simpleLookup(fullRecord, ((String)compToLow).substring(1), false);
		if(compToHigh instanceof String && ((String)compToHigh).startsWith("@"))
			compToHigh = JsonPath.simpleLookup(fullRecord, ((String)compToHigh).substring(1), false);
		
		
		// If one of the values is a double, then all the values need to be converted to doubles so they can be compared.
		if ((value instanceof Double) || (compToLow instanceof Double) || (compToHigh instanceof Double)) {
			if (value instanceof Integer) {
				value = new Double(((Integer) value).doubleValue());
			}
			
			if (compToLow instanceof Integer) {
				compToLow = new Double(((Integer) compToLow).doubleValue());
			}
			
			if (compToHigh instanceof Integer) {
				compToHigh = new Double(((Integer) compToHigh).doubleValue());
			}
		}
		
		int cmpLow = (value== null) ? Integer.MIN_VALUE : value.compareTo(compToLow);
		int cmpHigh = (value== null) ? Integer.MAX_VALUE : value.compareTo(compToHigh);
		if(cmpLow > 0 && cmpHigh < 0) {
			return true;
		} else if(cmpLow < 0 || cmpHigh > 0) {
			return false;
		} else {
			return cmpLow == 0 && leftInclusive || cmpHigh == 0 && rightInclusive;
		}
	}
	
	public String toString() {
		
		String leftBrace = leftInclusive ? "[" : "(";
		String leftValue = Literal.toString(low);
		String rightValue = Literal.toString(high);
		String rightBrace = rightInclusive ? "]" : ")";
		
		return String.format("BETWEEN %s%s,%s%s", leftBrace, leftValue, rightValue, rightBrace);
		
	}
	
	private void determineTypes(Comparable value, Object compToLow, Object compToHigh) {
		if ((value != null) && ((value instanceof Double) || (compToLow instanceof Double) || (compToHigh instanceof Double))) {
			if (value instanceof Integer) {
				value = new Double(((Integer) value).doubleValue());
			}
			
			if (compToLow instanceof Integer) {
				compToLow = new Double(((Integer) compToLow).doubleValue());
			}
			
			if (compToHigh instanceof Integer) {
				compToHigh = new Double(((Integer) compToHigh).doubleValue());
			}
		}
	}
	
	public static Between valueOf(String expression) {
		TermList terms = parser.parse(expression);
		Object low = terms.getTermAsLiteral(1);
		Object high = terms.getTermAsLiteral(2);
		boolean leftInclusive = terms.getTermAsText(0).equals("[");
		boolean rightInclusive = terms.getTermAsText(3).equals("]");
		return new Between(low, high, leftInclusive, rightInclusive);
	}
	
}
