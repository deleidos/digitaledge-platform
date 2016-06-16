package com.deleidos.rtws.ext.parser;

import java.util.Map;

import com.deleidos.rtws.core.framework.translator.AbstractConfigurableTranslator;

public class NoOpTranslator extends AbstractConfigurableTranslator {

	@Override
	public String customFieldTranslator(String outputFieldPath, String outputFieldKey, Map<String, String> recordMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
