package ${package};

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleLocalFileTransportServiceTest {

	private static SimpleLocalFileTransportService transport = new SimpleLocalFileTransportService();	// An instance of the transport
	private String baseDir;		// A string to the user's base directory
	
	@Before
	public void setUp() {			
		transport.setWatchedDirectory("./src/test/resource/");
	}
	
	@After
	public void tearDown() {
		transport.terminate();
	}
	
	@Test
	public void testExecute() throws Exception{
		transport.execute();
		Thread.sleep(1000);

		//assert(transport.getMessagesSent() > 0);
	}
}
