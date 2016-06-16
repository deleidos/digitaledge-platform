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
package com.deleidos.rtws.appliance.starter.validator;

import org.apache.log4j.Logger;

import com.deleidos.rtws.appliance.starter.exception.ConnectionFailedException;
import com.deleidos.rtws.appliance.starter.model.vmware.HostExecutionEnvironment;
import com.deleidos.rtws.appliance.starter.util.ServiceInstanceBuilder;
import com.vmware.vim25.mo.ServiceInstance;

public class CredentialsValidator {

	private static Logger log = Logger.getLogger(CredentialsValidator.class);

	public static boolean isValid(HostExecutionEnvironment applianceEnvironment) throws ConnectionFailedException {
		boolean valid = false;

		ServiceInstance si = null;
		try {
			log.info(String.format("Attempting to connect to %s.", applianceEnvironment.getvSphereHost()));
			si = ServiceInstanceBuilder.build(applianceEnvironment);
			log.info(String.format("Successfully connected to connect to %s.", applianceEnvironment.getvSphereHost()));
			valid = true;

		} finally {
			if (si != null)
				si.getServerConnection().logout();
		}
		return valid;
	}

}
