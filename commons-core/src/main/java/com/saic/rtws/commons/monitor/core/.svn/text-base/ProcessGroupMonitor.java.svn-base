package com.saic.rtws.commons.monitor.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.monitor.core.ManagedMonitor;

public class ProcessGroupMonitor extends ManagedMonitor {
	
	private static final Logger logger = Logger.getLogger(ProcessGroupMonitor.class);
	
	/** The list of processes being monitored */
	private ArrayList<ProcessMonitor> monitoredProcesses = new ArrayList<ProcessMonitor>();
	
	/**
	 * There is the possibility that shutDown() is called while the ProcessGroupMonitor 
	 * is running monitor(). This means the shutDown will have to wait until 
	 * monitor completes, since the monitoredProcesses list is synchronized in
	 * these methods. For some monitors, this is bad because the ProcessMonitor will
	 * restart their services if a problem is detected.  If their service is
	 * stopped, and the ProcessMonitor is supposed to be shut down, but the shut down
	 * is delayed by running through the list of ProcessMonitors, there is a 
	 * possibility that the ProcessMonitor will detect the shutdown service and
	 * restart it before being shutdown itself.
	 */
	private boolean forceShutDown = false;

	/**
	 * Instantiates a new process group monitor with an immutable name.
	 *
	 * @param name the immutable name
	 */
	public ProcessGroupMonitor(String name) {		
		super(name);
		
		// Get the process monitors that are required for this process group
		ArrayList<String> processMonitorList = retrieveProcessMonitorList();
		ArrayList<ProcessMonitor> requiredProcessMonitors = createProcessMonitors(processMonitorList);
		registerProcessMonitors(requiredProcessMonitors);
	}
	
	private ArrayList<String> retrieveProcessMonitorList() {
		ArrayList<String> processMonitors = new ArrayList<String>();
		
		// Get the file location
		String configFile = System.getProperty("com.saic.rtws.monitor.processgroup.config.file", "/usr/local/rtws/conf/monitored_processes.conf");
		
		BufferedReader configIn = null;
		try {
			configIn = new BufferedReader(new FileReader(new File(configFile)));
			
			String line;
			while( (line = configIn.readLine()) != null ) {
				logger.debug("Adding process monitor '" + line + "'.");
				processMonitors.add(line);
			}
		} catch (FileNotFoundException fnfe) {
			addError(String.format("Unable to locate configuration file: %s.", configFile));
			lockState();  // Don't let this message be erased or the error state be changed
		} catch (IOException ioe) {
			addError(String.format("Failed to read configuration file: %s: %s.", configFile, ioe.getMessage()));
			lockState();  // Don't let this message be erased or the error state be changed
			processMonitors.clear();
		}
		finally {
			if(configIn != null) {
				try {
					configIn.close();
				} catch (Exception ignore) {}
			}
		}
		
		return processMonitors;
	}
	
	/**
	 * Parses the required process monitors from a CSV list returning a
	 * process monitor for each value on the list.
	 *
	 * @param csvList the CSV list of process monitor class names
	 * @return the array list process monitors
	 */
	private ArrayList<ProcessMonitor> createProcessMonitors(ArrayList<String> processMonitorList) {
		ArrayList<ProcessMonitor> processMonitors = new ArrayList<ProcessMonitor>();
		
		for(int i = 0; i < processMonitorList.size(); i++) {
			processMonitors.add(ProcessMonitorFactory.createProcessMonitor(processMonitorList.get(i)));
		}
		
		return processMonitors;
	}

	/* (non-Javadoc)
	 * @see com.saic.rtws.commons.management.ManagedBean#buildObjectNameKeys(java.util.Properties)
	 */
	@Override
	public void buildObjectNameKeys(Properties keys) {
		keys.put("type", "ProcessGroupMonitor");
	}
	
	/**
	 * Register and start a ProcessMonitor to be monitored by this process group monitor.
	 *
	 * @param processMonitor the process monitor
	 */
	private void registerProcessMonitor(ProcessMonitor processMonitor) {
	  synchronized( monitoredProcesses ) {
		// Only register a process monitor if we don't already have it registered
		if(!monitoredProcesses.contains(processMonitor)) {
			if(!processMonitor.isRunning()) {
				processMonitor.start();
			}
			monitoredProcesses.add(processMonitor);
		}
	  }
	}

