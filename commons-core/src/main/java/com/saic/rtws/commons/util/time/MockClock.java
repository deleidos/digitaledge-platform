package com.saic.rtws.commons.util.time;

/**
 * Mock clock utility.
 */
public class MockClock implements Clock {

	/** Time. */
	private long mockTime;
	
	/** Constructor. */
	public MockClock() {
		super();
	}
	
	/** 
	 * Constructor.
	 * @param time Time
	 */
	public MockClock(final long time) {
		super();
		this.mockTime = time;
	}
	
	/**
	 * Return the time.
	 * @return long
	 */
	public synchronized long time() {
		return mockTime;
	}
	
	/** 
	 * Update the time.
	 * @param time The time
	 */
	public synchronized void update(final long time) {
		this.mockTime = time;
	}
	
	/**
	 * Mock sleeping the thread.
	 * @param wait Time to wait
	 */
	public synchronized void sleep(final long wait) {
		update(mockTime + wait);
	}
	
}
