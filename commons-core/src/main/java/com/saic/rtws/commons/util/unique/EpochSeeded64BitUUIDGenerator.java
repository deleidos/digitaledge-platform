package com.saic.rtws.commons.util.unique;

/**
 * 64-bit UUID generator seeded with current epoch.
 */
public class EpochSeeded64BitUUIDGenerator {
	
	/** Seed epoch. 32-bit first-half of 64-bit UUID. */
	long epochMsb = 0L;
	/** Incrementer. 32-bit second-half of 64-bit UUID. */
	int incrementer = 0;
	
	/** Constructor. */
	protected EpochSeeded64BitUUIDGenerator() {
		// get java supplied epoch as 64-bit signed long
		Long epochL = java.util.Calendar.getInstance().getTime().getTime() / 1000;
		// convert to int
		epochMsb = epochL.intValue() << 32;
	}
	
    /**
     * Holder class is loaded on the first execution of singleton getInstance() 
     * or the first access to holder class, not before.
     */
	protected static final class EpochSeeded64BitUUIDGeneratorHolder {
		/** Singleton instance. */
		protected static final EpochSeeded64BitUUIDGenerator INSTANCE = new EpochSeeded64BitUUIDGenerator();
	    
	    /** Private constructor. */
		protected EpochSeeded64BitUUIDGeneratorHolder() {
	    }
	}
	
	/**
	 * Return a thread-safe singleton instance of class.
	 * @return Returns singleton instance of class
	 */
	public static EpochSeeded64BitUUIDGenerator getInstance() {
		return EpochSeeded64BitUUIDGeneratorHolder.INSTANCE;
	}
	
	/**
	 * Get the next 64-bit UUID.
	 * @return Return the UUID
	 */
	public long getNextUUID() {
		incrementer++;
		return epochMsb + incrementer;
	}
}
