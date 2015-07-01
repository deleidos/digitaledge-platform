package com.saic.rtws.commons.net.dns.query;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.saic.rtws.commons.net.dns.exception.DNSException;
import com.saic.rtws.commons.net.dns.exception.QueryException;
import com.saic.rtws.commons.net.dns.query.dao.ChangeBatch;
import com.saic.rtws.commons.net.dns.query.dao.ChangeInfo;
import com.saic.rtws.commons.net.dns.query.dao.ChangeResourceRecordSetsResponse;
import com.saic.rtws.commons.net.dns.query.dao.ChangeResourceRequest;
import com.saic.rtws.commons.net.dns.query.dao.ErrorResponse;
import com.saic.rtws.commons.net.dns.query.dao.GetChangeResponse;
import com.saic.rtws.commons.net.dns.query.dao.ListResourceRecordSetsResponse;
import com.saic.rtws.commons.net.dns.query.dao.ResourceRecord;
import com.saic.rtws.commons.net.dns.query.dao.ResourceRecordSet;

/**
 * 
 * 
 * 
 * 
 * @author rainerr
 *
 */
public class Route53DNSQuery extends AbstractRoute53DNSQuery {
	
	private File propFile;
	private String accessKeyId;
	private byte[] key;
	private Header[] securityHeaders;
	
	public void setPropFile(File propFile) {
		this.propFile = propFile;
	}
	
	public File getPropFile() {
		return propFile;
	}
	
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	
	public String getAccessKeyId() {
		return accessKeyId;
	}
	
	public void setKey(byte[] key) {
		this.key = key;
	}
	
	public byte[] getKey() {
		return key;
	}
	
	public void init() throws QueryException {
		try {
			securityHeaders = buildSecurityHeaders(accessKeyId, key);
		} catch(SignatureException signatureException) {
			log.error("Error creating security headers " + signatureException.getMessage(), signatureException);
			throw new QueryException("Error creating security headers " + signatureException.getMessage(), signatureException);
		}
	}
	
	
	public ChangeInfo queryChange(String changeId, String queryUrl) throws QueryException, DNSException {
		GetMethod queryMethod = new GetMethod(queryUrl + changeId); 
		ChangeInfo changeInfo = null;
		
		try {
			Header[] securityHeaders = buildSecurityHeaders(accessKeyId, key);
			for(Header header : securityHeaders) {
				queryMethod.addRequestHeader(header);
			}
			int code = runMethod(queryMethod);
			
			if (code == HttpURLConnection.HTTP_OK) {
				GetChangeResponse getChangeResponse = loadResponse(queryMethod.getResponseBodyAsStream());
				changeInfo = getChangeResponse.getChangeInfo();
			} else {
				ErrorResponse errorResponse = loadResponse(queryMethod.getResponseBodyAsStream());
				com.saic.rtws.commons.net.dns.query.dao.Error error = errorResponse.getError();
				throw new DNSException(error.getMessage());
			}
		} catch (SignatureException se) {
			log.error("Signature Error: " + se.getMessage(), se);
			throw new QueryException("Signature Error: " + se.getMessage(), se);
		} catch (JAXBException jbe) {
			log.error("JAXB Error: " + jbe.getMessage(), jbe);
			throw new QueryException("JAXB Error: " + jbe.getMessage(), jbe);
		} catch (IOException ioe) {
			log.error("Write Problem: " + ioe.getMessage(), ioe);
			throw new QueryException("Write Problem: " + ioe.getMessage(), ioe);
		} finally {
			try { queryMethod.releaseConnection(); } catch (Exception e) { }
		}
		
		return changeInfo;
	}
	
	public ListResourceRecordSetsResponse queryAllChanges(String changeUrl, NameValuePair [] params) {
		GetMethod queryMethod = new GetMethod(changeUrl);
		queryMethod.setQueryString(params);

		int code = HttpURLConnection.HTTP_ACCEPTED;
		ListResourceRecordSetsResponse listResourceRecordSetsResponse = null;
		
		try {
			Header[] securityHeaders = buildSecurityHeaders(accessKeyId, key);
			for(Header header : securityHeaders) {
				queryMethod.addRequestHeader(header);
			}
			
			code = runMethod(queryMethod);
			
			if (code == HttpURLConnection.HTTP_OK) {
				listResourceRecordSetsResponse = loadResponse(queryMethod.getResponseBodyAsStream());
			} else {
				ErrorResponse errorResponse = loadResponse(queryMethod.getResponseBodyAsStream());
				com.saic.rtws.commons.net.dns.query.dao.Error error = errorResponse.getError();
				log.error("Error Querying Route53 DNS " + error.getMessage());
			}
		} catch (Exception ex) {
			log.error("Error Querying Route53 DNS " + ex.getMessage());
		} finally {
			try { queryMethod.releaseConnection(); } catch (Exception e) { }
		}
		
		return listResourceRecordSetsResponse;
	}
	
