package com.deleidos.rtws.systemcfg.properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.NestableException;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertyLoader extends TestCase {
	private CompositeConfiguration config;
	private PropertiesConfiguration tenetProps;
	private PropertiesConfiguration jmsProps;
	private PropertiesConfiguration jmxProps;
	
	@Before
	public void setUp() throws Exception {
		try {
			tenetProps = new PropertiesConfiguration("com/deleidos/rtws/systemcfg/config/tenant.properties");
			jmsProps = new PropertiesConfiguration("com/deleidos/rtws/systemcfg/config/jms.properties");
			jmxProps = new PropertiesConfiguration("com/deleidos/rtws/systemcfg/config/jmx.properties");
			
			config = new CompositeConfiguration();
			config.addConfiguration(tenetProps);
			config.addConfiguration(jmsProps);
			config.addConfiguration(jmxProps);
		} catch (NestableException ex) {
			throw ex;
		}
		
	}
	
	@After
	public void tearDown() throws Exception {
		config.clear();
	}
	
	@Test
	public void testLoadConfig() {
		assertEquals("rtws-test", config.getString("tenant.system.release"));
		assertEquals("localhost", config.getString("messaging.external.node"));
		assertEquals("localhost:61616", config.getString("messaging.external.connection.url"));
	}
	
	@Test
	public void testReplaceProperty() {
		config.clearProperty("tenant.system.release");
		config.addProperty("tenant.system.release", "rtws-new");
		assertEquals("rtws-new", config.getString("tenant.system.release"));
	}
	
	
	
	
}
