package com.saic.rtws.commons.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class HashUtil {

	private MessageDigest digest;
	private SecureRandom random;
	
	public HashUtil() throws NoSuchAlgorithmException{
		digest = MessageDigest.getInstance("SHA-256");
		random = new SecureRandom();
	}
	
	public byte [] hash(String value) throws UnsupportedEncodingException{
		//generate salt value
		byte [] bytes = new byte[20];
		random.nextBytes(bytes);
		
		digest.reset();
		
		digest.update(bytes);
		return digest.digest(value.getBytes("UTF-8"));
	}
	
	public String encode(byte [] value){
		return Base64.encodeBase64String(value);
	}
	
	public byte [] decode(String value){
		return Base64.decodeBase64(value);
	}
	
	public boolean compareHashes(String hashA, String hashB){
		return MessageDigest.isEqual(decode(hashA), decode(hashB));
	}
}
