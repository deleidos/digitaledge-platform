package com.deleidos.rtws.tools.s3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Test. */
public class S3SweeperExceptionTest {
	
	@Test
	public void testConstructors() {
		S3SweeperException e = new S3SweeperException("Hello 1");
		assertEquals(e.getMessage(), "Hello 1");
		
		e = new S3SweeperException("Hello 2", new Exception("Exception 1"));
		assertEquals(e.getMessage(), "Hello 2");
	}
}