package com.saic.rtws.commons.util.sorting.comparators;

import java.text.ParseException;
import java.util.Date;

public class BusTimeStampComparator<T> extends TimeStampComparator<String> {

	public static final int Year = 0;
	public static final int Month = 1;
	public static final int Day = 2;
	public static final int Hour = 3;
	public static final int Minutes = 4;
	public static final int Seconds = 5;
	
	public BusTimeStampComparator(int numHeaders, String pattern, String delimiter) {
		super(numHeaders, pattern, delimiter);
	}
	
	@Override
	public int compare(String one, String two) {
		Date dateOne = null;
		Date dateTwo = null;
		
		try {
			dateOne = format.parse(one.substring(0,19)); 
			dateTwo = format.parse(two.substring(0,19));
		} catch(ParseException pe) {
			System.err.println("Parsing Exception " + pe.getMessage());
			return 0;
		}
		return compare(dateOne, dateTwo);
	}
	
	public int compare(Date one, Date two){
		return one.compareTo(two);
	}

	@Override
	public boolean accept(String[] lineArray) {
		if ((lineArray != null) && (lineArray[Year].length() > 0) && (lineArray[Month].length() > 0) && (lineArray[Day].length() > 0)
				&& (lineArray[Hour].length() > 0) && (lineArray[Minutes].length() > 0)) {
			return true;
		} else {
			return false;
		}
	}
}
