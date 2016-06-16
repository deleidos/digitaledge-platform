package com.deleidos.rtws.alertviz.servlet;

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

import com.deleidos.rtws.commons.model.response.StandardResponse;


public interface DataService {
	@Path("/{button}")
	@POST
	StandardResponse<?> proxyDataCsvFileToS3(
			@Context HttpServletRequest request,
			@PathParam(value = "button") String button);
}
