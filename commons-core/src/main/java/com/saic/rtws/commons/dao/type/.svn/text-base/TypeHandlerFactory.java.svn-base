package com.saic.rtws.commons.dao.type;

import java.util.HashMap;
import java.util.Map;


/**
 * Factory used to get instances of TypeHandlers by name.
 */
public class TypeHandlerFactory {

	/** The list of managed objects. */
	private Map<String, TypeHandler<?>> cache = new HashMap<String, TypeHandler<?>>();

	/**
	 * Constructor.
	 */
	public TypeHandlerFactory() {
		super();
	}

	/**
	 * Return the list names of the registered type handlers.
	 */
	public String[] getNames() {
		return cache.keySet().toArray(new String[cache.size()]);
	}

	/**
	 * Returns the specified type handler.
	 */
	public TypeHandler<?> getInstance(String name) {
		if(cache.containsKey(name)) {
			return cache.get(name);
		} else {
			throw new IllegalArgumentException("Invalid data type '" + name + "'.");
		}
	}

	/**
	 * Returns the specified type handler. Checks to insure that the handler is compatible with the specified data type.
	 */
	@SuppressWarnings("unchecked")
	public <T> TypeHandler<T> getInstance(String name, Class<T> type) {
		TypeHandler<?> result = cache.get(name);
		if (result == null) {
			throw new IllegalArgumentException("Invalid data type '" + name + "'.");
		} else if (result.type() == type) {
			return (TypeHandler<T>) result;
		} else {
			throw new IllegalArgumentException("Handler '" + result.getClass().getSimpleName() + "(" + name + ")' is not compatible with type '" + type + "'.");
		}
	}

	/**
	 * Adds the specified type handler to the factory.
	 */
	public final void registerHandler(TypeHandler<?> handler) {
		cache.put(handler.name(), handler);
	}

	/**
	 * Add all the given TypeHandlers to the factor.
	 */
	public final void setCustomHandlers(TypeHandler<?>[] handlers) {
		for (TypeHandler<?> handler : handlers) {
			registerHandler(handler);
		}
	}

}
