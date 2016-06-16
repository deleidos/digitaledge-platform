package com.deleidos.rtws.systemcfg.beans.externalresource;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.deleidos.rtws.systemcfg.beans.UserAddressableImpl;

@JsonIgnoreProperties(ignoreUnknown=true)
public class JmsResource extends UserAddressableImpl implements ExternalResource {
	private Boolean isSecure;
	private String url;
	private String username;
	private String password;
	
	public Boolean isSecure() {
		return isSecure;
	}
	public void setSecure(Boolean isSecure) {
		this.isSecure = isSecure;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public <T> boolean nullSafeEquals(T obj1, T obj2) {
		boolean result = false;
		
		if(obj1 == null && obj2 == null) {
			result = true;
		}
		else if(obj1 == null && obj2 != null) {
			result = false;
		}
		else if(obj1 != null && obj2 == null) {
			result = false;
		}
		else {
			result = obj1.equals(obj2);
		}
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if(obj != null && (obj instanceof JmsResource)) {
			JmsResource that = (JmsResource)obj;
			
			result = (super.equals(obj) && nullSafeEquals(this.isSecure, that.isSecure) &&
					StringUtils.equals(this.url, that.url) &&
					StringUtils.equals(this.username, that.username) &&
					StringUtils.equals(this.password, that.password));
		}
		
		return result;
	}
	
	@Override
	public int hashCode() {
		ArrayList<Object> fields = new ArrayList<Object>();
		fields.add(userLabel);
		fields.add(isSecure);
		fields.add(url);
		fields.add(username);
		fields.add(password);
		return Arrays.hashCode(fields.toArray());
	}
}