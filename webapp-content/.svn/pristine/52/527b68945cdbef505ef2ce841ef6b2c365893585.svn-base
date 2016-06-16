package com.deleidos.rtws.webapp.contentapi.servlet.enunciate;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.deleidos.rtws.commons.util.fileset.FileSetId;
import com.deleidos.rtws.commons.util.fileset.AbstractBaseFileSetResource.FileSetEntry;

public interface ContentService {

	@Path("/directory")
	@GET
	Map<FileSetEntry, List<FileSetId>> directory(@Context HttpServletResponse response);
	
	@Path("/load")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@GET
	byte[] load(@QueryParam("model") String model, @QueryParam("version") String version, 
				@QueryParam("referenceName") String referenceName, @Context HttpServletResponse response);
	
	//@Path("/save/{model}/{version}")
	@Path("/save")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	@POST
	public String save(@QueryParam("model") String model, @QueryParam("version") String version, @Context HttpServletRequest request);

}
