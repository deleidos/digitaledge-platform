package com.saic.rtws.commons.net.jms;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.util.URISupport.CompositeData;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.ConfigEncryptor;


/**
 * Specialized implementation of javax.jms.ConnectionFactory that extends ActiveMQ's ActiveMQConnectionFactory.  This
 * class overrides ActiveMQs handling of composite urls to connect in Round Robin fashion as opposed to ActiveMQ's default
 * random connection selection.  All non composite urls are delegated directly to ActiveMQConnectionFactory without modification.
 */
public class RoundRobinJMSConnectionFactory extends ActiveMQConnectionFactory {

	private static Logger log = Logger.getLogger(RoundRobinJMSConnectionFactory.class);
	
	public final static String RTWS_FQDN = "RTWS_FQDN";
	
	/**
	 * Stores the node number of the AWS node (1 in the case of ingest-alert-node1.rtsaic.com)
	 */
	private static int nodeNumber = 1;
	
	/**
	 * JMS node indexes are incremented in a round robin fashion to distribute the returned connection
	 * This map stores the next node for each unique brokerURL over all instances of RoundRobinJMSConnectionFactory.
	 */
	private static Map<String, Integer> brokerURLToNextNodeMap = new HashMap<String, Integer>();
	
	/**
	 * An array of the individual URLs from the given composite URL
	 */
	private String[] individualBrokerURLs = null;
	
	/**
	 * Tracks if the brokerURL property is ever reset (need to re-parse the compositeURIs)
	 */
	private boolean urlReset = false;
	
	/**
	 * Flag to indicate that the brokerURL has already been parsed
	 */
	private boolean urlParsed = false;
	
	/**
	 * String version of the brokerURL, partent ActiveMQConnectionFactory stores this as a URI
	 */
	private String brokerURLAsString = "";
	
	/**
	 * Query portion of the given brokerURL
	 */
	private String queryString = "";
	
	
	/**
	 * Static block to retrieve RTWS_FQDN System property and determine the node number
	 */
	static {
		String domainName = System.getProperty(RTWS_FQDN);
		try {
			if (domainName != null) {
				Scanner s = new Scanner(domainName);
				String sNum = s.findInLine("\\d++");
				nodeNumber = Integer.parseInt(sNum);
				log.info("Parsed node number:" + nodeNumber + " from:" + domainName);
			} else {
				log.warn("RTWS_FQDN system property not found, will connect to first JMS node as default");
			}
		} catch (NumberFormatException nfe) {
			log.warn("Failed to retrieve node number from FQDN " + domainName + ", using default of 1");
		}
	}
	
	/**
	 * RoundRobin no-arg constructor
	 */
	public RoundRobinJMSConnectionFactory() {
		super();
	}
	
	/**
	 * RoundRobin constructor
	 * @param brokerURL
	 */
	public RoundRobinJMSConnectionFactory(String brokerURL) {
		super(brokerURL);
		processURL(brokerURL);
    }
	
	/**
	 * Create the JMS connection using the already set username and password
	 */
	public Connection createConnection() throws JMSException {
		return createConnection(userName, password);
	}
	
	/**
	 * Creates a JMS connection given the set composite url utilizing a round robin approach that continuously 
	 * loops through all available JMS nodes for all future createConnection calls.
	 * @param user 
	 * @param password
	 */
	public Connection createConnection(String user, String password) throws JMSException {
		individualBrokerURLs = parseCompositeURL();
		if (individualBrokerURLs == null) {
			// Not a composite url, so no round robin, just use the default ActiveMQConnectionFactory behavior 
			log.info("Single BrokerURL provided, cannot RoundRobin, connecting to JMS node: " + brokerURLAsString);
			return super.createConnection();
		} else {
			int jmsNodeIndex = getNextJMSNodeIndex(brokerURLAsString, individualBrokerURLs.length);
			// create a new ActiveMQConnectionFactory instance using the individually selected uri
			log.info("Connecting to JMS node: " + individualBrokerURLs[jmsNodeIndex]);
			return new ActiveMQConnectionFactory(individualBrokerURLs[jmsNodeIndex]).createConnection(user, password);
		}
	}
	
