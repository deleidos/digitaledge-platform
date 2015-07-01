package com.saic.rtws.commons.dao.jdbc.spatial;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;

/**
 * Record builder implementation used to convert a record from the ZIPCODE_LOCATIONS table into a JSON object.
 */
public class PostalLocationRecordBuilder implements RecordBuilder<JSONObject> {
	public JSONObject buildRecord(ResultSet result) {
		try {
			JSONObject location = new JSONObject();
			location.put("latitude", result.getDouble("latitude"));
			location.put("longitude", result.getDouble("longitude"));
			location.put("postalCode", result.getString("zip_code"));
			location.put("city", result.getString("city"));
			location.put("state", result.getString("state"));
			location.put("country", "USA");
			return location;
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}
}
