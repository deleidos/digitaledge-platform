package com.saic.rtws.commons.util.scripting;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.saic.rtws.commons.util.Initializable;

/**
/*	Describes a wrapper class for the Script Engine Manager Factory.  The engine name variable specifies what type of Script
/*  Engine will be returned.  For example, to run a Groovy script from the translator, the engine name would be set to "groovy".
 *  Scripting Engines are available from the scripting engine web site.  The scripting engines can be downloaded as jar files
 *  and put on the class path so that it is available to the JVM.  This interface allows the Scripting Engines to be configured
 *  for use of the Ingest Framework.  
 *  
 *  The scripts array contains the list of scripts available to this ScriptEngine. The Scripts object contains the directory and
 *  the script name of the scripts to load into the script engine.  When a script operation is specified in the translator, the 
 *  ScriptEngine will search these files for the script to run.  	 
 */

@XmlJavaTypeAdapter(ScriptEngineFactory.TypeAdapter.class)
public interface ScriptEngineFactory extends Initializable {
	// ScriptEngine allows the JVM to run script fragments and interrogate script files.
	ScriptEngine createScriptEngine(String engineName);
	// Invocable allows the JVM to invoke a function or use a class in a script file. 
	Invocable createInvocable() throws ScriptException;
	//List<ScriptInfo> getScriptInformation(ScriptEngine scriptEngine, File directory) throws ScriptException;
	void setEngineName(String engineName);
	String getEngineName();
	void setScriptFiles(ScriptFile[] scriptFiles);
	ScriptFile[] getScriptFiles();
	
	static class TypeAdapter extends XmlAdapter<Object, ScriptEngineFactory> {
		public ScriptEngineFactory unmarshal(Object element) { return (ScriptEngineFactory)element; }
		public Object marshal(ScriptEngineFactory factory) { return factory; }
	}
}
