package com.deleidos.rtws.webapp.contentapi.dao;

import com.deleidos.rtws.commons.util.unique.EpochSeeded64BitUUIDGenerator;

/**
 * 64-bit UUID generator seeded with current epoch.
 */
public class SimpleContentIdGenerator extends EpochSeeded64BitUUIDGenerator {
	
	/** Constructor. */
	protected SimpleContentIdGenerator() { }
	
    /**
     * Holder class is loaded on the first execution of singleton getInstance() 
     * or the first access to holder class, not before.
     */
	protected static final class SimpleContentIdGeneratorHolder {
		/** Singleton instance. */
		protected static final SimpleContentIdGenerator INSTANCE = new SimpleContentIdGenerator();
	    
	    /** Private constructor. */
		protected SimpleContentIdGeneratorHolder() {
	    }
	}
	
	/**
	 * Return a thread-safe singleton instance of class.
	 * @return Returns singleton instance of class
	 */
	public static SimpleContentIdGenerator getInstance() {
		return SimpleContentIdGeneratorHolder.INSTANCE;
	}
	
}
