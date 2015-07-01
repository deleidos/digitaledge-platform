package com.saic.rtws.commons.monitor.process;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.process.MongoDbProcess;

public class MongoDbMonitor extends ProcessMonitor {

	private static final String INGEST_CONF_PATH 				= "/usr/local/rtws/ingest/conf";
	private static final String MONGODB_PIPELINE_XML_FILE 		= "pipeline.mongo.default.xml";
	private static final String MONGODB_PIPELINE_XML_FILEPATH 	= INGEST_CONF_PATH + File.separator + MONGODB_PIPELINE_XML_FILE;
	private static final String MONGODB_HOST_EXPRESSION 		= "/pipeline-definition/sink/mongo-server-host";
	private static final String MONGODB_PORT_EXPRESSION 		= "/pipeline-definition/sink/mongo-server-port";
	
	private static final MongoDbProcess process = MongoDbProcess.newInstance();
	
	private Logger logger = LogManager.getLogger(getClass());
	private MongoDbInfo info = null;
	
	private class MongoDbInfo {
		String hostname = "127.0.0.1";
		int port = 27017;
	}
	
	public MongoDbMonitor(String name) {
		super(name);
		
		// Give the MongoDB server some time to start
		// and set monitoring interval.
		setStartupPeriod(1000 * 60 * 3);
		setMonitorInterval(1000 * 30);
	}

	@Override
	protected void monitor() {
		switch (process.getStatus()) {
			case Running :
				testConnection();
				break;
			case Stopped :
				addError("MongoDb is not running.");
				break;
			default :
				addError("MongoDb status is currently unknown.");
				break;
		}
	}
	
	private void testConnection() {
		// Check to see if the mongodb connection parameters are loaded
		if (this.info == null) try {
			logger.debug("Loading MongoDb pipline definition file ...");
			loadMongoDbPipelineDefinition();
		} catch (Exception ex) {
			addError("Unable to determine MongoDb host and port configuration.");
			logger.error("Failed to load MongoDb pipeline definition.", ex);
			return;
		}
		
		// Test the mongodb connection
		logger.debug("Testing MongoDb connection ...");
		if (! process.testConnection(this.info.hostname, this.info.port)) {
			addError("Failed MongoDb connection test.");
			return;
		}
		
		setStatus(MonitorStatus.OK);
	}
	
	/**
	 * Read the MongoDb pipeline definition file and extract out the
	 * host and port information.
	 */
	private void loadMongoDbPipelineDefinition() throws Exception {		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new FileInputStream(MONGODB_PIPELINE_XML_FILEPATH));
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		String hostname = xPath.compile(MONGODB_HOST_EXPRESSION).evaluate(doc);
		if (hostname == null || StringUtils.isEmpty(hostname)) {
			logger.debug("Parameter 'mongo-server-host' not found in pipeline definition file, assuming its running on localhost");
			hostname = "127.0.0.1";
		}
		int port = Integer.parseInt(xPath.compile(MONGODB_PORT_EXPRESSION).evaluate(doc));
		
		this.info = new MongoDbInfo();
		this.info.hostname = hostname;
		this.info.port = port;
		
		logger.debug(String.format("MongoDb host '%s' and port '%s'", hostname, port));
	}
	
}