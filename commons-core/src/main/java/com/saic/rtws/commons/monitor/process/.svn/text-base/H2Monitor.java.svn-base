package com.saic.rtws.commons.monitor.process;

import java.io.File;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.H2Process;

public class H2Monitor extends ProcessMonitor {

	H2Process h2Process = H2Process.newInstance();
	
	File shutdownSentinelFile = new File("/usr/local/rtws/commons-core/bin/boot/STOP_H2");

	public H2Monitor(String name) {
		super(name);

		/*
		 * Give the master process 3 minutes to start
		 */
		setStartupPeriod(1000 * 60 * 3);
		setMonitorInterval(1000 * 30);	// Only monitor every 30 seconds
	}

	@Override
	protected void monitor() {
		try {
			switch (h2Process.getStatus()) {
			case Running:
				setStatus(MonitorStatus.OK);
				break;
			case Stopped:
				
				if(shutdownSentinelFile.exists()){
					//ok state - shutting down
				}else{
					addError("H2 server is not running.");
					h2Process.start();
				}
				break;
			case Unknown:
			default:
				addError("H2 server is currently unknown.");
				break;
			}
		} catch (ServerException e) {
			addError(e.getMessage());
		}
	}

}
