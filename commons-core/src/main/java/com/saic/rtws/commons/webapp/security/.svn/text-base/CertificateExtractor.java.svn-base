package com.saic.rtws.commons.webapp.security;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Vector;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;

public final class CertificateExtractor {
	
	public static final String TENANT_ID_OBJECTID = "2.5.4.41";
	private static final String X509_CERTIFICATE_TYPE = "X.509";
	
	private CertificateExtractor() {
		// This is a utility class.
	}
	
	public static X509Certificate getX509Certificate(String filepath) throws FileNotFoundException, CertificateException {
		
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filepath));
		return (X509Certificate) CertificateFactory.getInstance(X509_CERTIFICATE_TYPE).generateCertificate(bis);
		
	}
	
	@SuppressWarnings("rawtypes")
	public static String getTenantId(X509Certificate certificate) throws CertificateEncodingException {
		
		X509Principal principal = PrincipalUtil.getSubjectX509Principal(certificate);
		for (Object oid : principal.getOIDs()) {
			if (oid instanceof DERObjectIdentifier) {
				DERObjectIdentifier doid = (DERObjectIdentifier) oid;
				if (doid.getId().equals(TENANT_ID_OBJECTID)) {
					Vector values = principal.getValues(doid);
					if (values.size() > 0 && values.get(0) instanceof String) {
						return (String) values.get(0);
					}
				}
			}
		}
		
		return null;
		
	}
	
	public static void main(String [] args) {
		
		if (args.length < 1) {
			System.out.println("Usage: java CertificateExtractor <cert-file-path>");
			System.exit(1);
		}
		
		try {
			X509Certificate certificate = CertificateExtractor.getX509Certificate(args[0]);
			System.out.println(String.format("Extracted tenant id '%s' from certificate %s.", CertificateExtractor.getTenantId(certificate), args[0]));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
}