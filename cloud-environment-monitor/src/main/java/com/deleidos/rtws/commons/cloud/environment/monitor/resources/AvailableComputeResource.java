/**
 * LEIDOS CONFIDENTIAL
 * __________________
 *
 * (C)[2007]-[2014] Leidos
 * Unpublished - All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the exclusive property of Leidos and its suppliers, if any.
 * The intellectual and technical concepts contained
 * herein are proprietary to Leidos and its suppliers
 * and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Leidos.
 */
package com.deleidos.rtws.commons.cloud.environment.monitor.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.deleidos.rtws.commons.cloud.CloudProvider;
import com.deleidos.rtws.commons.cloud.cache.AvailableCloudComputeResourcesCache;
import com.deleidos.rtws.commons.cloud.environment.monitor.representations.AvailableCloudComputeResources;
import com.deleidos.rtws.commons.cloud.environment.monitor.tasks.EucalyptusComputeResourcesCollector;
import com.yammer.metrics.annotation.Timed;

@Path("/retrive")
@Produces(MediaType.APPLICATION_JSON)
public class AvailableComputeResource {

	@GET
	@Path("/{cloudProvider}/resources")
	@Timed
	public AvailableCloudComputeResources availableCompute(@PathParam("cloudProvider") CloudProvider cloudProvider) throws Exception {

		AvailableCloudComputeResourcesCache.instance.setFetcher(new EucalyptusComputeResourcesCollector());
		return AvailableCloudComputeResourcesCache.instance.get(cloudProvider);
	}
}
