package com.deleidos.rtws.ext.datasink;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deleidos.rtws.ext.datasink.ScriptingDataSink;

import junit.framework.TestCase;

/**
 * 
 * A JUnit test case for the {@link ScriptingDataSink} class.
 * 
 */
public class ScriptingDataSinkTest extends TestCase {

	private static Logger logger = LogManager
			.getLogger(ScriptingDataSinkTest.class);

	private String baseDir = null;
	private JSONObject jsonObject = null;

	/**
	 * Before a test runs perform setup functionality.
	 */
	@Before
	public void setUp() {
		baseDir = System.getProperty("basedir");
		if (baseDir == null) {
			logger.warn("Need to set basedir in system properties at startup time");
		}

		// create JSONObject to test the data pass from Java to a script
		jsonObject = new JSONObject();
		jsonObject.element("name", "Bob");
		jsonObject.element("number", 2);
		jsonObject.element("phone", "555-777-9999");
	}

	/**
	 * A test case to test the ScriptEngine creation using a engine name.
	 */
	@Test
	public void testScriptEngineCreation() {
		String scriptPath = baseDir
				+ "/src/test/resources/scriptTestFiles/passJsonObjectJavaScriptTest.tar";
		ScriptingDataSink sink = new ScriptingDataSink("JavaScript", scriptPath);

		sink.initialize();

		// if no script engine was created then fail test
		assertNotNull(
				"ScriptEngine was not created in sink as expected for the given Engine name",
				sink.getScriptEngine());

		sink = new ScriptingDataSink("XYZ", scriptPath);

		sink.initialize();

		assertNull(
				"Found unexpected engine, test case is trying to not find an engine.",
				sink.getScriptEngine());

	}

	/**
	 * A test case to test the Data Sink processor, creates a ScriptEngine and
	 * passes a JSON object to the script to be processed. This tests a
	 * Javascript ScriptEngine.
	 */
	@Test
	public void testScriptingDataSinkWithJavascript() {
		String scriptPath = baseDir
				+ "/src/test/resources/scriptTestFiles/passJsonObjectJavaScriptTest.js";
		ScriptingDataSink sink = new ScriptingDataSink("JavaScript", scriptPath);

		// initialize data sink
		sink.initialize();

		// if no script engine was created then fail test
		assertNotNull("No ScriptEngine created in sink", sink.getScriptEngine());

		// process json object
		sink.process(jsonObject);

		// cleanup data sink
		sink.dispose();
	}

	/**
	 * A test case to test the Data Sink processor, creates a ScriptEngine and
	 * passes a JSON object to the script to be processed. This tests a Groovy
	 * ScriptEngine.
	 */
	@Test
	public void testScriptingDataSinkWithGroovy() {
		String scriptPath = baseDir
				+ "/src/test/resources/scriptTestFiles/passJsonObjectGroovyTest.groovy";
		ScriptingDataSink sink = new ScriptingDataSink("groovy", scriptPath);

		// initialize data sink
		sink.initialize();

		// if no script engine was created then fail test
		assertNotNull("No ScriptEngine created in sink", sink.getScriptEngine());

		// process json object
		sink.process(jsonObject);

		// cleanup data sink
		sink.dispose();
	}

	/**
	 * Test case for passing script parameters from Java to the script. N
	 * parameters can be passed to the script and then accessed in the same
	 * order by the script.
	 */
	@Test
	public void testJSRPassParametersGroovy() {
		String scriptPath = baseDir
				+ "/src/test/resources/scriptTestFiles/parameterPassToGroovyScript.groovy";

		// lookup ScriptEngine by file extension
		ScriptingDataSink sink = new ScriptingDataSink(null, scriptPath);

		// pass parameters to the script
		String[] parameters = new String[3];
		parameters[0] = baseDir + "/../JUnitResourceHolder/tmp/groovy.out";
		parameters[1] = "param2";
		parameters[2] = "80";

		sink.setParameter(parameters);

		// initialize data sink
		sink.initialize();

		// if no script engine was created then fail test
		assertNotNull("No ScriptEngine created by file extension in sink",
				sink.getScriptEngine());

		// process json object
		sink.process(jsonObject);

		// cleanup data sink
		sink.dispose();
	}

	/**
	 * Cleanup after a test case runs.
	 */
	@After
	public void cleanUp() {

	}
}
