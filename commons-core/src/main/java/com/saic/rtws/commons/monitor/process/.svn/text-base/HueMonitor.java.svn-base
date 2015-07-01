package com.saic.rtws.commons.monitor.process;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.HueProcess;

public class HueMonitor extends ProcessMonitor {
	
		HueProcess process = HueProcess.newInstance();

		public HueMonitor(String name) {
			super(name);
			setStartupPeriod(1000 * 60 * 3);
			setMonitorInterval(1000 * 60 * 5);
		}

		@Override
		protected void monitor() {
			try {
				switch (process.getStatus()) {
				case Running:
					setStatus(MonitorStatus.OK);
					break;
				case Stopped:
					addError("Hue is not running.");
					process.start();
					break;
				case Unknown:
				default:
					addError("Hue status is currently unknown.");
					break;
				}
			} catch (ServerException e) {
				addError(e.getMessage());
			}
		}
}
