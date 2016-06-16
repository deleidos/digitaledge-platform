package com.deleidos.rtws.core.framework.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Properties;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.deleidos.rtws.commons.config.PropertiesUtils;
import com.deleidos.rtws.core.framework.factory.ParserFactory;
import com.deleidos.rtws.core.framework.parser.Parser;

/**
 * JUnit class to test the ParserFactoryTest class.
 */
public class ParserFactoryTest {

	/** The base directory of the project */
	private static String basedir;

	@BeforeClass
	public static void init() {

		basedir = System.getProperty("basedir");
		if (basedir == null) {
			System.err
					.println("Need to set basedir in system properties at startup time");
		}
		Properties testProperties = System.getProperties();

		testProperties.setProperty("RTWS_TENANT_ID", "aws-dev");
		testProperties.setProperty("password", "redacted");
		testProperties.setProperty("RTWS_BUCKET_NAME", "test-bucket");
		testProperties.setProperty("RTWS_DOMAIN", "nothing.deleidos.com");
		testProperties.setProperty("RTWS_MOUNT_MODE", "s3cmd");
		testProperties.setProperty("RTWS_MAX_ALLOCATION_REQUEST", "");
		testProperties.setProperty("RTWS_TEST_MODE", "true");
		System.setProperties(testProperties);
	}

	/**
	 * Tears down the test fixture. (Called after every test case method.)
	 */
	@After
	public void tearDown() {

	}

	/**
	 * Performs a syntax/initialization check of each of the parser factory
	 * config files.
	 */
	@Test
	public void testConfiguration() throws Exception {
		File baseDir = new File(basedir);
		File defFile = new File(baseDir, "src/test/resources/parsers.xml");

		File curDir = new File(".");
		String pwd = curDir.getCanonicalPath();
		System.out.println(pwd);

		Properties properties = PropertiesUtils.loadProperties(System
				.getProperty("RTWS_CONFIG_DIR")
				+ File.separator
				+ "ingest/ingest.properties");
		ParserFactory factory = new ParserFactory();
		factory.setDefinitionFile(defFile.getCanonicalPath());
		factory.setProperties(properties);
		factory.initialize();
		if (factory.getNames().length == 0 && !factory.hasDefaultEntry()) {
			fail("Factory for configuration file '"
					+ defFile.getCanonicalPath() + "' is empty.");
		} else {
			for (String key : factory.getNames()) {
				Parser parser = factory.getInstance(key);
				assertNotNull(parser);
				assertNotSame(parser, factory.getDefaultEntry());
			}
		}
	}

}
