package com.saic.rtws.commons.util.scripting;

import java.util.List;
import com.saic.rtws.commons.util.scripting.ScriptFile;

/**
 * This interface describes the ScriptMethodInfo type. An object of this type will provide methods to obtain information
 * about available scripts. Different scripting engines can implement this interface in different ways.  The Groovy scripting
 * engine implements this interface as a script using the Invocation.getInterface method.    
 * 
 * @author rainerr
 *
 */

public interface ScriptMethodInfo {
	List<ScriptInfo> getScriptMethodInfo(ScriptFile[] scriptFiles);
}
