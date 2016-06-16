package com.deleidos.rtws.systemcfg.composers.activemq;

import java.util.Properties;

import com.deleidos.rtws.commons.util.Initializable;
import com.deleidos.rtws.systemcfg.beans.activemq.ActiveMQConfig;

public interface ActiveMQComposer extends Initializable {
	
	/**
	 * Write xml document to the file parameter.
	 * 
	 * @param file output file path
	 */
	public void writeFile(String file);
	
	/**
	 * Load the ActiveMQ defaults template xml file.
	 * 
	 * @param file path to file
	 */
	public void loadDefaults(String file);
	
	/**
	 * Composes/creates the configuration file for ActiveMQ nodes.
	 * 
	 * @param config {@link ActiveMQConfig} configuration
	 * @return a status string
	 */
	public String compose(ActiveMQConfig config);
	
	/**
	 * Get the properties created during compose.
	 * 
	 * @return {@link Properties} properties object
	 */
	public Properties getProperties();
}
