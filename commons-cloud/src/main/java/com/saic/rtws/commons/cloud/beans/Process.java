package com.saic.rtws.commons.cloud.beans;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class Process implements Cloneable {

	private int number;
	
	private boolean allocateInternetAddress;
	
	private String id;
	
	private String vpcId;
	
	private String subnetId;
	
	@XmlTransient
	private String imageId;
	
	@XmlTransient
	private String type;
	
	@XmlTransient
	private State state;
	
	private String group;
	
	private String publicDnsName;
	
	private String privateDnsName;
	
	private String persistentDnsName;
	
	private String publicIpAddress;
	
	private String privateIpAddress;
	
	private String persistentIpAddress;
	
	private String internetAddress;
	
	private String rootDeviceType;
	
	private Volume [] volumes;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean getAllocateInternetAddress() {
		return allocateInternetAddress;
	}

	public void setAllocateInternetAddress(boolean allocateInternetAddress) {
		this.allocateInternetAddress = allocateInternetAddress;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public String getSubnetId() {
		return subnetId;
	}

	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}

	public String getImageId() {
		return imageId;
	}
	
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getPublicDnsName() {
		return publicDnsName;
	}

	public void setPublicDnsName(String publicDnsName) {
		this.publicDnsName = publicDnsName;
	}

	public String getPrivateDnsName() {
		return privateDnsName;
	}

	public void setPrivateDnsName(String privateDnsName) {
		this.privateDnsName = privateDnsName;
	}

	public String getPersistentDnsName() {
		return persistentDnsName;
	}

	public void setPersistentDnsName(String persistentDnsName) {
		this.persistentDnsName = persistentDnsName;
	}

	public String getPublicIpAddress() {
		return publicIpAddress;
	}

	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public String getPersistentIpAddress() {
		return persistentIpAddress;
	}

	public void setPersistentIpAddress(String persistentIpAddress) {
		this.persistentIpAddress = persistentIpAddress;
	}
	
	public String getInternetAddress() {
		return internetAddress;
	}

	public void setInternetAddress(String internetAddress) {
		this.internetAddress = internetAddress;
	}

	public String getRootDeviceType() {
		return rootDeviceType;
	}

	public void setRootDeviceType(String rootDeviceType) {
		this.rootDeviceType = rootDeviceType;
	}

	public Volume[] getVolumes() {
		return volumes;
	}

	public void setVolumes(Volume[] volumes) {
		this.volumes = volumes;
	}

	public Process clone() {
		try {
			Process copy = (Process)super.clone();
			if (volumes != null) { 
				copy.volumes = new Volume[volumes.length];
				for (int i = 0; i < volumes.length; i++) {
					copy.volumes[i] = volumes[i].clone();
				}
			}
			return copy;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Process [number=" + number + ", allocateInternetAddress=" + allocateInternetAddress + ", id=" + id + ", vpcId="
				+ vpcId + ", subnetId=" + subnetId + ", imageId=" + imageId + ", type=" + type + ", state=" + state + ", group="
				+ group + ", publicDnsName=" + publicDnsName + ", privateDnsName=" + privateDnsName + ", persistentDnsName="
				+ persistentDnsName + ", publicIpAddress=" + publicIpAddress + ", privateIpAddress=" + privateIpAddress
				+ ", persistentIpAddress=" + persistentIpAddress + ", internetAddress=" + internetAddress + ", rootDeviceType="
				+ rootDeviceType + ", volumes=" + Arrays.toString(volumes) + "]";
	}

}
