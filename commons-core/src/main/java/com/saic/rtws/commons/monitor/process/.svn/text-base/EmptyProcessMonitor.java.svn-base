package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;

public class EmptyProcessMonitor extends ProcessMonitor {

	public EmptyProcessMonitor(String name) {
		super(name);
	}

	@Override
	protected void monitor() {
		setStatus(MonitorStatus.OK);
		lockState();
		// Max out the monitor interval just to decrease the (minimal) CPU usage from calling monitor repeatedly
		// since we have locked the monitor and are no longer monitoring
		setMonitorInterval(Integer.MAX_VALUE);
	}

}
