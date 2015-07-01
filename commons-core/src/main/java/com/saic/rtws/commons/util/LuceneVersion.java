package com.saic.rtws.commons.util;

import org.apache.lucene.util.Version;

/** Utility to help with versioning of Lucene. */
public final class LuceneVersion {
	
	/** Match settings and bugs in version of Lucene this system is using. */
	public static final Version LUCENE_CURRENT = Version.LUCENE_29;
	
	/** Constructor. */
	protected LuceneVersion() { }
	
}
