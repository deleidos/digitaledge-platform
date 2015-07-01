package com.saic.rtws.commons.model.tmsdb;

import java.sql.Timestamp;

/**
 * TMS DB GatewayAccount DTO object.
 */
public class GatewayAccount {
	private int accountId;
	private String instanceId;
	private String publicDns;
	private String region;
	private String availabilityZone;
	private Timestamp registrationTimestamp;
	
	/**
	 * Default constructor.
	 */
	public GatewayAccount(){
		
	}
	
	/**
	 * Get the account id.
	 * 
	 * @return int value
	 */
	public int getAccountId(){
		return accountId;
	}
	
	/**
	 * Set the account id.
	 * 
	 * @param accountId int value
	 */
	public void setAccountId(int accountId){
		this.accountId = accountId;
	}
	
	/**
	 * Get the instance id.
	 * 
	 * @return String value
	 */
	public String getInstanceId(){
		return instanceId;
	}
	
	/**
	 * Set the instance id.
	 * 
	 * @param instanceId String value
	 */
	public void setInstanceId(String instanceId){
		this.instanceId = instanceId;
	}
	
	/**
	 * Get the public dns.
	 * 
	 * @return String value
	 */
	public String getPublicDns(){
		return publicDns;
	}
	
	/**
	 * Set the instance id.
	 * 
	 * @param instanceId String value
	 */
	public void setPublicDns(String publicDns){
		this.publicDns = publicDns;
	}
	
	/**
	 * Get the region.
	 * 
	 * @return String value
	 */
	public String getRegion(){
		return region;
	}
	
	/**
	 * Set the region.
	 * 
	 * @param region String value
	 */
	public void setRegion(String region){
		this.region = region;
	}
	
	/**
	 * Get the availability zone.
	 * 
	 * @return String value
	 */
	public String getAvailabilityZone(){
		return availabilityZone;
	}
	
	/**
	 * Set the availability zone.
	 * 
	 * @param availabilityZone String value
	 */
	public void setAvailabilityZone(String availabilityZone){
		this.availabilityZone = availabilityZone;
	}
	
	/**
	 * Get the registrationTimestamp value.
	 * 
	 * @return String value
	 */
	public Timestamp getRegistrationTimestamp() {
		return registrationTimestamp;
	}

	/**
	 * Set the registrationTimestamp value.
	 * 
	 * @param registrationTimestamp String value
	 */
	public void setRegistrationTimestamp(Timestamp registrationTimestamp) {
		this.registrationTimestamp = registrationTimestamp;
	}

	
	/**
	 * Converts the object to a string format.
	 */
	public String toString(){
		String args = "account id:  %d\ninstance id:  %s\npublic dns:  %s\nregion:  %s\navailability zone:  %s\nregistration timestamp: %s\n";
		String account = String.format(args, accountId, instanceId, publicDns, region, availabilityZone, registrationTimestamp);
		
		return account;
	}
	
}
