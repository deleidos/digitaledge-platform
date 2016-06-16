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

import java.io.File;

public class Ovf {

	private File location;

	private String name;

	public File getFullyQualifiedOvf() {
		return new File(String.format("%s%s%s%s", location.getAbsolutePath(), File.separator, name, ".ovf"));
	}

	public File getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public void setLocation(File location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Ovf [location=").append(location).append(", name=").append(name).append("]");
		return builder.toString();
	}

	public String getTemplateName() {
		return String.format("%s%s", this.name, "_template");
	}
}
