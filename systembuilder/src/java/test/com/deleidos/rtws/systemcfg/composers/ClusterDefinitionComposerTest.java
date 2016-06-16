package com.deleidos.rtws.systemcfg.composers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.bind.MarshalException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

import com.deleidos.rtws.systemcfg.beans.JsonDeserializeTest;
import com.deleidos.rtws.systemcfg.beans.SystemConfig;
import com.deleidos.rtws.systemcfg.composers.ClusterDefinitionComposer;
import com.deleidos.rtws.systemcfg.serialize.DefinitionSerializer;
import com.deleidos.rtws.systemcfg.serialize.DefinitionSerializerImpl;


public class ClusterDefinitionComposerTest extends TestCase {
	DefinitionSerializer serializer;
	ClusterDefinitionComposer composer;
	SystemConfig config;
	
	@Before
	public void setUp() throws Exception {
		JsonDeserializeTest utils = new JsonDeserializeTest();
		config = utils.loadSystemConfigFromJsonResource("com/deleidos/rtws/systemcfg/beans/systemOutline.json");
		DefinitionSerializer serializer = new DefinitionSerializerImpl();
		serializer.initialize();
		composer = new ClusterDefinitionComposer();
		composer.setDefinitionSerializer(serializer);
		composer.initialize();
	}
	
	@After
	public void tearDown() throws Exception {
		//serializer.dispose();
	}
	
	@Test
	public void testCompose() {
		try {
			File inputFile = new File("C:\\Data\\rainerr\\Workspaces\\RTWS\\systembuilder\\src\\resource\\test\\com\\saic\\rtws\\systemcfg\\serialize\\empty-services.xml");
			assertTrue(inputFile.canRead());
			FileInputStream fileInputStream = new FileInputStream(inputFile);
			composer.setDefaultInputFile(inputFile);
			composer.loadDefaults(fileInputStream);
			composer.compose(config);
		} catch(FileNotFoundException fnfe) {
			fail("File Not Found " + fnfe.getMessage());
		} catch(MarshalException me) {
			fail("Marshalling Exception " + me.getMessage());
		}
	}

}
