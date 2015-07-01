package com.saic.rtws.commons.util.expression;

import static com.saic.rtws.commons.util.expression.Literal.LITERAL;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TermList {

	// Because the LITERAL pattern is going to be used to find() rather than match(), we need to account
	// for cases where one type of literal's pattern is a proper prefix of another; which would result in
	// a partial match, truncating the desired value. For example, both decimals and dates begin with valid
	// integers. The pattern below is extended to insure that the matching literal is immediately followed
	// by either a delimiter or end of line, preventing the match from stopping short when it has found an
	// integer.
	private static Pattern TERM = Pattern.compile(LITERAL + "(?=,|$|\\s)");
	
	private String[] terms;

	public TermList(String[] terms) {
		this.terms = terms;
	}
	
	public String getTermAsText(int index) {
		return terms[index];
	}
	
	public Object getTermAsLiteral(int index) {
		return Literal.valueOf(terms[index]);
	}
	
	public Object[] getTermAsList(int index) {
		LinkedList<Object> buffer = new LinkedList<Object>();
		Matcher matcher = TERM.matcher(terms[index]);
		while(matcher.find()) {
			buffer.add(Literal.valueOf(matcher.group()));
		}
		return buffer.toArray();
	}
	
	
}
