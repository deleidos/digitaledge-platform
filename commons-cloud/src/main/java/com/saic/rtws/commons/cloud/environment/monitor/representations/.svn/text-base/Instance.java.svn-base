package com.saic.rtws.commons.cloud.environment.monitor.representations;

public class Instance {

	private String type;

	private InstanceTypeDetails typeDetails;

	private int free;

	private int used;
	
	private int max;

	public Instance() {
		super();
	}

	public Instance(String type, int free, int used, int max) {
		this.type = type;
		this.free = free;
		this.used = used;
		this.max = max;
	}

	public int getFree() {
		return free;
	}

	public String getType() {
		return type;
	}

	public InstanceTypeDetails getTypeDetails() {
		return typeDetails;
	}

	public int getUsed() {
		return used;
	}

	public int getMax() {
		return max;
	}


	public void setTypeDetails(InstanceTypeDetails typeDetails) {
		this.typeDetails = typeDetails;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Instance [type=").append(type).append(", typeDetails=").append(typeDetails)
					.append(", free=").append(free).append(", used=").append(used)
					.append(", max=").append(max).append("]");
		return builder.toString();
	}

	public Instance withTypeDetails(InstanceTypeDetails instanceTypeDetails) {
		setTypeDetails(instanceTypeDetails);
		return this;
	}

}
