package com.saic.rtws.commons.cloud.util;


import com.saic.rtws.commons.cloud.CloudProvider;
import com.saic.rtws.commons.config.RtwsConfig;

public class CloudProviderUtil {

	public static CloudProvider getCloudProvider()  {

		if (RtwsConfig.getInstance().getString("rtws.cloud.provider", "UNKNOWN").equals("AWS")) {
			return CloudProvider.AWS;

		} else if (RtwsConfig.getInstance().getString("rtws.cloud.provider", "UNKNOWN").equals("EUC")) {
			return CloudProvider.EUCA;
		} else {
			throw new IllegalStateException("Unable to determine the cloud provider used.");
		}
	}
}
