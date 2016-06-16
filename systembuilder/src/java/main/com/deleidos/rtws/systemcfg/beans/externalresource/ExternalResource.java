package com.deleidos.rtws.systemcfg.beans.externalresource;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.deleidos.rtws.systemcfg.beans.Resource;
import com.deleidos.rtws.systemcfg.beans.UserAddressable;

/**
 * Marker Interface
 * @author rayju
 */
//Maps value of "type" field to an Impl
@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,  
	include = JsonTypeInfo.As.PROPERTY,  
	property = "type")  
@JsonSubTypes({
	@Type(name = "jms", value = JmsResource.class),
	@Type(name = "custom", value = CustomExternalResource.class)
	}
)
/* Maps value of "minClassName" to the minimum class identifier.  DON'T FORGET A LEADING "." IN THE JSON. */ 
/*@JsonTypeInfo(
		use=JsonTypeInfo.Id.MINIMAL_CLASS,
		include=JsonTypeInfo.As.PROPERTY,
		property="minClassName")
*/
/* Maps impl via fully qualified class name in "class" property. */
/*
@JsonTypeInfo(
		use=JsonTypeInfo.Id.CLASS,
		include=JsonTypeInfo.As.PROPERTY,
		property="class")
*/
public interface ExternalResource extends Resource, UserAddressable {

}
