package com.deleidos.rtws.systemcfg.beans;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.junit.Assert;
import org.junit.Test;

import com.deleidos.rtws.systemcfg.beans.SystemConfig;

public class SystemConfigDeserializationTest extends JsonDeserializeTest
{
	private static final String SYSTEM_NAME = "ElastaSales";
	private static final String SYSTEM_VERSION = "3.0.0";
	private static final String SYSTEM_DOMAIN = "elastasales3.salesrus.com";
	private static final Long SYSTEM_NODE_LAUNCH_TIMEOUT_MILLIS = 90000L;
	
	@Test
	public void testBasic() throws JsonParseException, IOException {
		SystemConfig simpleSystem = loadSystemConfigFromJsonResource("com/deleidos/rtws/systemcfg/beans/simpleSystem.json");
		Assert.assertNotNull(simpleSystem);
		Assert.assertEquals(simpleSystem.getName(), SYSTEM_NAME);
		Assert.assertEquals(simpleSystem.getVersion(), SYSTEM_VERSION);
		Assert.assertEquals(simpleSystem.getDomain(), SYSTEM_DOMAIN);
		Assert.assertEquals(simpleSystem.getNodeLaunchTimeoutMillis(), SYSTEM_NODE_LAUNCH_TIMEOUT_MILLIS);
	}
	
	@Test
	public void testNullValues() throws JsonParseException, IOException {
		SystemConfig simpleSystem = loadSystemConfigFromJsonResource("com/deleidos/rtws/systemcfg/beans/simpleSystemWithNullValues.json");
		Assert.assertNotNull(simpleSystem);
		Assert.assertEquals(simpleSystem.getName(), SYSTEM_NAME);
		Assert.assertEquals(simpleSystem.getVersion(), SYSTEM_VERSION);
		Assert.assertEquals(simpleSystem.getDomain(), SYSTEM_DOMAIN);
		Assert.assertEquals(simpleSystem.getNodeLaunchTimeoutMillis(), null);
	}

	@Test
	public void testMissingValues() throws JsonParseException, IOException {
		SystemConfig simpleSystem = loadSystemConfigFromJsonResource("com/deleidos/rtws/systemcfg/beans/simpleSystemWithMissingValues.json");
		Assert.assertNotNull(simpleSystem);
		Assert.assertEquals(simpleSystem.getName(), SYSTEM_NAME);
		Assert.assertEquals(simpleSystem.getVersion(), SYSTEM_VERSION);
		Assert.assertEquals(simpleSystem.getDomain(), SYSTEM_DOMAIN);
		Assert.assertEquals(simpleSystem.getNodeLaunchTimeoutMillis(), null);
	}
}
