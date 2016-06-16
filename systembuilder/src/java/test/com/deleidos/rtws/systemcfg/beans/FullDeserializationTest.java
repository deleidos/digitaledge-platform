package com.deleidos.rtws.systemcfg.beans;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.junit.Assert;
import org.junit.Test;

import com.deleidos.rtws.systemcfg.beans.SystemConfig;
import com.deleidos.rtws.systemcfg.beans.externalresource.CustomExternalResource;
import com.deleidos.rtws.systemcfg.beans.externalresource.ExternalResource;
import com.deleidos.rtws.systemcfg.beans.externalresource.JmsResource;

public class FullDeserializationTest extends JsonDeserializeTest {
	@Test
	public void testCompleteSystemOutline() throws JsonParseException, IOException {
		SystemConfig system = loadSystemConfigFromJsonResource("com/deleidos/rtws/systemcfg/beans/systemOutline.json");
		Assert.assertNotNull(system);
		Assert.assertEquals(2, system.getDataModels().size());
		Assert.assertEquals(2, system.getExternalResources().size());
		
		for(ExternalResource currExtResource : system.getExternalResources())
		{
			if(StringUtils.equals("Sales 'R Us JMS Server", currExtResource.getUserLabel()))
			{
				if((currExtResource instanceof JmsResource) == false)
				{
					Assert.fail("json was not deserialized to correct type.");
				}
				else
				{
					JmsResource jmsResource = (JmsResource)currExtResource;
					Assert.assertEquals("jms.b2b.salesrus.com", jmsResource.getUrl());
					Assert.assertEquals("someUser", jmsResource.getUsername());
					Assert.assertEquals("aPassword", jmsResource.getPassword());
				}
			}
			else if(StringUtils.equals("My Custom External Resource", currExtResource.getUserLabel()))
			{
				if((currExtResource instanceof CustomExternalResource) == false)
				{
					Assert.fail("json was not deserialized to correct type.");
				}
				else
				{
					CustomExternalResource customResource = (CustomExternalResource)currExtResource;
					Assert.assertEquals("com.salesrus.elastasales3.ext.MyCustomExternalResource", customResource.getClassName());
				}
			}
		}
	}
}
