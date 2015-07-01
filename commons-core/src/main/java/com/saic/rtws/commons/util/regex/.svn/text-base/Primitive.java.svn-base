package com.saic.rtws.commons.util.regex;

import java.util.regex.Pattern;

public class Primitive {

	public static final String NULL = "[nN][uU][lL][lL]";
	public static final Pattern NULL_PATTERN = Pattern.compile(NULL);
	
	public static final String TRUE = "[tT][rR][uU][eE]|[yY][eE][sS]|[oO][nN]";
	public static final String FALSE = "[fF][aA][lL][sS][eE]|[nN][oO]|[oO][fF][fF]";
	public static final String BOOLEAN = TRUE + "|" + FALSE;
	
	public static final Pattern TRUE_PATTERN = Pattern.compile(TRUE);
	public static final Pattern FALSE_PATTERN = Pattern.compile(FALSE);
	public static final Pattern BOOLEAN_PATTERN = Pattern.compile(BOOLEAN);
	
	public static final String INTEGER = "([+-])?(\\d+)";
	public static final Pattern INTEGER_PATTERN = Pattern.compile(INTEGER);
	
	public static final int INTEGER_GROUP_SIGN = 1;
	public static final int INTEGER_GROUP_VALUE = 2;
	
	public static final String DECIMAL = "([+-])?(\\d*)\\.(\\d+)(?:[eE]([+-]?\\d+))?";
	public static final Pattern DECIMAL_PATTERN = Pattern.compile(DECIMAL);
	
	public static final int DECIMAL_GROUP_SIGN = 1;
	public static final int DECIMAL_GROUP_WHOLE = 2;
	public static final int DECIMAL_GROUP_FRACTION = 3;
	public static final int DECIMAL_GROUP_EXPONENT = 4;
	
	public static final String NUMBER = INTEGER + "|" + DECIMAL;
	public static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER);
	
	public static boolean evaluateBoolean(String value) {
		if(value == null) {
			throw new NullPointerException();
		} else if(TRUE_PATTERN.matcher(value).matches()) {
			return true;
		} else if(FALSE_PATTERN.matcher(value).matches()) {
			return false;
		} else {
			throw new IllegalArgumentException(value);
		}
	}
	
}
