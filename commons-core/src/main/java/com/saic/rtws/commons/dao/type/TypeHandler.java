package com.saic.rtws.commons.dao.type;

/**
 * Defines a common interface for objects that can set/get properties from an object. Methods are defined to
 * access fields either by name or ordinal position. Implementors are not required to support all methods,
 * and should throw UnsupporteOperationException for unimplemented portions of the API.
 * 
 * It is expected that concrete implementations will want to operate on specific types of "bean" objects rather
 * than generic Objects as defined in this interface. To do so, it is recommended that the set/get methods be
 * overloaded to replace the generic Object parameter with the desired type, and the methods below should simply
 * delegate to the overloaded version.
 * 
 * Implementing classes should fill in the type parameter to eliminate the template, allowing them to be tied to
 * specific data types. The generic getObject methods that return field types of Object should return objects
 * of the specified template type. However, it is recommended that the implementations of setObject support both
 * the template type and String.
 * 
 * The format parameter is an optional parameter intended as a format mask for data types who's representation
 * is not strongly typed. For example, if a handler is intended to access a date on and object who's underlying
 * storage only allows text, the value would need to be formatted before being stored. Handlers that do not need
 * to perform format conversion can ignore this parameter.
 *
 * @param <T> The java type that can be handled by this object.
 */
public interface TypeHandler<T> {

	/**
	 * The name of the data type that this Handler handles.
	 */
	public String name();
	
	/**
	 * The native java type that this handlers type converts to.
	 */
	public Class<T> type();
	
	public T get(Object object, String field, String format);
	public Object getObject(Object object, String field, String format);

	public void set(Object object, String field, T value, String format);
	public void setObject(Object object, String field, Object value, String format);
	
	public T get(Object object, int field, String format);
	public Object getObject(Object object, int field, String format);

	public void set(Object object, int field, T value, String format);
	public void setObject(Object object, int field, Object value, String format);
	
}
