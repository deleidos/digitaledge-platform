package com.deleidos.rtws.systemcfg.beans.jms;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.deleidos.rtws.systemcfg.beans.UserAddressableImpl;

@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class JmsDestination extends UserAddressableImpl {
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if(obj != null && (obj instanceof JmsDestination)) {
			JmsDestination that = (JmsDestination)obj;
			
			result = (super.equals(that) &&
					StringUtils.equals(this.name, that.name));
		}
		
		return result;
	}
	
	@Override
	public int hashCode() {
		ArrayList<Object> fields = new ArrayList<Object>();
		fields.add(userLabel);
		fields.add(name);
		return Arrays.hashCode(fields.toArray());
	}
}
