package com.saic.rtws.commons.model.tmsdb;

/**
 * TMS DB SystemSizing DTO object.
 * 
 * @author LUCECU
 *
 */
public class SystemSizing {

	private String systemSize;
	private int jmsInstanceCount;
	private String jmsInstanceType;
	private Boolean combineIntExtFlag;
	
	/**
	 * Default constructor.
	 */
	public SystemSizing(){
		
	}
	
	/**
	 * Set the JMS system size.
	 * 
	 * @param systemSize String value.
	 */
	public void setSystemSize(String systemSize){
		this.systemSize = systemSize;
	}
	
	/**
	 * Get the JMS system size.
	 * 
	 * @return String value
	 */
	public String getSystemSize(){
		return systemSize;
	}
	
	/**
	 * Set the JMS instance count.
	 * 
	 * @param jmsInstanceCount int value
	 */
	public void setJmsInstanceCount(int jmsInstanceCount){
		this.jmsInstanceCount = jmsInstanceCount;
	}
	
	/**
	 * Get the JMS instance count.
	 * 
	 * @return int value
	 */
	public int getJmsInstanceCount(){
		return jmsInstanceCount;
	}
	
	/**
	 * Set the JMS instance type.
	 * 
	 * @param jmsInstanceType String value
	 */
	public void setJmsInstanceType(String jmsInstanceType){
		this.jmsInstanceType = jmsInstanceType;
	}
	
	/**
	 * Get the JMS instance type.
	 * 
	 * @return String value
	 */
	public String getJmsInstanceType(){
		return jmsInstanceType;
	}
	
	/**
	 * Set JMS combined internal/external node flag.
	 * 
	 * @param flag boolean value
	 */
	public void setCombineIntExtFlag(char flag){
		if(Character.toLowerCase(flag) == 'y'){
			this.combineIntExtFlag = true;
		}
		else{
			this.combineIntExtFlag = false;
		}
	}
	
	/**
	 * Get the JMS combined internal/external node flag.
	 * 
	 * @return boolean value
	 */
	public Boolean getCombineIntExtFlag(){
		return combineIntExtFlag;
	}
}
