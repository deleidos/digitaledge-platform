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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.deleidos.rtws.appliance.starter.model.PhaseResult;
import com.deleidos.rtws.appliance.starter.model.vmware.ExsiHost;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Network;

public class Inventory extends AbstractApplianceStartupPhase {

	private static final int REQUIRED_NUM_CORES = 8;

	private Logger log = Logger.getLogger(getClass());

	@Override
	public PhaseResult call() throws Exception {
		try {
			Folder rootFolder = si.getRootFolder();

			List<ExsiHost> verifiedHosts = new ArrayList<ExsiHost>();

			ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
			if (mes == null || mes.length == 0) {
				result.setMessage(String.format("Failed to obtain vSphere configuration information from: %s",
						environment.getvSphereHost()));
				return result;
			}

			for (ManagedEntity me : mes) {

				ExsiHost host = environment.getHost(me.getName());

				if (host != null && me.getName().equals(host.getIp())) {

					log.info(String.format("Performing inventory on host: %s", host));
					HostSystem hs = ((HostSystem) me);

					/*
					 * Does this host have enough CPU & Memory to run DE virtual
					 * appliances
					 */
					log.info(String.format("host: %s number of cpu cores: %s", host.getIp(), hs.getHardware().getCpuInfo()
							.getNumCpuCores()));
					if (hs.getHardware().getCpuInfo().getNumCpuCores() < REQUIRED_NUM_CORES) {

						result.setMessage(String.format(
								"%s does not meet the minimum number of cores requirement. ( Required: %s)", host.getIp(),
								REQUIRED_NUM_CORES));
						return result;
					}

					/*
					 * Is this host's datastore present, and if so, does it have
					 * sufficient space
					 */
					boolean foundDataStore = false;
					for (Datastore ds : hs.getDatastores()) {
						log.info(String.format("host: %s datastore: %s", host.getIp(), host.getDatastore()));

						if (ds.getName().equals(host.getDatastore())) {
							foundDataStore = true;
							break;
						}
					}

					if (!foundDataStore) {
						result.setMessage(String.format("%s's configured datastore was not found.", host.getIp()));
					}

					// Does the network exist on the fetched host
					boolean foundNetwork = false;
					for (Network network : hs.getNetworks()) {

						if (network.getName().equals(host.getNetwork())) {
							foundNetwork = true;
							break;
						}
					}
					if (!foundNetwork) {
						result.setMessage(String.format("%s's configured network was not found.", host.getIp()));
					}

					verifiedHosts.add(host);
				}
			}

			if (environment.getHosts().size() != verifiedHosts.size()) {
				result.setMessage(String.format("One or more hosts present in the configuration is not valid."));
			} else {
				result.setSuccessful(true);
			}

			return result;
		} finally {
			si.getServerConnection().logout();
		}
	}
}
