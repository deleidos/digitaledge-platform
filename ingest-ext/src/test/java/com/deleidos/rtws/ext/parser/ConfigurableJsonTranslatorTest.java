package com.deleidos.rtws.ext.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.deleidos.rtws.commons.exception.InitializationException;
import com.deleidos.rtws.core.framework.translator.AbstractConfigurableTranslator;

public class ConfigurableJsonTranslatorTest {
	
	private AbstractConfigurableTranslator cjsonTranslator = null;
	private String basedir;

	@BeforeClass
	public static void init() {
		Properties testProperties = System.getProperties();

		testProperties.setProperty("RTWS_TENANT_ID", "aws-dev");
		testProperties.setProperty("RTWS_BUCKET_NAME", "test-bucket");
		testProperties.setProperty("RTWS_DOMAIN", "nothing.deleidos.com");
		testProperties.setProperty("RTWS_MOUNT_MODE", "s3cmd");
		testProperties.setProperty("RTWS_MAX_ALLOCATION_REQUEST", "");
		testProperties.setProperty("RTWS_TEST_MODE", "true");
		testProperties.setProperty("username", "aws-dev");
		testProperties.setProperty("password", "redacted");
		testProperties.setProperty("webapp.repository.url.path","http://master.tms-dev.deleidos.com:80/repository");
		System.setProperties(testProperties);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		cjsonTranslator = new AbstractConfigurableTranslator() {
			
			@Override
			public String customFieldTranslator(String outputFieldPath, String outputFieldKey, Map<String, String> recordMap) {
				
				String result = null;
				
				if (outputFieldKey.equals("lineItemComment")) {
					return recordMap.get("LineItemComment");
				}
				else if (outputFieldKey.equals("orderComment")) {
					return recordMap.get("OrderComment");				
				}
				else if(outputFieldKey.equals("orderTime")) {
					return "12:59:59";
				}
				else {
					fail("Invalid field name");
				}
				
				return result;
			}			
		};
		basedir = System.getProperty("basedir");
		if (basedir == null) {
			System.err.println("Need to set basedir in system properties at startup time");
		}
		cjsonTranslator.setModelName("sales");
		cjsonTranslator.setModelVersion("2.0");
		cjsonTranslator.setInputFormatName("tpchcsv");
	}

	@After
	public void tearDown() throws Exception {
		cjsonTranslator = null;
	}

	@Test
	public void testSetFieldHandlingErrorPolicyString() {
		cjsonTranslator.setFieldHandlingErrorPolicy("DISCARD_AND_LOG");
		cjsonTranslator.setFieldHandlingErrorPolicy("DISCARD_AND_IGNORE");
		cjsonTranslator.setFieldHandlingErrorPolicy("asdlkfjasdkfhasd");
	}

	@Test
	public void testInitialize() throws InitializationException {
		cjsonTranslator.initialize();
		/*
		AbstractFileSetResource resource = cjsonTranslator.getDataModelResource();
		List<FileSetEntry> fileSets = resource.listFileSets(null);
		for (FileSetEntry fileSet : fileSets) {
			System.out.println("name=" + fileSet.name + ", desc=" + fileSet.description);
		}
		*/
	}
	
	@Test
	@Ignore(value = "disable until new data model mapping can be resolved")
	public void testRecordTranslation() throws Exception {
				
		cjsonTranslator.setModelVersion("0.0");
		cjsonTranslator.initialize();
		
		// Input data
		BufferedReader reader = new BufferedReader(new FileReader(basedir + "/src/test/resources/tpch_sample_file.psv"));

		// Results file to compare against
		BufferedReader resultsFile = new BufferedReader(new FileReader(basedir + "/src/test/resources/translator_test.out"));
		PrintWriter outputFile = new PrintWriter(new FileWriter(basedir + "/src/test/resources/translator_test.run"));
		
		String headerLine = reader.readLine();
		String headers[] = headerLine.split("\\|");
		
		String line;
		HashMap<String, String> map = new HashMap<String, String>();
		while ((line = reader.readLine()) != null) {
			String data[] = line.split("\\|");
			map.clear();
			for (int i=0; i<data.length; i++) {
				map.put(headers[i], data[i]);
			}
			
			// JSONObject jobj = cjsonTranslator.recordTranslation(map, new CustomFieldTranslatorTest()); 
			JSONObject jobj = cjsonTranslator.recordTranslation(map, "TEST", "UNCLASSIFIED");
			
			String correctResult = resultsFile.readLine();
			String resultWithUuid = jobj.toString();
			String resultWithoutUuid = resultWithUuid.replaceFirst("\\{\"uuid\":\".{36}\"", "{\"uuid\":\"\"");
			
			outputFile.println(resultWithoutUuid);
			assertFalse(resultWithUuid.equals(resultWithoutUuid));
			assertEquals(resultWithoutUuid,correctResult);
		}
		
		outputFile.close();
		reader.close();
		resultsFile.close();
	}

	@Test
	public void testSimpleRecord() throws Exception {
		
		cjsonTranslator.setModelVersion("4.0");
		
		cjsonTranslator.initialize();
		
		// Input data
		BufferedReader reader = new BufferedReader(new FileReader(basedir + "/src/test/resources/tpch_sample_file.psv"));

		String headerLine = reader.readLine();
		String headers[] = headerLine.split("\\|");
		
		String line;
		HashMap<String, String> map = new HashMap<String, String>();
		while ((line = reader.readLine()) != null) {
			String data[] = line.split("\\|");
			map.clear();
			for (int i=0; i<data.length; i++) {
				map.put(headers[i], data[i]);
			}
			
			// JSONObject jobj = cjsonTranslator.recordTranslation(map, new CustomFieldTranslatorTest()); 
			JSONObject jobj = cjsonTranslator.recordTranslation(map, "TEST", "UNCLASSIFIED");
			
			assertNotNull(jobj);
			
			String resultWithUuid = jobj.toString();
		}
		
		reader.close();
	}
	
	@Test
	public void testNestedArrayFail() throws Exception {
		
		cjsonTranslator.setModelVersion("4.0");
		
		cjsonTranslator.initialize();
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("zeroLevel", "zero");
		map.put("oneLevel[].key", "oneLevel");
		map.put("oneLevel[].nestedArray[]", "failure");
		
		try{ 
			cjsonTranslator.recordTranslation(map, "TEST", "UNCLASSIFIED");
		}catch(ParseException pe){
			//success
			return;
		}
		fail();
	}
	
	@Test
	@Ignore(value = "disabled until supported")
	public void testArrayRecord() throws Exception {
		cjsonTranslator.setModelName("ArrTest");
		cjsonTranslator.setModelVersion("1.0");
		cjsonTranslator.setInputFormatName("testjson");
		
		cjsonTranslator.initialize();
		
		// Input data
		HashMap<String, String> map = new HashMap<String, String>();
		for(int i=0;i<10;i++){
			map.put("ValArray["+i+"]", ""+i);
		}
			// JSONObject jobj = cjsonTranslator.recordTranslation(map, new CustomFieldTranslatorTest()); 
			JSONObject jobj = cjsonTranslator.recordTranslation(map, "testjson", "UNCLASSIFIED");
			
			assertNotNull(jobj);
			JSONArray arrObj = jobj.getJSONArray("ValArray");
			assertNotNull(arrObj);
			assertTrue(arrObj.size()==10);
		}
	}
