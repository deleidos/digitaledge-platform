package com.saic.rtws.commons.dao.jdbc.spatial;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.model.geo.Coordinate;

/**
 * Record builder implementation that builds a Coordinate object from a lat/lon field.
 */
public class CoordinateBuilder implements RecordBuilder<Coordinate> {

	/** The name of the result set field that represents the latitude value. */
	private String latFieldName = "latitude";

	/** The name of the result set field that represents the longitude value. */
	private String lonFieldName = "longitude";

	/**
	 * Constructor.
	 */
	public CoordinateBuilder() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param latFieldName
	 *            The name of the result set field that represents the latitude value.
	 * @param lonFieldName
	 *            The name of the result set field that represents the longitude value.
	 */
	public CoordinateBuilder(String latFieldName, String lonFieldName) {
		this.latFieldName = latFieldName;
		this.lonFieldName = lonFieldName;
	}

	/**
	 * The name of the result set field that represents the latitude value.
	 */
	public String getLatFieldName() {
		return latFieldName;
	}

	/**
	 * The name of the result set field that represents the latitude value.
	 */
	public void setLatFieldName(String latFieldName) {
		this.latFieldName = latFieldName;
	}

	/**
	 * The name of the result set field that represents the longitude value.
	 */
	public String getLonFieldName() {
		return lonFieldName;
	}

	/**
	 * The name of the result set field that represents the longitude value.
	 */
	public void setLonFieldName(String lonFieldName) {
		this.lonFieldName = lonFieldName;
	}

	/**
	 * Builds the coordinate object.
	 */
	public Coordinate buildRecord(ResultSet result) {
		try {
			Coordinate coordinate = new Coordinate();
			coordinate.setLatitude(result.getDouble(latFieldName));
			coordinate.setLongitude(result.getDouble(lonFieldName));
			return coordinate;
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}

}
