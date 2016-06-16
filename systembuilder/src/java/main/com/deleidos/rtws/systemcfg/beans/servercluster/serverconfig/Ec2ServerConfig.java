package com.deleidos.rtws.systemcfg.beans.servercluster.serverconfig;


public class Ec2ServerConfig implements IaasServerConfig {
	public static enum EC2_SERVER_SIZE {
		SMALL, MEDIUM, LARGE, X_LARGE, XX_LARGE
	}
	
	private String imageId;
	private EC2_SERVER_SIZE size;
	private String preferredRegion;
	private String preferredZone;
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public EC2_SERVER_SIZE getSize() {
		return size;
	}
	public void setSize(EC2_SERVER_SIZE size) {
		this.size = size;
	}
	public String getPreferredRegion() {
		return preferredRegion;
	}
	public void setPreferredRegion(String preferredRegion) {
		this.preferredRegion = preferredRegion;
	}
	public String getPreferredZone() {
		return preferredZone;
	}
	public void setPreferredZone(String preferredZone) {
		this.preferredZone = preferredZone;
	}
}
