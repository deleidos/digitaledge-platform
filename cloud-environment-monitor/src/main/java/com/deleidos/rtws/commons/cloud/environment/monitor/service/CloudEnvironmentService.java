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
package com.deleidos.rtws.commons.cloud.environment.monitor.service;

import com.deleidos.rtws.commons.cloud.environment.monitor.configuration.AvailableCloudConfiguration;
import com.deleidos.rtws.commons.cloud.environment.monitor.healthcheck.CloudMonitorHealthCheck;
import com.deleidos.rtws.commons.cloud.environment.monitor.resources.AvailableComputeResource;
import com.deleidos.rtws.commons.cloud.environment.monitor.resources.SystemMetricsReportingResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class CloudEnvironmentService extends Service<AvailableCloudConfiguration> {

	public static void main(String[] args) throws Exception {
		new CloudEnvironmentService().run(args);
	}

	@Override
	public void initialize(Bootstrap<AvailableCloudConfiguration> bootstrap) {
		bootstrap.setName("available-resources");
	}

	@Override
	public void run(AvailableCloudConfiguration configuration, Environment environment) {

		CloudEnvironmentServiceMetrics.INSTANCE.start();
		CloudEnvironmentServiceMetrics.INSTANCE.getMetrics().counter("cloud-environment-monitor.startup").inc();

		environment.addResource(new AvailableComputeResource());
		environment.addResource(new SystemMetricsReportingResource());
		environment.addHealthCheck(new CloudMonitorHealthCheck("cloud-monitor"));

	}
}