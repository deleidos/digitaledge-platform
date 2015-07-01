package com.saic.rtws.commons.dao.jdbc.spatial;

import net.sf.json.JSONObject;

/**
 * Data access object used to load the ZIPCODE_LOCATIONS table into a spatial index.
 */
public class PostalLocationDao extends AbstractSpatialDao<JSONObject> {
	@Override
	protected String buildSql() {
		return "SELECT latitude, longitude, zip_code, city, state FROM dimensions.zipcode_locations";
	}
}
