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

import org.apache.log4j.Logger;

import com.deleidos.rtws.appliance.starter.model.PhaseResult;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class CloneAppliance extends AbstractApplianceStartupPhase {

	private Logger log = Logger.getLogger(getClass());

	@Override
	public PhaseResult call() throws Exception {
		try {

			Folder rootFolder = si.getRootFolder();
			VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine",
					environment.getOvf().getName());

			if (vm == null) {
				log.info("No VM " + environment.getOvf().getName() + " found");
				si.getServerConnection().logout();
				return result;
			}

			VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
			cloneSpec.setLocation(new VirtualMachineRelocateSpec());
			cloneSpec.setPowerOn(false);
			cloneSpec.setTemplate(true);

			Task task = vm.cloneVM_Task((Folder) vm.getParent(), environment.getOvf().getTemplateName(), cloneSpec);
			log.info("Launching the VM clone task. Please wait ...");

			String status = task.waitForTask();
			if (status == Task.SUCCESS) {
				log.info("Successfully cloned vm.");
				result.setSuccessful(true);
			} else {
				result.setMessage(String.format("Failure -: VM cannot be cloned status:%s", status));
			}

			return result;
		} finally {
			si.getServerConnection().logout();
		}
	}
}
