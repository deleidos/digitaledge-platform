package com.saic.rtws.commons.dao.type.json;

import net.sf.json.JSONObject;

public class NumberHandler extends JsonTypeHandler<Number> {

	public NumberHandler() {
		super("NUMBER", Number.class);
	}
	
	public Number get(JSONObject object, String field, String format) {
		return object.optDouble(field);
	}
	
	public void set(JSONObject object, String field, Number value, String format) {
		if(value != null) {
			object.put(field, value);
		}
	}
	
}
