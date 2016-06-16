package com.deleidos.rtws.systemcfg.serialize;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import junit.framework.TestCase;

import com.deleidos.rtws.commons.cloud.platform.aws.AwsConnectionFactory;
import com.deleidos.rtws.commons.cloud.platform.aws.AwsServiceInterface;
import com.deleidos.rtws.master.core.beans.ClusterDefinition;
import com.deleidos.rtws.master.core.beans.ProcessGroup;
import com.deleidos.rtws.systemcfg.serialize.DefinitionSerializer;
import com.deleidos.rtws.systemcfg.serialize.DefinitionSerializerImpl;

public class DefinitionSerializerImplTest extends TestCase {
	DefinitionSerializer serializer;
	
	
	@Before
	public void setUp() throws Exception {
		serializer = new DefinitionSerializerImpl();
		serializer.initialize();
	}
	
	@After
	public void tearDown() throws Exception {
		serializer.dispose();
	}
	
	@Test
	public void testClusterDefinitionSerialize() {
		try {
			ClusterDefinition clusterDef = new ClusterDefinition();
			clusterDef.setProcessReallocationInterval(90000);
			clusterDef.setTestMode(false);
			clusterDef.setDomainName("master.saic.com");
			clusterDef.setProcessStateFile(new File("default.xml"));
			
			AwsServiceInterface provider = new AwsServiceInterface();
			AwsConnectionFactory connectionFactory = new AwsConnectionFactory();
			provider.setConnectionFactory(connectionFactory);
			provider.setTimeout(300000);
			
			clusterDef.setServiceInterface(provider);
			
			ProcessGroup[] groups = new ProcessGroup[2];
			ProcessGroup pg1 = new ProcessGroup();
			pg1.setName("Test1");
			groups[0] = pg1;
			
			ProcessGroup pg2 = new ProcessGroup();
			pg2.setName("Test2");
			groups[1] = pg2;
			
			clusterDef.setProcessGroups(groups);
		
			serializer.createDefinitionFile(clusterDef, new File("temp"));
		} catch (Exception ex) {
			fail("Unable to marshal object " + ex.getMessage());
		}
	}
	
	@Test
	public void testClusterDefinitionDeserialize() {
		try {
			File inputFile = new File("C:\\Data\\rainerr\\Workspaces\\RTWS\\systembuilder\\src\\resource\\test\\com\\saic\\rtws\\systemcfg\\serialize\\test-services.xml");
			assertTrue(inputFile.canRead());
			ClusterDefinition clusterDefinition = serializer.createObject(inputFile, ClusterDefinition.class);
		} catch (Exception ex) {
			fail("Unable to unmarshal object " + ex.getMessage());
		}
	}
	
}
