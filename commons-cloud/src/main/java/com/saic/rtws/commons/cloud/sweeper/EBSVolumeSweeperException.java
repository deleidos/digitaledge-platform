package com.saic.rtws.commons.cloud.sweeper;

/**
 * Exception for the ebs volume sweeper application.
 */
public class EBSVolumeSweeperException extends Exception {
	
	private static final long serialVersionUID = 2916210950556711162L;

	public EBSVolumeSweeperException(String message) {
		super(message);
	}
	
	public EBSVolumeSweeperException(String message, Throwable th) {
		super(message, th);
	}

}
