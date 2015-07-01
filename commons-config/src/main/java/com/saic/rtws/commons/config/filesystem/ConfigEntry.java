package com.saic.rtws.commons.config.filesystem;

import java.io.File;

public class ConfigEntry {
	private String id;
	private File configFile;
	
	public ConfigEntry() {
		super();
	}
	
	public ConfigEntry(String id, File configFile) {
		this.id = id;
		this.configFile = configFile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}
}
