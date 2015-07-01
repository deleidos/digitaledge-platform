package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.process.ZookeeperProcess;

public class ZookeeperMonitor extends ProcessMonitor {

	ZookeeperProcess zookeeperProcess = ZookeeperProcess.newInstance();

	public ZookeeperMonitor(String name) {
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
		switch (zookeeperProcess.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				addError("Zookeeper server is not running.");
				zookeeperProcess.start();
				break;
			default:
				addError("Zookeeper server status is currently unknown.");
				break;
		}
	}

}
