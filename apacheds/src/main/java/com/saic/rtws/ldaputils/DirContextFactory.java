package com.saic.rtws.ldaputils;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.lang.StringUtils;

import com.saic.rtws.commons.config.RtwsConfig;

public class DirContextFactory {
	
	private static String LDAP_CONTEXT_FACTORY_CLASS = "com.sun.jndi.ldap.LdapCtxFactory";

	private static Hashtable<String, String> env = new Hashtable<String, String>();
	
	private DirContextFactory() {
		// Not instantiable
	}
	
	public static DirContext getBaseDirContext(String password) throws NamingException {
		
		RtwsConfig config = RtwsConfig.getInstance();

		String providerUrl = config.getString("ldap.provider.url");
		String authentication = config.getString("ldap.authentication");
		String principal = config.getString("ldap.principal");
		String credentials;
		if (StringUtils.isNotBlank(password)) {
			credentials = password;
		} else {
			credentials = config.getString("ldap.credentials");
		}

		principal = principal.replaceAll("[|]", ",");
		
		StringBuilder url = new StringBuilder(providerUrl);

		env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY_CLASS);
		env.put(Context.PROVIDER_URL, url.toString());
		env.put(Context.SECURITY_AUTHENTICATION, authentication);
		env.put(Context.SECURITY_PRINCIPAL, principal);
		env.put(Context.SECURITY_CREDENTIALS, credentials);

		return new InitialDirContext(env);
		
	}
	
	public static DirContext getUserDirContext() throws NamingException {
		
		RtwsConfig config = RtwsConfig.getInstance();

		String providerUrl = config.getString("ldap.provider.url");
		String searchBaseDn = config.getString("ldap.search.baseDn");
		String authentication = config.getString("ldap.authentication");
		String principal = config.getString("ldap.principal");
		String credentials = config.getString("ldap.credentials");

		searchBaseDn = searchBaseDn.replaceAll("[|]", ",");
		principal = principal.replaceAll("[|]", ",");
		
		StringBuilder url = new StringBuilder(providerUrl);
		url.append('/').append(searchBaseDn);

		env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY_CLASS);
		env.put(Context.PROVIDER_URL, url.toString());
		env.put(Context.SECURITY_AUTHENTICATION, authentication);
		env.put(Context.SECURITY_PRINCIPAL, principal);
		env.put(Context.SECURITY_CREDENTIALS, credentials);

		return new InitialDirContext(env);
		
	}
	
}
