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
package sandbox;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

@Ignore("Used for local testing")
public class ApiLearningTest {

	private static String VCENTER_HOST = "192.168.6.213";

	private static String USERNAME = "bullockja";

	private static String PW = "SOMETHING_YOU_SHOULD_NOT_KNOW";

	private static ServiceInstance si;

	@BeforeClass
	public static void setup() throws RemoteException, MalformedURLException {
		long start = System.currentTimeMillis();
		si = new ServiceInstance(new URL(String.format("https://%s/sdk", VCENTER_HOST)), USERNAME, PW, true);
		long end = System.currentTimeMillis();
		System.out.println("time taken:" + (end - start));
	}

	@AfterClass
	public static void cleanup() {
		si.getServerConnection().logout();
	}

	@Test
	public void helloWorld() throws RemoteException, MalformedURLException {

		Folder rootFolder = si.getRootFolder();
		String name = rootFolder.getName();
		System.out.println("root:" + name);
		ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
		if (mes == null || mes.length == 0) {
			return;
		}

		for (ManagedEntity managedEntity : mes) {
			VirtualMachine vm = (VirtualMachine) managedEntity;

			VirtualMachineConfigInfo vminfo = vm.getConfig();
			VirtualMachineCapability vmc = vm.getCapability();

			vm.getResourcePool();
			if (vm.getName().equals("de-appliance-v6")) {
				System.out.println("Hello " + vm.getName());
				System.out.println("GuestOS: " + vminfo.getGuestFullName());
				System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());

				System.out.println("" + vm.getConfig().getCpuAffinity() + " " + vm.getConfig().getCpuAllocation().getLimit());
			}
		}
	}

	@Test
	public void cloneTest() throws Exception {
		Folder rootFolder = si.getRootFolder();
		String vmname = "DigitalEdge_appliance_sales.yourdomain.for.vmware.com_1.3.0_1408133164";
		VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmname);

		if (vm == null) {
			System.out.println("No VM " + vmname + " found");
			si.getServerConnection().logout();
			return;
		}

		ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
		if (mes == null || mes.length == 0) {
			return;
		}

		VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
		VirtualMachineRelocateSpec host_249 = new VirtualMachineRelocateSpec();
		ManagedObjectReference host = new ManagedObjectReference();

		for (ManagedEntity me : mes) {
			System.out.println(me.getName() + " " + me.getMOR().getVal());

			if (me.getName().equals("192.168.1.249")) {
				host = me.getMOR();
			}
		}

		host_249.setHost(host);
		host_249.setDiskMoveType("moveAllDiskBackingsAndAllowSharing");

		ManagedEntity[] mes2 = new InventoryNavigator(rootFolder).searchManagedEntities("Datastore");
		if (mes2 == null || mes2.length == 0) {
			return;
		}

		for (ManagedEntity md2 : mes2) {
			System.out.println(md2.getName() + " " + md2.getMOR().getVal());

			if (md2.getName().equals("datastore1 (1)")) {
				host_249.setDatastore(md2.getMOR());
			}
		}

		cloneSpec.setLocation(host_249);
		cloneSpec.setPowerOn(false);
		cloneSpec.setTemplate(false);
		

		Task task = vm.cloneVM_Task((Folder) vm.getParent(), vmname + "_Clone", cloneSpec);
		System.out.println("Launching the VM clone task. " + "Please wait ...");

		String status = task.waitForTask();
		if (status == Task.SUCCESS) {
			System.out.println("VM got cloned successfully.");
		} else {
			System.out.println("Failure -: VM cannot be cloned" + status);
		
			
		}

	}

}
