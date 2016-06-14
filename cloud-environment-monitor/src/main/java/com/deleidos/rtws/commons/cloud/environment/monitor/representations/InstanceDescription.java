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

import com.deleidos.rtws.commons.cloud.beans.State;

public class InstanceDescription {

	private String instanceId;
	private String publicIp;
	private String privateIp;
	private State state;
	private String type;
	
	public InstanceDescription() {
		super();
	}
	
	public InstanceDescription(String instanceId, String publicIp, String privateIp, String state, String type) {
		this.instanceId = instanceId;
		this.publicIp = publicIp;
		this.privateIp = privateIp;
		this.state = State.fromValue(state);
		this.type = type;
	}
	
	public String getInstanceId() {
		return instanceId;
	}
	
	public String getPublicIp() {
		return publicIp;
	}
	
	public String getPrivateIp() {
		return privateIp;
	}
	
	public State getState() {
		return state;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstanceDescription [")
				.append("instanceId=").append(instanceId)
				.append(", publicIp=").append(publicIp)
				.append(", privateIp=").append(privateIp)
				.append(", state=").append(state)
				.append(", type=").append(type)
				.append("]");
		return builder.toString();
	}
}
