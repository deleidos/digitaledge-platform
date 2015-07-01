package com.saic.rtws.commons.model.jmx;

import java.io.File;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DiskInfoBean   {
	private File fVolume;
	private String volume;
	
	
	public DiskInfoBean() {
		
	}
	
	public DiskInfoBean(String volume) {
		this.volume = volume;
		fVolume = new File(volume);
	}
	
	public DiskInfoBean(File fVolume) {
		this.fVolume = fVolume;
		this.volume = fVolume.getAbsolutePath();
	}
	
	public String getVolume() {
		return volume;
	}
	
	public void setVolume(String volume) {
		this.volume = volume;
		fVolume = new File(volume);
	}
	
	public long getUsableSpace() {
		return fVolume.getUsableSpace();
	}
	
	public long getFreeSpace() {
		return fVolume.getFreeSpace();
	}
	
	public long getTotalSpace() {
		return fVolume.getTotalSpace();
	}
	
	public long getSpaceUsed() {
		return fVolume.getTotalSpace() - fVolume.getFreeSpace();
	}
	
	public double getPercentSpaceUsed() {
		return calculatePercentSpaceUsed();
	}
	
	public double getPercentSpaceAvailable() {
		return 100.00d - calculatePercentSpaceUsed();
	}
	
	private double calculatePercentSpaceUsed() {
		double dUsableSpace = Long.valueOf(getUsableSpace()).doubleValue();
		double dSpaceUsed = Long.valueOf(getSpaceUsed()).doubleValue();
		
		// Calculate the percentage of space used.
		double percentSpaceUsed = ((dSpaceUsed * 100.00d) / (dSpaceUsed + dUsableSpace));
		
		return percentSpaceUsed;
	}

	public void fetch() {
		getUsableSpace();
		getFreeSpace();
		getTotalSpace();
		getSpaceUsed();
		getPercentSpaceUsed();
	}
	
}
