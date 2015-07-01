package com.saic.rtws.commons.util.fileset;

import java.util.regex.Pattern;

/**
 * Represents the identity of a fileset, consisting of a name and a version.
 */
public class FileSetId implements Comparable<FileSetId> {
	
	public static final String VERSION = "^(\\d+\\.\\d+)$";
	public static final Pattern VERSION_PATTERN = Pattern.compile(VERSION);
	
	private String  name;
	private Version version;

	/**
	 * Construct a FileSetId using the string form of the version.
	 */
	public FileSetId(String name, String version) {
		this.name = name;
		this.version = new Version(version);
	}

	/**
	 * Construct a FileSetId using the numeric form of the version.
	 */
	public FileSetId(String name, int major, int minor) {
		this.name = name;
		this.version = new Version(major, minor);
	}

	/** Return the name */
	public String getName() {
		return name;
	}
	
	/** Return the version */
	public Version getVersion() {
		return version;
	}
	
	/** Return name_<maj>.v<min> */
	public String toString() {
		return name + "_v" + version;
	}

	/** Compare two FileSetIds, first by name, then by version number */
	public int compareTo(FileSetId o) {
		int result = this.name.compareTo(o.name);
		if (result == 0) {
			result = this.version.compareTo(o.version);
		}
		return result;
	}
	
	/**
	 * Class represents a major and minor version number
	 */
	public class Version implements Comparable<Version> {
		private int    major;
		private int    minor;
		private String versionString;

		/**
		 * Version as major and minor numbers
		 */
		public Version(int major, int minor) {
			this.major = major;
			this.minor = minor;
			this.versionString = major + "." + minor;
		}
		
		/**
		 * Version in the form of a string: <major>.<minor>
		 * Example: "1.0"
		 */
		public Version(String s) {
			String parts[] = s.split("\\.");
			this.major = Integer.parseInt(parts[0]);
			this.minor = Integer.parseInt(parts[1]);
			this.versionString = s;
		}

		/** Return <major>.<minor> as a string */
		public String toString() {
			return versionString;
		}
		
		/** Return major version number */
		public int getMajor() {
			return major;
		}
		
		/** Return minor version number */
		public int getMinor() {
			return minor;
		}

		/** Comparing of version numbers */
		public int compareTo(Version o) {
			int result = Integer.signum(this.major - o.major);
			if (result == 0) {
				result = Integer.signum(this.minor - o.minor);
			}
			return result;
		}
	}
}
