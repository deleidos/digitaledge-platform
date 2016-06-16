package com.deleidos.rtws.systemcfg.beans;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;

import com.deleidos.rtws.systemcfg.beans.SystemConfig;

@Ignore("Base Class containing support methods.")
public class JsonDeserializeTest {
	public SystemConfig loadSystemConfigFromJsonResource(String resourceName) throws JsonParseException, IOException {
		return loadFromJsonResource(SystemConfig.class, resourceName);
	}
	
	protected <T> T loadFromJsonResource(Class<T> clazz, String resourceName) throws JsonParseException, IOException {
		T result = null;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		InputStream systemInputStream = null;
		try {
			systemInputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
			if(systemInputStream == null)
			{
				throw new IllegalArgumentException("Could not load json for testing");
			}
			result = mapper.readValue(systemInputStream, clazz);
		}
		finally {
			if(systemInputStream != null) {
				try {
					systemInputStream.close();
				}
				catch(Exception ignored){}
			}
		}
		
		return result;
	}
}
