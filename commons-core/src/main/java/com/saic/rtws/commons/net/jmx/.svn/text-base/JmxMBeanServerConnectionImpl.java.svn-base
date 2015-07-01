package com.saic.rtws.commons.net.jmx;

import java.io.IOException;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * Convenience class that wraps an MBeanServerConnection and it's related JMXConnector into a single class.
 */
public class JmxMBeanServerConnectionImpl implements JmxMBeanServerConnection {

	private JMXConnector connector;
	private MBeanServerConnection connection;
	
	protected JmxMBeanServerConnectionImpl(JMXServiceURL url) throws IOException {
		connector = JMXConnectorFactory.connect(url);
		connection = connector.getMBeanServerConnection();
	}
	
	public MBeanProxy proxy(ObjectName name) {
		return new MBeanProxy(this, name);
	}

	public void addNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, IOException {
		connection.addNotificationListener(name, listener, filter, handback);
	}

	public void addNotificationListener(ObjectName name, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, IOException {
		connection.addNotificationListener(name, listener, filter, handback);
	}

	public ObjectInstance createMBean(String className, ObjectName name) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
		return connection.createMBean(className, name);
	}

	public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
		return connection.createMBean(className, name, loaderName);
	}

	public ObjectInstance createMBean(String className, ObjectName name, Object[] params, String[] signature) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
		return connection.createMBean(className, name, params, signature);
	}

	public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName, Object[] params, String[] signature) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
		return connection.createMBean(className, name, loaderName, params, signature);
	}

	public Object getAttribute(ObjectName name, String attribute) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
		return connection.getAttribute(name, attribute);
	}

	public AttributeList getAttributes(ObjectName name, String[] attributes) throws InstanceNotFoundException, ReflectionException, IOException {
		return connection.getAttributes(name, attributes);
	}

	public String getDefaultDomain() throws IOException {
		return connection.getDefaultDomain();
	}

	public String[] getDomains() throws IOException {
		return connection.getDomains();
	}

	public Integer getMBeanCount() throws IOException {
		return connection.getMBeanCount();
	}

	public MBeanInfo getMBeanInfo(ObjectName name) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
		return connection.getMBeanInfo(name);
	}

	public ObjectInstance getObjectInstance(ObjectName name) throws InstanceNotFoundException, IOException {
		return connection.getObjectInstance(name);
	}

	public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
		return connection.invoke(name, operationName, params, signature);
	}

	public boolean isInstanceOf(ObjectName name, String className) throws InstanceNotFoundException, IOException {
		return connection.isInstanceOf(name, className);
	}

	public boolean isRegistered(ObjectName name) throws IOException {
		return connection.isRegistered(name);
	}

	public Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query) throws IOException {
		return connection.queryMBeans(name, query);
	}

	public Set<ObjectName> queryNames(ObjectName name, QueryExp query) throws IOException {
		return connection.queryNames(name, query);
	}

	public void removeNotificationListener(ObjectName name, ObjectName listener) throws InstanceNotFoundException,ListenerNotFoundException, IOException {
		connection.removeNotificationListener(name, listener);
	}

	public void removeNotificationListener(ObjectName name, NotificationListener listener) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
		connection.removeNotificationListener(name, listener);
	}

	public void removeNotificationListener(ObjectName name, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
		connection.removeNotificationListener(name, listener, filter, handback);
	}

	public void removeNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
		connection.removeNotificationListener(name, listener, filter, handback);
	}

	public void setAttribute(ObjectName name, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
		connection.setAttribute(name, attribute);
	}

	public AttributeList setAttributes(ObjectName name, AttributeList attributes) throws InstanceNotFoundException, ReflectionException, IOException {
		return connection.setAttributes(name, attributes);
	}

	public void unregisterMBean(ObjectName name) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
		connection.unregisterMBean(name);
	}

	public void close() throws IOException {
		connector.close();
	}

	protected void finalize() {
		try {
			close();
		} catch(Exception e) {
			// Ignore.
		}
	}

}
