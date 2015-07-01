package com.saic.rtws.commons.util.expression.primitive;


import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


import com.saic.rtws.commons.util.expression.Matcher;

public class Matchers {
	
	private static Set<String> loggedExpressions = new HashSet<String>();
	private static Logger log = Logger.getLogger(Matchers.class);

	public static Matcher compile(String expression) throws IllegalArgumentException{
		try {return IsEqual.valueOf(expression);} catch(Exception ignore) { }
		try {return IsLess.valueOf(expression);} catch(Exception ignore) { }
		try {return IsGreater.valueOf(expression);} catch(Exception ignore) { }
		try {return NotEqual.valueOf(expression);} catch(Exception ignore) { }
		try {return NotLess.valueOf(expression);} catch(Exception ignore) { }
		try {return NotGreater.valueOf(expression);} catch(Exception ignore) { }
		try {return Between.valueOf(expression);} catch(Exception ignore) { }
		try {return In.valueOf(expression);} catch(Exception ignore) { }
		try {return Like.valueOf(expression);} catch(Exception ignore) { }
		try {return NotNull.valueOf(expression);} catch(Exception ignore){ }
		//error case - does not match any expression
		if(!loggedExpressions.contains(expression)){
			log.warn("Invalid expression syntax '" + expression + "'.");
			loggedExpressions.add(expression);
			
		}
		throw new IllegalArgumentException("Invalid expression syntax '" + expression + "'.");
	}
	
	
	
}
