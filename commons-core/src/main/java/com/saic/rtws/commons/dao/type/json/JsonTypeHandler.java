package com.saic.rtws.commons.dao.type.json;

import java.util.Date;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.dao.type.AbstractTypeHandler;

public abstract class JsonTypeHandler<T> extends AbstractTypeHandler<T> {

	public static final JsonTypeHandler<String> STRING = new StringHandler();
	public static final JsonTypeHandler<Date> DATETIME = new DateTimeHandler();
	public static final JsonTypeHandler<Number> NUMBER = new NumberHandler();
	
	protected JsonTypeHandler(String name, Class<T> type) {
		super(name, type);
	}
	
	public final T get(Object object, String field, String format) {
		if (object == null) {
			throw new NullPointerException();
		} else if (object instanceof JSONObject) {
			return get((JSONObject) object, field, format);
		} else {
			throw new IllegalArgumentException("Incompatible type '" + object.getClass() + "'.");
		}
	}

	public final void set(Object object, String field, T value, String format) {
		if (object == null) {
			throw new NullPointerException();
		} else if (object instanceof JSONObject) {
			set((JSONObject) object, field, value, format);
		} else {
			throw new IllegalArgumentException("Incompatible type '" + object.getClass() + "'.");
		}
	}

	public final T get(Object object, int field, String format) {
		throw new UnsupportedOperationException();
	}

	public final void set(Object object, int field, T value, String format) {
		throw new UnsupportedOperationException();
	}

	public abstract T get(JSONObject object, String field, String format);
	
	public abstract void set(JSONObject object, String field, T value, String format);
	
	
}
