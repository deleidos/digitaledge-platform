package ${package};

import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.deleidos.rtws.core.framework.Description;
import com.deleidos.rtws.core.framework.UserConfigured;
import com.deleidos.rtws.core.framework.parser.AbstractLineParser;

/**
 * A example parser for demonstration purposes
 */
@Description("A description of what this parser can do goes here.")
public class ExampleParser extends AbstractLineParser {

	private static final Logger log = Logger.getLogger(ExampleParser.class);	

	protected String headerKeyFilename;

	protected String headerKeys[];

	protected String defaultSource;

	protected String defaultAccessLabel;

	protected char delimiter = ',';

	protected boolean stopOnMissingField = true;

	/**
	 * No-Arg Constructor.
	 */
	public ExampleParser() {
		super("UTF-8");
	}

	/**
	 * Set header key file that describes the layout of the fields separated by
	 * commas.
	 */
	public void setHeaderKeyFile(String filename) {
		this.headerKeyFilename = filename;
	}

	/**
	 * @return the header key file
	 */
	public String getHeaderKeyFile() {
		return this.headerKeyFilename;
	}

	/**
	 * @return the header keys
	 */
	public String[] getHeaderKeys() {
		return this.headerKeys;
	}

	/**
	 * Set the delimiter for the data (does not affect header key file)
	 */
	@UserConfigured(value = ",", description = "The delimiter separating data fields.", flexValidator = { "RegExpValidator expression=^.$ noMatchError=\"Please provide a single character\"" })
	public void setDelimiter(String value) {
		delimiter = value.charAt(0);
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return Character.toString(this.delimiter);
	}

	/**
	 * Set the default source string of the data. Can be overridden by the input
	 * stream parameters.
	 */
	@UserConfigured(value = "UNKNOWN", description = "The string describing the source of the data.")
	public void setDefaultSource(String defaultSource) {
		this.defaultSource = defaultSource;
	}

	/**
	 * @return the default source
	 */
	public String getDefaultSource() {
		return this.defaultSource;
	}

	/**
	 * Set the default access label. Can be overridden by the input stream
	 * parameters or the translator.
	 */
	@UserConfigured(value = "UNCLASSIFIED", description = "The default access label to include with the data.")
	public void setDefaultAccessLabel(String defaultAccessLabel) {
		this.defaultAccessLabel = defaultAccessLabel;
	}

	/**
	 * @return the default access label
	 */
	public String getDefaultAccessLabel() {
		return this.defaultAccessLabel;
	}

	/**
	 * Set the stop-on-missing field.
	 */
	@UserConfigured(value = "true", description = "Specifies whether or not to stop processing a record when fields are missing.", flexValidator = { "RegExpValidator expression=^(true|false)$" })
	public void setStopOnMissingField(boolean stopOnMissingField) {
		this.stopOnMissingField = stopOnMissingField;
	}

	/**
	 * @return the stop-on-missing field
	 */
	public boolean getStopOnMissingField() {
		return this.stopOnMissingField;
	}

	/**
	 * Perform disposal work on the parser.
	 */
	public void dispose() {
		
	}

	/**
	 * Parse and return the next JSON record, or null if all done or error.
	 */
	public JSONObject parse() {
		try {
			JSONObject json = new JSONObject();

			String input = nextRecord();
			if (input == null) {
				throw new ParseException("Input data is missing.", -1);
			}

			String streamAccessLabel = null ; //info.getProperty(StandardHeader.ACCESS_LABEL_KEY);
			String accessLabel = (streamAccessLabel == null) ? defaultAccessLabel
					: streamAccessLabel;

			String streamSource = null; // info.getProperty(StandardHeader.SOURCE_KEY);
			String source = (streamSource == null) ? defaultSource
					: streamSource;
			HashMap<String, String> map = new HashMap<String, String>();
			Matcher m;

			/*
			 * Perform custom parsing work here and store results in json doc....
			 */

			return json;

		} catch (ParseException e) {
			log.error("Error parsing record in ExampleParser: ", e);
			return null;
		}
	}

	public void parseHeaders() {

	}
}
