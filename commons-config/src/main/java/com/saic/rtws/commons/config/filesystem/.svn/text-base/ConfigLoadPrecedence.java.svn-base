package com.saic.rtws.commons.config.filesystem;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

public interface ConfigLoadPrecedence {
	/**
	 * ConfigEntries will be loaded in order (0-n) ... with the first entry
	 * taking precedence over later occurrences.
	 *  
	 * @return A List of ConfigEntry instances indicating the order in which to load config resources.
	 */
	public List<ConfigEntry> getConfigEntries() throws ConfigurationException;
}
