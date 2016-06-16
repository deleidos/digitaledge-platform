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

import java.io.File;

import org.apache.log4j.Logger;

import com.deleidos.rtws.appliance.starter.model.PhaseResult;

public class OvfValidation extends AbstractApplianceStartupPhase {

	private Logger log = Logger.getLogger(getClass());

	@Override
	public PhaseResult call() throws Exception {
		/*
		 * Convention is that the DE Appliances produced are in OVF format, and
		 * as such follow:
		 * 
		 * <name>.ovf <name>.mf <name>-disk1.vmdk.gz
		 */
		String[] ovfFiles = { environment.getOvf().getLocation() + File.separator + environment.getOvf().getName() + ".ovf",
				environment.getOvf().getLocation() + File.separator + environment.getOvf().getName() + ".mf",
				environment.getOvf().getLocation() + File.separator + environment.getOvf().getName() + "-disk1.vmdk.gz" };

		for (String ovfFle : ovfFiles) {
			File f = new File(ovfFle);
			log.info(String.format("Validating: %s", f));
			if (!f.exists() && !f.canRead()) {
				result.setMessage(String.format("%s is missing or is not readable.", f.getAbsolutePath()));
				return result;
			}
		}

		result.setSuccessful(true);

		return result;
	}

}
