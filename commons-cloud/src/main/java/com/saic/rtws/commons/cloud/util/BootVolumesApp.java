package com.saic.rtws.commons.cloud.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.cloud.platform.ServiceInterface;

/**
 * The Class BootVolumesApp.
 */
public class BootVolumesApp {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		BootVolumesApp app = new BootVolumesApp();
		System.exit(app.waitForMyVolumes());
	}

	/** The logger. */
	private Logger logger = Logger.getLogger(getClass());

	/** The service. */
	private ServiceInterface service;

	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	public ServiceInterface getService() {
		return service;
	}

	/**
	 * Sets the service.
	 * 
	 * @param service
	 *            the new service
	 */
	public void setService(ServiceInterface service) {
		this.service = service;
	}

	/**
	 * Wait for my volumes.
	 * 
	 * @return the int
	 */
	public int waitForMyVolumes() {
		int returnCode = 0;

		if (service == null)
			service = InterfaceConfig.getInstance().getServiceInterface();

		/*
		 * If service is still null after call; die horribly with NPE :)
		 */

		String instanceId = service.getInstanceId();

		if (instanceId != null) {

			List<String> volumeIds = service.getExpectedVolumeIds(instanceId);

			/*
			 * TODO switch to waiting indefinitely ?????
			 */
			service.waitForVolumeAttachment(volumeIds, false);
		} else {
			logger.error("Unable to determine instance id");
			returnCode = 1;
		}

		return returnCode;
	}
}