	public boolean queryRecordExists(String changeUrl, String fqdn, ResourceRecordSet.RECORDTYPE type) {
		boolean foundEntry = false;
		
		List<ResourceRecord> resourceRecords = queryFQDNByRecordType(changeUrl, fqdn, type);
		if (resourceRecords.size() > 0) {
			foundEntry = true;
		}
		return foundEntry;
	}
	
	public boolean queryFQDNExists(String changeUrl, String fqdn) {
		boolean foundEntry = false;
		
		ResourceRecordSet resourceRecordSet = queryFQDN(changeUrl, fqdn);
		if (resourceRecordSet.getResourceRecords().size() > 0) {
			foundEntry = true;
		}
		
		return foundEntry;
	}
	
	public List<ResourceRecord> queryFQDNByRecordType(String changeUrl, String fqdn, ResourceRecordSet.RECORDTYPE type) {
		List<ResourceRecord> resourceRecords = new ArrayList<ResourceRecord>();
		String dnsFQN = fqdn + ".";
		
		//build query params
		NameValuePair [] params = new NameValuePair[1];
		params[0] = new NameValuePair("name", dnsFQN);  //DNS domain name at which to start listing resource record sets&
		
		ListResourceRecordSetsResponse listResourceRecordSetsResponse = queryAllChanges(changeUrl, params);
		
		if (listResourceRecordSetsResponse != null) {
			List<ResourceRecordSet> resourceRecordSets = listResourceRecordSetsResponse.getResouruceRecordSets();
			for(ResourceRecordSet resourceRecordSet : resourceRecordSets) {
				if (resourceRecordSet.getName().equalsIgnoreCase(dnsFQN)) {
					if (resourceRecordSet.getType() == type) {
						resourceRecords = resourceRecordSet.getResourceRecords();
						break;
					}
				}
			}
		}
		return resourceRecords;
	}
	
	public ResourceRecordSet queryFQDN(String changeUrl, String fqdn) {
		ResourceRecordSet resourceRecords = new ResourceRecordSet();
		String dnsFQN = fqdn + ".";
		
		//build query params
		NameValuePair [] params = new NameValuePair[1];
		params[0] = new NameValuePair("name", dnsFQN);  //DNS domain name at which to start listing resource record sets&
		
		ListResourceRecordSetsResponse listResourceRecordSetsResponse = queryAllChanges(changeUrl, params);
		
		if (listResourceRecordSetsResponse != null) {
			List<ResourceRecordSet> resourceRecordSets = listResourceRecordSetsResponse.getResouruceRecordSets();
			for(ResourceRecordSet resourceRecordSet : resourceRecordSets) {
				if (resourceRecordSet.getName().equalsIgnoreCase(dnsFQN)) {
					resourceRecords = resourceRecordSet;
					break;
				}
			}
		}
		return resourceRecords;
	}
	
	public ChangeInfo postChange(ChangeBatch changeBatch, String changeUrl) throws QueryException, DNSException {
		PostMethod postMethod = new PostMethod(changeUrl);
		ChangeResourceRequest changeResourceRequest = new ChangeResourceRequest();
		ChangeInfo changeInfo = null;
		
		try {
			Header[] securityHeaders = buildSecurityHeaders(accessKeyId, key);
			for(Header header : securityHeaders) {
				postMethod.addRequestHeader(header);
			}
			
			changeResourceRequest.setChangeBatch(changeBatch);
			ByteArrayOutputStream stream = loadRequest(changeResourceRequest);
			ByteArrayRequestEntity entity = new ByteArrayRequestEntity(stream.toByteArray());
			postMethod.setRequestEntity(entity);
			
			int code = runMethod(postMethod);
			log.debug(postMethod.getResponseBodyAsString());
			if (code == HttpURLConnection.HTTP_OK) {
				ChangeResourceRecordSetsResponse changeResourceRecordSetsResponse = loadResponse(postMethod.getResponseBodyAsStream());
				changeInfo = changeResourceRecordSetsResponse.getChangeInfo();
			} else {
				ErrorResponse errorResponse = loadResponse(postMethod.getResponseBodyAsStream());
				com.saic.rtws.commons.net.dns.query.dao.Error error = errorResponse.getError();
				throw new DNSException(error.getMessage());
			}
		} catch (SignatureException se) {
			log.error("Signature Error: " + se.getMessage(), se);
			throw new QueryException("Signature Error: " + se.getMessage(), se);
		} catch (JAXBException jbe) {
			log.error("JAXB Error: " + jbe.getMessage(), jbe);
			throw new QueryException("JAXB Error: " + jbe.getMessage(), jbe);
		} catch (IOException ioe) {
			log.error("Write Problem: " + ioe.getMessage(), ioe);
			throw new QueryException("Write Problem: " + ioe.getMessage(), ioe);
		} finally {
			try { postMethod.releaseConnection(); } catch (Exception e) { }
		}
		
		return changeInfo;
	}
}
