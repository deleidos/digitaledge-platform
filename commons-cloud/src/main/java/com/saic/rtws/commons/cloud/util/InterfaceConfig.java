package com.saic.rtws.commons.cloud.util;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.MarshalException;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.cloud.marshaller.Interfaces;
import com.saic.rtws.commons.cloud.platform.ServiceInterface;
import com.saic.rtws.commons.cloud.platform.StorageInterface;
import com.saic.rtws.commons.cloud.platform.aws.AwsServiceInterface;
import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.util.CommonSerializer;


public class InterfaceConfig {
	
	private static final Logger LOGGER = Logger.getLogger(InterfaceConfig.class);

	private static final String INTERFACE_CONFIG_FILE = "interfaces-config.xml";
	private String interfaceConfigFilePath;
	private static final String INTERFACE_MAPPING_FILE = "/mappings/interfaces-mapping.xml";
	
	static class SingletonHolder{
      static final InterfaceConfig CONFIG_INSTANCE = new InterfaceConfig();
	}
		
	/**
	 * Returns an instance of InterfaceConfig 
	 */
	public static InterfaceConfig getInstance()
	{
		return SingletonHolder.CONFIG_INSTANCE;
	}
	
	protected InterfaceConfig() {
		interfaceConfigFilePath = String.format("%s/%s",
					RtwsConfig.getInstance().getString("rtws.interfaces.config.path"),
					INTERFACE_CONFIG_FILE);
	}
		
	public ServiceInterface getServiceInterface() {
		Interfaces interfaces = unmarshalInterfaces();
				
		return interfaces.getServiceInterface();
	}
	
	public void setServiceInterface(ServiceInterface newServiceInterface) {
		// Get the current configuration
		Interfaces interfaces = unmarshalInterfaces();
		
		// Update the service interface
		interfaces.setServiceInterface(newServiceInterface);
		
		// Overwrite the old configuration
		marshalInterfaces(interfaces);
	}
	
	public StorageInterface getStorageInterface() {
		Interfaces interfaces = unmarshalInterfaces();
		
		return interfaces.getStorageInterface();
	}
	
	public void setStorageInterface(StorageInterface newStorageInterface) {
		// Get the current configuration
		Interfaces interfaces = unmarshalInterfaces();
		
		// Update the storage interface
		interfaces.setStorageInterface(newStorageInterface);
		
		// Overwrite the old configuration
		marshalInterfaces(interfaces);
	}
	
	private void marshalInterfaces(Interfaces interfaces) {
		try {
			CommonSerializer.marshalToFile(interfaces, new File(interfaceConfigFilePath), INTERFACE_MAPPING_FILE);
		} catch (MarshalException e) {
			LOGGER.error("Unable to marshal interface object: " + e.toString(), e);
		}	
	}
	
	private Interfaces unmarshalInterfaces() {
		Interfaces interfaces = null;
		
		try {
			interfaces = CommonSerializer.unmarshalFromFile(interfaceConfigFilePath, Interfaces.class, INTERFACE_MAPPING_FILE);
		} catch (MarshalException e) {
			LOGGER.error("Unable to unmarshal interface object: " + e.toString(), e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error("Unable to unmarshal interface object: " + e.toString(), e);
			e.printStackTrace();
		}
		
		return interfaces;
	}
	
	public boolean isRunningInAws() {
		// TODO Revisit this assumption that if not running in AWS, your running in Eucalyptus
		boolean inAws = false;
		ServiceInterface service = getInstance().getServiceInterface();
		if (service instanceof AwsServiceInterface)
			inAws = true;
		
		return inAws;
	}
}
