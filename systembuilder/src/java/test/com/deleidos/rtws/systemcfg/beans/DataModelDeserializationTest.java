package com.deleidos.rtws.systemcfg.beans;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

import com.deleidos.rtws.systemcfg.beans.DataModel;

public class DataModelDeserializationTest extends JsonDeserializeTest {
	private static final String USER_LABEL = "Sales Data";
	private static final DataModel.DataModelVisibility VISIBILITY = DataModel.DataModelVisibility.PRIVATE;
	private static final String PATH = "salesrus/models";
	private static final String FILENAME = "sales";
	private static final String VERSION = "2.1.0";
	
	@Test
	public void testSimple() throws JsonParseException, IOException {
		DataModel dataModel = loadFromJsonResource(DataModel.class, "com/deleidos/rtws/systemcfg/beans/simpleDataModel.json");
		Assert.assertNotNull(dataModel);
		Assert.assertEquals(dataModel.getUserLabel(), USER_LABEL);
		Assert.assertEquals(dataModel.getVisibility(), VISIBILITY);
		Assert.assertEquals(dataModel.getPath(), PATH);
		Assert.assertEquals(dataModel.getFilename(), FILENAME);
		Assert.assertEquals(dataModel.getVersion(), VERSION);
	}
	
	@Test
	public void testInvalidVisibility() throws JsonParseException, IOException {
		try
		{
			DataModel dataModel = loadFromJsonResource(DataModel.class, "com/deleidos/rtws/systemcfg/beans/simpleDataModelWithInvalidVisibility.json");
			Assert.fail("Visibility value was parsed correctly and should not have.");
		}
		catch(JsonMappingException expectedException)
		{
			// This exception is expected as the data contained an invalid visibility value.
		}
	}
}
