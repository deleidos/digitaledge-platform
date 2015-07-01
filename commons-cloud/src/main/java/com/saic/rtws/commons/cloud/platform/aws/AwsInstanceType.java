package com.saic.rtws.commons.cloud.platform.aws;

public enum AwsInstanceType {
	Micro("t1.micro"), Small("m1.small"), Large("m1.large"), ExtraLarge("m1.xlarge"), HighMemoryExtraLarge("m2.xlarge"), HighMemoryDoubleExtraLarge("m2.2xlarge"), HighMemoryQuadrupleExtraLarge("m2.4xlarge"), HighCpuExtraLarge("c1.xlarge");
	
	private String name;
	
	private AwsInstanceType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}