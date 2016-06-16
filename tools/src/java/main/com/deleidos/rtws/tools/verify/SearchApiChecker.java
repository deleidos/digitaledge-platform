package com.deleidos.rtws.tools.verify;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.deleidos.rtws.tools.verify.exception.SearchApiCheckerException;

/**
 * This program submits a http GET request to the searchapi and verifies that
 * documents exists in the realtime and nearrealtime api.
 */
public class SearchApiChecker {
	
	public static final String SCHEME_OPT_KEY = "scheme";
	
	public static final String HOST_OPT_KEY = "host";
	
	public static final String PORT_OPT_KEY = "port";
	
	public static final String CONTEXT_OPT_KEY = "context";
	
	private CommandLine line;
	
	private Options options;
	
	private XPathExpression expr;
	
	public SearchApiChecker(String [] args) throws SearchApiCheckerException {
		
		options = buildOptions();
		
		try {
			CommandLineParser parser = new PosixParser();
			this.line = parser.parse(options, args);
			
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			this.expr = xpath.compile("//response/result");
		} catch (ParseException pe) {
			throw new SearchApiCheckerException("SearchApiChecker - ParseException: " + pe.getMessage(), pe);
		} catch (XPathExpressionException xe) {
			throw new SearchApiCheckerException("SearchApiChecker - XPathExpressionException: " + xe.getMessage(), xe);
		}
		
		checkOptions(options);
		
	}
	
	public void execute() throws SearchApiCheckerException {
	
		String url = null;
	
		url = buildUrl("realtime");
		BigInteger numRealTimeDocs = getNumDocuments(url);

		url = buildUrl("nearrealtime");
		BigInteger numNearRealTimeDocs = getNumDocuments(url);
		
		System.out.println("SearchApiChecker located " + numRealTimeDocs.toString() + " documents from the realtime searchapi." );
		System.out.println("SearchApiChecker located " + numNearRealTimeDocs.toString() + " documents from the nearrealtime searchapi." );
		
		if (numRealTimeDocs.equals(BigInteger.ZERO) || numNearRealTimeDocs.equals(BigInteger.ZERO)) {
			System.out.println("SearchApiChecker fail to locate documents from both realtime and nearrealtime searchapi.");
			System.exit(3);
		}
		else {
			System.out.println("SearchApiChecker located documents from both the realtime and nearrealtime searchpi!");
		}
	}
	
	private String buildUrl(String path) {
		
		String scheme = line.getOptionValue(SCHEME_OPT_KEY);
		String host = line.getOptionValue(HOST_OPT_KEY);
		String port = line.getOptionValue(PORT_OPT_KEY);
		String context = line.getOptionValue(CONTEXT_OPT_KEY);
		
		StringBuilder url = new StringBuilder();
		url.append(scheme);
		url.append("://");
		url.append(host);
		url.append(':');
		url.append(port);
		url.append('/');
		url.append(context);
		url.append('/');
		url.append(path);
		
		return url.toString();
		
	}
	
	private BigInteger getNumDocuments(String url) throws SearchApiCheckerException {
		
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new NameValuePair("q", "*:*"));
		params.add(new NameValuePair("indent", "on"));
		
		GetMethod getMethod = new GetMethod(url);
		getMethod.setQueryString(params.toArray(new NameValuePair[params.size()]));
		
		try {
			int status = client.executeMethod(getMethod);

			if (status == HttpStatus.SC_OK) 
			{
				InputStream is = getMethod.getResponseBodyAsStream();
				 
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = builder.parse(is);
				
				Object result = expr.evaluate(doc, XPathConstants.NODESET);
				
				NodeList nodes = (NodeList) result;
				Node node = nodes.item(0);
				NamedNodeMap nnm = node.getAttributes();
				
				return new BigInteger(nnm.getNamedItem("numFound").getNodeValue());
			}
			
			return BigInteger.ZERO;
		} catch (HttpException he) {
			throw new SearchApiCheckerException("execute - HttpException: " + he.getMessage(), he);
		} catch (IOException ioe) {
			throw new SearchApiCheckerException("execute - IOException: " + ioe.getMessage(), ioe);
		} catch (ParserConfigurationException pce) {
			throw new SearchApiCheckerException("execute - ParserConfigurationException: " + pce.getMessage(), pce);
		} catch (SAXException se) {
			throw new SearchApiCheckerException("execute - SAXException: " + se.getMessage(), se);
		} catch (XPathExpressionException xe) {
			throw new SearchApiCheckerException("execute - XPathExpressionException: " + xe.getMessage(), xe);
		} finally {
			try { getMethod.releaseConnection(); } catch (Exception e) { }
		}
	}
	
	private Options buildOptions() {
		
		Options options = new Options();
		
		options.addOption(SCHEME_OPT_KEY, true, "the url scheme");
		options.addOption(HOST_OPT_KEY, true, "the url hostname");
		options.addOption(PORT_OPT_KEY, true, "the url port");
		options.addOption(CONTEXT_OPT_KEY, true, "the url context");
		
		return options;
		
	}
	
	private void checkOptions(Options options) throws SearchApiCheckerException {

		if (! line.hasOption(SCHEME_OPT_KEY) && 
			! line.hasOption(HOST_OPT_KEY) &&
			! line.hasOption(PORT_OPT_KEY) && 
			! line.hasOption(CONTEXT_OPT_KEY)) {
			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("com.deleidos.rtws.tools.verify.SearchApiChecker", options);
			System.exit(1);
			
		}
		
	}
	
	public static void main(String [] args) {
		
		try {
			SearchApiChecker checker = new SearchApiChecker(args);
			checker.execute();
		} catch (Exception ex) {
			System.out.println("SearchApiChecker failed execution. Reason: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(2);
		}
		
	}
	
}