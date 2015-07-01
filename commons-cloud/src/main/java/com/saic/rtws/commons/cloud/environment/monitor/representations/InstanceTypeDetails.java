package com.saic.rtws.commons.cloud.environment.monitor.representations;

public class InstanceTypeDetails {

	private int cpus;

	private int memoryInMb;

	private int ephermalDiskInGb;

	public InstanceTypeDetails(int cpus, int memoryInMb, int ephermalDiskInGb) {
		super();
		this.cpus = cpus;
		this.memoryInMb = memoryInMb;
		this.ephermalDiskInGb = ephermalDiskInGb;
	}
	
	public InstanceTypeDetails(){
		
	}

	public int getCpus() {
		return cpus;
	}
	public void setCpus(int value){
		cpus = value;
	}


	public int getMemoryInMb() {
		return memoryInMb;
	}
	public void setMemoryInMb(int value) {
		memoryInMb = value;
	}
	
	
	public int getEphermalDiskInGb(){
		return ephermalDiskInGb;
	}
	public void setEphermalDiskInGb(int value){
		ephermalDiskInGb = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstanceDetails [cpus=").append(cpus).append(", memoryInMb=").append(memoryInMb).append(", ephermalDiskInGb=").append(ephermalDiskInGb).append("]");
		return builder.toString();
	}

}
