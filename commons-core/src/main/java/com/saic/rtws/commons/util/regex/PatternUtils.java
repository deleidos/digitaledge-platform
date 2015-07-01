package com.saic.rtws.commons.util.regex;

import static java.util.regex.Matcher.quoteReplacement;

import java.util.regex.*;

public class PatternUtils {

	/**
	 * Converts a simple wildcard expression to a regex compatible pattern string.
	 * 
	 * @param expression
	 *            The expression to be converted
	 * @param single
	 *            The symbol that indicates any single character should match.
	 * @param multi
	 *            The symbol that indicates a number of consecutive characters should match.
	 */
	public static String convertWildcardToRegex(String expression, char single, char multi) {
		String pattern = "\\Q" + expression + "\\E";
		pattern = pattern.replaceAll("\\" + single, quoteReplacement("\\E.\\Q"));
		pattern = pattern.replaceAll("\\" + multi, quoteReplacement("\\E.*\\Q"));
		return pattern;
	}

	/**
	 * Converts an expression that follows the unix shell expansion wildcard syntax to a java regex expression.
	 */
	public static String convertUnixWildcardToRegex(String expression) {
		return convertWildcardToRegex(expression, '?', '*');
	}

	/**
	 * Converts an expression that follows the Oracle "like" operator syntax to a java regex expression.
	 */
	public static String convertOracleWildcardToRegex(String expression) {
		return convertWildcardToRegex(expression, '_', '%');
	}

	/**
	 * Converts any capturing groups within the given pattern into non-capturing groups.
	 */
	public static String removeCapturingCroups(String pattern) {
		// Find all left parenthesis that are neither preceded by a backslash nor
		// are succeeded by a question mark; replace them with "(?:".
		return pattern.replaceAll("(?<!\\\\)\\((?!\\?)", "\\(\\?:");
	}

	/**
	 * Extracts all the capturing groups matched against the given pattern.
	 */
	public static String[] capture(String value, Pattern pattern) {
		Matcher matcher = pattern.matcher(value);
		if(matcher.matches()) {
			String[] buffer = new String[matcher.groupCount() + 1];
			for(int i = 0; i < buffer.length; i++) {
				buffer[i] = matcher.group(i);
			}
			return buffer;
		} else {
			return null;
		}
	}

	/**
	 * Extracts all the capturing groups matched against the given pattern.
	 */
	public static String capture(String value, Pattern pattern, int group) {
		Matcher matcher = pattern.matcher(value);
		if(matcher.matches()) {
			return matcher.group(group);
		} else {
			return null;
		}
	}

}
