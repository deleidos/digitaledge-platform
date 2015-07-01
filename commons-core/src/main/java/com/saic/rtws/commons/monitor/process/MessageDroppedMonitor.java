package com.saic.rtws.commons.monitor.process;

import java.io.File;
import java.util.Date;

import com.saic.rtws.commons.monitor.core.ProcessMonitor;

public class MessageDroppedMonitor extends ProcessMonitor {

	private File pipelineDroppingMessagesFlag = new File("/tmp/.droppingMsgs.txt");
	private Date triggeredOn = null;

	public MessageDroppedMonitor(String name) {
		super(name);
		setStartupPeriod(1000 * 60 * 3); // Allow for enough time for the ingest framework to initialize
		setMonitorInterval(1000 * 60 * 5); // Poll every 5 minutes for dropped messages
	}

	@Override
	protected void monitor() {
		try {

			// Trigger is the presence of a file as a simple solution for cross pid notification
			if (pipelineDroppingMessagesFlag.exists()) {
				if (triggeredOn == null)
					triggeredOn = new Date();
				addError("A throttle condition has triggered the dropping of messages. Start (" + triggeredOn + ")");
			} else {
				setStatus(MonitorStatus.OK);
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}
	}
}
