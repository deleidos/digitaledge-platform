package com.saic.rtws.commons.monitor.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.saic.rtws.commons.management.ManagedBean;

public abstract class ManagedMonitor implements ManagedMonitorMXBean, ManagedBean, Runnable {
	
	/** The allowable statuses for a Monitor */
	public enum MonitorStatus {
		OK("OK"), ERROR("ERROR"), UNKNOWN("ERROR"), STARTING("STARTING"), STOPPED("STOPPED");
		
		private String value;
		
		/**
		 * Instantiates a new MonitorStatus.
		 *
		 * @param string the string to create the MonitorStatus from
		 */
		private MonitorStatus(String string) {
			this.value = string;
		}
		
		/**
		 * Creates a MonitorStatus from a string. If no matching
		 * MonitorStatus is found, null is returned.
		 *
		 * @param asString the string value to create a MonitorStatus from
		 * @return a MonitorStatus or null 
		 */
		public static MonitorStatus fromString(String asString) {
			for(MonitorStatus status : values()) {
				if(status.value.equals(asString)) {
					return status;
				}
			}
			return null;
		}
	}
	
	/** The name for the monitor. All monitors must have names */
	private String name = "";
	
	/** If the monitor is running */
	private boolean running = false;
	
	/** A thread executor used to run the monitor in it's own thread */
	private ExecutorService launcher = Executors.newSingleThreadExecutor();
	
	/** The current status of the monitor. Starts as MonitorStatus.UNKNOWN */
	private MonitorStatus status = MonitorStatus.UNKNOWN;
	
	/**
	 * A map of errors for all the item's being monitoring.  The key is a
	 * unique name for the monitored item and the value is an array of error
	 * messages associated with the item. 
	 **/
	private HashMap<String, ArrayList<String>> errorMessages = new HashMap<String, ArrayList<String>>();
	
	/** Interval between performing monitoring sessions. Default of 10 seconds */
	private int interval = 1000 * 10;
	
	/**
	 * A period of time before the monitor will start monitoring. This allows whatever process 
	 * is being monitored to start up.  During this period, the monitor will return a starting
	 * status.
	 **/
	private int startupPeriod = 1000 * 60 * 3;  // Default startup is 3 minutes
	
	/** The actual time the monitoring will start*/
	private Date startTime = new Date(System.currentTimeMillis() + startupPeriod);
	
	/** Allows for a way to lock the status of a monitor */
	private boolean stateLocked = false;
	
	/**
	 * Gets the monitor interval.
	 *
	 * @return the monitor interval
	 */
	public int getMonitorInterval() {
		return interval;
	}

	/**
	 * Sets the monitor interval.
	 *
	 * @param monitorInterval the new monitor interval
	 */
	public void setMonitorInterval(int monitorInterval) {
		this.interval = monitorInterval;
	}
	
	/**
	 * Gets the number of milliseconds to wait before monitoring begins
	 *
	 * @return the number of milliseconds to wait before monitoring begins
	 */
	public int getStartupPeriod() {
		return startupPeriod;
	}
	
	/**
	 * Sets the number of milliseconds to wait before monitoring begins
	 *
	 * @param startupPeriod the number of milliseconds to wait before monitoring begins
	 */
	public void setStartupPeriod(int startupPeriod) {
		this.startupPeriod = startupPeriod;
	}

