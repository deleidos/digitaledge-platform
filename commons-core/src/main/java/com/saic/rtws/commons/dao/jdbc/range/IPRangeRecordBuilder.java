package com.saic.rtws.commons.dao.jdbc.range;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;

/**
 * Class for creating a JSON object representing the IPTOCOUNTRY table.
 * @author floydac
 *
 */
public class IPRangeRecordBuilder implements RecordBuilder<JSONObject>{
	
	public JSONObject buildRecord(ResultSet result) {
		try {
			JSONObject iprange = new JSONObject();
			iprange.put(IPRangeDao.ipstart, result.getLong(IPRangeDao.ipstart));
			iprange.put(IPRangeDao.ipend, result.getLong(IPRangeDao.ipend));
			iprange.put(IPRangeDao.country, result.getString(IPRangeDao.country));
			iprange.put(IPRangeDao.region, result.getString(IPRangeDao.region));
			iprange.put(IPRangeDao.postalCode, result.getString(IPRangeDao.postalCode));
			iprange.put(IPRangeDao.latitude, result.getString(IPRangeDao.latitude));
			iprange.put(IPRangeDao.longitude, result.getString(IPRangeDao.longitude));
			iprange.put(IPRangeDao.metroCode, result.getString(IPRangeDao.metroCode));
			iprange.put(IPRangeDao.areaCode, result.getString(IPRangeDao.areaCode));
			return iprange;
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}
}
