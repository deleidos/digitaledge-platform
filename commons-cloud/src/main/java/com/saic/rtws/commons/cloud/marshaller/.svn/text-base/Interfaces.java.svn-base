package com.saic.rtws.commons.cloud.marshaller;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.saic.rtws.commons.cloud.platform.ServiceInterface;
import com.saic.rtws.commons.cloud.platform.StorageInterface;

/**
 * A collection of unrelated interfaces for marshalling to a consolidated location
 * 
 * @author HAMILTONERI
 *
 */
@XmlRootElement
@XmlType(propOrder={
		"serviceInterface", 
		"storageInterface"})
public class Interfaces {
	
	private ServiceInterface serviceInterface;
	private StorageInterface storageInterface;
	
	public Interfaces() {
		super();
	}
	
	/**
	 * Set the ServiceInterface.
	 * 
	 * @param serviceInterface {@link ServiceInterface}
	 */
	public void setServiceInterface(ServiceInterface serviceInterface){
		this.serviceInterface = serviceInterface;
	}
	
	/**
	 * Get the service interface.
	 * 
	 * @return {@link ServiceInterface}
	 */
	public ServiceInterface getServiceInterface(){
		return serviceInterface;
	}
	
	/**
	 * Set the storage interface.
	 * 
	 * @param storageInterface {@link StorageInterface}
	 */
	public void setStorageInterface(StorageInterface storageInterface){
		this.storageInterface = storageInterface;
	}
	
	/**
	 * Get the storage interface.
	 * 
	 * @return {@link StorageInterface}
	 */
	public StorageInterface getStorageInterface(){
		return storageInterface;
	}
}
