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

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.codahale.metrics.Gauge;
import com.deleidos.rtws.commons.cloud.environment.monitor.service.CloudEnvironmentServiceMetrics;
import com.yammer.metrics.annotation.Timed;

@Path("/report")
public class SystemMetricsReportingResource {

	private AtomicLong numberOfDigitalEdgeSystems = new AtomicLong(0);

	public SystemMetricsReportingResource() {
		super();

		CloudEnvironmentServiceMetrics.INSTANCE.getMetrics().register("running.digitaledge.systems", new Gauge<Long>() {
			@Override
			public Long getValue() {
				return numberOfDigitalEdgeSystems.get();
			}
		});

	}

	@POST
	@Path("{count}/running/systems")
	@Timed
	public Response numberOfSystems(@PathParam("count") final long count) throws Exception {

		numberOfDigitalEdgeSystems.set(count);

		return Response.ok().build();
	}
}
