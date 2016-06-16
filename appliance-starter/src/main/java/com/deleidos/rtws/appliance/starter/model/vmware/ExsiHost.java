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
package com.deleidos.rtws.appliance.starter.model.vmware;

/**
 * Container for information related to the EXSi Hosts allowed for appliance
 * execution
 * 
 * 
 */
public class ExsiHost {

	public String datastore;

	public String ip;

	private String network;

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getNetwork() {
		return network;
	}

	public String getDatastore() {
		return datastore;
	}

	public String getIp() {
		return ip;
	}

	public void setDatastore(String datastore) {
		this.datastore = datastore;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExsiHost [datastore=").append(datastore).append(", ip=").append(ip).append(", network=")
				.append(network).append("]");
		return builder.toString();
	}
}
