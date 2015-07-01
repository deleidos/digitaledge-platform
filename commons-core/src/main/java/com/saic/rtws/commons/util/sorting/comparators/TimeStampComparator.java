package com.saic.rtws.commons.util.sorting.comparators;

import java.util.Comparator;
import java.text.SimpleDateFormat;

public abstract class TimeStampComparator<T> implements Comparator<String> {
	protected int numHeaders;
	protected String delimiter;
	protected String pattern;
	protected SimpleDateFormat format;

	public TimeStampComparator(int numHeaders, String pattern, String delimiter) {
		this.numHeaders = numHeaders;
		this.delimiter = delimiter;
		this.pattern = pattern;
		format = new SimpleDateFormat(pattern);
	}
	
	public int getNumHeaders() {
		return numHeaders;
	}
	
	public void setNumHeaders(int numHeaders) {
		this.numHeaders = numHeaders;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public abstract int compare(String one, String two);
	
	public abstract boolean accept(String[] lineArray);
}
