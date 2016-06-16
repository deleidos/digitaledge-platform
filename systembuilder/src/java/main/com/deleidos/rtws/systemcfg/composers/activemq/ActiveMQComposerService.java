package com.deleidos.rtws.systemcfg.composers.activemq;

import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.deleidos.rtws.commons.exception.InitializationException;
import com.deleidos.rtws.systemcfg.beans.activemq.ActiveMQConfig;
import com.deleidos.rtws.systemcfg.beans.activemq.ActiveMQConfig.CursorType;
import com.deleidos.rtws.systemcfg.parser.XMLParser;

public class ActiveMQComposerService implements ActiveMQComposer{
	
	private static Logger logger = LogManager.getLogger(ActiveMQComposerService.class);
	
	private XMLParser parser;
	
	private Properties properties;
	
	/*
	 * String template replace formats for internal and external nodes.
	 * Contains templates for properties name/value pairs and the xml config file.
	 */
	private final String extPropName = "messaging.external.node?";
	private final String intPropName = "messaging.internal.node?";
	private final String extJMXPropName = "messaging.external.node?.jmx.url";
	private final String intJMXPropName = "messaging.internal.node?.jmx.url";
	private final String extURLPropName = "messaging.external.connection.url";
	private final String intURLPropName = "messaging.internal.connection.url";
	private final String failOverURL = "failover:(@)?jms.prefetchPolicy.all=3";
	private final String failOverNode = "ssl://@:61616";
	private final String jmxURL = "service:jmx:rmi:///jndi/rmi://?:1099/jmxrmi";
	
	private final String extNodeName = "jms-ext-node?.@";
	private final String intNodeName = "jms-int-node?.@";
	private final String extTemplate = "/activemq-jms-ext-node?.xml";
	private final String intTemplate = "/activemq-jms-int-node?.xml";
	private final String extURITemplate = "static:(ssl://${messaging.external.node?}:61616)";
	private final String intURITemplate = "static:(ssl://${messaging.internal.node?}:61616)";
	
	public ActiveMQComposerService(){
		this.parser = new XMLParser();
		this.properties = new Properties();
	}
	
	@Override
	public void initialize() throws InitializationException {
		//not implemented
	}

	@Override
	public void dispose() {
		//not implemented
	}

	@Override
	public void writeFile(String file) {
		parser.writeXmlFile(file);
	}

	@Override
	public void loadDefaults(String file) {
		parser.readXmlFile(file);
		
	}

	@Override
	public String compose(ActiveMQConfig config) {
		String status = "done";
		
		//create a activemq.xml file for each node in the cluster
		//and setup the connectors for each node
		try{
			createExternalNodes(config);
			createInternalNodes(config);
		}
		catch(Exception e){
			status = "fail";
			logger.error(e.toString(), e);
		}
		
		return status;
	}
	
	@Override
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Create external ActiveMG node config files.
	 * 
	 * @param config
	 */
	private void createExternalNodes(ActiveMQConfig config){
		for(int i = 1; i <= config.getNumNodes(); i++){
			String fileName = extTemplate.replace('?', (char)(i + 48));
			
			//add cursor type to queue and subscriber
			Element element = parser.getElement("pendingSubscriberPolicy");
			element.appendChild(createCursor(config.getSubscriberPolicy()));
			
			element = parser.getElement("pendingQueuePolicy");
			element.appendChild(createCursor(config.getQueuePolicy()));
			
			element = parser.getElement("networkConnectors");
			for(int k = 1; k <= config.getNumNodes(); k++){
				if(i != k){
					element.appendChild(createConnector(k, parser.createElement("networkConnector"), extURITemplate));
				}
			}
			
			parser.writeXmlFile(config.getOutDir() + fileName);
			parser.readXmlFile(config.getTemplateFile());  //reset
		}
		
		createExternalNodeProperties(config);  ///create properties
	}
	
