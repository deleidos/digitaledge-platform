package com.saic.rtws.commons.management;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.Hashtable;
import java.util.Properties;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.exception.InitializationException;
import com.saic.rtws.commons.util.Initializable;

/**
 * Base class for publishing managed resources to the default MBean server. Beans exposed through this class can be
 * pojo's rather than needing to implement formal MBean semantics. However, they do need to implement the ManagedBean
 * interface, which allows them to define their own object name. This allows objects to be registered in the MBean
 * server without the publishing entity needing to implement the extra step of building the object name before hand. The
 * ManagementContext is tied to a specific JMX domain string; all beans published in a given context will share that
 * domain.
 */
public class ManagementContext implements Initializable {

	private static final Logger log = Logger.getLogger(ManagementContext.class);

	/** The JMX domain represented by this management context. */
	private String domain;

	/** The registry object that this management context uses to publish managed resources. */
	private static MBeanServer registry = ManagementFactory.getPlatformMBeanServer();

	/**
	 * Constructor.
	 */
	public ManagementContext() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param domain
	 *            The JMX domain to be used for all object names assigned by this management context.
	 */
	public ManagementContext(String domain) {
		this.domain = domain;
	}

	/**
	 * The JMX domain represented by this management context.
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * The JMX domain represented by this management context.
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Attaches this management context to the default MBean server.
	 */
	public void initialize() {
		try {
			// Do a syntax check on the JMX domain.
			new ObjectName(domain + ":*");
		} catch (MalformedObjectNameException e) {
			throw new InitializationException("Invalid JMX domain name '" + domain + "'.", e);
		}
	}

	/**
	 * Releases all managed resources.
	 */
	public void dispose() {
		try {
			for (ObjectName name : registry.queryNames(new ObjectName(domain + ":*"), null)) {
				try {
					registry.unregisterMBean(name);
				} catch (MBeanRegistrationException e) {
					log.error("Unable to unregister managed resource '" + name + "'.", e);
				} catch (InstanceNotFoundException e) {
					// Ignore.
				}
			}
		} catch (MalformedObjectNameException e) {
			log.debug(e);
		}
	}

	/**
	 * Determines whether the given managed resource is registered.
	 */
	public final boolean isRegistered(ManagedBean bean) {
		try {
			return registry.isRegistered(buildObjectName(bean));
		} catch (MalformedObjectNameException e) {
			return false;
		}
	}

	/**
	 * Publishes the given bean into the MBean server.
	 */
	public final void register(ManagedBean bean) {
		try {
			ObjectName name = buildObjectName(bean);
			try {
				register(name, bean);
			} catch (Exception e) {
				log.warn("Unable to publish managed resource '" + name + "'.", e);
			}
		} catch (MalformedObjectNameException e) {
			log.error("Unable to publish managed resource.", e);
		}
	}

	/**
	 * Publishes the given bean into the MBean server using the given object name.
	 */
	private void register(ObjectName name, Object bean) throws Exception {
		registry.registerMBean(bean, name);
	}

	/**
	 * Removes the given bean from the MBean server.
	 */
	public final void unregister(ManagedBean bean) {
		try {
			ObjectName name = buildObjectName(bean);
			try {
				unregister(name);
			} catch (InstanceNotFoundException e) {
				// Ignore.
			} catch (Exception e) {
				log.error("Unable to unregister managed resource '" + name + "'.", e);
			}
		} catch (MalformedObjectNameException e) {
			log.debug(e);
		}
	}

	/**
	 * Removes the bean bound to the given object name from the MBean server.
	 */
	private void unregister(ObjectName name) throws Exception {
		registry.unregisterMBean(name);
	}

	/**
	 * Builds the JMX object name for the given bean.
	 */
	@SuppressWarnings("unchecked")
	protected final ObjectName buildObjectName(ManagedBean bean) throws MalformedObjectNameException {
		try {
			Properties keys = new Properties();
			bean.buildObjectNameKeys(keys);
			return new ObjectName(domain, (Hashtable) keys);
		} catch (ClassCastException e) {
			throw new MalformedObjectNameException("Invalid class type for object name key.");
		}
	}

	/**
	 * Insures that the RMI registry is running and that a JMX connector is available.
	 */
	static {

		// Lookup system properties to define JMX connection.
		String rmiPort = System.getProperty("com.saic.rtws.management.rmiPort", "1099");
		String jmxPort = System.getProperty("com.saic.rtws.management.jmxPort", "61516");
		String connectorName = System.getProperty("com.saic.rtws.management.connectorName", "jmxrmi");

		try {
			// Start the RMI registry on the default port.
			LocateRegistry.createRegistry(Integer.parseInt(rmiPort));
		} catch (Exception e) {
			// Assume it already exists.
		}

		try {
			// Setup a JMX connector for RMI access to managed resources in the default MBean server.
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost:" + jmxPort + "/jndi/rmi://localhost:" + rmiPort + "/" + connectorName);
			JMXConnectorServerFactory.newJMXConnectorServer(url, null, registry).start();

		} catch (Exception e) {
			log.error("Unable to establish JMX connector.", e);
		}

	}

}
