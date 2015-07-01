package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.JettyProcess;

public class JettyMonitor extends ProcessMonitor {

	// JettyProcess is a class provided in the CommandListener which determines
	// if the Jetty software is installed and if it is currently running or not
	JettyProcess jettyProcess = JettyProcess.newInstance(false);

	public JettyMonitor(String processName) {
		super(processName);
		setStartupPeriod(1000 * 60 * 3);  // Give the master process 3 minutes to start
		setMonitorInterval(1000 * 30);	// Only monitor every 30 seconds
	}

	@Override
	public void monitor() {
		try {
			switch(jettyProcess.getStatus()) {
				case Running :
					setStatus(MonitorStatus.OK);
					break;
				case Stopped :
					addError("Jetty web server is not running.");
					break;
				case Unknown :
				default :
					addError("Jetty web server status is currently unknown.");
					break;
			}
		} catch (ServerException e) {
			// A ServerException is thrown if the Jetty software
			// is not installed on the instance
			addError(e.getMessage());
		}
	}
}
