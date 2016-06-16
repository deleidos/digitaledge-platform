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

import java.util.Scanner;

import com.deleidos.rtws.appliance.starter.model.PhaseResult;

public class PromptForInput extends AbstractApplianceStartupPhase {

	private String promptMessage;

	public PromptForInput(String message) {
		this.promptMessage = message;
	}

	@Override
	public PhaseResult call() throws Exception {
		Scanner reader = new Scanner(System.in);
		System.out.println(promptMessage);

		try {
			reader.next();
		} catch (Exception e) {
			// ignore
		} finally {
		}

		result.setSuccessful(true);
		return result;
	}

}
