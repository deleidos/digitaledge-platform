package com.deleidos.rtws.core.framework.factory;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;

import com.deleidos.rtws.commons.config.PropertiesUtils;
import com.deleidos.rtws.core.framework.factory.PipelineFactory;

@Ignore("Disable until referenced default xml templates can be specified with a fully qualified path")
public class PipelineFactoryTest {

	private String rootDir = System.getProperty("basedir", "") + File.separator
			+ "src/test/resources/XMLTestFiles/";
	private String[] files = { "pipeline.alert.xml", "pipeline.enrichment.xml",
			"pipeline.parse.xml", "pipeline.zoie.xml" };

	/**
	 * Performs a syntax/initialization check of each of the pipeline factory
	 * config files.
	 */
	@Test
	public void testConfiguration() throws Exception {
		Properties properties = PropertiesUtils.loadProperties(System
				.getProperty("RTWS_CONFIG_DIR")
				+ File.separator
				+ "ingest/ingest.properties");
		for (String file : files) {
			PipelineFactory factory = new PipelineFactory();
			factory.setDefinitionFile(rootDir + file);
			factory.setProperties(properties);
			factory.initialize();
			if (factory.getNames().length == 0 && !factory.hasDefaultEntry()) {
				fail("Factory for configuration file '" + rootDir + file
						+ "' is empty.");
			}
		}
	}
}
