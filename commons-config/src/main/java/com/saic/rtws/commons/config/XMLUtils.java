package com.saic.rtws.commons.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.exolab.castor.xml.Unmarshaller;

/**
 * XML helper utilities.
 *
 */
public final class XMLUtils {

    /**
     * Private constructor for XMLUtils.
     */
    protected XMLUtils() {
    }

	/**
	 * Unmarshals the given type of class from xml format. 
     * @param file File object
     * @param type Class-type for unmarshalling the file
     * @param <T> Generic class xml should be unmarshalled into
     * @throws Exception Exception could be thrown while reading and unmarshalling
     * @return Object loaded with xml
	 */
    public 
    static <T extends Object> T loadXML(final File file, final Class<T> type) throws Exception 
    {
		// if the file "cannot be found", then try to read it off the "classpath"
		if (file != null && !file.exists()) {
			return loadXML(file.getName(), type);
		}
		
		// read in file
		java.io.FileReader reader = null;
		try {
			reader = new java.io.FileReader(file);
			return type.cast(Unmarshaller.unmarshal(type, reader));
		} catch (NoClassDefFoundError e) {
        	throw new Exception(e);
        } finally {
			try {
				reader.close();
			} catch (Exception ignore) {
				// ignore
			}
		}
	}

    /**
     * Unmarshalls the given type of class from xml format.
     * @param file Path to xml file
     * @param type Class-type for unmarshalling the file
     * @param <T> Generic class xml should be unmarshalled into
     * @throws Exception Exception could be thrown while reading and unmarshalling
     * @return Object loaded with xml
     */
    @SuppressWarnings("unchecked")
    public
    static <T extends Object> T loadXML(final String file, final Class<T> type) throws Exception 
    {
        Reader reader = null;
        if (file != null) {
            try {
                reader = new InputStreamReader(PropertiesUtils.loadResource(file));
                return (T) Unmarshaller.unmarshal(type, reader);
            } catch (NoClassDefFoundError e) {
            	throw new Exception(e);
            } finally {
                try {
                    reader.close();
                } catch (Exception ignore) {
                    // Ignore.
                }
            }
        } else {
            throw new FileNotFoundException("null");
        }
    }

    /**
     * Unmarshalls the given type of calss from an XML string
     * 
     * @param xml xml document already loaded into a String
     * @param type Class-type of object created from unmarshalling 
     * @return Class-type object loaded from XML
     * @throws Exception
     */
	public
	static <T extends Object> T loadXmlString(final String xml, final Class<T> type) throws Exception {
		Reader reader = null;
		T returnValue = null;
		if (xml != null && xml.trim().length() > 0) {
			try {
				reader = new StringReader(xml);
				returnValue = type.cast(Unmarshaller.unmarshal(type, reader));
			} catch (NoClassDefFoundError e) {
            	throw new Exception(e);
            } finally {
				try {
					reader.close();
				}
				catch (Exception ignore) {}
			}
		}
		return returnValue;
	}
}
