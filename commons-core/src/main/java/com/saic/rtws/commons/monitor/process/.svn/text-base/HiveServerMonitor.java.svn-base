package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.HiveProcess;

public class HiveServerMonitor extends ProcessMonitor {

	// HiveProcess is a class provided in the CommandListener which determines
	// if the hive server software is installed and if it is currently running
	// or not
	HiveProcess hiveProcess = HiveProcess.newInstance();

	public HiveServerMonitor(String name) {
		super(name);

		/*
		 * Give the process 3 minutes to start and
		 * only monitor at 2 minute intervals
		 */
		setStartupPeriod(1000 * 60 * 3);
		setMonitorInterval(1000 * 60 * 2);
	}

	@Override
	protected void monitor() {
		try {
			switch (hiveProcess.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				addError("Hive server is not running.");
				hiveProcess.start();
				break;
			case Unknown:
			default:
				addError("Hive server status is currently unknown.");
				break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
