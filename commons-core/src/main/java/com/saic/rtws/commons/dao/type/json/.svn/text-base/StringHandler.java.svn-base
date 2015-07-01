package com.saic.rtws.commons.dao.type.json;

import net.sf.json.JSONObject;

public class StringHandler extends JsonTypeHandler<String> {

	public StringHandler() {
		super("STRING", String.class);
	}
	
	public String get(JSONObject object, String field, String format) {
		return object.optString(field);
	}
	
	public void set(JSONObject object, String field, String value, String format) {
		if(value != null) {
			object.put(field, value);
		}
	}
	
}