	/**
	 * Create internal ActiveMQ node config files.
	 * 
	 * @param config
	 */
	private void createInternalNodes(ActiveMQConfig config){
		for(int i = 1; i <= config.getNumNodes(); i++){
			String fileName = intTemplate.replace('?', (char)(i + 48));
			
			//add cursor type to queue and subscriber
			Element element = parser.getElement("pendingSubscriberPolicy");
			element.appendChild(createCursor(config.getSubscriberPolicy()));
			
			element = parser.getElement("pendingQueuePolicy");
			element.appendChild(createCursor(config.getQueuePolicy()));
			
			element = parser.getElement("networkConnectors");
			for(int k = 1; k <= config.getNumNodes(); k++){
				if(i != k){
					element.appendChild(createConnector(k, parser.createElement("networkConnector"), intURITemplate));
				}
			}
			
			parser.writeXmlFile(config.getOutDir() + fileName);
			parser.readXmlFile(config.getTemplateFile());  //reset
		}
		
		createInternalNodeProperties(config);  //create properties
	}
	
	/**
	 * Create cursor Node.
	 * 
	 * @param type {@link CursorType} cursor type
	 * @return {@link Element} node to add to document
	 */
	private Element createCursor(CursorType type){
		Element element = null;
		
		if(type == CursorType.STORE_CURSOR){
			element = parser.createElement("storeCursor");
		}
		else if(type == CursorType.VM_CURSOR){
			element = parser.createElement("vmCursor");
		}
		else{
			//type not recognized
		}
		
		return element;
	}
			
	
	/**
	 * Create network connector Node.
	 * 
	 * @param id an identification number
	 * @param connector {@link Element} a document node
	 * @param uriTemplate a template to perform name replacement
	 * @return
	 */
	private Element createConnector(int id, Element connector, final String uriTemplate){
		String uri = uriTemplate.replace('?', (char) (id + 48));
		
		connector.setAttribute("name", String.format("connector%d", id));
		connector.setAttribute("uri", uri);
		connector.setAttribute("networkTTL", "2");
		connector.setAttribute("decreaseNetworkConsumerPriority", "true");
		
		return connector;
	}
	
	/**
	 * Creates the internal ActiveMQ node properties.
	 * 
	 * @param config {@link ActiveMQConfig} configuration object
	 */
	public void createInternalNodeProperties(ActiveMQConfig config){
		StringBuilder connURL = new StringBuilder("");
		
		//node name properties 
		for(int i = 1; i <= config.getNumNodes(); i++){
			//build properties file
			String nodeValue = (intNodeName.replace('?', (char)(i + 48))).replace("@", config.getDomainName());
			properties.setProperty(intPropName.replace('?', (char)(i + 48)), nodeValue);
			connURL.append(failOverNode.replace("@", nodeValue) + ",");
		}
		
		connURL.replace(connURL.length() - 1, connURL.length(), "");
		
		//jmx properties
		for(int i = 1; i <= config.getNumNodes(); i++){
			//build properties file
			String nodeValue = (intNodeName.replace('?', (char)(i + 48))).replace("@", config.getDomainName());
			properties.setProperty(intJMXPropName.replace('?', (char)(i + 48)), jmxURL.replace("?", nodeValue));
		}
		
		//connection url
		properties.setProperty(intURLPropName, failOverURL.replace("@", connURL));
		
	}
	
	/**
	 * Creates the external ActiveMQ node properties.
	 * 
	 * @param config {@link ActiveMQConfig} configuration object
	 */
	public void createExternalNodeProperties(ActiveMQConfig config){
		StringBuilder connURL = new StringBuilder();
		
		//node name properties 
		for(int i = 1; i <= config.getNumNodes(); i++){
			//build properties file
			String nodeValue = (extNodeName.replace('?', (char)(i + 48))).replace("@", config.getDomainName());
			properties.setProperty(extPropName.replace('?', (char)(i + 48)), nodeValue);
			connURL.append(failOverNode.replace("@", nodeValue) + ",");
		}
		connURL.replace(connURL.length() - 1, connURL.length(), "");
		
		//jmx properties
		for(int i = 1; i <= config.getNumNodes(); i++){
			//build properties file
			String nodeValue = (extNodeName.replace('?', (char)(i + 48))).replace("@", config.getDomainName());
			properties.setProperty(extJMXPropName.replace('?', (char)(i + 48)), jmxURL.replace("?", nodeValue));
		}
		
		//connection url
		properties.setProperty(extURLPropName, failOverURL.replace("@", connURL));
		
	}
}


