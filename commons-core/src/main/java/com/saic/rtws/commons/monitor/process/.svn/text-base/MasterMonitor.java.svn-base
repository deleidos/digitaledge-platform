package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.MasterProcess;

public class MasterMonitor extends ProcessMonitor {

	// MasterProcess is a class provided in the CommandListener which determines
	// if the master software is installed and if it is currently running or not
	MasterProcess masterProcess = MasterProcess.newInstance();

	public MasterMonitor(String name) {
		super(name);
		setStartupPeriod(1000 * 60 * 3);  // Give the master process 3 minutes to start
		setMonitorInterval(1000 * 30);	// Only monitor every 30 seconds
	}

	@Override
	protected void monitor() {
		try {
			switch(masterProcess.getStatus()) {
				case Running :
					setStatus(MonitorStatus.OK);
					break;
				case Stopped :
					addError("Master server is not running.");
					break;
				case Unknown :
				default :
					addError("Master server status is currently unknown.");
					break;
			}
		} catch (ServerException e) {
			// A ServerException is thrown if the master software
			// is not installed on the instance
			addError(e.getMessage());
		}
	}

}
