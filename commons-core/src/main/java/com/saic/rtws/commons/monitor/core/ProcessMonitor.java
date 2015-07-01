package com.saic.rtws.commons.monitor.core;

import java.util.Properties;

import com.saic.rtws.commons.monitor.core.ManagedMonitor;

public abstract class ProcessMonitor extends ManagedMonitor {

	/**
	 * Instantiates a new process monitor with an immutable name.
	 *
	 * @param name the immutable name
	 */
	public ProcessMonitor(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see com.saic.rtws.commons.management.ManagedBean#buildObjectNameKeys(java.util.Properties)
	 */
	@Override
	public void buildObjectNameKeys(Properties keys) {
		keys.put("type", getName());
	}
}
