package com.saic.rtws.commons.cloud.beans;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public enum State {
	Pending("pending"), Running("running"), ShuttingDown("shutting-down"), 
	Stopped("stopped"), Stopping("stopping"), Terminated("terminated");
	
	private String name;
	
	private State(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static State fromValue(String value) {
		for (State state : State.values()) {
			if (state.getName().equalsIgnoreCase(value)) {
				return state;
			}
		}
		
		return null;
	}
	
	public static boolean isTerminated(String value) {
		return Terminated.toString().equalsIgnoreCase(value);
	}
	
	public static boolean isStopped(String value) {
		return Stopped.toString().equalsIgnoreCase(value);
	}
}