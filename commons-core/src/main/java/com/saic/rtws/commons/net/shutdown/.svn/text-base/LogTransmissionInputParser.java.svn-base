package com.saic.rtws.commons.net.shutdown;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * The Class LogTransmissionInputParser.
 */
public class LogTransmissionInputParser {

	/**
	 * Parses the.
	 * 
	 * @param input
	 *            the input
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
	public List<LogTransmissionRequest> parse(Object input)
			throws JSONException {
		List<LogTransmissionRequest> requests = new ArrayList<LogTransmissionRequest>();

		
		JSONArray logsToTransmit = JSONArray.fromObject(input);
		int len = logsToTransmit.size();
		for (int i = 0; i < len; i++) {
			JSONObject o = logsToTransmit.getJSONObject(i);
			LogTransmissionRequest r = (LogTransmissionRequest) JSONObject
					.toBean(o, LogTransmissionRequest.class);

			// Ensure conditions are valid
			if (r.getName() == null || r.getName().length() <= 0)
				throw new InvalidLogFileRequest(
						"The name of the log is invalid.  Specific entry:" + o);
			
			if ((r.getStart() < 0 && r.getEnd() >= 0) || (r.getStart() >= 0 && r.getEnd() < 0) )
				throw new InvalidLogFileRequest(
						"When requesting the end of the file, both start & end must be -1.  Specific entry:"
								+ o);

			requests.add(r);
		}

		return requests;

	}
}
