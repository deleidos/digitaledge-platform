package com.saic.rtws.commons.net.listener.common;

import java.util.concurrent.atomic.AtomicBoolean;

public class CommandListenerEnv {
	
	private AtomicBoolean isShuttingDown = new AtomicBoolean(false);

	private static class CommandListenerEnvSingletonHolder { 
		static final CommandListenerEnv instance = new CommandListenerEnv();
	}

	public static CommandListenerEnv getInstance() {
		return CommandListenerEnvSingletonHolder.instance;
	}
	
	private CommandListenerEnv() {
		// Not instantiable
	}
	
	public boolean isShuttingDown() {
		return this.isShuttingDown.get();
	}
	
	public void setToShuttingDown() {
		this.isShuttingDown.set(true);
	}
	
}