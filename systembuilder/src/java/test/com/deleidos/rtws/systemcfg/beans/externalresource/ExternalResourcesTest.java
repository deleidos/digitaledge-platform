package com.deleidos.rtws.systemcfg.beans.externalresource;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.junit.Assert;
import org.junit.Test;

import com.deleidos.rtws.systemcfg.beans.JsonDeserializeTest;
import com.deleidos.rtws.systemcfg.beans.Resource;
import com.deleidos.rtws.systemcfg.beans.externalresource.CustomExternalResource;
import com.deleidos.rtws.systemcfg.beans.externalresource.ExternalResource;
import com.deleidos.rtws.systemcfg.beans.externalresource.JmsResource;

public class ExternalResourcesTest extends JsonDeserializeTest {
	@Test
	public void testSimpleJmsDeserialization() throws JsonParseException, IOException {
		Resource resource = loadFromJsonResource(ExternalResource.class, "com/deleidos/rtws/systemcfg/beans/externalresource/jmsTest.json");
		Assert.assertNotNull(resource);
		if((resource instanceof JmsResource) == false)
		{
			Assert.fail("json was not deserialized to correct type.");
		}
		else
		{
			JmsResource jmsResource = (JmsResource)resource;
			Assert.assertEquals("Sales 'R Us JMS Server", jmsResource.getUserLabel());
			Assert.assertEquals(Boolean.TRUE, jmsResource.isSecure());
			Assert.assertEquals("jms.b2b.salesrus.com", jmsResource.getUrl());
			Assert.assertEquals("someUser", jmsResource.getUsername());
			Assert.assertEquals("aPassword", jmsResource.getPassword());
		}
	}
	
	@Test
	public void testCustomExternalResourceDeserialization() throws JsonParseException, IOException {
		Resource resource = loadFromJsonResource(ExternalResource.class, "com/deleidos/rtws/systemcfg/beans/externalresource/customExternalResourceTest.json");
		Assert.assertNotNull(resource);
		if((resource instanceof CustomExternalResource) == false)
		{
			Assert.fail("json was not deserialized to correct type.");
		}
		else
		{
			CustomExternalResource customResource = (CustomExternalResource)resource;
			Assert.assertEquals("My Custom External Resource", customResource.getUserLabel());
			Assert.assertEquals("com.salesrus.elastasales3.ext.MyCustomExternalResource", customResource.getClassName());
			Assert.assertNotNull(customResource.getBeanConfig());
			if(!(customResource.getBeanConfig() instanceof Map<?, ?>))
			{
				Assert.fail("beanConfig not marshalled into a Map object");
			}
			else
			{
				Map<?,?> beanConfig = (Map<?, ?>)customResource.getBeanConfig();
				Object customFieldValue = beanConfig.get("aCustomStringField");
				Assert.assertEquals("aCustomValue", customFieldValue);
				Object customObj = beanConfig.get("aCustomObjectField");
				if(!(customObj instanceof Map<?, ?>))
				{
					Assert.fail("aCustomObjectField not marshalled into a Map object");
				}
				else
				{
					Object nestedFieldValue = ((Map<?,?>)customObj).get("aNestedStringField");
					Assert.assertEquals("aNestedValue", nestedFieldValue);
				}
			}
		}
	}
}
