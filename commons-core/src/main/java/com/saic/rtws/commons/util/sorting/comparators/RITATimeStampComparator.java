package com.saic.rtws.commons.util.sorting.comparators;

import java.text.ParseException;
import java.util.Date;

public class RITATimeStampComparator<T> extends TimeStampComparator<String> {
	public static final int flightDate = 4;
	public static final int actualArrivalTime = 21;
	
	public RITATimeStampComparator(int numHeaders, String pattern, String delimiter) {
		super(numHeaders, pattern, delimiter);
	}
	
	@Override
	public int compare(String one, String two) {
		Date dateOne = null;
		Date dateTwo = null;
		
		try {
			String[] oneArray = one.split(delimiter);
			dateOne = format.parse(oneArray[flightDate] + delimiter + oneArray[actualArrivalTime]);
			String[] twoArray = two.split(",");
			dateTwo = format.parse(twoArray[flightDate] + delimiter + twoArray[actualArrivalTime]);
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
		if ((lineArray != null) && (lineArray[actualArrivalTime].length() > 0) && (lineArray[flightDate].length() > 0)) return true;
		else return false;
	}

}