	/**
	 * Stops the specified ProcessMonitor and no longer monitors it.
	 * NOTE: do not call while iterating monitoredProcesses!!!
	 *
	 * @param processMonitor the process monitor
	 */
	private void unregisterProcessMonitor(ProcessMonitor processMonitor) {
	  synchronized( monitoredProcesses ) {		
		if(monitoredProcesses.contains(processMonitor)) {		
			processMonitor.stop();
			monitoredProcesses.remove(processMonitor);
			removeErrors(processMonitor.getName());
		}
	  }
	}
	
	/**
	 * Register a group of ProcessMonitors to be monitored by this process group monitor.
	 * This will start each ProcessMonitor in the group.
	 *
	 * @param processMonitors the process monitors
	 */
	private void registerProcessMonitors(ArrayList<ProcessMonitor> processMonitors) {
		for (ProcessMonitor processMonitor : processMonitors) {
			registerProcessMonitor(processMonitor);
		}
	}
	
	/**
	 * Unregister a group of ProcessMonitors being monitored by this process group monitor.
	 * This will stop each ProcessMonitor in the group.
	 * NOTE: do not call this method with this.monitoredProcesses as an argument!!!
	 *
	 * @param processMonitors the process monitors
	 */
	@Deprecated
	private void unregisterProcessMonitors(ArrayList<ProcessMonitor> processMonitors) {
		for (ProcessMonitor processMonitor : processMonitors) {
			unregisterProcessMonitor(processMonitor);
		}
	}

	/**
	 * Monitor all the ProcessMonitors.  This is done by checking their monitored
	 * status.  If their status is not OK, the status of this monitor will be
	 * set to an error status and any error message associated with the ProcessMonitor
	 * who tripped the error will be saved.  If all ProcessMonitors are an OK status,
	 * then this ProcessGroupMonitor will be an OK status with no error messages.
	 */
	@Override
	public void monitor() {
		boolean statusOK = true;

		synchronized( monitoredProcesses ) {
			
			if(monitoredProcesses.isEmpty()) {
				addError("No registered process monitors.");
				statusOK = false;
			}
			else {
				// Run through the processes to determine the current status
				for(ProcessMonitor monitoredProcess : monitoredProcesses) {
					// Shut down has been called, exit the loop so that we
					// release the synchronization on monitoredProcesses
					// and shutDown can then access it.
					if(forceShutDown) {
						break;
					}
					
					// See if the process status has changed for logging purposes
					MonitorStatus processStatus = monitoredProcess.getStatus();
					
					switch(processStatus) {
						case STARTING : // Treat the same as OK, fall through to OK case
						case OK :
							// The Process is running OK, remove any error messages associated with it
							removeErrors(monitoredProcess.getName());
							break;
						case ERROR :
							// The Process has an error.  Save the error message associated with the process.
							addErrors(monitoredProcess.getName(), monitoredProcess.getErrorMessages());
							statusOK = false;
							break;
						case STOPPED :
							addError(monitoredProcess.getName(), "The monitor is stopped.");
							addErrors(monitoredProcess.getName(), monitoredProcess.getErrorMessages());
							statusOK = false;
						case UNKNOWN :
						default :
							addError(monitoredProcess.getName(), String.format("Process %s has %s status", monitoredProcess.getName(), processStatus.toString()));
							statusOK = false;
							break;
					}
				}
			}
		}
			
		if(statusOK) {
			// None of the process monitors indicate an error, so set this status to OK
			setStatus(MonitorStatus.OK);
		}
	}
	
	private void unregisterAllProcessMonitors() {
		logger.info("Unregistering all process monitors.");
		try
		{
			synchronized( monitoredProcesses )
			{
				for (java.util.ListIterator< ProcessMonitor > iter = monitoredProcesses.listIterator(); iter.hasNext();){
					ProcessMonitor processMonitor = iter.next();		
					processMonitor.stop();
					removeErrors(processMonitor.getName());
					iter.remove();
				}
			}
		} catch (Exception ex) {
			logger.error("Couldn't unregister all process monitors. Reason: ", ex);
		}
	}
	
	public void shutDown() {
		// Short circuit running through the list of monitors in the monitor() method
		forceShutDown = true;
		
		// Stop all the processes being monitored
		unregisterAllProcessMonitors();
		
		// Remove any errors
		removeAllErrors();
		
		// Stop monitoring
		stop();
		
		// Set that we have stopped the process group monitor.
		setStatus(MonitorStatus.STOPPED);

		// Use addError to bubble up a warning that we have stopped the monitor.
		// If we are shutting down, this doesn't matter... if for some reason we
		// aren't shutting down, this will alert the user via MC that the monitor
		// has been shut down erroneously.
		addError("Process group monitor has been shutdown.");
	}
}
