package com.deleidos.rtws.webapp.contentapi.servlet.enunciate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

public interface ImageService {

	@Path("/saveImage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	@POST
	public String saveImage(@Context HttpServletRequest request);
	
	@Path("/loadImage/{contentId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@GET
	public byte[] loadImage(@PathParam("contentId") long contentId, @Context HttpServletResponse response);

}
