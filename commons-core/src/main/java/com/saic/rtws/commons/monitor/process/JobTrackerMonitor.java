package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.JobTrackerProcess;

public class JobTrackerMonitor extends ProcessMonitor {

	JobTrackerProcess jobTrackerProcess = JobTrackerProcess.newInstance();

	public JobTrackerMonitor(String name) {
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
			switch (jobTrackerProcess.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				addError("JobTracker server is not running.");
				jobTrackerProcess.start();
				break;
			case Unknown:
			default:
				addError("JobTracker server status is currently unknown.");
				break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
