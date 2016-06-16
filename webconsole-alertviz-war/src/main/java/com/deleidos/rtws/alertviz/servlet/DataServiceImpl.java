package com.deleidos.rtws.alertviz.servlet;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.deleidos.rtws.alertviz.servlet.DataService;
import com.deleidos.rtws.alertviz.util.UserDataUtil;
import com.deleidos.rtws.commons.cloud.exception.StorageException;
import com.deleidos.rtws.commons.cloud.platform.jetset.JetSetConnectionFactory;
import com.deleidos.rtws.commons.cloud.platform.jetset.JetSetThreadedStorageService;
import com.deleidos.rtws.commons.jersey.config.JerseyClientConfig;
import com.deleidos.rtws.commons.model.response.ErrorResponse;
import com.deleidos.rtws.commons.model.response.PropertiesResponse;
import com.deleidos.rtws.commons.model.response.StandardResponse;
import com.deleidos.rtws.commons.model.tmsdb.EndPointURL;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;

@Path("/data")
public class DataServiceImpl implements DataService {

	private static final Logger logger = Logger.getLogger(DataServiceImpl.class);
	private static final String SCENARIO = "Scenario";
	private static final String RF_BUTTON = "RF";
	private static final String CHECKPOINT_BUTTON = "Checkpoint";
	private static final String TRACKING_BUTTON = "Tracking";
	private static final String NUCLEAR_BUTTON = "Nuclear";
	private static final String fileInputs[] = { "Real_RF.csv", "Real_Checkpoint.csv",
		"Real_tracking.csv", "Real_nuclear.csv"};
	private static final String bucketNames[] = { "athena.rf", "athena.checkpoint",
		"athena.tracking", "athena.Nuclear"};
	
	public DataServiceImpl()
	{
		super();
		
	}
	
	public StandardResponse proxyDataCsvFileToS3(HttpServletRequest request, String button) {
		
		try {
			if (button.contains(SCENARIO))
			{	
				String scenario = button.replaceAll( "[\\D]", "");
				for ( int i=0; i < fileInputs.length ; i++ )
				{
					String fileName = fileInputs[i].substring(0,fileInputs[i].indexOf(".")) ;
					fileName = fileName.concat(scenario).concat(fileInputs[i].substring(fileInputs[i].indexOf(".")));
					//System.out.print(scenNum);
					uploadFile(bucketNames[i], fileName);
				}				
			}
			
			else if (button.contains(RF_BUTTON)) {
				uploadFile(bucketNames[0], fileInputs[0]);
			}
			else if (button.contains(CHECKPOINT_BUTTON)) {
				uploadFile(bucketNames[1], fileInputs[1]);
			}
			else if (button.contains(TRACKING_BUTTON)) {
				uploadFile(bucketNames[2], fileInputs[2]);
			}
			else if (button.contains(NUCLEAR_BUTTON)) {
				uploadFile(bucketNames[3], fileInputs[3]);
			}
			else {
				throw new Exception ("Incorrect Button Data Received...");
			}
			
			StandardResponse response = new StandardResponse();
			response.setStandardHeaderCode(200);
			return response;
		} catch (Exception e) {
			logger.error("Error in data upload: " + e.getMessage());
			
			return buildErrorResponse(500, "Error in data upload: " + e.getMessage());
		}
	}

	protected ErrorResponse buildErrorResponse(int code, String message) {
		
		ErrorResponse response = new ErrorResponse();
		response.setStandardHeaderCode(code);
		response.setMessage(message);
		return response;
		
	}

	private void uploadFile(String bucket, String file) throws StorageException, MalformedURLException {
		if ((bucket != null) && (file != null)) {
			
			String webappPath = "/usr/local/jetty/webapps/alertviz/WEB-INF/classes/";
			
			String accessKey = UserDataUtil.getAccessKey();
			String secretKey = UserDataUtil.getSecretKey();
			String storageEndpointString = UserDataUtil.getStorageEndpoint();
			EndPointURL endpointURL = null;
			
			endpointURL = new EndPointURL(storageEndpointString);

			String storageEndpoint = endpointURL.getEndpoint();
			String storagePortNumber = String.valueOf(endpointURL.getPort());
			String storageVirtualPath = endpointURL.getVirtualpath();
			
			JetSetConnectionFactory factory = new JetSetConnectionFactory();
			factory.setCredentials(accessKey, secretKey);
			factory.setStorageEndpoint(storageEndpoint);
			factory.setStoragePortNumber(storagePortNumber);
			factory.setStorageVirtualPath(storageVirtualPath);
			
			JetSetThreadedStorageService jsss = new JetSetThreadedStorageService();
			jsss.setConnectionFactory(factory);
			
			File csvFile = new File(webappPath + file);

			jsss.storeFile(bucket, file, csvFile);
			
		}		
	}


}
