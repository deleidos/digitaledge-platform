package com.deleidos.rtws.webapp.contentapi.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import junit.framework.TestCase;
import junit.framework.TestSuite;
//import static org.junit.Assert.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.deleidos.rtws.webapp.contentapi.dao.SimpleContentStorageDAO;

public class SimpleContentStorageDAOTest extends TestCase {
	
	public SimpleContentStorageDAOTest(String testName) {
		super(testName);
	}
	
	public static TestSuite suite() {
		TestSuite suite= new TestSuite();
	    suite.addTest(new SimpleContentStorageDAOTest("testConstructor"));
	    suite.addTest(new SimpleContentStorageDAOTest("testGetBasePath"));
	    suite.addTest(new SimpleContentStorageDAOTest("testGetContentDir"));
	    suite.addTest(new SimpleContentStorageDAOTest("testGetContentObjectPath"));
	    suite.addTest(new SimpleContentStorageDAOTest("testGetContentPropertiesPath"));
	    suite.addTest(new SimpleContentStorageDAOTest("testContentObjectHandling"));
	    return suite;
	}
	
    /**
     * Sets up the test fixture. 
     * (Called before every test case method.) 
     */ 
    protected void setUp() { 
    } 
    
    /**
     * Tears down the test fixture. 
     * (Called after every test case method.) 
     */ 
    protected void tearDown() { 
    } 
    
	@Test
	public void testConstructor() {
		SimpleContentStorageDAO obj = new SimpleContentStorageDAO(0);
		assertNotNull(obj);
	}
	
	@Test
	public void testGetBasePath() {
		SimpleContentStorageDAO obj = new SimpleContentStorageDAO(0);
		obj.setBasePath("/base");
		assertEquals(obj.getBasePath(), "/base");
	}
	
	@Test
	public void testGetContentDir() {
		SimpleContentStorageDAO obj = new SimpleContentStorageDAO(0);
		obj.setBasePath("/base");
		assertEquals(obj.getBasePath(), "/base");
		assertEquals(obj.getContentDir(1L), "/base/1");
	}
	
	@Test
	public void testGetContentObjectPath() {
		SimpleContentStorageDAO obj = new SimpleContentStorageDAO(0);
		obj.setBasePath("/base");
		assertEquals(obj.getContentObjectPath(1L), "/base/1/1");
	}
	
	@Test
	public void testGetContentPropertiesPath() {
		SimpleContentStorageDAO obj = new SimpleContentStorageDAO(0);
		obj.setBasePath("/base");
		assertEquals(obj.getContentPropertiesPath(1L), "/base/1/1.properties");
	}

	@Test
	public void testContentObjectHandling() throws java.io.IOException {
		File tempDir = File.createTempFile("data_", "dir");
		if (tempDir.delete()) {
			tempDir.mkdirs();
		}
		
		SimpleContentStorageDAO obj = new SimpleContentStorageDAO(0);
		obj.setBasePath(tempDir.getAbsolutePath());
		
		File f = File.createTempFile("tempcontentobject", ".txt");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(f));
			pw.write("Test content object file");
		}
		finally {
			try { pw.close(); } catch (Exception e) { }
		}
		
		// 
		long contentId = 0L;
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			contentId = obj.saveContentObject(f.getName(), "text/plain", is);
		}
		finally {
			try { is.close(); } catch (Exception e) { }
		}
		
		// 
		Properties p = obj.getContentProperties(contentId);
		assertEquals("Content object file name not stored correctly in properties file", p.getProperty("Filename"), f.getName());
		assertEquals("Content object file name not stored correctly in properties file", p.getProperty("Content-Type"), "text/plain");
		
		// 
		File f2 = File.createTempFile("tempcontentobject2", ".txt");
		OutputStream os = null;
		try {
			is = obj.getContentObjectStream(contentId);
			os = new FileOutputStream(f2);
			
			IOUtils.copyLarge(is, os);
		}
		finally {
			try { is.close(); } catch (Exception e) { }
			try { os.close(); } catch (Exception e) { }
		}
		
		//
		assertEquals("Files are not equal", f.length(), f2.length());
		
		// cleanup
		String filePath = obj.getContentObjectPath(contentId);
		String propPath = obj.getContentPropertiesPath(contentId);
		f = new File(filePath);
		f.deleteOnExit();
		f = new File(propPath);
		f.deleteOnExit();
		
	}
}
