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

import info.ganglia.gmetric4j.gmetric.GMetric;
import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ganglia.GangliaReporter;

public enum CloudEnvironmentServiceMetrics {
	INSTANCE;

	private MetricRegistry metrics = new MetricRegistry();
	private GMetric ganglia;
	private GangliaReporter reporter;
	private Logger log = LoggerFactory.getLogger(getClass());

	private CloudEnvironmentServiceMetrics() {
	}

	public void start() {

		if (ganglia == null) {
			try {
				String hostname = Inet4Address.getLocalHost().getHostName();
				if (hostname != null) {
					ganglia = new GMetric(hostname, 8666, UDPAddressingMode.UNICAST, 1);
				} else {
					ganglia = new GMetric(Inet4Address.getLocalHost().getHostAddress(), 8666, UDPAddressingMode.UNICAST, 1);
				}

			} catch (IOException e) {
				log.error(e.getMessage(), e);
			} finally {
			}

		}

		if (reporter == null) {
			try {
				reporter = GangliaReporter.forRegistry(metrics).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build(ganglia);
			} finally {
			}
		}

		log.info("Starting Ganglia Reporter with 5 minute intervals.");
		reporter.start(1, TimeUnit.MINUTES);

	}

	public void stop() {

		if (reporter != null) {
			reporter.stop();
			reporter.close();

		}
		if (ganglia != null) {
			try {
				ganglia.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public MetricRegistry getMetrics() {
		return metrics;
	}

}
