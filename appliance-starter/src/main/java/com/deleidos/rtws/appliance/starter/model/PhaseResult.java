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
package com.deleidos.rtws.appliance.starter.model;

public class PhaseResult {

	private String message;

	private boolean successful = false;

	public String getMessage() {
		return message;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

}
