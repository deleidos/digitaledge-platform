package com.saic.rtws.ldaputils;

import java.security.MessageDigest;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.apache.commons.codec.binary.Base64;

import com.saic.rtws.commons.config.RtwsConfig;

public class LdapModify {

	LdapModify() {
	}

	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.println("USAGE: LdapModify [changeSystemPassword]");
			System.exit(-1);
		}
		
		if ("changeSystemPassword".equalsIgnoreCase(args[0])) {
			try {
				LdapModify ldap = new LdapModify();
				String principal = RtwsConfig.getInstance().getString("ldap.principal");
				principal = principal.replaceAll("[|]", ",");
				String credentials = RtwsConfig.getInstance().getString("ldap.credentials");
				boolean result = ldap.modifySystemPassword(principal, "secret", credentials);
				if (result) {
					System.out.println("SUCCESSFULLY changed LDAP system password");
				}
			} catch (NamingException e) {
				System.out.println("FAILED to change LDAP system password");
				e.printStackTrace();
			}
		}
	}

	public boolean modifySystemPassword(String uid, String oldPassword, String newPassword)
			throws NamingException {

		try {
			DirContext ctx = DirContextFactory.getUserDirContext();
			System.out.println("LDAP system password has already been changed to the correct value.");
			ctx.close();
			return false;
		} catch (Exception ignore) {}
		
		// Hash the new password as MD5, encode in Base64, and oh yeah, prepend with "{MD5}"
		String newPasswordBase64MD5Hash = "{MD5}" + new String(Base64.encodeBase64(toMD5Hash(newPassword)));
		
		DirContext ctx = DirContextFactory.getBaseDirContext(oldPassword);
		ModificationItem[] mods = new ModificationItem[1];
		Attribute attr = new BasicAttribute("userpassword", newPasswordBase64MD5Hash);
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
		ctx.modifyAttributes(uid, mods);
		ctx.close();
		return true;
		
	}

	private byte[] toMD5Hash(String originalText) {
		byte[] originalTextBytes;
		MessageDigest md;
		
		try {
			originalTextBytes = originalText.getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			// Something failed with either encoding the bytes
			// or the message digest getting an instance of MD5.
			// Return the original text
			return originalText.getBytes();
		}

		return md.digest(originalTextBytes);
	}

}
