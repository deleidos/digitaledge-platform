package com.saic.rtws.commons.util.expression;

import static com.saic.rtws.commons.util.regex.PatternUtils.removeCapturingCroups;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import com.saic.rtws.commons.util.regex.Primitive;

/**
 * Utility class for parsing/validating literal values of simple types, such as (Boolean, Integer, Double, String)
 */
public class Literal {

	// List of date format masks such that the index in which the mask is stored
	// is equal to the length of the date string it is capable of parsing.
	private static final String[] DATE_FORMAT_MASKS = {
		"",
		"",
		"",
		"",
		"yyyy",
		"",
		"",
		"yyyy/MM",
		"",
		"",
		"yyyy/MM/dd",
		"",
		"",
		"yyyy/MM/dd HH",
		"",
		"",
		"yyyy/MM/dd HH:mm",
		"",
		"",
		"yyyy/MM/dd HH:mm:ss",
		"",
		"",
		"",
		"yyyy/MM/dd HH:mm:ss.SSS",
		"yyyy/MM/dd HH:mm:ssZ",
		"",
		"",
		"",
		"yyyy/MM/dd HH:mm:ss.SSSZ"
	};

	protected static final String DATE_FORMAT_MASK = DATE_FORMAT_MASKS[DATE_FORMAT_MASKS.length - 1];

	/** Pattern that matches the "null" keyword. */
	protected static final String NULL = removeCapturingCroups(Primitive.NULL);

	/** Pattern that matches boolean values (true, false). */
	protected static final String BOOLEAN = removeCapturingCroups(Primitive.BOOLEAN);

	/** Pattern that matches integer values. */
	protected static final String INTEGER = removeCapturingCroups(Primitive.INTEGER);

	/** Pattern that matches decimal values (but not integers). */
	protected static final String DECIMAL = removeCapturingCroups(Primitive.DECIMAL);

	/** Pattern that matches date/time values. */
	protected static final String DATE = "\\d{4}/\\d{2}/\\d{2}(?:\\s+\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{3})?(?:[+-]\\d{4})?)?";
	
	/** Pattern that matches quoted string values. */
	protected static final String STRING = "[\"\'](?:[^\"\'\\\\]|\\\\[\"\'\\\\])*[\"\']";

	/** Pattern that matches any of the above literal values. */
	protected static final String LITERAL = "(?:" + BOOLEAN + "|" + INTEGER + "|" + DECIMAL + "|" + STRING + "|" + DATE + "|" + NULL + ")";

	/** Pattern that matches a comma separated list of literal values. */
	protected static final String LIST = LITERAL + "(?:\\s*,\\s*" + LITERAL + ")*";

	/**
	 * Formats the given object as a textual literal, quoting string where needed.
	 */
	public static String toString(Object value) {
		if (value == null) {
			return "null";
		} else if (value instanceof Boolean) {
			return value.toString();
		} else if (value instanceof Number) {
			return value.toString();
		} else if (value instanceof Date) {
			return new SimpleDateFormat(DATE_FORMAT_MASK).format(value);
		} else if (value instanceof String) {
			return quote(value.toString());
		} else {
			throw new IllegalArgumentException("Object is not a simple type '" + value.getClass().getName() + "'.");
		}
	}

	/**
	 * Formats the given array as a comma separated list of textual literals, quoting string where needed.
	 */
	public static String toString(Object[] values) {
		StringBuilder buffer = new StringBuilder();
		if (values == null) {
			throw new NullPointerException();
		} else if (values.length == 0) {
			throw new IllegalArgumentException("Empty list.");
		} else {
			for (Object value : values) {
				buffer.append(toString(value));
				buffer.append(", ");
			}
			return buffer.substring(0, buffer.length() - 2);
		}
	}

	/**
	 * Converts the given textual literal values to an object of the appropriate java type.
	 */
	public static Object valueOf(String value) {
		if (value == null) {
			return null;
		} else if (value.matches(NULL)) {
			return null;
		} else if (value.matches(BOOLEAN)) {
			return Primitive.evaluateBoolean(value);
		} else if (value.matches(INTEGER)) {
			return Integer.valueOf(value);
		} else if (value.matches(DECIMAL)) {
			return Double.valueOf(value);
		} else if (value.matches(DATE)) {
			try {
				return new SimpleDateFormat(DATE_FORMAT_MASKS[value.length()]).parse(value);
			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		} else if (value.matches(STRING)) {
			return unquote(value);
		} else {
			throw new IllegalArgumentException("Invalid literal value '" + value + "'.");
		}
	}

	/**
	 * Encloses the given string in quotation marks, escaping embedded characters where needed.
	 */
	public static String quote(String value) {
		value = value.replaceAll("\\\\", Matcher.quoteReplacement("\\\\"));
		value = value.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		value = value.replaceAll("\'", Matcher.quoteReplacement("\\\'"));
		return "'" + value + "'";
	}

	/**
	 * Removes the enclosing quotes from the given string, an un-mask any escaped characters.
	 */
	public static String unquote(String value) {
		value = value.replaceAll("\\\\\\\\", Matcher.quoteReplacement("\\"));
		value = value.replaceAll("\\\\\"", Matcher.quoteReplacement("\""));
		value = value.replaceAll("\\\\'", Matcher.quoteReplacement("\'"));
		return value.substring(1, value.length() - 1);
	}

}
