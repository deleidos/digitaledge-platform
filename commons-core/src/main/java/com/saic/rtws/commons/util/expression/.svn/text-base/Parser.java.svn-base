package com.saic.rtws.commons.util.expression;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.saic.rtws.commons.util.expression.Literal.LITERAL;
import static com.saic.rtws.commons.util.expression.Literal.LIST;

/**
 * Utility class for parsing simple expressions. Given a regular expression containing
 * capturing groups, this class matches the pattern against a given text string, and if
 * it matches extracts each captured group as a term.
 * 
 * As a convenience, this class adds two additional character groups to the standard ones
 * available in the Pattern class. The sequence \l and \L can be used to match a java style
 * literal value or a comma separated list of literal values.
 */
public class Parser {

	/** The pattern to be matched. */
	private Pattern pattern;
	
	/**
	 * Constructor.
	 */
	private Parser(String pattern) {
		this.pattern = Pattern.compile(substituteLiteralPlaceholders(pattern)); 
	}
	
	/**
	 * Factory style instantiation.
	 */
	public static Parser compile(String pattern) {
		return new Parser(pattern);
	}
	
	/**
	 * Parses the given expression using the configured pattern, and returns the matching groups. 
	 */
	public TermList parse(String expression) {
		LinkedList<String> buffer = new LinkedList<String>();
		Matcher matcher = pattern.matcher(expression);
		if(matcher.matches()) {
			for(int i = 1; i <= matcher.groupCount(); i++) {
				buffer.add(matcher.group(i));
			}
			return new TermList(buffer.toArray(new String[buffer.size()]));
		} else {
			throw new IllegalArgumentException("Invalid expression syntax '" + expression + "'.");
		}
	}
	
	/**
	 * Replace any instances of \l or \L within the given patter with the full patterns.
	 */
	protected static String substituteLiteralPlaceholders(String expression) {
		return expression.replaceAll("\\\\l", Matcher.quoteReplacement(LITERAL)).replaceAll("\\\\L", Matcher.quoteReplacement(LIST));
	}
	
}
