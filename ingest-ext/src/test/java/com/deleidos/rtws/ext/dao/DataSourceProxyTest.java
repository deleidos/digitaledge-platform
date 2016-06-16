package com.deleidos.rtws.ext.dao;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deleidos.rtws.ext.dao.DataSourceProxy;

public class DataSourceProxyTest {
	
	/** Called before every test case method. */
	@Before
	public void setUp() { } 


    /** Called after every test case method. */ 
	@After
	public void tearDown() { } 

	/** Test constructor. */
	@Test
	public void testConstructor() {
		DataSourceProxy obj1 = new DataSourceProxy();
		assertNotNull(obj1);
	}

	/**
	 * Test getters and setters.
	 */
	@Test
	public void testGettersAndSetters() {
		// Create instance of bean
		DataSourceProxy beanObj = new DataSourceProxy();
		
		// Create instance of tester
		//src.facundo.olano.getterAndSetterTester.GetterAndSetterTester beanTester 
		//	= new src.facundo.olano.getterAndSetterTester.GetterAndSetterTester();
		
		// Test Class
		//beanTester.testClass(DataSourceProxy.class);
		
		// Test Instance
		//beanTester.setIgnoredFields("definition", "description");
		//beanTester.testInstance(beanObj);
	}

}