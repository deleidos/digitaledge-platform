package com.saic.rtws.commons.config;

import java.io.File;

public class ExecutionEnvironment {

	public static boolean isInOperationalEnvironment() {
		// TODO If needed, provide a better way to determine if we are running
		// in a system
		File rtwsrc = new File("/etc/rtwsrc");
		return rtwsrc.exists();
	}
}
