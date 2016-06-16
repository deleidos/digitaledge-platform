package ${package};

import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.deleidos.rtws.core.framework.parser.AbstractLineParser;

public class SimpleLogParser extends AbstractLineParser {

	private static final Logger log = Logger.getLogger(SimpleLogParser.class);
	
	//Timestamp format: "YYYY-MM-DD HH:MM:SS" or "WWW MMM DD HH:MM:SS" or "MMM DD HH:MM:SS" or "DD/MMM/YYYY:HH:MM:SS"
	//or "HH:MM:SS"
	protected static final String TIMESTAMP_REGEX = 
			"((([0-9]{1,2}/[A-z]{3}/[0-9]{4}:)|([0-9]{4}-[0-9]{2}-[0-9]{2} *)|(([A-z]{3} *)+[0-9]{1,2} ))*[0-9]{2}:[0-9]{2}:[0-9]{2})";
	protected static final Pattern TIMESTAMP_PATTERN = Pattern.compile(TIMESTAMP_REGEX);
	
	protected String defaultSource;
	
	protected String defaultAccessLabel;
	
	public SimpleLogParser() {
		super("UTF-8");
	}

	@Override
	public void parseHeaders() {
	}
	
	public JSONObject parse() {
		try {
			String input = nextRecord();
			if (input == null) return null;
			
			String streamAccessLabel = null; //info.getProperty(StandardHeader.ACCESS_LABEL_KEY);
			String accessLabel = (streamAccessLabel == null) ? defaultAccessLabel : streamAccessLabel;
			
			String streamSource = null; //info.getProperty(StandardHeader.SOURCE_KEY);
			String source = (streamSource == null) ? defaultSource : streamSource;
			HashMap<String, String> map = new HashMap<String, String>();
			Matcher m;
			
			//Step One: Process Timestamp
			m = TIMESTAMP_PATTERN.matcher(input);
			if(!m.find())
				throw new ParseException("No timestamp found.", -1);
			else
				map.put("TIMESTAMP", m.group());
				
			//Cut the timestamp out of the input so it doesn't get in the way
			input = input.replaceFirst(TIMESTAMP_REGEX, "");
			
			//Step two: Process Message (the rest of the input)
			map.put("MESSAGE", input);
			
			JSONObject json = new JSONObject();
			json.element("TIMESTAMP", map.get("TIMESTAMP"));
			json.element("MESSAGE", map.get("MESSAGE"));
			return json;
			
		} catch (ParseException e) {
			log.error("Error parsing record in SimpleLogParser: ", e);
			return null;
		}
	}
}
