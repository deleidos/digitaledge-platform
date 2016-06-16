package ${package};

import net.sf.json.JSONObject;

import com.deleidos.rtws.commons.exception.InitializationException;
import com.deleidos.rtws.core.framework.DataSink;
import com.deleidos.rtws.core.framework.Description;
import com.deleidos.rtws.core.framework.SystemConfigured;
import com.deleidos.rtws.core.framework.processor.AbstractDataSink;

@Description("This is a data sink for an Example Data Sink.")
public class ExampleDataSink extends AbstractDataSink  {
	
	/** Constructor. */
	public ExampleDataSink() {
		super();
	}
	
	/**
	 * Perform disposal work for the data sink.
	 */
	public void dispose() {
		//TODO: Close Connection to DataSink
		return;
	}

	/**
	 * Perform initialization work for the data sink.
	 * @throws InitializationException Could throw an InitializationException
	 */
	public void initialize() throws InitializationException {
		//TODO: Open Connection to DataSink
		return;
	}
	
	/**
	 * Process the record for the data sink.
	 */
	protected void processInternal(JSONObject record, FlushCounter counter) {
		//TODO: Handle record for this DataSink
		counter.increment(1, record.toString().length());
	}

	/**
	 * Perform flush for the data sink.
	 */
	public void flush() {
		//TODO: Flush Event Handle
	}
	
	/**
	 * This data sink's name.
	 */
	@Override
	@SystemConfigured(value = "An Example Datasink for the DigitalEdge platform.")
	public void setName(String name) {
		super.setName(name);
	}

	/**
	 * A short name (less than 12 characters) without special characters for your datasink.
	 */
	@Override
	@SystemConfigured(value = "example")
	public void setShortname(String shortname) {
		super.setShortname(shortname);
	}

}
