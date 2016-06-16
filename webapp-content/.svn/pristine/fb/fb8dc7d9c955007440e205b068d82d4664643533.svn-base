package com.deleidos.rtws.webapp.contentapi.dao;

import junit.framework.TestCase;
import junit.framework.TestSuite;
//import static org.junit.Assert.*;
import org.junit.Test;

import com.deleidos.rtws.webapp.contentapi.dao.SimpleContentIdGenerator;

public class SimpleContentIdGeneratorTest extends TestCase {
	
	public SimpleContentIdGeneratorTest(String testName) {
		super(testName);
	}
	
	public static TestSuite suite() {
		TestSuite suite= new TestSuite();
	    suite.addTest(new SimpleContentIdGeneratorTest("testConstructor"));
	    suite.addTest(new SimpleContentIdGeneratorTest("testGetInstance"));
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
		SimpleContentIdGenerator obj = new SimpleContentIdGenerator();
		assertNotNull(obj);
		
		SimpleContentIdGenerator.SimpleContentIdGeneratorHolder obj2 = new SimpleContentIdGenerator.SimpleContentIdGeneratorHolder();
		assertNotNull(obj2);
	}

	@Test
	public void testGetInstance() {
		SimpleContentIdGenerator obj = SimpleContentIdGenerator.getInstance();
		assertNotNull(obj);
	}

}
