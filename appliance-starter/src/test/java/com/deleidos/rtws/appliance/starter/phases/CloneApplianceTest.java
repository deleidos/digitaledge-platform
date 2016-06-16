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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.deleidos.rtws.appliance.starter.model.PhaseResult;
import com.deleidos.rtws.appliance.starter.model.vmware.HostExecutionEnvironment;

@Ignore("Used for local testing")
public class CloneApplianceTest {

	private static HostExecutionEnvironment environment;

	private static ObjectMapper mapper = new ObjectMapper();

	@BeforeClass
	public static void init() throws JsonParseException, JsonMappingException, IOException {
		environment = mapper.readValue(new File("./src/main/conf/cic.json"), HostExecutionEnvironment.class);
	}

	@Test
	public void testCloning() throws Exception {
		CloneAppliance phase = new CloneAppliance();
		phase.initialize(environment);
		PhaseResult rslt = phase.call();
		assertTrue(rslt.isSuccessful());
	}

}
