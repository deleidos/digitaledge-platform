
package com.deleidos.rtws.ext.parser;

import java.util.Map;

public interface CustomFieldTranslator {
	/**
	 * Method called to perform a custom field translation.
	 * @param outputFieldPath path to the canonical output field being produced
	 * @param outputFieldKey key name to the canonical output field being produced
	 * @param recordMap map of input name/value pairs
	 * @return the custom string value to be added to the output
	 * @throws UnsupportedOperationException
	 */
	public String customFieldTranslator(String outputFieldPath, String outputFieldKey, Map<String, String> recordMap);
}
