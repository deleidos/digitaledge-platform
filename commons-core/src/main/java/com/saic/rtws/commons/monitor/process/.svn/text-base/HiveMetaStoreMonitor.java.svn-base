package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.HiveMetaStoreProcess;

public class HiveMetaStoreMonitor extends ProcessMonitor {

	HiveMetaStoreProcess hiveMetaStoreProcess = HiveMetaStoreProcess.newInstance();

	public HiveMetaStoreMonitor(String name) {
		super(name);

		/*
		 * Give the process 5 minutes to start and only monitor at 5 minute intervals
		 */
		setStartupPeriod(1000 * 60 * 5);
		setMonitorInterval(1000 * 60 * 5);
	}

	@Override
	protected void monitor() {
		try {
			switch (hiveMetaStoreProcess.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				addError("Hive Metastore server is not running.");
				hiveMetaStoreProcess.start();
				break;
			case Unknown:
			default:
				addError("Hive Metastore server is currently unknown.");
				break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
