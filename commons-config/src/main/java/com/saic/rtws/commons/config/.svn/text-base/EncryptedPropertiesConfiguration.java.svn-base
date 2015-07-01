package com.saic.rtws.commons.config;

import java.io.File;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.properties.PropertyValueEncryptionUtils;

/**
 * <p>
 * A wrapper around Apache Commons Configuration to decrypt String properties
 * that are encrypted at rest.
 * </p>
 * <p>
 * An example of an encrypted property is:
 * <ul>
 * <li><tt>my.resource.password=ENC(0IRHmPASNUQxSrBfWGGxklNFQ5cuYqlW)</tt></li>
 * </ul>
 * </p>
 * 
 * @author moserm
 */
public class EncryptedPropertiesConfiguration extends PropertiesConfiguration {

	public EncryptedPropertiesConfiguration() {
		super();
	}

	public EncryptedPropertiesConfiguration(File f)  throws ConfigurationException {
		super(f);
	}

	public EncryptedPropertiesConfiguration(String s)  throws ConfigurationException {
		super(s);
	}

	public EncryptedPropertiesConfiguration(URL u)  throws ConfigurationException {
		super(u);
	}

	@Override
	public void setProperty(String key, Object value) {
		if (value instanceof String) {
			if (key.contains("password") || key.contains("credentials")) {
				String valueStr = (String) value;
				if (! PropertyValueEncryptionUtils.isEncryptedValue(valueStr)) {
					try {
						value = PropertyValueEncryptionUtils.encrypt(valueStr, ConfigEncryptor.instance());
					} catch (EncryptionOperationNotPossibleException e) {
						// Property could not be encrypted, just use the normal property
					}
				}
			}
		}
		super.setProperty(key, value);
	}

	@Override
	public void addProperty(String key, Object value) {
		if (value instanceof String) {
			if (key.contains("password") || key.contains("credentials")) {
				String valueStr = (String) value;
				if (! PropertyValueEncryptionUtils.isEncryptedValue(valueStr)) {
					try {
						value = PropertyValueEncryptionUtils.encrypt(valueStr, ConfigEncryptor.instance());
					} catch (EncryptionOperationNotPossibleException e) {
						// Property could not be encrypted, just use the normal property
					}
				}
			}
		}
		super.addProperty(key, value);
	}

	@Override
	public Object getProperty(String key) {
		Object value = super.getProperty(key);
		if (value instanceof String) {
			String valueStr = (String) value;
			try {
				if (PropertyValueEncryptionUtils.isEncryptedValue(valueStr)) {
					return PropertyValueEncryptionUtils.decrypt(valueStr, ConfigEncryptor.instance());
				}
			} catch (EncryptionOperationNotPossibleException e) {
				// Encrypted property could not be decrypted, just return the encrypted property
			}
		}
		return value;
	}

}
