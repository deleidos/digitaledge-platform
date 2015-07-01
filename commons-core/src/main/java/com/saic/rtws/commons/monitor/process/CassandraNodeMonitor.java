package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.CassandraNodeProcess;

public class CassandraNodeMonitor extends ProcessMonitor {

	private CassandraNodeProcess process = CassandraNodeProcess.newInstance();
	
	public CassandraNodeMonitor(String name) {
		super(name);
		setStartupPeriod(1000 * 60 * 10);
		setMonitorInterval(1000 * 60 * 10);
	}

	@Override
	protected void monitor() {
		try {
			switch(process.getStatus()) {
				case Running :
					setStatus(MonitorStatus.OK);
					break;
				case Stopped :
					addError("Cassandra node is not running.");
					break;
				case Unknown :
					// Not sure of the status
				default :
					addError("Cassandra node status is currently unknown.");
					break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
