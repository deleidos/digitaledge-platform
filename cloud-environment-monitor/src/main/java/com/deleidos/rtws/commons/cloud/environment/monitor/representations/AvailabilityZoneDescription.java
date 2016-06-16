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
package com.deleidos.rtws.commons.cloud.environment.monitor.representations;

public class AvailabilityZoneDescription {
	private String type;

	private InstanceTypeDetails typeDetails;

	private int free;
	
	private int max;
	
	public AvailabilityZoneDescription() {
		super();
	}

	public AvailabilityZoneDescription(String type, int free, int max) {
		this.type = type;
		this.free = free;
		this.max = max;
	}

	public int getFree() {
		return free;
	}

	public String getType() {
		return type;
	}

	public InstanceTypeDetails getTypeDetails() {
		return typeDetails;
	}

	public int getMax() {
		return max;
	}

	public void setTypeDetails(InstanceTypeDetails typeDetails) {
		this.typeDetails = typeDetails;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AvailabilityZoneDescription [type=").append(type).append(", typeDetails=").append(typeDetails).append(", free=").append(free).append(", max=").append(max).append("]");
		return builder.toString();
	}

	public AvailabilityZoneDescription withTypeDetails(InstanceTypeDetails instanceTypeDetails) {
		setTypeDetails(instanceTypeDetails);
		return this;
	}
}
