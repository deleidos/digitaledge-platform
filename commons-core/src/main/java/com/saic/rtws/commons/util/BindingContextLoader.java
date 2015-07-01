package com.saic.rtws.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import com.saic.rtws.commons.config.PropertiesUtils;

public class BindingContextLoader {

	/**
	 * Loads the specified binding context file. This file contains a list of classes that are needed to initialize a
	 * JAXB binding context capable of reading the XML configuration file.
	 */
	public static  Class<?>[] loadBindingContext(String bindingFile) throws IOException {

		Class<?>[] configClasses = null;
		InputStream stream = null;
		String line = null;

		try {

			// Open the binding context file.
			stream = PropertiesUtils.loadResource(bindingFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			LinkedList<Class<?>> list = new LinkedList<Class<?>>();

			// Read the list of class names.
			while ((line = reader.readLine()) != null) {
				if (!line.matches("\\s*")) {
					list.add(Class.forName(line));
				}
			}

			// Load the class objects.
			configClasses = list.toArray(new Class<?>[list.size()]);

		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			try {stream.close();} catch (Exception ignore) { }
		}
		return configClasses;
	}

}
