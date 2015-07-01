package com.saic.rtws.commons.cloud.beans;

import java.util.Date;

public class Snapshot implements Cloneable {
	
	private String snapshotId;
	private String state;
	private String volumeId;
	private String description;
	private Date startTime;
	
	public Snapshot() {
		super();
	}
	
	public Snapshot(String snapshotId, String state, String volumeId, String description, Date startTime) {
		super();
		
		this.snapshotId = snapshotId;
		this.state = state;
		this.volumeId = volumeId;
		this.description = description;
		this.startTime = startTime;
	}
	
	public String getSnapshotId() {
		return this.snapshotId;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getVolumeId() {
		return this.volumeId;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Date getStartTime() {
		return this.startTime;
	}
	
	public Snapshot clone() {
		try {
			return (Snapshot) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	} 
	
}