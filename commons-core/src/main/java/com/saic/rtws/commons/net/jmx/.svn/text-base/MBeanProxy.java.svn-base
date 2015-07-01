package com.saic.rtws.commons.net.jmx;

import java.io.IOException;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * Proxy object that may be used to access the attributes/operations of a remote MBean.
 */
public class MBeanProxy {

	/** The connection over which to invoke remote calls. */
	private final MBeanServerConnection connection;
	
	/** The ObjectName of the bean being proxied. */
	private final ObjectName name;

	/**
	 * Constructor.
	 * 
	 * @param connection The connection over which to invoke remote calls.
	 * @param name The ObjectName of the bean being proxied.
	 */
	public MBeanProxy(MBeanServerConnection connection, ObjectName name) {
		super();
		this.connection = connection;
		this.name = name;
	}

	/**
	 * Retrieve the given attribute's value.
	 */
	public Object getAttribute(String attribute) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return connection.getAttribute(name, attribute);
	}

	/**
	 * Retrieve values for the given list of attributes.
	 */
	public AttributeList getAttributes(String[] attributes) throws InstanceNotFoundException, ReflectionException, IOException {
		return connection.getAttributes(name, attributes);
	}

	/**
	 * Invoke the given operation.
	 */
	public Object invoke(String actionName, Object[] params, String[] signature) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return connection.invoke(name, actionName, params, signature);
	}

	/**
	 * Assigns the given attribute value.
	 */
	public void setAttribute(Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
		connection.setAttribute(name, attribute);
	}

	/**
	 * Assigns the given attribute values.
	 */
	public void setAttributes(AttributeList attributes) throws InstanceNotFoundException, ReflectionException, IOException {
		connection.setAttributes(name, attributes);
	}

}
