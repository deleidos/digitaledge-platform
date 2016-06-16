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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;

import com.deleidos.rtws.appliance.starter.model.PhaseResult;
import com.deleidos.rtws.appliance.starter.model.vmware.ExsiHost;
import com.vmware.vim25.HttpNfcLeaseDeviceUrl;
import com.vmware.vim25.HttpNfcLeaseInfo;
import com.vmware.vim25.HttpNfcLeaseState;
import com.vmware.vim25.OvfCreateImportSpecParams;
import com.vmware.vim25.OvfCreateImportSpecResult;
import com.vmware.vim25.OvfFileItem;
import com.vmware.vim25.OvfNetworkMapping;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.HttpNfcLease;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Network;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.samples.ovf.LeaseProgressUpdater;

/**
 * Adapted from Vijava examples (ImportLocalOvfVApp.java)
 * 
 */
public class ImportOvf extends AbstractApplianceStartupPhase {

	private Logger log = Logger.getLogger(getClass());

	private static final int CHUCK_LEN = 10204 * 1024;

	public static LeaseProgressUpdater leaseUpdater;

	@Override
	public PhaseResult call() throws Exception {
		try {
			Folder rootFolder = si.getRootFolder();
			ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
			if (mes == null || mes.length == 0) {
				result.setMessage(String.format("Failed to obtain vSphere configuration information from: %s",
						environment.getvSphereHost()));
				return result;
			}

			// Find the first host from our config, and use it as the source for
			// the
			// initial import & cloning
			HostSystem host = null;
			ExsiHost exsiHost = null;
			for (ManagedEntity me : mes) {
				ExsiHost ehost = environment.getHost(me.getName());

				if (ehost != null && me.getName().equals(ehost.getIp())) {
					host = (HostSystem) me;
					exsiHost = ehost;
					break;
				}
			}

			log.info("Host Name : " + host.getName());
			log.info("Network : " + host.getNetworks()[0].getName());
			log.info("Datastore : " + host.getDatastores()[0].getName());

			Folder vmFolder = (Folder) host.getVms()[0].getParent();

			OvfCreateImportSpecParams importSpecParams = new OvfCreateImportSpecParams();
			importSpecParams.setHostSystem(host.getMOR());
			importSpecParams.setLocale("US");
			importSpecParams.setEntityName(environment.getOvf().getName());
			importSpecParams.setDeploymentOption("");

			OvfNetworkMapping networkMapping = new OvfNetworkMapping();
			networkMapping.setName(environment.getHosts().get(0).getNetwork());

			// set the MOR for the specified network in the config
			for (Network network : host.getNetworks()) {
				if (network.getName().equals(exsiHost.getNetwork()))
					networkMapping.setNetwork(network.getMOR());
			}

			if (networkMapping.getNetwork() == null) {
				networkMapping.setNetwork(host.getNetworks()[0].getMOR());
				log.warn(String.format("Requested network does not exist on host %s.  Defaulting to %s.", host.getName(),
						host.getNetworks()[0].getName()));
			}

			importSpecParams.setNetworkMapping(new OvfNetworkMapping[] { networkMapping });
			importSpecParams.setPropertyMapping(null);

			String ovfDescriptor = readOvfContent(environment.getOvf().getFullyQualifiedOvf().getAbsolutePath());
			if (ovfDescriptor == null) {
				return result;
			}

			log.info("ovfDesc:" + ovfDescriptor);

			ResourcePool rp = ((ComputeResource) host.getParent()).getResourcePool();

			OvfCreateImportSpecResult ovfImportResult = si.getOvfManager().createImportSpec(ovfDescriptor, rp,
					host.getDatastores()[0], importSpecParams);

			if (ovfImportResult == null) {
				return result;
			}

			long totalBytes = addTotalBytes(ovfImportResult);
			log.info("Total bytes to be uploaded: " + totalBytes);

			HttpNfcLease httpNfcLease = null;

			httpNfcLease = rp.importVApp(ovfImportResult.getImportSpec(), vmFolder, host);

			// Wait until the HttpNfcLeaseState is ready
			HttpNfcLeaseState hls;
			for (;;) {
				hls = httpNfcLease.getState();
				if (hls == HttpNfcLeaseState.ready || hls == HttpNfcLeaseState.error) {
					break;
				}
			}

			if (hls.equals(HttpNfcLeaseState.ready)) {
				log.info("HttpNfcLeaseState: ready ");
				HttpNfcLeaseInfo httpNfcLeaseInfo = (HttpNfcLeaseInfo) httpNfcLease.getInfo();
				printHttpNfcLeaseInfo(httpNfcLeaseInfo);

				leaseUpdater = new LeaseProgressUpdater(httpNfcLease, 5000);
				leaseUpdater.start();

				HttpNfcLeaseDeviceUrl[] deviceUrls = httpNfcLeaseInfo.getDeviceUrl();

				long bytesAlreadyWritten = 0;
				for (HttpNfcLeaseDeviceUrl deviceUrl : deviceUrls) {
					String deviceKey = deviceUrl.getImportKey();
					for (OvfFileItem ovfFileItem : ovfImportResult.getFileItem()) {
						if (deviceKey.equals(ovfFileItem.getDeviceId())) {
							log.info("Import key==OvfFileItem device id: " + deviceKey);
							String absoluteFile = environment.getOvf().getFullyQualifiedOvf().getParent() + File.separator
									+ ovfFileItem.getPath();
							String urlToPost = deviceUrl.getUrl().replace("*", environment.getHosts().get(0).getIp());
							uploadVmdkFile(ovfFileItem.isCreate(), absoluteFile, urlToPost, bytesAlreadyWritten, totalBytes);
							bytesAlreadyWritten += ovfFileItem.getSize();
							log.info(String.format("Completed uploading the VMDK file: %s to ESXi host: %s", absoluteFile,
									host.getName()));
						}
					}
				}

				leaseUpdater.interrupt();
				httpNfcLease.httpNfcLeaseProgress(100);
				httpNfcLease.httpNfcLeaseComplete();

				// Find the newly imported vm and mark it as a template
				VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine",
						environment.getOvf().getName());
				/*VirtualMachineDeviceManager vmdm = new VirtualMachineDeviceManager(vm);
				vmdm.createNetworkAdapter(VirtualNetworkAdapterType.VirtualVmxnet3, null, environment.getHost(host.getName())
						.getNetwork(), true, true);*/
				vm.markAsTemplate();

				result.setSuccessful(true);
			}
			return result;
		} finally {
			si.getServerConnection().logout();
		}
	}

	public long addTotalBytes(OvfCreateImportSpecResult ovfImportResult) {
		OvfFileItem[] fileItemArr = ovfImportResult.getFileItem();

		long totalBytes = 0;
		if (fileItemArr != null) {
			for (OvfFileItem fi : fileItemArr) {
				printOvfFileItem(fi);
				totalBytes += fi.getSize();
			}
		}
		return totalBytes;
	}

	private void uploadVmdkFile(boolean put, String diskFilePath, String urlStr, long bytesAlreadyWritten, long totalBytes)
			throws IOException {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		});

		HttpsURLConnection conn = (HttpsURLConnection) new URL(urlStr).openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setChunkedStreamingMode(CHUCK_LEN);
		conn.setRequestMethod(put ? "PUT" : "POST"); // Use a post method to
														// write the file.
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "application/x-vnd.vmware-streamVmdk");
		conn.setRequestProperty("Content-Length", Long.toString(new File(diskFilePath).length()));

		BufferedOutputStream bos = new BufferedOutputStream(conn.getOutputStream());

		BufferedInputStream diskis = new BufferedInputStream(new FileInputStream(diskFilePath));
		int bytesAvailable = diskis.available();
		int bufferSize = Math.min(bytesAvailable, CHUCK_LEN);
		byte[] buffer = new byte[bufferSize];

		long totalBytesWritten = 0;
		while (true) {
			int bytesRead = diskis.read(buffer, 0, bufferSize);
			if (bytesRead == -1) {
				log.info("Total bytes written: " + totalBytesWritten);
				break;
			}

			totalBytesWritten += bytesRead;
			bos.write(buffer, 0, bufferSize);
			bos.flush();
			log.info("Total bytes written: " + totalBytesWritten);
			int progressPercent = (int) (((bytesAlreadyWritten + totalBytesWritten) * 100) / totalBytes);
			leaseUpdater.setPercent(progressPercent);
		}

		diskis.close();
		bos.flush();
		bos.close();
		conn.disconnect();
	}

	public static String readOvfContent(String ovfFilePath) throws IOException {
		StringBuffer strContent = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(ovfFilePath)));
		String lineStr;
		while ((lineStr = in.readLine()) != null) {
			strContent.append(lineStr);
		}
		in.close();
		return strContent.toString();
	}

	private void printHttpNfcLeaseInfo(HttpNfcLeaseInfo info) {
		log.info("================ HttpNfcLeaseInfo ================");
		HttpNfcLeaseDeviceUrl[] deviceUrlArr = info.getDeviceUrl();
		for (HttpNfcLeaseDeviceUrl durl : deviceUrlArr) {
			log.info("Device URL Import Key: " + durl.getImportKey());
			log.info("Device URL Key: " + durl.getKey());
			log.info("Device URL : " + durl.getUrl());
			log.info("Updated device URL: " + durl.getUrl());
		}
		log.info("Lease Timeout: " + info.getLeaseTimeout());
		log.info("Total Disk capacity: " + info.getTotalDiskCapacityInKB());
		log.info("==================================================");
	}

	private void printOvfFileItem(OvfFileItem fi) {
		log.info("================ OvfFileItem ================");
		log.info("chunkSize: " + fi.getChunkSize());
		log.info("create: " + fi.isCreate());
		log.info("deviceId: " + fi.getDeviceId());
		log.info("path: " + fi.getPath());
		log.info("size: " + fi.getSize());
		log.info("==============================================");
	}
}
