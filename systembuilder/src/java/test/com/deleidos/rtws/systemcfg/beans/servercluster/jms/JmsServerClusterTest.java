package com.deleidos.rtws.systemcfg.beans.servercluster.jms;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.junit.Assert;
import org.junit.Test;

import com.deleidos.rtws.systemcfg.beans.JsonDeserializeTest;
import com.deleidos.rtws.systemcfg.beans.jms.JmsQueue;
import com.deleidos.rtws.systemcfg.beans.jms.JmsTopic;
import com.deleidos.rtws.systemcfg.beans.servercluster.jms.JmsServerCluster;

public class JmsServerClusterTest extends JsonDeserializeTest {
	private static final String USER_LABEL = "External JMS Interface";
	private static final String NAME = "jms.external";
	private static final String POS_QUEUE_USER_LABEL = "Incoming POS Data";
	private static final String POS_QUEUE_NAME = "com.salesrus.pos.parse";
	private static final String DUP_QUEUE_USER_LABEL = "Duplicate Queue (Only 1 Goes In)";
	private static final String DUP_QUEUE_NAME = "com.salesrus.queue.dup";
	private static final String TOPIC_1_USER_LABEL = "Test Topic 1";
	private static final String TOPIC_1_NAME = "com.salesrus.topic.test1";
	private static final String DUP_TOPIC_USER_LABEL = "Duplicate Topic (Only 1 Goes In)";
	private static final String DUP_TOPIC_NAME = "com.salesrus.topic.dup";
	

	@Test
	public void testSimple() throws JsonParseException, IOException {
		JmsServerCluster jmsServerCluster = loadFromJsonResource(
				JmsServerCluster.class,
				"com/deleidos/rtws/systemcfg/beans/servercluster/jms/simpleJmsServerCluster.json");
		Assert.assertNotNull(jmsServerCluster);
		Assert.assertEquals(jmsServerCluster.getUserLabel(), USER_LABEL);
		Assert.assertEquals(jmsServerCluster.getName(), NAME);
		Assert.assertEquals(jmsServerCluster.getQueues().size(), 2);
		for(JmsQueue currQueue : jmsServerCluster.getQueues())
		{
			if(currQueue.getUserLabel().equals(POS_QUEUE_USER_LABEL))
			{
				Assert.assertEquals(currQueue.getName(), POS_QUEUE_NAME);
			}
			else if(currQueue.getUserLabel().equals(DUP_QUEUE_USER_LABEL))
			{
				Assert.assertEquals(currQueue.getName(), DUP_QUEUE_NAME);
			}
			else
			{
				Assert.fail("Invalid JMS Queue User Label");
			}
		}
		Assert.assertEquals(jmsServerCluster.getTopics().size(), 2);
		for(JmsTopic currTopic : jmsServerCluster.getTopics())
		{
			if(currTopic.getUserLabel().equals(TOPIC_1_USER_LABEL))
			{
				Assert.assertEquals(currTopic.getName(), TOPIC_1_NAME);
			}
			else if(currTopic.getUserLabel().equals(DUP_TOPIC_USER_LABEL))
			{
				Assert.assertEquals(currTopic.getName(), DUP_TOPIC_NAME);
			}
			else
			{
				Assert.fail("Invalid JMS Topic User Label");
			}
		}
	}
}
