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
package com.deleidos.rtws.appliance.starter.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.deleidos.rtws.appliance.starter.exception.ConnectionFailedException;
import com.deleidos.rtws.appliance.starter.model.ApplianceStartupPhase;
import com.deleidos.rtws.appliance.starter.model.PhaseResult;
import com.deleidos.rtws.appliance.starter.model.vmware.HostExecutionEnvironment;
import com.deleidos.rtws.appliance.starter.phases.ImportOvf;
import com.deleidos.rtws.appliance.starter.phases.Inventory;
import com.deleidos.rtws.appliance.starter.phases.OvfValidation;
import com.deleidos.rtws.appliance.starter.phases.PromptForInput;
import com.deleidos.rtws.appliance.starter.validator.CredentialsValidator;

public class Starter {

	private static Logger log = Logger.getLogger(Starter.class);

	private static ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings({ "static-access" })
	public static void main(String[] args) {
		CommandLineParser parser = new GnuParser();
		HelpFormatter formatter = new HelpFormatter();
		String header = "Performs the initial configuration and startup of a DigitalEdge Appliance.\n\n";
		String footer = "\nPlease report issues at digitaledgesupport@leidos.com";
		Options options = new Options();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {

			options.addOption(OptionBuilder.withLongOpt("config").hasArg().withArgName("config_file")
					.withDescription("The path to an application configuration describing the processing to be performed.")
					.isRequired().create());
			options.addOption(OptionBuilder.withLongOpt("help").create('h'));

			CommandLine cmd = parser.parse(options, args);

			Starter app = new Starter();

			HostExecutionEnvironment config = mapper.readValue(new File(cmd.getOptionValue("config")),
					HostExecutionEnvironment.class);

			int retries = 0;
			boolean validCredentials = false;
			do {

				try {

					validCredentials = CredentialsValidator.isValid(config);

					Thread.sleep(30000);

				} catch (ConnectionFailedException e) {
					log.error(e.getMessage());
				} catch (InterruptedException e) {
					log.error(e.getMessage());
				} finally {
					retries++;
				}
			} while (retries <= 3 && !validCredentials);

			if (!validCredentials) {
				log.error("Unable to successfully connect to vSphere.  Quitting....");
				System.exit(1);
			}

			app.run(config);

		} catch (ParseException e) {
			System.err.println(e.getMessage());
			formatter.printHelp("DigitalEdge Appliance Starter", header, options, footer, true);
		} catch (JsonParseException e) {
			log.error(e.getMessage());
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {

		}
	}

	private List<ApplianceStartupPhase> phases = new ArrayList<ApplianceStartupPhase>();

	private ExecutorService workers = Executors.newFixedThreadPool(1);

	private void init() {
		phases.add(new Inventory());
		phases.add(new OvfValidation());
		phases.add(new ImportOvf());
		// phases.add(new CloneAppliance());
		phases.add(new PromptForInput("Please review the inported VM template, and modify it's configuration if necessary.  "
				+ "The template will be the basis for all Virtual Machines launched for this appliance.   "
				+ "When ready, press any key to continue."));

	}

	private void run(HostExecutionEnvironment applianceEnvironment) {
		init();

		log.info(String.format("Begin processing with the following configuration: %s", applianceEnvironment));

		try {

			for (ApplianceStartupPhase phase : phases) {
				try {
					phase.initialize(applianceEnvironment);
					PhaseResult result = workers.submit(phase).get();
					if (!result.isSuccessful())
						throw new IllegalStateException(String.format("Phase: %s Execution Result: %s Message: %s", phase
								.getClass().getName(), result.isSuccessful(), result.getMessage()));

				} catch (InterruptedException e) {
					log.error(e.getMessage());
					throw new IllegalStateException(e.getMessage());
				} catch (ExecutionException e) {
					log.error(e.getMessage());
					throw new IllegalStateException(e.getMessage());
				}
			}
			workers.shutdown();
			try {
				workers.awaitTermination(5l, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}

		} catch (IllegalStateException e) {
			log.error(e.getMessage());
			System.exit(1);
		} finally {

		}
	}
}