package com.deleidos.rtws.systemcfg.composers;

import java.util.Properties;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.deleidos.rtws.systemcfg.beans.activemq.ActiveMQConfig;
import com.deleidos.rtws.systemcfg.beans.activemq.ActiveMQConfig.CursorType;
import com.deleidos.rtws.systemcfg.composers.activemq.ActiveMQComposer;
import com.deleidos.rtws.systemcfg.composers.activemq.ActiveMQComposerService;

import junit.framework.TestCase;


/**
 * Test class for the ActiveMQComposer.
 */
public class ActiveMQComposerTest extends TestCase {
	
	private ActiveMQConfig config;
	
	@Before
	public void setUp() throws Exception {
		String baseDir = System.getProperty("basedir");
		
		if(baseDir == null){
			System.out.println("Need to set system property basedir.");
			fail("Need to congiure the test properly");
		}
		
		String file = baseDir + "/src/resource/test/com/deleidos/rtws/systemcfg/templates/activemq-template.xml";
		
		
		config = new ActiveMQConfig();
		config.setNumNodes(6);
		config.setQueuePolicy(CursorType.STORE_CURSOR);
		config.setSubscriberPolicy(CursorType.VM_CURSOR);
		config.setTemplateFile(file);
		config.setOutDir("/tmp");
		config.setDomainName("nightly.rtsaic.com");
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testActiveMQComposer(){
		ActiveMQComposer composer = new ActiveMQComposerService();
		
		composer.initialize();
		//add load to compose?
		composer.loadDefaults(config.getTemplateFile());
		
		String status = composer.compose(config);
		assertEquals("Exception occured during ActiveMQComposer.compose", "done", status);
		
		Properties properties = composer.getProperties();
		properties.list(System.out);
		
		System.out.println(properties.get("messaging.external.connection.url"));
		System.out.println(properties.get("messaging.internal.connection.url"));
	}
	
}
