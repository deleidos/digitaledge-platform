package com.saic.rtws.commons.net.listener.executor;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.saic.rtws.commons.net.listener.common.BroadcastStatus;

public class BroadcastStatusMap {
	
	private HashMap<String, AtomicReference<BroadcastStatus>> holder = 
		new HashMap<String, AtomicReference<BroadcastStatus>>();
	
	private String commandInProgress = null;
	
	/**
	 * Maps a command to a broadcast status.
	 */
	public void registerCommand(String command) {
		holder.put(command, new AtomicReference<BroadcastStatus>(BroadcastStatus.Idle));
	}
	
	/**
	 * @return the broadcast status of the given command
	 */
	public BroadcastStatus getStatus(String command) {
		if (holder.containsKey(command)) {
			return holder.get(command).get();
		}
		
		return null;
	}
	
	/**
	 * Sets the broadcast status for the given command. If the status
	 * is changing from idle to working we'll mark the command as in
	 * progress. If the status is changing from something else to
	 * idle we'll mark it as not in progress. All else will simply
	 * update the status for the given command.
	 * 
	 * @return true if command found and status changed, else false
	 */
	public boolean setStatus(String command, BroadcastStatus newValue) {
		if (isAnyCommandInProgress() && ! isCommandInProgress(command)) {
			return false;
		}
		
		if (holder.containsKey(command)) {
			BroadcastStatus prev = holder.get(command).getAndSet(newValue);
			if (prev == BroadcastStatus.Idle && newValue == BroadcastStatus.Working) {
				// Changing the status from idle to working means that
				// this command allowed and is in progress.
				
				this.commandInProgress = command;
			} else if (prev != newValue && newValue == BroadcastStatus.Idle) {
				// Changing the status from something else to idle means that this
				// command has been completed so reset the working command variable
				// state.
				
				this.commandInProgress = null;
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return true if a command is in progress, else false
	 */
	public boolean isAnyCommandInProgress() {
		return this.commandInProgress != null;
	}
	
	/**
	 * @return true if the given command is in progress, else false
	 */
	public boolean isCommandInProgress(String command) {
		return this.commandInProgress != null && this.commandInProgress.equals(command);
	}
	
}