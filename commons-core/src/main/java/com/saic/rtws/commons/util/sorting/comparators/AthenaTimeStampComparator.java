package com.saic.rtws.commons.util.sorting.comparators;

import java.text.ParseException;
import java.util.Date;

public class AthenaTimeStampComparator<T> extends TimeStampComparator<String> {
	public static final int date = 1;
	public static final int time = 2;
	
	public AthenaTimeStampComparator(int numHeaders, String pattern, String delimiter) {
		super(numHeaders, pattern, delimiter);
	}
	
	@Override
	public int compare(String one, String two) {
		Date dateOne = null;
		Date dateTwo = null;
		
		try {
			String[] oneArray = one.split(delimiter);
			dateOne = format.parse(oneArray[date] + delimiter + oneArray[time]);
			String[] twoArray = two.split(",");
			dateTwo = format.parse(twoArray[date] + delimiter + twoArray[time]);
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
		if ((lineArray != null) && (lineArray[time].length() > 0) && (lineArray[date].length() > 0)) return true;
		else return false;
	}

}
