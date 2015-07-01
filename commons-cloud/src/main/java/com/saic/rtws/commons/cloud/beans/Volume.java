package com.saic.rtws.commons.cloud.beans;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class Volume implements Cloneable{
	
	private String id;
	
	@XmlTransient
	private Float size = 0.0f;
	
	private String device;
	
	private String state;
	
	private Date createTime;
	
	public Volume() {
		super();
	}
	
	public Volume(String id, Float size, String device, String state, Date createTime) {
		super();
		this.id = id;
		this.size = size;
		this.device = device;
		this.state = state;
		this.createTime = createTime;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public Float getSize() {
		return this.size;
	}
	
	public void setSize(Float size){
		this.size = size;
	}
	
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	
	public String getState() {
		return this.state;
	}
	
	public void setState(String state){
		this.state = state;
	}
	
	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	public Volume clone() {
		try {
			return (Volume) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}