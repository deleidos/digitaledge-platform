package com.deleidos.rtws.systemcfg.beans;

import org.apache.commons.lang3.StringUtils;


public class UserAddressableImpl implements UserAddressable{
	protected String userLabel;

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		
		if(obj != null && obj instanceof UserAddressable)
		{
			UserAddressable that = (UserAddressable)obj;
			result = StringUtils.equals(this.getUserLabel(), that.getUserLabel());		
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		return (userLabel == null ? 0 : userLabel.hashCode());
	}
}
