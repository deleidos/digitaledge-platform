package com.saic.rtws.commons.util.scripting.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Inherited;

/**
 * An annotation that describes a script.  It provides the complete script signature and a brief description.
 * 
 * @author rainerr
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ScriptSignature {
	public String signature();
	
	public String description() default "";
}
