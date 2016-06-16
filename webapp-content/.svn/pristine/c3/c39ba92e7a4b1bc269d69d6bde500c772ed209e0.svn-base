package com.deleidos.rtws.webapp.contentapi.servlet;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import com.deleidos.rtws.commons.exception.InvalidParameterException;
import com.deleidos.rtws.webapp.contentapi.servlet.ContentObjectServlet;

import static org.mockito.Mockito.*;

public class ContentObjectServletTest extends TestCase {
	
	public ContentObjectServletTest(String testName) {
		super(testName);
	}
	
	public static TestSuite suite() {
		TestSuite suite= new TestSuite();
	    suite.addTest(new ContentObjectServletTest("testConstructor"));
	    suite.addTest(new ContentObjectServletTest("testParseParameterContentId"));
	    suite.addTest(new ContentObjectServletTest("testParseParameterFilename"));
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
		ContentObjectServlet obj = new ContentObjectServlet();
		assertNotNull(obj);
	}
	
	@Test
	public void testParseParameterContentId() throws InvalidParameterException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		ContentObjectServlet obj = new ContentObjectServlet();
		
		when(request.getPathInfo()).thenReturn("/1/");
		assertEquals(obj.parseParameterContentId(request), 1);
	}
	
	@Test
	public void testParseParameterFilename() throws InvalidParameterException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		ContentObjectServlet obj = new ContentObjectServlet();
		
		when(request.getPathInfo()).thenReturn("/name/");
		assertEquals(obj.parseParameterFilename(request), "name");
	}
}
