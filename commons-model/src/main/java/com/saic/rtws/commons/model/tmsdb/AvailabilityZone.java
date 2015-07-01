package com.saic.rtws.commons.model.tmsdb;


/**
 * TMS DB AvailabilityZone DTO object.
 * 
 * @author LUCECU
 *
 */
public class AvailabilityZone {
	
	private String iaasServiceName;
	private String iaasRegion;
	private String iaasAZone;
	private String iaasSWBucket;
	private String description;
	private String propertiesFile;
	private EndPointURL storageEndpoint;
	private EndPointURL serviceEndpoint;

	
	/**
	 * Default Constructor.
	 */
	public AvailabilityZone(){
		
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
	 * Set the iaas region.
	 * 
	 * @param iaasRegion String value
	 */
	public void setIaasRegion(String iaasRegion){
		this.iaasRegion = iaasRegion;
	}
	
	/**
	 * Get the iaas region value.
	 * 
	 * @return String value
	 */
	public String getIaasRegion(){
		return iaasRegion;
	}
	
	/**
	 * Set the iaas a zone.
	 * 
	 * @param iaasAZone String value
	 */
	public void setIaasAZone(String iaasAZone){
		this.iaasAZone = iaasAZone;
	}
	
	/**
	 * Get the iaas a zone.
	 * 
	 * @return String value
	 */
	public String getIaasAZone(){
		return iaasAZone;
	}
	
	/**
	 * Set the iaas sw bucket.
	 * 
	 * @param iaasSWBucket String value
	 */
	public void setIaasSWBucket(String iaasSWBucket){
		this.iaasSWBucket=iaasSWBucket;
	}
	
	/**
	 * Get the iaas sw bucket.
	 * 
	 * @return String value
	 */
	public String getIaasSWBucket(){
		return this.iaasSWBucket;
	}

	/**
	 * Set the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the properties file.
	 *
	 * @return the properties file
	 */
	public String getPropertiesFile() {
		return propertiesFile;
	}

	/**
	 * Get the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the storageEndpoint.
	 * 
	 * @return the storageEndpoint
	 */
	public EndPointURL getStorageEndpoint() {
		return storageEndpoint;
	}

	/**
	 * Set the properties file.
	 *
	 * @param propertiesFile the new properties file
	 */
	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	/**
	 * Set the storageEndpoint.
	 *
	 * @param storageEndpoint the new storageEndpoint
	 */
	public void setStorageEndpoint(EndPointURL storageEndpoint) {
		this.storageEndpoint = storageEndpoint;
	}

	/**
	 * Get the service endpoint.
	 *
	 * @return the service endpoint
	 */
	public EndPointURL getServiceEndpoint() {
		return serviceEndpoint;
	}
	
	/**
	 * Get the ServiceEndpoint.
	 *
	 * @return the ServiceEndpoint
	 */
	public void setServiceEndpoint(EndPointURL serviceEndpoint) {
		this.serviceEndpoint = serviceEndpoint;
	}

	@Override
	public String toString() {
		return "AvailabilityZone [iaasServiceName=" + iaasServiceName
				+ ", iaasRegion=" + iaasRegion + ", iaasAZone=" + iaasAZone
				+ ", iaasSWBucket=" + iaasSWBucket + "]";
	}
	
}
