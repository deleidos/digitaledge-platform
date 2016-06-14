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

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.deleidos.rtws.commons.cloud.environment.monitor.representations.AvailabilityZoneDescription;
import com.deleidos.rtws.commons.cloud.environment.monitor.tasks.EucaDescribeAvailabilityZones;

public class EucaDescribeAvailabilityZonesTest {

	@Test
	public void parse() {

		EucaDescribeAvailabilityZones command = new EucaDescribeAvailabilityZones(new MockEucaDescribeAvailabilityZones());
		List<AvailabilityZoneDescription> azDescriptions = command.fetch();
		assertNotNull(azDescriptions);

		for (AvailabilityZoneDescription azDescription : azDescriptions) {
			System.out.println("returned:" + azDescription);

		}

	}

}
