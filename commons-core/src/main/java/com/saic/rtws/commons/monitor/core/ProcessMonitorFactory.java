package com.saic.rtws.commons.monitor.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.monitor.process.UnknownProcessMonitor;

public final class ProcessMonitorFactory {
	
	private static final Logger logger = Logger.getLogger(ProcessMonitorFactory.class);
	
	public static ProcessMonitor createProcessMonitor(String className) {
		className = className.trim();

		try {
			Class<?> monitorClass = Class.forName(className);
			
			// ProcessMonitors must have a name, so make sure to use the constructor that only accepts a String argument 
			return (ProcessMonitor) monitorClass.getConstructor(String.class).newInstance(monitorClass.getSimpleName());
		} catch (ClassNotFoundException cnfe) {
			logger.debug("Did not find process monitor '" + className + "' on system class path. Checking other known DigitalEdge libraries.");
			
			int retryCount = 12;
			int retryWait = 15000;	// 15 seconds
			for(int i = 1; i <= retryCount; i++) {
				
				ArrayList<URL> jarFiles = getJarUrls();
				
				try {
					URL[] rtwsUrl = new URL[jarFiles.size()];
					jarFiles.toArray(rtwsUrl);
					URLClassLoader rtwsClassLoader = new URLClassLoader(rtwsUrl);
					System.out.println();
					Class<?> monitorClass = Class.forName(className, true, rtwsClassLoader);
	
					return (ProcessMonitor) monitorClass.getConstructor(String.class).newInstance(monitorClass.getSimpleName());
				} catch (Exception e) {
					logger.debug("Attempt " + i + ": Did not find process monitor '" + className + "' in known DigitalEdge libraries. " +
								 "Library might not yet be populated. Checking again in " + (retryWait/1000) + " seconds...");
					retryCount--;
				}
				
				try {
					Thread.sleep(retryWait);
				} catch (InterruptedException ignore) { }
			}
			
			logger.warn("Did not find process monitor '" + className + "' in any known DigitalEdge libraries.");
			return new UnknownProcessMonitor(className);			
		} catch (Exception e) {
			logger.warn("Failed to load process monitor '" + className + "'.", e);
			return new UnknownProcessMonitor(className + ": " + e.getMessage());
		}
	}
	
	private static ArrayList<URL> getJarUrls() {
		ArrayList<URL> jars = new ArrayList<URL>();
		
		// Look where libraries containing monitors might be
		String[] libPaths = new String[] {
			RtwsConfig.getInstance().getString("master.path.home", "/usr/local/rtws/master") + "/lib/",
			RtwsConfig.getInstance().getString("transport.path.home", "/usr/local/rtws/transport")  + "/lib/",
			RtwsConfig.getInstance().getString("ingest.path.home", "/usr/local/rtws/ingest") + "/lib/" };
		
		for(int libIndex = 0; libIndex < libPaths.length; libIndex++) {
			try {
				File libFolder = new File(libPaths[libIndex]);
				logger.debug("Found DigitalEdge library: " + libPaths[libIndex]);
				
				for( final File file : libFolder.listFiles() ) {
					if(file.isFile() && file.getName().endsWith(".jar")) {
						try {
							jars.add(file.toURI().toURL());
						} catch (MalformedURLException ignore) { }
					}
				}
			} catch (NullPointerException npe) {
				// Library does not exist on this node, just skip it
				logger.debug("Did not find library: " + libPaths[libIndex]);
				continue;
			}
		}
		
		return jars;
	}
}
