import com.deleidos.rtws.commons.util.scripting.ScriptInfo
import com.deleidos.rtws.commons.util.scripting.ScriptFile
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ClassHelper
import java.lang.reflect.Modifier
import java.util.ArrayList

/**
*
* This script will feed into which are listed in the ScriptFile array.  It will read the annontations in the scripts and
* create ScriptInfo objects.  The ScriptInfo objects will be stored in a list and returned to the ScriptManager.  The script 
* will find all the methods and then will filter out the methods that are a part of every Groovy class. First,  the Synthetic
* methods will be filtered out. These are methods created by the Groovy compiler and can only be called the Groovy Virutal 
* Machine.  The "main: and "run" methods are part of every Groovy class and are not to be invoked from the translator.  The 
* script signature and description will be loaded into the ScriptInfo object. Unfortunately, Groovy puts all annotations in
* AnnotationNode objects. This makes it necessary to reconstruct an object to send it back to the ScriptManager. The signature
* and description strings are removed from the AnnotationNode's member map and put back into the SCriptInfo object.    
*
*/


def getScriptMethodInfo(ScriptFile[] scriptFiles) {
	
	GroovyShell shell = new GroovyShell();
	List<ScriptInfo> scriptInfoList = new ArrayList<ScriptInfo>();	
			

	for(ScriptFile scriptFile : scriptFiles) {
		
		File file = new File(scriptFile.scriptDirectory + File.separator + scriptFile.scriptName)
		Script script = shell.parse(file);
		ClassNode cnode = ClassHelper.make(script.class);
		
		cnode.methods.
      			findAll{!Modifier.isSynthetic(it.modifiers)}.  // Filter out synthetic methods.
      		each {
			// Filter out the main and run methods.
			if ((it.name != "main") && (it.name != "run")) {
				if (it.annotations != []) {
					for(AnnotationNode annotation : it.annotations) {
						// Check to see if this is an actual ScriptSignature annotation
						
						if (annotation.members.containsKey("signature")) {
							ScriptInfo scriptInfo = new ScriptInfo();
							scriptInfo.signature = annotation.members.get("signature").getValue();
							if (annotation.members.containsKey("description")) {
								scriptInfo.description = annotation.members.get("description").getValue();
							}
							scriptInfoList.add(scriptInfo);
							
						} 
					}
				}
			}
		} 

	} 
	
	
	return scriptInfoList;

}