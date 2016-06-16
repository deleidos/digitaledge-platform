package com.deleidos.rtws.systemcfg.beans;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DataModel extends UserAddressableImpl {
	private DataModelVisibility visibility;
	private String path;
	private String filename;
	private String version;
	
	public static enum DataModelVisibility {
		PUBLIC, PRIVATE;
	}
	
	public DataModelVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(DataModelVisibility visibility) {
		this.visibility = visibility;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if(obj != null && (obj instanceof DataModel) && super.equals(obj)) {
			DataModel that = (DataModel)obj;
			
			result = (this.visibility == that.visibility &&
					StringUtils.equals(this.path, that.path) &&
					StringUtils.equals(this.filename, that.filename) &&
					StringUtils.equals(this.version, that.version));
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		ArrayList<Object> fields = new ArrayList<Object>();
		fields.add(userLabel);
		fields.add(visibility);
		fields.add(path);
		fields.add(filename);
		fields.add(version);
		return Arrays.hashCode(fields.toArray());
	}
}
