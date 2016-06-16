 package com.deleidos.rtws.webapp.contentapi.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


public class ContentObjectServletTestIntegration extends TestCase {
	
	Logger logger = Logger.getLogger(ContentObjectServletTestIntegration.class);
	
	public ContentObjectServletTestIntegration(String testName) {
		super(testName);
	}
	
	public static TestSuite suite() {
		TestSuite suite= new TestSuite();
	    suite.addTest(new ContentObjectServletTestIntegration("testPostGetLoop1"));
	    return suite;
	}
	
	/**
	 * 
	 */
	public void testPostGetLoop1() {
        HttpClient client = new HttpClient();
        
        /*
         * Check for server
         * 
         */
        String baseUrl = "http://localhost:8080";
        //baseUrl = "http://ec2-75-101-199-222.compute-1.amazonaws.com";
        
        GetMethod testGet = new GetMethod(baseUrl + "/contentapi/isalive.jsp");
		try {
			logger.info("Testing " + baseUrl + "/contentapi/ispresent.jsp");
			int status = client.executeMethod(testGet);
			
            if (status != 200) {
            	// exit
            	return;
            }
		} catch (Exception e) {
			logger.error("Failed to test connection: ", e);
        	// exit
        	return;
		} finally {
			testGet.releaseConnection();
		}
        
		/*
		 * Check for temp directory
		 * 
		 */
		File dir = new File("C:/Temp");
		if (!dir.exists()) {
			dir = new File("/tmp");
			if (!dir.exists()) {
				logger.error("Could not find temp directory");
				return;
			}
		}
		
		File inFile = new File(dir.getAbsolutePath() + "/ContentObjectServletTestIntegrationContent_in.txt");
		File outFile = new File(dir.getAbsolutePath() + "/ContentObjectServletTestIntegrationContent_out.txt");
		
		PrintWriter pw = null;
		try {
			// flag for cleanup
			inFile.deleteOnExit();
			
			pw = new PrintWriter(inFile);
			pw.write("Test content file");
		}
		catch (Exception e) {
			logger.error("Failed to create test file: ", e);
			fail("Failed to create test file: " + e.getMessage());
		}
		finally { 
			try { pw.close(); } catch (Exception e) { }
		}
		
		String url = baseUrl + "/contentapi/json/content/object";
		PostMethod filePost = new PostMethod(url);
        
        filePost.getParams().setBooleanParameter(
                HttpMethodParams.USE_EXPECT_CONTINUE,
                true);
        
        String responseString = "";
        JSONObject responseObject = null;
        
        try {
            
            logger.info("Uploading " + inFile.getAbsolutePath() + " to " + url);
            
            Part[] parts = {
                new FilePart(inFile.getName(), inFile)
            };
            
            filePost.setRequestEntity(
                    new MultipartRequestEntity(parts, 
                    filePost.getParams())
                    );
            
            client.getHttpConnectionManager().
                    getParams().setConnectionTimeout(5000);
            
            int status = client.executeMethod(filePost);
            
            if (status == HttpStatus.SC_OK) {
            	responseString = filePost.getResponseBodyAsString(); 
            	logger.info(
                        "Upload complete, response=" + responseString
                        
                        );
                
            	//System.out.println(responseString);
                logger.info("Response = " + responseString);
                
                // serialize into string
                responseObject = (JSONObject) JSONSerializer.toJSON(responseString);
                
            } else {
            	responseString = HttpStatus.getStatusText(status); 
            	logger.info(
                        "Upload failed, response=" + responseString
                        );
            	fail("Failed to get perform POST and interpret results: " + responseString);
            }
            
        } catch (Exception ex) {
			logger.error("Failed to get perform POST and interpret results: ", ex);
			fail("Failed to get perform POST and interpret results: " + ex.getMessage());
			return;
        } finally {
            filePost.releaseConnection();
        }
        
        url = baseUrl + responseObject.getJSONObject("properties").getString("contentURL");
        
        logger.info("Save " + url + " to " + outFile.getAbsolutePath());
        GetMethod fileGet = new GetMethod(url);
		InputStream is = null;
		OutputStream os = null;
		try {
			int status = client.executeMethod(fileGet);
			assertEquals("Did not return expected code of 200", status, 200);
			
            if (status == HttpStatus.SC_OK) {
            	// open input stream from foreign server
            	is = fileGet.getResponseBodyAsStream();
            	
            	// cleanup after jvm ends
				outFile.deleteOnExit();
				// open stream to output file
				os = new FileOutputStream(outFile);
			
				// copy streams
				IOUtils.copyLarge(is, os);
            }
		} catch (Exception e) {
			logger.error("Failed to copy get method input stream to file from " + url + ": ", e);
		} finally {
			fileGet.releaseConnection();
		}
		
		// file length check
		assertEquals("Length of input file does not match output file.", inFile.length(), outFile.length());
		
	}
}
