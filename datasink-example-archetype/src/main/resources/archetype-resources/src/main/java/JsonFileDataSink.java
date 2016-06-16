package ${package};

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import net.sf.json.JSONObject;

import com.deleidos.rtws.commons.exception.InitializationException;
import com.deleidos.rtws.core.framework.DataSink;
import com.deleidos.rtws.core.framework.Description;
import com.deleidos.rtws.core.framework.SystemConfigured;
import com.deleidos.rtws.core.framework.processor.AbstractDataSink;

@Description("This is a data sink for an Json File Data Sink.")
public class JsonFileDataSink extends AbstractDataSink {
	
	final private String DATA_SINK_FILE_PATH = "/tmp/datasink";		// Location of the Data Sink File
	final private String DATA_SINK_FILE = "datasinkFile.txt";		// Data Sink File name
	private BufferedWriter dataSinkOut;								// A writer to the Data Sink File
	
	/** Constructor. */
	public JsonFileDataSink() {
		super();
	}
	
	/**
	 * Perform disposal work for the data sink.
	 * 
	 * In our case, this means closing the Data Sink File.
	 */
	public void dispose() {
		// Close Connection to Data Sink File
		try {
			System.out.println("DEBUG: About to close the data sink file");
			dataSinkOut.close();
			System.out.println("DEBUG: Data sink file successfully closed");
		} catch (IOException e) {
			System.out.println("ERROR: Data sink file failed to close: " + e.getMessage());
		}
		
		return;
	}

	/**
	 * Perform initialization work for the data sink.
	 * 
	 * In our case, we need to make sure the Data Sink File path exists
	 * and then open the file.  If an error occurs with either of these
	 * we have failed to initialize and therefore need to throw an
	 * InitializationException.
	 * 
	 * @throws InitializationException Could throw an InitializationException
	 */
	public void initialize() throws InitializationException {
		
		// Make sure the file path exists
		if(!(new File(DATA_SINK_FILE_PATH).exists())) {
			// The file path does not exist so we must create it.
			System.out.println("DEBUG: " + DATA_SINK_FILE_PATH + " does not exist. Creating directories");
			if(!new File(DATA_SINK_FILE_PATH).mkdirs()) {
				// The path failed to be created so throw an InitializationException
				throw new InitializationException(DATA_SINK_FILE_PATH + " was not successfully created");
			}
			System.out.println("DEBUG: " + DATA_SINK_FILE_PATH + " successfully created");
		}
		else {
			// The file path exists, continue on
			System.out.println("DEBUG: " + DATA_SINK_FILE_PATH + " exists");
		}
		
		// Open the connection to Data Sink File
		try {
			System.out.println("DEBUG: About to open the data sink file");
			dataSinkOut = new BufferedWriter(new FileWriter(DATA_SINK_FILE_PATH + File.separator + DATA_SINK_FILE));
			System.out.println("DEBUG: Data sink file successfully opened");
		} catch (IOException e) {
			// File failed to open so throw an InitializationException
			throw new InitializationException("Data sink file failed to open: " + e.getMessage(), e);
		}
		
		return;
	}
	
	/**
	 * Process the record to be stored in the data sink.
	 * 
	 * In our case, this means converting the record into text format for the
	 * Data Sink File.
	 */
	protected void processInternal(JSONObject record, FlushCounter counter) {
		// We want to count the record as received.  AbstractData sink has a
		// FlushCounter in it which counts both the number of records received
		// as well as the number of bytes received.  This is useful when using
		// data sinks which require caching before flushing to the data sink.
		// In our case, we do not cache the records before flushing to the
		// data sink, but we can still track the amount received.
		counter.increment(1, record.toString().length());

		// Write the record directly to the Data Sink File
		try {
			System.out.println("DEBUG: About to write record \"" + record.toString() + "\" to data sink file");
			dataSinkOut.write(record.toString() + "");
			System.out.println("DEBUG: Record successfully written to data sink file");
		} catch (IOException e) {
			// Writing to file failed so log the error
			System.out.println("ERROR: Failed to write record to data sink file: " + e.getMessage());
		}
	}

	/**
	 * Perform flush for the data sink.
	 */
	public void flush() {
		// In our case, there is no caching to flush.
		
		// A data sink can be configured to flush based on any combination
		// of the number of records between flushes, number of
		// bytes between flushes, or amount of time(ms) between flushes.
		// If none of these are configured, the flush must be called explicitly.
		System.out.println("DEBUG: Data sink flush() called erroneously");
	}
	
	/**
	 * This data sink's name.
	 */
	@Override
	@SystemConfigured(value = "An Example Datasink for the DigitalEdge platform which consumes JSON data and writes it to a local file.")
	public void setName(String name) {
		super.setName(name);
	}

	/**
	 * A short name (less than 12 characters) without special characters for your datasink.
	 */
	@Override
	@SystemConfigured(value = "example_json")
	public void setShortname(String shortname) {
		super.setShortname(shortname);
	}

}
