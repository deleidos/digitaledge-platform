package com.saic.rtws.commons.config;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.properties.PropertyValueEncryptionUtils;

public class ConfigEncryptor implements StringEncryptor {
 
	public static synchronized ConfigEncryptor instance() {
		if (_instance == null) {
			_instance = new ConfigEncryptor();
		}
		return _instance;
	}

	@Override
	public String encrypt(String plain) {
		return this.encryptor.encrypt(plain);
	}

	/**
	 * Encrypt the parameter and return the result surrounded by <tt>"ENC("</tt>
	 * at the beginning and <tt>")"</tt> at the end.
	 */
	public String encryptWithWrapper(String plain) {
		String black = plain;
		try {
			black = PropertyValueEncryptionUtils.encrypt(plain, ConfigEncryptor.instance());
		} catch (EncryptionOperationNotPossibleException e) {
			// Property could not be encrypted, just use the normal property
		}
		return black;
	}

	@Override
	public String decrypt(String black) {
		return this.encryptor.decrypt(black);
	}

	/**
	 * If the parameter starts with <tt>"ENC("</tt> and ends with <tt>")"</tt>
	 * then this will decrypt the string in the middle and return it. Otherwise
	 * it just returns the parameter itself.
	 */
	public String decryptWithWrapper(String black) {
		String plain = black;
		try {
			if (PropertyValueEncryptionUtils.isEncryptedValue(black)) {
				plain = PropertyValueEncryptionUtils.decrypt(black, ConfigEncryptor.instance());
			}
		} catch (EncryptionOperationNotPossibleException e) {
			// Encrypted property could not be decrypted, just return the encrypted property
		}
		return plain;
	}

	private ConfigEncryptor() {
		// Let's obfuscate a key so we don't have it sitting in a Java String
		// to be decompiled at will. This will not stop a determined hacker but
		// is better than nothing.
		String variable = this.getClass().getName();
		StandardStringDigester d = new StandardStringDigester();
		d.setAlgorithm("SHA-1");
		d.setSaltSizeBytes(0);
		
		this.encryptor = new StandardPBEStringEncryptor();
		this.encryptor.setAlgorithm("PBEWITHMD5ANDDES");
		this.encryptor.setPassword(d.digest(variable));
		this.encryptor.initialize();
	}

	public static void main(String[] args) {
		boolean verbose = false;
		boolean decryptOnly = false;
		for (int i=0; i < args.length; i++) {
			if (args[i].equals("-v")) {
				verbose = true;
				continue;
			}
			if (args[i].equals("-d")) {
				decryptOnly = true;
				continue;
			}

			if (decryptOnly) {
				String black = args[i];
				String plain = ConfigEncryptor.instance().decrypt(black);
				if (verbose) {
					System.out.println("Decrypted value of [" + black + "] is [" + plain + "]");
				} else {
					System.out.println(plain);
				}
			}

			else {
				String plain = args[i];
				String black = ConfigEncryptor.instance().encrypt(plain);
				if (verbose) {
					System.out.println("Encrypted value of [" + plain + "] is [" + black + "]");
				} else {
					System.out.println(black);
				}
				String decrypt = ConfigEncryptor.instance().decrypt(black);
				if (verbose) {
					System.out.println("Decrypted back to [" + decrypt + "]");
				}
			}
		}
	}

	private static ConfigEncryptor _instance;

	private StandardPBEStringEncryptor encryptor = null;

}
