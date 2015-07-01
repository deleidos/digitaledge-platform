package com.saic.rtws.commons.util.sorting;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import com.saic.rtws.commons.util.sorting.comparators.TimeStampComparator;
import com.saic.rtws.commons.util.sorting.comparators.BusTimeStampComparator;

public class TimeStampSorter {

	private File inputFile;
	private File outputFile;
	private TimeStampComparator<String> timeStampComparator;
	
	public TimeStampSorter(TimeStampComparator<String> timeStampComparator, File inputFile, File outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.timeStampComparator = timeStampComparator;
		//format = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
		//format = new SimpleDateFormat("\"yyyy-MM-dd\",\"HHmm\"");
	}
	
	public void sortData() {
		try {
			if(!(inputFile.canRead())) {
				System.err.println("Cannot read input file");
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			String line;
			
			// removeHeaderFields();
			for(int i=0; i < timeStampComparator.getNumHeaders(); i++) {
				reader.readLine(); 
			}
			
			ArrayList<String> list = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
	            if ((line != null) && (line.length() > 0)) {
	            	String[] lineArray = line.split(timeStampComparator.getDelimiter());
	            	if( timeStampComparator.accept(lineArray)) {
	            		list.add(line);
	            	}	
	            }	
			}
			
			Collections.sort(list, timeStampComparator);
			
			FileWriter fw = new FileWriter(outputFile);
			for(String sortedLine : list) {
				fw.write(sortedLine + "\n");
			}
			fw.close();
		} catch(FileNotFoundException fnfe) {
			System.err.println("Input Files not Found: " + fnfe.getMessage());
		} catch(IOException ioe) {
			System.err.println("Error reading input file: " + ioe.getMessage());
		} 
	}
	
	public static void main(String[] args) {
		if(args.length < 2) {
			System.err.println("Usage: TimeStampSorter <inputFile> <outputFile>");
			return;
		}
		
		File iFile = new File(args[0]);
		File oFile = new File(args[1]);
		
		TimeStampComparator<String> comparator = new BusTimeStampComparator<String>(0, "yyyy,MM,dd,HH,mm,ss", ",");
		
		TimeStampSorter timeStampSorter = new TimeStampSorter(comparator, iFile, oFile);
		System.out.println("Begining Sort....");
		timeStampSorter.sortData();
		System.out.println("Sort Completed");
	}

}
