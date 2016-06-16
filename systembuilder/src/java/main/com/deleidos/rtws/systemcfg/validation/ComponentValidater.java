package com.deleidos.rtws.systemcfg.validation;

import javax.xml.bind.ValidationException;

public interface ComponentValidater {
	public void validate(Object bean, String version) throws ValidationException;
}
