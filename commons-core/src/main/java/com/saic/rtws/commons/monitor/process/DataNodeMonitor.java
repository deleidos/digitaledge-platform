package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.DataNodeProcess;

public class DataNodeMonitor extends ProcessMonitor {

	DataNodeProcess dataNodeProcess = DataNodeProcess.newInstance();

	public DataNodeMonitor(String name) {
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
			switch (dataNodeProcess.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				addError("DataNode is not running.");
				dataNodeProcess.start();
				break;
			case Unknown:
			default:
				addError("DataNode status is currently unknown.");
				break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
