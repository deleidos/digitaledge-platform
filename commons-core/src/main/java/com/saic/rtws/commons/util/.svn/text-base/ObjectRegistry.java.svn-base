package com.saic.rtws.commons.util;

import java.util.HashMap;

/**
 * Simple static registry of named objects.
 */
public class ObjectRegistry {
	
	private static HashMap<String, Object> map;
	
	public static void put(String name, Object obj) {
		map.put(name, obj);
	}
	
	public static Object get(String name) {
		return map.get(name);
	}
	
	public static <T> T get(String name, Class<T> type) {
		return type.cast(map.get(name));
	}
	
	static {
		map = new HashMap<String, Object>();
	}
}