	/**
	 * Instantiates a new ManagedMonitor with an immutable name.
	 *
	 * @param name the immutable name
	 */
	public ManagedMonitor(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see com.saic.rtws.commons.monitor.ManagedMonitorMXBean#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see com.saic.rtws.commons.monitor.ManagedMonitorMXBean#getStatus()
	 */
	public MonitorStatus getStatus() {
		return status;
	}
	
	/**
	 * Start the monitor if it is not already running.
	 */
	public void start() {
		if(!running) {
			setStatus(MonitorStatus.STARTING);
			running = true;
			
			startTime = new Date(System.currentTimeMillis() + startupPeriod);
			
			removeAllErrors();
			
			try {
				launcher.execute(this);
			} catch (Exception e) {
				stop(String.format("The monitor has failed to start: %s", e.getMessage()));
			}
		}
	}
	
	/**
	 * Stop the monitor if it is currently running.
	 */
	public void stop() {
		stop("");
	}
	
	/**
	 * Stop the monitor with a specific error message.
	 * @param error the error message to stop the monitor with
	 */
	protected void stop(String error) {
		if(running) {
			setStatus(MonitorStatus.STOPPED);
			running = false;
			
			unlockState();		// Unlock the state if it had been locked
			
			launcher.shutdownNow();
			
			removeAllErrors();
			if(!error.isEmpty()) {
				addError(error);
			}
		}
	}
	
	/**
	 * Checks if the monitor is running.
	 *
	 * @return true, if monitor is running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Locks the monitor from changing its state.
	 */
	protected void lockState() {
		stateLocked = true;
	}
	
	/**
	 * Allows the monitor to be in a state where it
	 * can be modified.
	 */
	protected void unlockState() {
		stateLocked = false;
	}
	
	/**
	 * Checks if the monitor is in a locked state.  If the monitor is
	 * in a locked state, than it will not perform monitoring nor allows
	 * external changes to it's state.
	 *
	 * @return true, if monitor is in a locked state
	 */
	public boolean isLocked() {
		return stateLocked;
	}
	
	/**
	 * Sets the monitor status.  If the status is set to
	 * MonitorStatus.OK, the error messages for the monitor
	 * will be cleared since the monitor is no longer in
	 * an erroneous state.
	 *
	 * @param newStatus the new monitor status
	 */
	protected void setStatus(MonitorStatus newStatus) {
		if(!stateLocked) {
			status = newStatus;
			if(status == MonitorStatus.OK) {
				errorMessages.clear();
			}
		}
	}
	
	/**
	 * Adds an error with the name of this monitor as the key.
	 *
	 * @param message the message
	 */
	protected void addError(String message) {
		// Add the error with this as the key
		addError(name, message);
	}
	
	/**
	 * Adds the error for the specified key.  Since an error is being added,
	 * the monitor must be in an erroneous state so the status is set to
	 * MonitorStatus.ERROR.
	 *
	 * @param key the key of a monitored item
	 * @param message the error message to add
	 */
	protected void addError(String key, String message) {
		if(!stateLocked) {
			// Make sure we have an error status
			setStatus(MonitorStatus.ERROR);
			
			// Create an entry for the errors if one doesn't exist
			if(!errorMessages.containsKey(key)) {
				errorMessages.put(key, new ArrayList<String>());
			}
		
			// Only add the error if we don't already have it recorded
			if(!errorMessages.get(key).contains(message)) {
				errorMessages.get(key).add(message);
			}
		}
	}

	/**
	 * Adds the errors for the specified key.  Will result in the status being
	 * set to MonitorStatus.ERROR.
	 *
	 * @param key the key of a monitored item
	 * @param messages the error messages to add
	 */
	protected void addErrors(String key, String[] messages) {
		for(String error : messages) {
			addError(key, error);
		}			
	}
	
	/**
	 * Removes the errors for the associated key.
	 *
	 * @param key the key of a monitored item
	 */
	protected void removeErrors(String key) {
		if(!stateLocked) {
			errorMessages.remove(key);
		}
	}
	
	protected void removeAllErrors() {
		if(!stateLocked) {
			errorMessages.clear();
		}
	}
	
	/**
	 * Returns all errors contained 
	 */
	public String[] getErrorMessages() {
		// Create a static copy of the error messages in case errors are added/removed while the 
		// error messages are being placed into a String[] to avoid going out of array bounds.
		Set<Entry<String, ArrayList<String>>> staticErrorsCopy = errorMessages.entrySet();
		ArrayList<String> returnErrors = new ArrayList<String>();
		Iterator<Entry<String, ArrayList<String>>> it = staticErrorsCopy.iterator();
		
		while(it.hasNext()) {
			Entry<String, ArrayList<String>> errorMessageEntry = it.next();
			String key = errorMessageEntry.getKey();
			ArrayList<String> entryErrorMessages = errorMessageEntry.getValue();
			
			for(int i = 0; i < entryErrorMessages.size(); i++) {
				String errorMessage = entryErrorMessages.get(i);
				
				// If the key is empty or the key is to this monitor, do not
				// append it's name to the front of the error message, otherwise
				// append the name of the monitored object to the message so that
				// it can be identified where that error is coming from.
				if(!key.isEmpty() && !key.equals(name)) {
					errorMessage = String.format("[%s]: %s", key, errorMessage);
				}
				returnErrors.add(errorMessage);
			}
		}
		
		return returnErrors.toArray(new String[returnErrors.size()]);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// Only run if we set the running state: prevents the thread being run by external means(i.e. ExecutorService, etc)
		if(running) {
			doRun();
		}
	}
	
	private void doRun() {		
		// Wait until the actual startTime to run
		Date currentTime;
		while( (currentTime = new Date(System.currentTimeMillis())).before(startTime) ) {
			try {
				Thread.sleep(startTime.getTime() - currentTime.getTime());
			} catch (InterruptedException ignore) { }
		}
		
		while(running) {
			if(!stateLocked) {
				// Clear the last monitoring session's exceptions and errors, if they happen again,
				// they will be reported again, otherwise they are assumed resolved
				removeAllErrors();
				try {
					monitor();
				} catch (Exception e) {
					// We've encountered an uncaught exception while monitoring, report it as an error
					addError(e.getMessage());
				}
			}
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException doNothing) { }
		}		
	}
	
	/**
	 * Perform the task of monitoring.
	 */
	protected abstract void monitor();
	
}
