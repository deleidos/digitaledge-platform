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
package com.deleidos.rtws.appliance.starter.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.deleidos.rtws.appliance.starter.exception.ConnectionFailedException;
import com.deleidos.rtws.appliance.starter.model.vmware.HostExecutionEnvironment;
import com.vmware.vim25.mo.ServiceInstance;

public class ServiceInstanceBuilder {

	public static ServiceInstance build(HostExecutionEnvironment applianceEnvironment) throws ConnectionFailedException {
		ServiceInstance si = null;
		Logger log = Logger.getLogger(ServiceInstanceBuilder.class);

		try {
			si = new ServiceInstance(new URL(String.format("https://%s/sdk", applianceEnvironment.getvSphereHost())),
					applianceEnvironment.getUsername(), applianceEnvironment.getPassword(), true);
		} catch (RemoteException e) {
			log.error(e.getMessage(), e);
			throw new ConnectionFailedException(e.getMessage());
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
			throw new ConnectionFailedException(e.getMessage());
		} finally {
		}

		return si;
	}
}
