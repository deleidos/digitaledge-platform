package com.saic.rtws.commons.util.scripting.util;

import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.util.scripting.ScriptFile;
import com.saic.rtws.commons.util.scripting.ScriptInfo;
import com.saic.rtws.commons.util.scripting.ScriptMethodInfo;

/**
 * These are additional functions for the ScriptEngine.
 * 
 * @author rainerr
 *
 */
public class ScriptEngineUtil {

	/** Logger. */
	private static final Logger log = Logger.getLogger(ScriptEngineUtil.class);
	
	/**
	 * This method will publish information about the scripts which are configured in this Script Engine.  The scripts must
	 * have a ScriptSignature annotation for each script function.  The ScriptSignature annotation is necessary because the 
	 * Groovy reflection will not return a good descriptive representation of the function.  Only the functions which are 
	 * decorated with this annotation will be returned in the list.
	 * 
	 * @param scriptEngine - The Script Engine which contains the scripts that contain the script information.
	 * 
	 * @return List<ScriptInfo> - A List containing the ScriptInfo objects which contain the signature of the scripts
	 * and a brief description. 
	 * 	 
	 */
	
	public List<ScriptInfo> getScriptInformation(Invocable inv, ScriptFile[] scriptFiles) {
		List<ScriptInfo> scriptInfoList = new ArrayList<ScriptInfo>();
		
		if (inv == null) {
			log.error("No Script Engine configured");
		}
		
		try {
			ScriptMethodInfo scriptMethodInfo = inv.getInterface(ScriptMethodInfo.class);
			if(scriptMethodInfo != null) {
				scriptInfoList = scriptMethodInfo.getScriptMethodInfo(scriptFiles);
			} 
		} catch (Exception ex) {
			log.error("Error Retrieving Script Information " + ex.getMessage());
		} 
		
		return scriptInfoList;
	} 
	
}
