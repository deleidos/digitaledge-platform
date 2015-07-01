package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.RegionServerProcess;

public class RegionServerMonitor extends ProcessMonitor {

	RegionServerProcess regionServerProcess = RegionServerProcess.newInstance();

	public RegionServerMonitor(String name) {
		super(name);

		/*
		 * Give the process 10 minutes to start due to the timing required for HDFS and only monitor at 3 minute intervals
		 */
		setStartupPeriod(1000 * 60 * 10);
		setMonitorInterval(1000 * 60 * 3);
	}

	@Override
	protected void monitor() {
		try {
			switch (regionServerProcess.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				addError("Hbase RegionServer server is not running.");
				regionServerProcess.start();
				break;
			case Unknown:
			default:
				addError("Hbase RegionServer server is currently unknown.");
				break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
