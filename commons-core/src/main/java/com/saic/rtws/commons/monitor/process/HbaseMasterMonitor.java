package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.HbaseMasterProcess;

public class HbaseMasterMonitor extends ProcessMonitor {

	HbaseMasterProcess hbaseMasterProcess = HbaseMasterProcess.newInstance();

	public HbaseMasterMonitor(String name) {
		super(name);

		/*
		 * Give the process 10 minutes to start due to the timing required for HDFS and only monitor at 5 minute intervals
		 */
		setStartupPeriod(1000 * 60 * 10);
		setMonitorInterval(1000 * 60 * 5);
	}

	@Override
	protected void monitor() {
		try {
			switch (hbaseMasterProcess.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				addError("HbaseMaster server is not running.");
				hbaseMasterProcess.start();
				break;
			case Unknown:
			default:
				addError("HbaseMaster server is currently unknown.");
				break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
