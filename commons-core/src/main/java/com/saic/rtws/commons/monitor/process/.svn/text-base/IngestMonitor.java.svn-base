package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.IngestProcess;

public class IngestMonitor extends ProcessMonitor {

	// IngestProcess is a class provided in the CommandListener which determines
	// if the ingest software is installed and if it is currently running or not
	IngestProcess ingestProcess = IngestProcess.newInstance(false);
	
	public IngestMonitor(String name) {
		super(name);
		setStartupPeriod(1000 * 60 * 3);  // Give the ingest process 3 minutes to start
		setMonitorInterval(1000 * 30);	// Only monitor every 30 seconds
	}

	@Override
	protected void monitor() {
		try {
			switch(ingestProcess.getStatus()) {
				case Running :
					setStatus(MonitorStatus.OK);
					break;
				case Restarting :
					setStatus(MonitorStatus.OK);
					break;
				case Stopped :
					addError("Ingest server is not running.");
					break;
				case Unknown :
					// Not sure of the status
				default :
					addError("Ingest server status is currently unknown.");
					break;
			}
		} catch (ServerException e) {
			// A ServerException is thrown if the ingest software
			// is not installed on the instance
			addError(e.getMessage());
		}
	}

}
