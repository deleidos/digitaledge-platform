package com.saic.rtws.commons.dao.type;

import java.util.Date;

import com.saic.rtws.commons.dao.exception.DataFormatException;


/**
 * Base class for TypeHandles.
 */
public abstract class AbstractTypeHandler<T> implements TypeHandler<T> {

	/** The name of the data type this handler is tied to. */
	private String name;

	/** The java type that this type's native representation converts to. */
	private Class<T> type;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name of the data type this handler is tied to.
	 * @param type
	 *            The java type that this type's native representation converts to.
	 */
	protected AbstractTypeHandler(String name, Class<T> type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * The name of the data type this handler is tied to.
	 */
	public final String name() {
		return name;
	}

	/**
	 * The java type that this type's native representation converts to.
	 */
	public final Class<T> type() {
		return type;
	}

	/**
	 * Gets the given named field from the given object.
	 */
	public final Object getObject(Object object, String field, String format) {
		return get(object, field, format);
	}

	/**
	 * Sets the given named field on the given object.
	 */
	public final void setObject(Object object, String field, Object value, String format) {
		if (value instanceof String) {
			set(object, field, parse((String) value, format), format);
		} else {
			set(object, field, type.cast(value), format);
		}
	}

	/**
	 * Gets the given ordinal field from the given object.
	 */
	public final Object getObject(Object object, int field, String format) {
		return get(object, field, format);
	}

	/**
	 * Sets the given ordinal field on the given object.
	 */
	public final void setObject(Object object, int field, Object value, String format) {
		if (value instanceof String) {
			set(object, field, parse((String) value, format), format);
		} else {
			set(object, field, type.cast(value), format);
		}
	}

	/**
	 * Converts the given String into an object of the template type defined for this class. Conversions are supported
	 * for common java types (String, Date, Boolean, Number). Extending class that are intended to support other types
	 * should override this method with an appropriate implementation.
	 */
	protected T parse(String value, String format) {
		try {
			Object result = null;
			if (value == null) {
				// Nothing to do.
			} else if (type == String.class) {
				result = value;
			} else if (type == Boolean.class) {
				result = TypeUtil.toBoolean(value);
			} else if (type == Date.class) {
				result = TypeUtil.toDate(value, format);
			} else if (type == Number.class) {
				result = TypeUtil.toDouble(value, format);
			} else if (type == Byte.class) {
				result = TypeUtil.toByte(value, format);
			} else if (type == Short.class) {
				result = TypeUtil.toShort(value, format);
			} else if (type == Integer.class) {
				result = TypeUtil.toInteger(value, format);
			} else if (type == Long.class) {
				result = TypeUtil.toLong(value, format);
			} else if (type == Float.class) {
				result = TypeUtil.toFloat(value, format);
			} else if (type == Double.class) {
				result = TypeUtil.toDouble(value, format);
			} else {
				throw new DataFormatException("Conversion to type '" + type.getCanonicalName() + "' is not supported.");
			}
			return type.cast(result);
		} catch (DataFormatException e) {
			throw e;
		} catch (Exception e) {
			throw new DataFormatException(e);
		}
	}

}
