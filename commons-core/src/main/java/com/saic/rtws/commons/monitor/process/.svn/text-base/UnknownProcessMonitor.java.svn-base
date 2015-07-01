package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;

public class UnknownProcessMonitor extends ProcessMonitor {

	public UnknownProcessMonitor(String unknownProcessName) {
		super(unknownProcessName);
	}

	@Override
	public void monitor() {
		addError("", String.format("'%s' is an unknown process monitor.", getName()));
		lockState();
		// Max out the monitor interval just to decrease the (minimal) CPU usage from calling monitor repeatedly
		// since we have locked the monitor and are no longer monitoring
		setMonitorInterval(Integer.MAX_VALUE);
	}
}
