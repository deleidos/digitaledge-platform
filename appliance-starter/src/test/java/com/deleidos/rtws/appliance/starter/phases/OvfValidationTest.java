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
package com.deleidos.rtws.appliance.starter.phases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.deleidos.rtws.appliance.starter.model.PhaseResult;
import com.deleidos.rtws.appliance.starter.model.vmware.HostExecutionEnvironment;
import com.deleidos.rtws.appliance.starter.model.vmware.Ovf;

@Ignore("Added debug, but disable auto execution on CI for now.")
public class OvfValidationTest {

	private HostExecutionEnvironment environment = new HostExecutionEnvironment("123.33.33.333", "abcd", "1234", new Ovf());

	@Test
	public void exists() throws Exception {

		environment.getOvf().setLocation(new File("./src/test/resources/mock_ovf"));
		environment.getOvf().setName("DigitalEdge_appliance_sales.yourdomain.for.vmware.com_1.3.0_1408133164");

		OvfValidation ovfValidator = new OvfValidation();
		ovfValidator.initialize(environment);
		PhaseResult rslt = ovfValidator.call();
		assertTrue(rslt.getMessage(), rslt.isSuccessful());
	}

	@Test
	public void doesNotExist() throws Exception {

		environment.getOvf().setLocation(new File("./src/test/resources/mock_ovf"));
		environment.getOvf().setName("gonefishing");

		OvfValidation ovfValidator = new OvfValidation();
		ovfValidator.initialize(environment);
		PhaseResult rslt = ovfValidator.call();
		assertFalse(rslt.getMessage(), rslt.isSuccessful());
	}
}
