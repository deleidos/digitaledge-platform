package com.saic.rtws.commons.cloud.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.chrono.GregorianChronology;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceBlockDeviceMapping;
import com.saic.rtws.commons.cloud.beans.Volume;
import com.saic.rtws.commons.cloud.platform.ServiceInterface;
import com.saic.rtws.commons.config.RtwsConfig;

/**
 * The class CheckVolumeCreationTime.
 */
public class CheckVolumeCreationTime {

	private int createdWithinHours;
	private boolean canCreateFileSystem = true;
	
	public CheckVolumeCreationTime(){
		createdWithinHours = RtwsConfig.getInstance().getInt("rtws.attacher.created.within.hours", 8);
	}
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		CheckVolumeCreationTime app = new CheckVolumeCreationTime();
		app.checkVolumeCreationDate();  //check each volume
		
		if(app.getCreateStatus() == true){
			System.out.println("All volumes are within creation window, can create a new file system on the volumes.");
			System.exit(0);
		}
		else{
			System.out.println("All volumes are not within creation window, can not create a new file system on the volumes.");
			System.exit(4);
		}
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

	public boolean getCreateStatus(){
		return canCreateFileSystem;
	}
	
	/**
	 * Check the volume create timestamp for each volume attached to instance, if created within threshold a new
	 * file system can be created on the volume.
	 * 
	 */
	public void checkVolumeCreationDate() {
		try{
			if(service == null){
				service = InterfaceConfig.getInstance().getServiceInterface();
			}
	
			/*
			 * If service is still null after call; die horribly with NPE :)
			 */
	
			String instanceId = service.getInstanceId();
	
			if(instanceId != null){
				Instance instance = service.describeInstance(instanceId);
				String rootDevice = instance.getRootDeviceName();
				
				for(InstanceBlockDeviceMapping mapping :instance.getBlockDeviceMappings()){
					//if a root device is not present or volume is not root device check create time
					if(StringUtils.isBlank(rootDevice) == true || !rootDevice.equals(mapping.getDeviceName())){
						String volumeId = mapping.getEbs().getVolumeId();
						Volume current = service.listVolume(volumeId);
						
						logger.info(String.format("Checking volume %s for creation time, created %s.", volumeId, current.getCreateTime().toString()));
						
						//check that volume has been created within time range, if not then it can not have a file system created
						Hours hours = Hours.hoursBetween(new DateTime(current.getCreateTime()), new DateTime(GregorianChronology.getInstance()));
						if(hours.getHours() > createdWithinHours){
							canCreateFileSystem = false;
						}
					}//end if
				}//end for
			} //end if
			else{
				logger.error("Unable to determine instance id");
				canCreateFileSystem = false;
			}
		}catch(Exception e){
			logger.error(e.toString(), e);
			canCreateFileSystem = false;
		}
	}
}
