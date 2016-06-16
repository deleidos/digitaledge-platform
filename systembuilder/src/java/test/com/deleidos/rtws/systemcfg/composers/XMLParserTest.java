package com.deleidos.rtws.systemcfg.composers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.deleidos.rtws.systemcfg.parser.XMLParser;

import junit.framework.TestCase;

/**
 * Test class for the XMLParser.
 */
public class XMLParserTest extends TestCase{

	private String file = null;
	private String outFile1 = null;
	private String outFile2 = null;
	
	@Before
	public void setUp(){
		String baseDir = System.getProperty("basedir");
		
		if(baseDir == null){
			System.out.println("Need to set system property basedir.");
		}
		
		file = baseDir + "/src/resource/test/com/deleidos/rtws/systemcfg/templates/activemq-beans.xml";
		file = baseDir + "/src/resource/test/com/deleidos/rtws/systemcfg/templates/activemq-jms-ext-node1.xml";
		
		outFile1 = "/tmp/activemq-test-out-1.xml";
		outFile2 = "/tmp/activemq-test-out-2.xml";
	}
	
	@After
	public void cleanUp(){
		
	}
	
	@Test
	public void testXMLReadWrite(){
		XMLParser parser = new XMLParser();
		parser.readXmlFile(file);
		parser.writeXmlFile(outFile1);
	}
	
	@Test
	public void testGetElement(){
		XMLParser parser = new XMLParser();
		parser.readXmlFile(file);
		NodeList nodeList = parser.getElementList("networkConnectors");
		
		assertEquals("Expected networkConnectors element in broker", 1, nodeList.getLength());
		
		if(nodeList.getLength() > 0){
			for(int i = 0; i < nodeList.getLength(); i++){
				Node node = nodeList.item(i);
				if(node.hasChildNodes()){
					NodeList cNodes = node.getChildNodes();
					for(int j = 0; j < cNodes.getLength(); j++){
						Node child = cNodes.item(j);
						if(child.getNodeType() == Node.ELEMENT_NODE) {
							 System.out.println("Element node");
							 System.out.println(child.getNodeName());
						      Element element = (Element) child;
						      NamedNodeMap map = element.getAttributes();
						      for(int a = 0; a < map.getLength(); a++){
						    	  Node tmp = map.item(a);
						    	  System.out.println(tmp.getNodeName() + "=" + tmp.getNodeValue());
						      }
						}//end if
					}//end for
				}//end if
			}//end for
		}//end if
		else{
			fail("No networkConnectors element found in xml file.");
		}
	}
	
	@Test
	public void testAddNodeToElement(){
		XMLParser parser = new XMLParser();
		parser.readXmlFile(file);
		NodeList nodeList = parser.getElementList("networkConnectors");
		
		System.out.println(nodeList.getLength());
		
		if(nodeList.getLength() > 0){
			for(int i = 0; i < nodeList.getLength(); i++){
				Node node = nodeList.item(i);
				
				
				Element connector = parser.createElement("networkConnector");
				connector.setAttribute("name", "connector4");
				connector.setAttribute("uri", "static:(ssl://@messaging.external.node4@:61616)");
				connector.setAttribute("networkTTL", "2");
				connector.setAttribute("decreaseNetworkConsumerPriority", "true");
				
				node.appendChild(connector);
			}
		}
		
		parser.writeXmlFile(outFile2);
	}
}
