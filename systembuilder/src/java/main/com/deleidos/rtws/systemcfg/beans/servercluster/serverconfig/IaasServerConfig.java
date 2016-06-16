package com.deleidos.rtws.systemcfg.beans.servercluster.serverconfig;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.deleidos.rtws.systemcfg.beans.servercluster.serverconfig.Ec2ServerConfig.EC2_SERVER_SIZE;


/**
 * Marker Interface
 * @author rayju
 */
@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,  
	include = JsonTypeInfo.As.PROPERTY,  
	property = "type")  
@JsonSubTypes({
	@Type(name = "ec2", value = Ec2ServerConfig.class)
	}
)
public interface IaasServerConfig {
	public String getImageId();
	public void setImageId(String imageId);
	
	public EC2_SERVER_SIZE getSize();
	public void setSize(EC2_SERVER_SIZE size);
	
	public String getPreferredRegion();
	public void setPreferredRegion(String region);
	
	public String getPreferredZone();
	public void setPreferredZone(String zone);
}
