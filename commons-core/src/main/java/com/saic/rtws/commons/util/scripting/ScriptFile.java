package com.saic.rtws.commons.util.scripting;

import javax.validation.constraints.NotNull;

/**
 * 
 * This class holds information about a script file.  It contains a directory variable and the name of the 
 * script file.
 * 
 * @author rainerr
 *
 */


public class ScriptFile {
	
	private String scriptDirectory;
	private String scriptName;
	
	public void setScriptDirectory(String scriptDirectory) {
		this.scriptDirectory = scriptDirectory;
	}
	
	@NotNull
	public String getScriptDirectory() {
		return scriptDirectory;
	}
	
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	
	@NotNull
	public String getScriptName() {
		return scriptName;
	}
}