	/**
	 * Sets the brokerURL (calling parent set) and sets the urlReset flag to true
	 */
	public void setBrokerURL(String brokerURL) {
		super.setBrokerURL(brokerURL);
		processURL(brokerURL);
	}
	
	/**
	 * Calculates the nextIndex of the individualBrokerURLs[] to use for creating the JMS connection.  A mod of the NodeNumber
	 * by the jmsNodeCount is used to determine the initial index to start on and then all future requests increment
	 * the index, looping back to 0 when the end is reached.
	 * @param jmsNodeCount the total number of JMS nodes in the system
	 * @return the next index to use in the URI[] to establish a connection
	 */
	private static synchronized int getNextJMSNodeIndex(String brokerURL, int jmsNodeCount) {
		int nextJMSNodeIndex = 0;
		if (!brokerURLToNextNodeMap.containsKey(brokerURL) ||
			brokerURLToNextNodeMap.get(brokerURL) == -1) {
			// calculate the initial jmsNode based on mod of node number
			nextJMSNodeIndex = nodeNumber % jmsNodeCount;
			// if evenly divisible (mod 0), set to the last node
			if (nextJMSNodeIndex == 0) nextJMSNodeIndex = jmsNodeCount;
			// subtract 1 for zero based array indexing
			nextJMSNodeIndex--;
		} else {
			// after the initial calc, just loop through the available nodes for all future requests
			nextJMSNodeIndex = brokerURLToNextNodeMap.get(brokerURL);
			if (++nextJMSNodeIndex >= jmsNodeCount)  nextJMSNodeIndex = 0;
		}
		log.debug("Calculated next JMS Node index:" + nextJMSNodeIndex);
		brokerURLToNextNodeMap.put(brokerURL, nextJMSNodeIndex);
		return nextJMSNodeIndex;
	}
	
	/**
	 * Attempts to parse the set brokerURL into it's individual URL pieces.  If the URL is not a 
	 * composite URL this method returns null.
	 * @return the array of individual URLs as strings parsed from the current brokerURL.
	 */
	private String[] parseCompositeURL() {
		if (!urlParsed || urlReset) {
			try {
				if (URISupport.isCompositeURI(brokerURL)) {
					CompositeData compositeData = URISupport.parseComposite(brokerURL);
					URI[] compositeURIs = compositeData.getComponents();
					if (compositeURIs.length > 1) {
						individualBrokerURLs = new String[compositeURIs.length];
						for (int i=0; i<compositeURIs.length; i++) {
							individualBrokerURLs[i] = compositeURIs[i].toString() + queryString;
						}
					}
				}
		    } catch (URISyntaxException e) {
		        throw (IllegalArgumentException)new IllegalArgumentException("Invalid broker URI: " + brokerURLAsString).initCause(e);
		    }
			urlParsed = true;
		}
		return individualBrokerURLs;
	}
	
	
	/**
	 * Saves the query portion of the URL, inserts into the brokerURLToNextNodeMap if not exists, 
	 * and saves a string version of the URL. 
	 * @param brokerURL
	 */
	private void processURL(String brokerURL) {
		if (brokerURL.indexOf("?") > -1) {
			queryString = brokerURL.substring(brokerURL.indexOf("?"), brokerURL.length());
		}
		if (!brokerURLToNextNodeMap.containsKey(brokerURL)) {
			brokerURLToNextNodeMap.put(brokerURL, -1);
		}
		brokerURLAsString = brokerURL;
		urlReset = true;
	}
	
	@Override
	public void setPassword(String password) {
		super.setPassword(ConfigEncryptor.instance().decryptWithWrapper(password));
	}
	
}
