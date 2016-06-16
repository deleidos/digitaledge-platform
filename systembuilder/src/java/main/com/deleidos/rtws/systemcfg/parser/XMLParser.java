package com.deleidos.rtws.systemcfg.parser;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	
	private static final Logger logger = LogManager.getLogger(XMLParser.class);
	
	private Document doc;
	
	/**
	 * Default constructor.
	 */
	public XMLParser(){
		
	}
	
	/**
	 * Read xml file into a document object.
	 * 
	 * @param xmlFile
	 */
	public void readXmlFile(String xmlFile){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			//working copy of the xml file
			doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a {@link NodeList} from {@link Document}.
	 * 
	 * @param tagName
	 * @return {@link NodeList}
	 */
	public NodeList getElementList(String tagName){
		NodeList nList = doc.getElementsByTagName(tagName);
		return nList;
	}
	
	/**
	 * Get a {@link Element} from a {@link Document}
	 * 
	 * @param tagName
	 * @return {@link Element}
	 */
	public Element getElement(String tagName){
		NodeList nList = doc.getElementsByTagName(tagName);
		Element element = null;
		if(nList.getLength() > 0){
			element = (Element) nList.item(0);
		}
		
		return element;
	}
	
	/**
	 * Add a {@link Node} to the current {@link Document}.
	 * 
	 * @param tagName
	 * @param node
	 */
	public void addElement(String tagName, Node node){
		NodeList nList = doc.getElementsByTagName(tagName);
		if(nList.getLength() > 0){
			Node n = nList.item(0);
			n.appendChild(node);
		}
	}
	
	/**
	 * Create a new {@link Node} using the {@Document} to create it.
	 * 
	 * @param name
	 * @return{@link Element}s
	 */
	public Element createElement(String name){
		return doc.createElement(name);
	}
	
	/**
	 * Write out the current {@link Document} to a file.
	 * @param outFile the output file
	 * @throws Exception 
	 */
	public void writeXmlFile(String outFile){
		try{
			OutputStream os = new FileOutputStream(outFile);
			
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			doc.getDocumentElement().normalize();
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(os);
			transformer.transform(source, result); 
			
			//cleanup
			os.close();
		}
		catch(Exception e ){
			logger.error(e.toString(), e);
		}
	}
}
	
	