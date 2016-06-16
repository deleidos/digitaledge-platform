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
package com.deleidos.rtws.commons.cloud.environment.monitor.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EucaDescribeAvailabilityZonesClCommand implements EucaDescribeCommand {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private List<String> command = new LinkedList<String>();

	public EucaDescribeAvailabilityZonesClCommand() {
		super();
		command.add("euca-describe-availability-zones");
		command.add("verbose");
	}

	@Override
	public String execute() {
		String results = "";

		ProcessBuilder pb = new ProcessBuilder(command);
		InputStream is = null;

		try {
			Process p = pb.start();
			int rt = p.waitFor();
			if (rt == 0) {
				is = p.getInputStream();
				results = IOUtils.toString(is);

				logger.info("results: {}", results);

			} else {
				logger.error(IOUtils.toString(p.getErrorStream()));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} finally {
			IOUtils.closeQuietly(is);
		}

		return results;
	}

}
