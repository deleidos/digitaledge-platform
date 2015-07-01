package com.saic.rtws.commons.util;

/**
 * Computes various names based on a data model and data model version.
 */
public class DataModelBasedNames {
	
	public static final String MODEL_ROOT = "models";
	public static final String CANONICAL  = "canonical";
	public static final String ENRICHED   = "enriched";
	public static final String TRANSLATE  = "xlate";
	
	private String          modelName;
	private int             modelMajorVersion;
	private int             modelMinorVersion;
	
	public DataModelBasedNames(String modelName, int modelMajorVersion, int modelMinorVersion) {
		this.modelName = modelName;
		this.modelMajorVersion = modelMajorVersion;
		this.modelMinorVersion = modelMinorVersion;
	}
	
	public DataModelBasedNames(String modelName, String modelVersionString) {
		this.modelName = modelName;
		String parts[] = modelVersionString.split("\\.");
		this.modelMajorVersion = Integer.parseInt(parts[0]);
		this.modelMinorVersion = Integer.parseInt(parts[1]);
	}

	/**
	 * Return the name of the data model.
	 */
	public String getModelName() {
		return this.modelName;
	}
	
	/**
	 * Return the model version string <major>.<minor>
	 */
	public String getModelVersionString() {
		return modelMajorVersion + "." + modelMinorVersion;
	}
	
	/**
	 * Return the data model major version number.
	 */
	public int getMajorVersion() {
		return this.modelMajorVersion;
	}

	/**
	 * Return the data model minor version number.
	 */
	public int getMinorVersion() {
		return this.modelMinorVersion;
	}
	
	/**
	 * Returns <modelName>_v<majorVersion>.<minorVersion>
	 */
	public String getFullModelName() {
		return modelName + "_v" + modelMajorVersion + "." + modelMinorVersion;
	}

	/**
	 * Returns the model zip filename
	 */
	public String getFullModeZipFilename() {
		return getFullModelName() + ".zip";
	}
	
	/**
	 * Returns a translation directives filename given an input format name.
	 */
	public String getTranslationFilename(String inputFormatName) {
		return TRANSLATE + "_" + inputFormatName + "_to_" + getFullModelName() + ".json";
	}
	
	/**
	 * Returns the canonical format filename.
	 */
	public String getCanonicalFilename() {
		return CANONICAL + "_" + getFullModelName() + ".json";
	}

	/**
	 * Returns the enriched format filename.
	 */
	public String getEnrichedFilename() {
		return ENRICHED + "_" + getFullModelName() + ".json";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + modelMajorVersion;
		result = prime * result + modelMinorVersion;
		result = prime * result + ((modelName == null) ? 0 : modelName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DataModelBasedNames other = (DataModelBasedNames) obj;
		if (modelMajorVersion != other.modelMajorVersion) return false;
		if (modelMinorVersion != other.modelMinorVersion) return false;
		if (modelName == null) {
			if (other.modelName != null) return false;
		} else if (!modelName.equals(other.modelName)) return false;
		return true;
	}
	
	
}
