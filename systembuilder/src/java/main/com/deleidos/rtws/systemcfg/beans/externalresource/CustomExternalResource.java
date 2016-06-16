package com.deleidos.rtws.systemcfg.beans.externalresource;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.deleidos.rtws.systemcfg.beans.UserAddressableImpl;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CustomExternalResource extends UserAddressableImpl implements ExternalResource {
	private String className;
	private Object beanConfig;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Object getBeanConfig() {
		return beanConfig;
	}
	public void setBeanConfig(Object beanConfig) {
		this.beanConfig = beanConfig;
	}
}
