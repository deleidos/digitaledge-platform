package com.saic.rtws.commons.dao.type.json;

import com.saic.rtws.commons.dao.type.TypeHandlerFactory;

public class JsonTypeHandlerFactory extends TypeHandlerFactory {

	public JsonTypeHandlerFactory() {
		super();
		registerHandler(JsonTypeHandler.STRING);
		registerHandler(JsonTypeHandler.DATETIME);
		registerHandler(JsonTypeHandler.NUMBER);
	}
	
}
