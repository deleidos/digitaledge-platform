package com.saic.rtws.commons.cloud.beans;

import java.util.Properties;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;

import com.saic.rtws.commons.config.RtwsConfig;

public class ProcessDefinition implements Cloneable {

	private String image;
	private String type;
	private String zone;
	private String key;
	private String kernel;
	private String ramdisk;
	private String security;
	private String securityId;
	private boolean allocatePersistentIpAddress;
	private String persistentIpAddress;
	private boolean allocateInternetAddress;
	private String internetAddress;
	private int volumeCount;
	private int volumeSize;
	private String subnet;  // VPC subnet
	
	private Properties properties = new Properties();

	private static final String DEFAULT_DEVICE_PREFIX = "/dev/sdf";

	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKernel() {
		return kernel;
	}

	public void setKernel(String kernel) {
		this.kernel = kernel;
	}

	public String getRamdisk() {
		return ramdisk;
	}

	public void setRamdisk(String ramdisk) {
		this.ramdisk = ramdisk;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}
	
	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public boolean getAllocatePersistentAddress() {
		return allocatePersistentIpAddress;
	}

	public void setAllocatePersistentAddress(boolean allocate) {
		this.allocatePersistentIpAddress = allocate;
	}
	
	public String getPersistentIpAddress() {
		return persistentIpAddress;
	}

	public void setPersistentIpAddress(String address) {
		this.persistentIpAddress = address;
	}
	
	public boolean getAllocateInternetAddress() {
		return allocateInternetAddress;
	}

	public void setAllocateInternetAddress(boolean allocate) {
		this.allocateInternetAddress = allocate;
	}
	
	public String getInternetAddress() {
		return internetAddress;
	}

	public void setInternetAddress(String address) {
		this.internetAddress = address;
	}
	
	public boolean hasAttachedStorage() {
		return volumeCount > 0 && volumeSize > 0;
	}
	
	public int getVolumeCount() {
		return volumeCount;
	}
	
	public void setVolumeCount(int count) {
		this.volumeCount = count;
	}
	
	public int getVolumeSize() {
		return volumeSize;
	}
	
	public void setVolumeSize(int size) {
		this.volumeSize = size;
	}
	
	
	@XmlTransient
	public String getVolumeBlockPrefix(){
		if(StringUtils.isNotBlank(RtwsConfig.getInstance().getString("device.name.prefix"))) {
			return(RtwsConfig.getInstance().getString("device.name.prefix"));
		}
		else {
			return(DEFAULT_DEVICE_PREFIX);
		}
	}
	
	public String getSubnet() {
		return subnet;
	}
	
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public ProcessDefinition clone() {
		try {
			ProcessDefinition copy = (ProcessDefinition)super.clone();
			copy.properties = (Properties)this.properties.clone();
			return copy;
		} catch(CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "ProcessDefinition [image=" + image + ", type=" + type + ", zone=" + zone + ", key=" + key + ", kernel=" + kernel
				+ ", ramdisk=" + ramdisk + ", security=" + security + ", securityId=" + securityId + ", allocatePersistentIpAddress="
				+ allocatePersistentIpAddress + ", persistentIpAddress=" + persistentIpAddress + ", allocateInternetAddress="
				+ allocateInternetAddress + ", internetAddress=" + internetAddress + ", volumeCount=" + volumeCount
				+ ", volumeSize=" + volumeSize + ", volumeBlockPrefix=" + getVolumeBlockPrefix() + ", subnet=" + subnet
				+ ", properties=" + properties + "]";
	}
	
}
