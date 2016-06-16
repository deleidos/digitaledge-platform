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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Container for information about the VMware deployment to be used by the
 * appliance.
 */
public class HostExecutionEnvironment {

	@JsonProperty
	private String datacenter;

	public String getDatacenter() {
		return datacenter;
	}

	public void setDatacenter(String datacenter) {
		this.datacenter = datacenter;
	}

	@JsonProperty
	private List<ExsiHost> hosts = new ArrayList<ExsiHost>();

	@JsonProperty
	private Ovf ovf;

	@JsonProperty
	private String password;

	@JsonProperty
	private String username;

	@JsonProperty
	private String vSphereHost;

	@JsonCreator
	public HostExecutionEnvironment(@JsonProperty("vSphereHost") String vSphereHost, @JsonProperty("username") String username,
			@JsonProperty("password") String password, @JsonProperty("ovf") Ovf ovf) {
		super();
		this.vSphereHost = vSphereHost;
		this.username = username;
		this.password = password;
		this.ovf = ovf;
	}

	public ExsiHost getHost(String ip) {
		for (ExsiHost hst : hosts) {
			if (hst.getIp().equals(ip))
				return hst;
		}
		return null;
	}

	public List<ExsiHost> getHosts() {
		return hosts;
	}

	public Ovf getOvf() {
		return ovf;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getvSphereHost() {
		return vSphereHost;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HostExecutionEnvironment [datacenter=").append(datacenter).append(", hosts=").append(hosts)
				.append(", ovf=").append(ovf).append(", password=").append(password).append(", username=").append(username)
				.append(", vSphereHost=").append(vSphereHost).append("]");
		return builder.toString();
	}
}
