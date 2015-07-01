package com.saic.rtws.commons.cloud.beans.vpc;

public class PortRange {

	private int from;
	private int to;
	
	public PortRange(){
		
	}
	
	public void setFrom(int from){
		this.from = from;
	}
	
	public int getFrom(){
		return from;
	}
	
	public void setTo(int to){
		this.to = to;
	}
	
	public int getTo(){
		return to;
	}
}
