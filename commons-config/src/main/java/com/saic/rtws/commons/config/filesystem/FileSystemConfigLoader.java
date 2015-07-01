package com.saic.rtws.commons.config.filesystem;

import java.util.List;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.tree.OverrideCombiner;
import org.apache.commons.lang.StringUtils;

import com.saic.rtws.commons.config.EncryptedPropertiesConfiguration;

public class FileSystemConfigLoader {
	private ConfigLoadPrecedence loadPrecedence;
	private CombinedConfiguration combinedConfiguration;
	
	public FileSystemConfigLoader(ConfigLoadPrecedence loadPrecedence) throws ConfigurationException {
		setLoadPrecedence(loadPrecedence, loadPrecedence != null);
	}
	
	public ConfigLoadPrecedence getLoadPrecedence() {
		return loadPrecedence;
	}
	
	public void setLoadPrecedence(ConfigLoadPrecedence loadPrecedence) throws ConfigurationException {
		this.setLoadPrecedence(loadPrecedence, true);
	}

	public void setLoadPrecedence(ConfigLoadPrecedence loadPrecedence, boolean loadImmediately) throws ConfigurationException {
		this.loadPrecedence = loadPrecedence;
		if(loadImmediately)
		{
			loadConfig();
		}
	}

	public synchronized CombinedConfiguration getCombinedConfiguration() {
		return combinedConfiguration;
	}
	
	public void loadConfig() throws ConfigurationException {
		if(this.loadPrecedence == null)
		{
			this.combinedConfiguration = null;
		}
		else
		{
			List<ConfigEntry> configEntries = this.loadPrecedence.getConfigEntries();
			validateConfigEntries(configEntries);
			synchronized (this) {
				this.combinedConfiguration = new CombinedConfiguration(new OverrideCombiner());
				for(ConfigEntry currEntry : configEntries)
				{
					combinedConfiguration.addConfiguration(new EncryptedPropertiesConfiguration(currEntry.getConfigFile()), currEntry.getId());
				}
			}
		}
	}
	
	private void validateConfigEntries(List<ConfigEntry> configEntries) throws ConfigurationException
	{
		if(configEntries == null || configEntries.size() == 0)
		{
			throw new ConfigurationException("Invalid Configuration ... no ConfigEntry instances found.");
		}
		else
		{
			for(ConfigEntry currEntry : configEntries)
			{
				if(currEntry == null || StringUtils.isBlank(currEntry.getId()) || currEntry.getConfigFile() == null)
				{
					throw new ConfigurationException("Invalid Configuration ... empty or null value");
				}
				else if(currEntry.getConfigFile().exists() == false || currEntry.getConfigFile().isDirectory())
				{
					throw new ConfigurationException("ConfigEntry.getConfigFile() must be a file");
				}
				else if(currEntry.getConfigFile().canRead() == false)
				{
					throw new ConfigurationException("Cannot read resource '" + currEntry.getConfigFile().getAbsolutePath() + "'");
				}
				else
				{
					for(ConfigEntry dupCheckEntry : configEntries)
					{
						if(dupCheckEntry != currEntry && dupCheckEntry.getId().equals(currEntry.getId()))
						{
							throw new ConfigurationException("ConfigEntry ID '" + currEntry.getId() + "' is not unique.");
						}
					}
				}
			}
		}
	}
}
