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
package com.deleidos.rtws.commons.cloud.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.deleidos.rtws.commons.cloud.CloudProvider;
import com.deleidos.rtws.commons.cloud.cache.AvailableCloudComputeResourcesCache;
import com.deleidos.rtws.commons.cloud.environment.monitor.representations.AvailableCloudComputeResources;

public class AvailableCloudComputeResourcesCacheTest {

	@BeforeClass
	public static void init() {
		System.setProperty("RTWS_CONFIG_DIR", "./src/test/resources");
	}
	
	
	@Test
	public void compute() throws Exception {
		AvailableCloudComputeResourcesCache.instance.setFetcher(new MockComputeResourcesFetcher());
		AvailableCloudComputeResources computeResources = AvailableCloudComputeResourcesCache.instance.get(CloudProvider.EUCA);
		assertNotNull(computeResources);
		assertTrue(computeResources.getPercentageUsed() == 18);

	}

}
