package com.saic.rtws.commons.util.fileset;

/**
 * Represents the identity of a fileset, consisting of a name, a version, and a file extension.
 */
public class FileSetIdWithExtension extends FileSetId {
	
	private String extension;

	/**
	 * Construct a FileSetIdWithExtension using the string form of the version.
	 */
	public FileSetIdWithExtension(String name, String version, String extension) {
		super(name, version);
		this.extension = extension;
	}

	/**
	 * Construct a FileSetIdWithExtension using the numeric form of the version.
	 */
	public FileSetIdWithExtension(String name, int major, int minor, String extension) {
		super(name, major, minor);
		this.extension = extension;
	}

	/** Return the name */
	public String getExtension() {
		return this.extension;
	}
	
	
	/** Return name_v<maj>.<min>.<extension> */
	public String toString() {
		return super.toString() + "." + this.extension;
	}

	/** Compare two FileSetIdWithExtensions, first by name, then by version number, and lastly by the extension */
	public int compareTo(FileSetIdWithExtension o) {
		int result = super.compareTo(o);
		if (result == 0) {
			result = this.extension.compareTo(o.extension);
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
