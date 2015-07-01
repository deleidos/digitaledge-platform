package com.saic.rtws.UploadUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.saic.rtws.splitter.Splitter;
import com.saic.rtws.splitter.SplitterFactory;

public class WildcardPatternFilterMap {
	
	public final static int WILDCARD_PATTERN_INDEX = 0;
	public final static int RECORD_FORMAT_INDEX = 1;
	public final static int RECORD_HEADER_LINES_INDEX = 2;
	public final static int INPUT_FORMAT_INDEX = 3;
	
	public final static String RECORD_FORMAT_PARAM = "record-format";
	public final static String RECORD_HEADER_LINES_PARAM = "record-header-lines";
	public final static String INPUT_FORMAT_PARAM = "input-format";
	
	
	private HashMap<String, Splitter> splitters = new HashMap<String, Splitter>();
	
	private Map<String, Map<String, String>> wildcardPatternToDataSourceMap = new HashMap<String, Map<String, String>>();
	private Logger logger;
	
	public WildcardPatternFilterMap(){
		logger = Logger.getLogger(this.getClass());
		// Initialize the splitters
		String[] availableSplitters = SplitterFactory.getSplitterTypes();
		for(String splitterType : availableSplitters) {
			logger.debug(String.format("Initializing splitter for splitter type [%s]", splitterType));
			splitters.put(splitterType.toUpperCase(), SplitterFactory.create(splitterType));
		}
	}
	
	private void mapPattern(String wildcardPattern, Map<String, String> parameters) {
		wildcardPatternToDataSourceMap.put(wildcardPattern, parameters);
	}
	
	public Map<String, String> filter(File file) {
		return filter(file.getName());
	}
	
	public Map<String, String> filter(String fileName) {
		// Strip the path from the name since we are only filtering on file name
		fileName = FilenameUtils.getName(fileName);
		
		for(String wildcardPattern : wildcardPatternToDataSourceMap.keySet()) {
			if(FilenameUtils.wildcardMatch(fileName, wildcardPattern)) {
				// We have a matching wildcard, we need the data source from it
				return wildcardPatternToDataSourceMap.get(wildcardPattern);
			}
		}
		
		// No matching wildcard pattern
		return null;
	}
	
	public void buildFilterMap(String fileMappingString) {
		String[] mappings = fileMappingString.split(",");
		logger.debug(String.format("File mapping string split into [%d] different mappings.", mappings.length));
		for(String mapping : mappings) {
			logger.debug(String.format("Parsing mapping [%s].", mapping));
			String[] mappingParts = mapping.split(":");
			
			// Get and validate the parts of the mapping
			String wildcardPattern = mappingParts[WILDCARD_PATTERN_INDEX].trim();
			logger.debug(String.format("Mapping [%s] wildcard pattern is [%s].", mapping, wildcardPattern));
			if(wildcardPattern.isEmpty()) {
				logger.warn(String.format("Mapping [%s] wildcard pattern is empty. Discarding mapping.", mapping));
				continue;
			}
			
			String recordFormat = mappingParts[RECORD_FORMAT_INDEX].trim().toUpperCase();
			logger.debug(String.format("Mapping [%s] record format is [%s].", mapping, recordFormat));
			if(recordFormat.isEmpty()) {
				logger.warn(String.format("Mapping [%s] record format is empty. Discarding mapping.", mapping));
				continue;
			}
			else if(!splitters.containsKey(recordFormat)) {
				logger.warn(String.format("Mapping [%s] contains unknown record format [%s]. Discarding mapping.", mapping, recordFormat));
				continue;
			}
			
			String recordHeaderLines = mappingParts[RECORD_HEADER_LINES_INDEX].trim();
			logger.debug(String.format("Mapping [%s] reacordHeaderLines is [%s].", mapping, recordHeaderLines));
			try {
				Integer.parseInt(recordHeaderLines);
			} catch (NumberFormatException e) {
				logger.warn(String.format("Mapping [%s] record header lines count is not a valid number. Discarding mapping.", mapping));
				continue;
			}
			
			String inputFormat = mappingParts[INPUT_FORMAT_INDEX].trim();
			logger.debug(String.format("Mapping [%s] input format is [%s].", mapping, inputFormat));
			if(inputFormat.isEmpty()) {
				logger.warn(String.format("Mapping [%s] input format is empty. Discarding mapping.", mapping));
				continue;
			}
			
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(RECORD_FORMAT_PARAM, recordFormat);
			parameters.put(RECORD_HEADER_LINES_PARAM, recordHeaderLines);
			parameters.put(INPUT_FORMAT_PARAM, inputFormat);
			
			mapPattern(wildcardPattern, parameters);
			logger.debug(String.format("Mapping [%s] successfully parsed.", mapping));
		}
	}

	public Splitter getSplitterForFormat(String recordFormat) {
		return splitters.get(recordFormat);
	}
}
