package com.saic.rtws.commons.net.listener.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.process.log.ProcessStream;

public final class ConfigurationUtil {

	private static Logger log = Logger.getLogger(ConfigurationUtil.class);

	private ConfigurationUtil() {
		// Not instantiable.
	}

	public static boolean download() {
		String mntDevice = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_MOUNT_DEVICE);
		String softwareVersion = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_SW_VERSION);
		String domain = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_DOMAIN);
		
		StringBuilder download = new StringBuilder();
		download.append("s3cmd -c /home/rtws/.s3cfg get -rf s3://")
				.append(mntDevice).append('/').append(softwareVersion).append("/configuration/").append(domain)
				.append("/ /mnt/appfs/configuration/").append(domain).append('/');
		
		ProcessBuilder downloadProcessBuilder = new ProcessBuilder("/bin/bash", "-c", download.toString()).redirectErrorStream(true);
		
		try {
			log.info("Downloading configuration ...");
			
			Process downloadProcess = downloadProcessBuilder.start();
			new ProcessStream(downloadProcess, log);
			downloadProcess.waitFor();
			
			if (downloadProcess.exitValue() != 0) {
				throw new Exception("Bad return code from configuration download.");
			}
			
			log.info("Done downloading configuration.");
			
			return true;
		} catch (Exception ex) {
			log.error("Error downloading configuration.", ex);
		}
		
		return false;
	}

	public static boolean untar(String destDirPath, String confFileName) {
		String domain = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_DOMAIN);
		
		StringBuilder untar = new StringBuilder();
		untar.append("tar -xzf  /mnt/appfs/configuration/").append(domain).append(confFileName);
		
		ProcessBuilder untarProcessBuilder = new ProcessBuilder("/bin/bash", "-c", untar.toString())
			.redirectErrorStream(true).directory(new File(destDirPath));
		
		try {
			log.info(String.format("Untarring %s configuration to %s",confFileName, destDirPath));
			
			Process untarProcess = untarProcessBuilder.start();
			new ProcessStream(untarProcess, log);
			untarProcess.waitFor();
			
			if (untarProcess.exitValue() != 0) {
				throw new Exception("Bad return code from configuration untar process.");
			}
		
			log.info("Done untarring " + confFileName + " configuation.");
			
			return true;
		} catch (Exception ex) {
			log.error("Error untarring configuration.", ex);
		}
		
		return false;
	}

}
