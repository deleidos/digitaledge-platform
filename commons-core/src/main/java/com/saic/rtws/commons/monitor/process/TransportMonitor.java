package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.TransportProcess;

public class TransportMonitor extends ProcessMonitor {

	// TransportProcess is a class provided in the CommandListener which determines
	// if the transport software is installed and if it is currently running or not
	TransportProcess transportProcess = TransportProcess.newInstance();
	
	public TransportMonitor(String name) {
		super(name);
		setStartupPeriod(1000 * 60 * 3);  // Give the transport process 3 minutes to start
		setMonitorInterval(1000 * 30);	// Only monitor every 30 seconds
	}

	@Override
	protected void monitor() {
		try {
			switch(transportProcess.getStatus()) {
				case Running :
					setStatus(MonitorStatus.OK);
					break;
				case Stopped :
					addError("Transport server is not running.");
					break;
				case Unknown :
				default :
					addError("Transport server status is currently unknown.");
					break;
			}
		} catch (ServerException e) {
			// A ServerException is thrown if the transport software
			// is not installed on the instance
			addError(e.getMessage());
		}	}

}
