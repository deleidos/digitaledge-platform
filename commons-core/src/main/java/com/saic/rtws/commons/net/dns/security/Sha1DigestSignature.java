package com.saic.rtws.commons.net.dns.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import org.apache.commons.codec.binary.Base64;

public class Sha1DigestSignature implements Signature {
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	
	public String sign(String inputData, byte[] key) throws SignatureException {
		String digest = "";
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1_ALGORITHM);
			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
	
			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(inputData.getBytes());
	
			
			// base64-encode the hmac
			digest = Base64.encodeBase64String(rawHmac);
		
		} catch (IllegalArgumentException iae) {
			throw new SignatureException("Illegal Argument: " + iae.getMessage(), iae);
		} catch (NoSuchAlgorithmException nse) {
			throw new SignatureException("No Such Algorithm: " + nse.getMessage(), nse);
		} catch (InvalidKeyException ike) {
			throw new SignatureException("Invalid Key: " + ike.getMessage(), ike);
		} catch (Exception ex) {
			throw new SignatureException("Signature Error: " + ex.getMessage(), ex);
		}
		
		return digest.trim();
	}
}
