package com.saic.rtws.commons.model.tmsdb;

/**
 * TMS DB IAASConfiguration DTO object.
 * 
 * @author LUCECU
 *
 */
public class IAASConfiguration {

	private String iaasServiceName;
	private String description;
	private String serviceInterfaceXml;
	private String internetDnsXml;
	private String storageInterfaceXml;
	
	/**
	 * Default constructor.
	 * 
	 */
	public IAASConfiguration(){
		
	}
	
	/**
	 * Set the iaas service name.
	 * 
	 * @param iaasServiceName String value
	 */
	public void setIaasServiceName(String iaasServiceName){
		this.iaasServiceName = iaasServiceName;
	}
	
	/**
	 * Get the iaas service name.
	 * 
	 * @return String value
	 */
	public String getIaasServiceName(){
		return iaasServiceName;
	}
	
	/**
	 * Set the description.
	 * 
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * Get the description.
	 * 
	 * @return
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Set the service interface xml template.
	 * 
	 * @param serviceInterfaceXml String value
	 */
	public void setServiceInterfaceXml(String serviceInterfaceXml){
		this.serviceInterfaceXml = serviceInterfaceXml;
	}
	
	/**
	 * Get the service interface xml template.
	 * 
	 * @return String value
	 */
	public String getServiceInterfaceXml(){
		return serviceInterfaceXml;
	}
	
	/**
	 * Set the internet dns XML template.
	 * 
	 * @param internetDnsXml String value
	 */
	public void setInternetDnsXml(String internetDnsXml){
		this.internetDnsXml = internetDnsXml;
	}
	
	/**
	 * Get the internet dns XML template.
	 * 
	 * @return String value
	 */
	public String getInternetDnsXml(){
		return internetDnsXml;
	}
	
	/**
	 * Set the storage interface xml template.
	 * 
	 * @param storageInterfacexml String value
	 */
	public void setStorageInterfaceXml(String storageInterfacexml){
		this.storageInterfaceXml = storageInterfacexml;
	}
	
	/**
	 * Get the storage interface xml template.
	 * 
	 * @return String value
	 */
	public String getStorageInterfaceXml(){
		return storageInterfaceXml;
	}
	
	/**
	 * Converts the obect to a string format.
	 */
	public String toString(){
		String format = "iaasServiceName: %s\ndescription:  %s\nserviceInterfaceXml:  %s\ninternetDnSXml:  %s\nstorageInterfaceXml:  %s\n";
		String config = String.format(format, iaasServiceName, description, serviceInterfaceXml, internetDnsXml, storageInterfaceXml);
		
		return config;
	}
}
