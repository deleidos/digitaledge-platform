package com.saic.rtws.commons.net.listener.common;

/**
 * Idle - Ready to broadcast event. Working - Broadcasting event is already
 * happening. Finished - Broadcast event completed. Error - Broadcast event
 * encountered an error.
 */
public enum BroadcastStatus {
	Idle, Working, Finished, Error;
}