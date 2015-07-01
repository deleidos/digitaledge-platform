package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.NameNodeProcess;

public class NameNodeMonitor extends ProcessMonitor {

	NameNodeProcess nameNodeProcess = NameNodeProcess.newInstance();

	public NameNodeMonitor(String name) {
		super(name);

		/*
		 * Give the process 5 minutes to start and
		 * only monitor at 5 minute intervals
		 */
		setStartupPeriod(1000 * 60 * 5);
		setMonitorInterval(1000 * 60 * 5);
	}

	@Override
	protected void monitor() {
		try {
			switch (nameNodeProcess.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				addError("Namenode server is not running.");
				nameNodeProcess.start();
				break;
			case Unknown:
			default:
				addError("Namenode server is currently unknown.");
				break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
