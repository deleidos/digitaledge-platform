package com.saic.rtws.commons.model.tmsdb;

/**
 * TMS DB MachineImage DTO object.
 * 
 * @author LUCECU
 *
 */
public class MachineImage {

	private String iaasServiceName;
	private String iaasRegion;
	private String swVersionId;
	private String tenantId;
	private String mi64BitInstance;
	
	/**
	 * Default constructor.
	 */
	public MachineImage(){
		
	}
	
	/**
	 * Set iaas service name.
	 * 
	 * @param iaasServiceName String value
	 */
	public void setIaasServiceName(String iaasServiceName){
		this.iaasServiceName = iaasServiceName;
	}
	
	/**
	 * Get iaas service name.
	 * 
	 * @return String value
	 */
	public String getIaasServiceName(){
		return iaasServiceName;
	}
	
	/**
	 * Set iaas region.
	 * 
	 * @param iaasRegion String value
	 */
	public void setIaasRegion(String iaasRegion){
		this.iaasRegion = iaasRegion;
	}
	
	/**
	 * Get iaas region.
	 * 
	 * @return String value
	 */
	public String getIaasRegion(){
		return iaasRegion;
	}
	
	/**
	 * Set software version id.
	 * 
	 * @param swVersionId String value.
	 */
	public void setSWVersionId(String swVersionId){
		this.swVersionId = swVersionId;
	}
	
	/**
	 * Get software version id.
	 * 
	 * @return String value
	 */
	public String getSWVersionId(){
		return swVersionId;
	}
	
	/**
	 * Set machine image 64 bit instance storage name.
	 * 
	 * @param mi64BitInstance
	 */
	public void setMI64BitInstance(String mi64BitInstance){
		this.mi64BitInstance = mi64BitInstance;
	}
	
	/**
	 * Get machine image 64 bit instance storage name.
	 * 
	 * @return
	 */
	public String getMI64BitInstance(){
		return mi64BitInstance;
	}

	/**
	 * Get the tenantId.
	 * 
	 * @return
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * Set the tenantId.
	 * 
	 * @param tenantId
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MachineImage [iaasServiceName=").append(iaasServiceName).append(", iaasRegion=").append(iaasRegion).append(", swVersionId=").append(swVersionId).append(", tenantId=")
				.append(tenantId).append(", mi64BitInstance=").append(mi64BitInstance).append("]");
		return builder.toString();
	}
	
}
