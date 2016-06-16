package com.deleidos.rtws.systemcfg.composers;

import java.io.InputStream;
import javax.xml.bind.MarshalException;

import com.deleidos.rtws.systemcfg.beans.SystemConfig;
import com.deleidos.rtws.systemcfg.serialize.DefinitionSerializer;

public abstract class AbstractDefinitionComposer implements DefinitionComposer {
	protected DefinitionSerializer serializer;
	
	public abstract void writeFile(String version, String fileName);
	public abstract String compose(SystemConfig systemConfig);
	
	public abstract void loadDefaults(InputStream  stream) throws MarshalException;
	
	public void setDefinitionSerializer(DefinitionSerializer serializer) {
		this.serializer = serializer;
	}
	
	public DefinitionSerializer getDefinitionSerializer() {
		return serializer;
	}
	
}
