package com.saic.rtws.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogListingUtil {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public List<Pattern> fileNamePatterns = new ArrayList<Pattern>();

	public LogListingUtil() {

		// List of filename patterns acceptable for log viewing via Management
		// Console under those filtered directories
		String[] regex = { "rtws_([a-zA-Z])*+\\.log" };

		try {
			for (String reg : regex) {
				Pattern p = Pattern.compile(reg);
				fileNamePatterns.add(p);
			}

		} catch (PatternSyntaxException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public boolean isAcceptable(String fileName) {
		boolean acceptable = false;
		for (Pattern pattern : fileNamePatterns) {
			if (!acceptable)
				acceptable = pattern.matcher(fileName).matches();
		}
		return acceptable;
	}
}
