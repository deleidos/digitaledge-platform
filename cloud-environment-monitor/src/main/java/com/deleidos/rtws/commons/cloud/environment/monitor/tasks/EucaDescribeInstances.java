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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deleidos.rtws.commons.cloud.environment.monitor.representations.InstanceDescription;
import com.google.common.base.Splitter;

public class EucaDescribeInstances {

	private static Logger logger = LoggerFactory.getLogger(EucaDescribeInstances.class);

	private EucaDescribeCommand command;
	
	public List<InstanceDescription> fetch() {

		String data = command.execute();
		if (data.length() > 0)
			return parse(data);

		return null;

	}
	
	private static List<InstanceDescription> parse(String input) {
		List<InstanceDescription> instanceDescriptions = new LinkedList<InstanceDescription>();
		
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new StringReader(input));

			int ct = 0;
			String line = null;
			while (reader.ready() && (line = reader.readLine()) != null) {

				if (line != null) {
					if (!line.startsWith("#") && line.length() > 0) {

						// Process odd # lines because event number lines are RESERVATION lines
						if ( line.startsWith("INSTANCE")) {

							if (logger.isDebugEnabled())
								logger.debug("line {} => trimmed {}", line, line.replaceAll("\\s++", "|"));

							Splitter sp = Splitter.on("|").trimResults();
							List<String> tokens = sp.splitToList(line.replaceAll("\\s++", "|"));
							
							instanceDescriptions.add(new InstanceDescription(tokens.get(1), tokens.get(3), 
																			 tokens.get(4), tokens.get(5),
																			 tokens.get(8)));
						}
					}
				}
				ct++;
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return instanceDescriptions;
	}
	
	public EucaDescribeInstances(EucaDescribeCommand command) {
		this.command = command;
	}
}
