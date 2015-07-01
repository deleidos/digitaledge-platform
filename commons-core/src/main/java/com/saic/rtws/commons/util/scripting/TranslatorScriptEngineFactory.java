package com.saic.rtws.commons.util.scripting;

import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;

/**
 * This class implements the ScriptEngineFactory and it allows the Ingest
 * Translator to access script methods. It supports any scripting language that
 * is supported by the JVM ScriptEngineManager. Be sure to download the
 * scripting engine from the Java Scripting Engine web site. See the Scripting
 * Engine interface for more information.
 * 
 * @author rainerr
 * 
 */

public class TranslatorScriptEngineFactory implements ScriptEngineFactory {

	private static final Logger log = Logger
			.getLogger(TranslatorScriptEngineFactory.class);

	private ScriptEngineManager scriptEngineManager;
	private String engineName;
	private ScriptFile[] scriptFiles;

	/**
	 * This method returns an object which can be used to invoke a method in a
	 * script file. This method will create a script engine for the specified
	 * engineName. It will then load the configured the scripts and return an
	 * object to call the methods in the scripts.
	 * 
	 */

	public Invocable createInvocable() throws ScriptException {
		ScriptEngine scriptEngine = scriptEngineManager
				.getEngineByName(engineName);
		loadScripts(scriptEngine);
		return (Invocable) scriptEngine;
	}

	/**
	 * 
	 * This method is used to create a Script Engine which will be used to run a
	 * script fragment. The script fragment can be a string and sent to the eval
	 * method of the Script Engine. The scripts can be stored as files and read
	 * into the eval method using a FileReader. You would not be able to call
	 * individual functions or use classes. For that you will need an Invocable
	 * object (see createInvocable). The ScriptEngine can also be used to
	 * interrogate scripts to see what functions and classes they contain.
	 * 
	 * @param engineName
	 * @return
	 */

	public ScriptEngine createScriptEngine(String engineName) {
		return scriptEngineManager.getEngineByName(engineName);
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	@NotNull
	public String getEngineName() {
		return engineName;
	}

	public void setScriptFiles(ScriptFile[] scriptFiles) {
		this.scriptFiles = scriptFiles;
	}

	@NotNull
	public ScriptFile[] getScriptFiles() {
		return scriptFiles;
	}

	public void initialize() {
		scriptEngineManager = new ScriptEngineManager();
	}

	public void dispose() {
		// No Implementation
	}

	/**
	 * This method will load the script files into the Script Engine. The script
	 * files to load are configured in the parsers.xml file.
	 * 
	 * @param scriptEngine
	 *            - The ScriptEngine to load the scripts into.
	 * 
	 */

	private void loadScripts(ScriptEngine scriptEngine) throws ScriptException {
		String scriptName = "";
		try {
			if (scriptFiles != null) {
				for (ScriptFile scriptFile : scriptFiles) {
					scriptName = scriptFile.getScriptName();
					FileReader fileReader = new FileReader(
							scriptFile.getScriptDirectory() + File.separator
									+ scriptName);
					scriptEngine.eval(fileReader);
				}
			} 
		} catch (ScriptException ex) {
			// now just warning for an unsupported or malformed scripts
			log.warn("Failed to load the script: " + scriptName + " Message:" + ex.getMessage());
		} catch (Exception ex) {
			// for other types of exceptions we still rethrow
			log.error("Failed to load the script: " + scriptName + " Message:" + ex.getMessage());
			throw new ScriptException(ex);
		}
	}
}
