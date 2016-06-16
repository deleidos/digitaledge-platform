package com.deleidos.rtws.systemcfg.beans.activemq;

/**
 *ActiveMQ node configuration.
 */
public class ActiveMQConfig {

	private int numNodes;
	public enum CursorType {STORE_CURSOR, VM_CURSOR};
	private CursorType queuePolicy;
	private CursorType subscriberPolicy;
	private String templateFile;
	private String outDir;
	private String domainName;
	
	/**
	 * Default Constructor.
	 */
	public ActiveMQConfig(){
		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param numNodes
	 * @param queuePolicy
	 * @param subscriberPolicy
	 */
	public ActiveMQConfig(int numNodes, CursorType queuePolicy, CursorType subscriberPolicy, String templateFile,
								String outDir, String domainName){
		this.numNodes = numNodes;
		this.queuePolicy = queuePolicy;
		this.subscriberPolicy = subscriberPolicy;
		this.templateFile = templateFile;
		this.outDir = outDir;
		this.domainName = domainName;
	}
	
	/**
	 * Set size of node cluster.
	 * 
	 * @param numNodes
	 */
	public void setNumNodes(int numNodes){
		this.numNodes = numNodes;
	}
	
	/**
	 * Get size of node cluster.
	 * 
	 * @return
	 */
	public int getNumNodes(){
		return numNodes;
	}
	
	/**
	 * Set the queue cursor policy.
	 * 
	 * @param queuePolicy
	 */
	public void setQueuePolicy(CursorType queuePolicy){
		this.queuePolicy = queuePolicy;
	}
	
	/**
	 * Get the queue cursor policy.
	 * 
	 * @return
	 */
	public CursorType getQueuePolicy(){
		return queuePolicy;
	}
	
	/**
	 * Set the subscriber cursor policy.
	 * 
	 * @param subscriberPolicy
	 */
	public void setSubscriberPolicy(CursorType subscriberPolicy){
		this.subscriberPolicy = subscriberPolicy;
	}
	
	/**
	 * Get the subscriber cursor policy.
	 * 
	 * @return
	 */
	public CursorType getSubscriberPolicy(){
		return subscriberPolicy;
	}
	
	/**
	 * Set the template file.
	 * 
	 * @param templateFile
	 */
	public void setTemplateFile(String templateFile){
		this.templateFile = templateFile;
	}
	
	/**
	 * Get the template file.
	 * 
	 * @return
	 */
	public String getTemplateFile(){
		return templateFile;
	}
	
	/**
	 * Set the output directory for created files.
	 * 
	 * @param outDir
	 */
	public void setOutDir(String outDir){
		this.outDir = outDir;
	}
	
	/**
	 * Get the output directory for created files.
	 * 
	 * @return
	 */
	public String getOutDir(){
		return outDir;
	}
	
	/**
	 * Set the domain name.
	 * 
	 * @param domainName
	 */
	public void setDomainName(String domainName){
		this.domainName = domainName;
	}
	
	/**
	 * Get the domain name.
	 * @return
	 */
	public String getDomainName(){
		return domainName;
	}
	
	public String toString(){
		String config = String.format("Num Nodes:  %d, Queue Policy:  %s, Subsciber Policy:  %s, Domain Name:  %s, Output Dir:  %s, Tempalte File:  %s", 
										this.numNodes, this.queuePolicy, this.subscriberPolicy, this.domainName, this.outDir, this.templateFile);
		
		return config;
	}
}
