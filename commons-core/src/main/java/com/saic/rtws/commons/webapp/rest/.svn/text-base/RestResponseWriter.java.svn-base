package com.saic.rtws.commons.webapp.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import com.saic.rtws.commons.model.response.StandardResponse;

public class RestResponseWriter {
	
	public RestResponseWriter() { }
	
	public static void write(HttpServletRequest request, HttpServletResponse response, StandardResponse<?> content) throws IOException, MarshalException, ValidationException {
		BaseRestServlet bhs = new BaseRestServlet();
		write(response, bhs.parseFormat(request), content);
	}
	
	public static void write(HttpServletResponse response, String format, StandardResponse<?> content) throws IOException, MarshalException, ValidationException {
		// Set Content-Type 
		String contentType = RestResponseFormat.getFormatContentType(format);
		if (contentType != null && !"".equals(contentType)) { 
			response.setContentType(format); 
		}
		else {
			response.setContentType(RestResponseFormat.getFormatContentType(RestResponseFormat.JSON));
		}
		
		// Output
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			
			if (RestResponseFormat.XML.equals(format)) {
				writeXML(pw, content);
			}
			else {
				writeJSON(pw, content);
			}
		}
		finally {
			try { pw.close(); } catch (Exception e) { }
		}
	}
	
	public static void writeJSON(PrintWriter pw, StandardResponse<?> content) throws IOException, MarshalException, ValidationException {
		// create a new marshaler
		JSONObject jsonObj = JSONObject.fromObject(content);

		// marshal the object
		jsonObj.write(pw);
	}
	
	public static void writeXML(PrintWriter pw, StandardResponse<?> content) throws IOException, MarshalException, ValidationException {
		// create a new marshaler
		Marshaller marshaller = new Marshaller();
		marshaller.setWriter(pw);

		// marshal the object
		marshaller.marshal(content);
	}
}
