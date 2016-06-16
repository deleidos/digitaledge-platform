package com.deleidos.rtws.systemcfg.beans.servercluster;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.junit.Assert;
import org.junit.Test;

import com.deleidos.rtws.systemcfg.beans.JsonDeserializeTest;
import com.deleidos.rtws.systemcfg.beans.servercluster.jms.JmsServerCluster;
import com.deleidos.rtws.systemcfg.beans.servercluster.serverconfig.Ec2ServerConfig;

public class ServerClusterTest extends JsonDeserializeTest {
	private static final String USER_LABEL = "External JMS Interface";
	private static final String NAME = "jms.external";
	private static final String CLUSTER_SUB_DOMAIN = "input.jms";
	private static final Boolean IS_PERM_IP_NEEDED = Boolean.TRUE;
	private static final String IMAGE_ID = "@platform.image.id.i386.instance.default@";
	private static final Ec2ServerConfig.EC2_SERVER_SIZE SERVER_SIZE = Ec2ServerConfig.EC2_SERVER_SIZE.SMALL;
	private static final String PREFERRED_REGION = "aws-us-east";
	private static final String PREFERRED_ZONE = "1";

	@Test
	public void testSimple() throws JsonParseException, IOException {
		JmsServerCluster anyServerCluster = loadFromJsonResource(
				JmsServerCluster.class,
				"com/deleidos/rtws/systemcfg/beans/servercluster/simpleCluster.json");
		Assert.assertNotNull(anyServerCluster);
		Assert.assertEquals(USER_LABEL, anyServerCluster.getUserLabel());
		Assert.assertEquals(NAME, anyServerCluster.getName());
		Assert.assertEquals(CLUSTER_SUB_DOMAIN, anyServerCluster.getClusterSubDomain());
		Assert.assertEquals(IS_PERM_IP_NEEDED, anyServerCluster.isPermIpNeeded());
		Assert.assertNotNull(anyServerCluster.getIaasServerConfig());
		try {
			Ec2ServerConfig serverConfig = (Ec2ServerConfig)anyServerCluster.getIaasServerConfig();
			Assert.assertEquals(IMAGE_ID, serverConfig.getImageId());
			Assert.assertEquals(SERVER_SIZE, serverConfig.getSize());
			Assert.assertEquals(PREFERRED_REGION, serverConfig.getPreferredRegion());
			Assert.assertEquals(PREFERRED_ZONE, serverConfig.getPreferredZone());
		} catch (ClassCastException e) {
			Assert.fail("IaasServerConfig should be an instance of Ec2ServerConfig");
		}
		
		
		// TODO Other stuff like scaling, choke, and security
	}
}
