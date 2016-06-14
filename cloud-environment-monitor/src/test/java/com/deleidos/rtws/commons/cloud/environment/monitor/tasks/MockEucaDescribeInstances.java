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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.IOUtils;

import com.deleidos.rtws.commons.cloud.environment.monitor.tasks.EucaDescribeCommand;

public class MockEucaDescribeInstances implements EucaDescribeCommand {

	@Override
	public String execute() {
		Reader input = null;
		try {
			input = new FileReader("./src/test/resources/euca-describe-instances-verbose_sample_format.txt");
			return IOUtils.toString(input);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			IOUtils.closeQuietly(input);
		}
		return "";
	}
}
