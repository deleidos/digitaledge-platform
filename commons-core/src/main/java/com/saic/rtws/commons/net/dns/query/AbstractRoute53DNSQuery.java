package com.saic.rtws.commons.net.dns.query;

import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.security.SignatureException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.util.Conversion;
import com.saic.rtws.commons.net.dns.exception.DNSException;
import com.saic.rtws.commons.net.dns.exception.QueryException;
import com.saic.rtws.commons.net.dns.query.dao.ChangeBatch;
import com.saic.rtws.commons.net.dns.query.dao.ChangeInfo;
import com.saic.rtws.commons.net.dns.query.dao.ChangeResourceRequest;
import com.saic.rtws.commons.net.dns.query.dao.ChangeResourceRecordSetsResponse;
import com.saic.rtws.commons.net.dns.query.dao.Error;
import com.saic.rtws.commons.net.dns.query.dao.ErrorResponse;
import com.saic.rtws.commons.net.dns.query.dao.GetChangeResponse;
import com.saic.rtws.commons.net.dns.query.dao.ResourceRecord;
import com.saic.rtws.commons.net.dns.query.dao.ResourceRecordSet;
import com.saic.rtws.commons.net.dns.query.dao.ListResourceRecordSetsResponse;
import com.saic.rtws.commons.net.dns.security.Signature;
import com.saic.rtws.commons.net.dns.security.Sha1DigestSignature;

/**
 * 
 * A utility class used to communicate with an Amazon Route53 DNS Server.  This class will marshal and unmarshal objects that will become
 * rest messages for communication.  It will marshal the request and then unmarshal the response.  It also provides functionality to build the
 * security headers which are need for authentication.  This class provides the primitives that can be used to build a Route53 DNS client.
 * 
 * @author rainerr
 *
 */

public abstract class AbstractRoute53DNSQuery {
	protected Logger log = Logger.getLogger(AbstractRoute53DNSQuery.class); 
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static final String AMAZON_DATE = "x-amz-date";
	private static final String AMAZON_AUTH = "X-Amzn-Authorization";
	private Class<?>[] DEFAULT_CLASSES = {ChangeBatch.class, ChangeInfo.class, ChangeResourceRequest.class, ChangeResourceRecordSetsResponse.class, 
			Error.class, ErrorResponse.class,GetChangeResponse.class, ResourceRecord.class, ResourceRecordSet.class, ListResourceRecordSetsResponse.class};
	private Class<?>[] configClasses = DEFAULT_CLASSES;
	private Signature signature;
	private Marshaller marshaller = null;
	private Unmarshaller unMarshaller = null;
	private HttpClient client;
	
	/**
	 * 
	 * Constructor to build new Route53DNSQuery. This method should be called by a concrete class to initialize connection and create the
	 * marshaller and unmarshaller.
	 * 
	 */
	
	public AbstractRoute53DNSQuery() {
		signature = new Sha1DigestSignature();
		client = new HttpClient();
		try {
			JAXBContext context = JAXBContext.newInstance(configClasses);
			unMarshaller = context.createUnmarshaller();
			marshaller = context.createMarshaller();
		} catch(JAXBException jbe) {
			log.error("Error creating Unmarshaller: " + jbe.getMessage(), jbe);
		}
	}
	
	/**
	 * 
	 * When communicating with a Route53 DNS Server, it is necessary to provide security headers. 
	 * These headers includes a signed piece of  data.  In this case, the data is the current 
	 * date/time in RFC 2616 format.  The headers include the actual date/time value used and a signed 
	 * digest containing an HMAC SHA1 signature.
	 *  
	 * 
	 * @param accessKeyId - Amazon AWS access key id.
	 * @param key - Amazon AWS public key.
	 * @return An array of security headers which can be added to a GET or POST request.
	 * @throws SignatureException
	 */
	
	protected synchronized Header[] buildSecurityHeaders(String accessKeyId, byte[] key) throws SignatureException {
		StringBuffer authString = new StringBuffer();
		String digest = "";
		Header[] securityHeaders = new Header[2];
		String serverDate = Conversion.convertToRFC2616Time(new Date()); 
		
		digest = signature.sign(serverDate, key);
		authString.append("AWS3-HTTPS AWSAccessKeyId=" + accessKeyId + ",");
		authString.append("Algorithm=" + HMAC_SHA1_ALGORITHM + ",");
		authString.append("Signature=" + digest);
	
		securityHeaders[0] = new Header(AMAZON_DATE, serverDate); 
		securityHeaders[1] = new Header(AMAZON_AUTH, authString.toString());
		
		
		return securityHeaders;
	}
	
	/**
	 * 
	 * Given a input stream this method will return the unmarshalled object.
	 * 
	 * @param <T> Type of object returned.
	 * @param inputStream Stream containing the response object.
	 * @return Object returned from the unmarshalling.
	 * @throws JAXBException
	 */
	
	
	protected synchronized <T extends Object> T loadResponse(InputStream inputStream) throws JAXBException {
		StreamSource source = null;
		
		try {
			source = new StreamSource(inputStream);
			return  (T) unMarshaller.unmarshal(source); 
		} finally {
			try {
				if (source != null) {
					source.getInputStream().close();
				}
			} catch(IOException ioe) {
				log.error("Error closing Stream during Load Response", ioe);
			}
			
		}
	}
	
	/**
	 * 
	 * Given an input object, this method will output a ByteArrayOutputStream.
	 * 
	 * @param <T> Type of object returned.
	 * @param T   Type of input object.
	 * @return A ByteArrayOutputStream with the marshalled object.
	 * @throws JAXBException
	 */
	
	protected synchronized <T extends Object> ByteArrayOutputStream loadRequest(T T) throws JAXBException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		marshaller.marshal(T, stream);
		return stream;
	}
	
	/**
	 * 
	 * 
	 * @param method  HTTP Method to run.
	 * @return  The status code of the HTTP Method after it has run.
	 * @throws HttpException  
	 * @throws IOException
	 */
	
	protected synchronized int runMethod(HttpMethod method) throws HttpException, IOException {
		return client.executeMethod(method);
	}
	
	public abstract ChangeInfo queryChange(String changeId, String queryUrl) throws QueryException, DNSException;
	
	public abstract ChangeInfo postChange(ChangeBatch changeBatch, String changeUrl) throws QueryException, DNSException;
}
