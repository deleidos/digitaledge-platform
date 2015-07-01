package com.saic.rtws.commons.dao.type.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.saic.rtws.commons.dao.exception.DataFormatException;

import net.sf.json.JSONObject;

public class DateTimeHandler extends JsonTypeHandler<Date> {

	public DateTimeHandler() {
		super("DATETIME", Date.class);
	}
	
	public Date get(JSONObject object, String field, String format) {
		try {
			String value = object.optString(field);
			if(value == null) {
				return null;
			} else {
				return new SimpleDateFormat(format).parse(value);
			}
		} catch(ParseException e) {
			throw new DataFormatException(e);
		}
	}
	
	public void set(JSONObject object, String field, Date value, String format) {
		if(value != null) { 
			object.put(field, new SimpleDateFormat(format).format(value));
		}
	}
	
}
