package com.deleidos.rtws.systemcfg.validation;

import javax.xml.bind.ValidationException;

public class SystemBuilderValidator {

	private ComponentValidater[] validators;
	
	public boolean validateAll(Object[] beans, String version) throws ValidationException {
		for(ComponentValidater  validator : validators)  {
			validator.validate(beans, version);
		}
		
		return true;
	}
}
