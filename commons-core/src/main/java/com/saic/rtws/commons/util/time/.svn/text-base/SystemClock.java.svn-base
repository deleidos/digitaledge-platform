package com.saic.rtws.commons.util.time;

/**
 * System clock.
 */
public class SystemClock implements Clock {

	/** Constructor. */
	public SystemClock() {
		super();
	}
	
	/** 
	 * Get the system time.
	 * @return Return long value with system time
	 */
	public long time() {
		return System.currentTimeMillis();
	}
	
	/**
	 * Sleep the thread.
	 * @param time Time to sleep (milliseconds)
	 */
	public void sleep(final long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// Ignore.
		}
	}
}
