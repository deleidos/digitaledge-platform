package com.saic.rtws.commons.dao.jdbc.spatial;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.AbstractResultHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.model.geo.Coordinate;
import com.saic.rtws.commons.util.index.CoordinateIndex;

/**
 * Result handler implementation that load data into a CoordinateIndex.
 */
public class SpatialIndexResultHandler<T> extends AbstractResultHandler<CoordinateIndex<T>> {

	/** Record builder used to extract the coordinate fields from the result set. */
	private CoordinateBuilder coordinateBuilder = new CoordinateBuilder();

	/** Record builder used to extract the record from the result set. */
	private RecordBuilder<T> recordBuilder;

	/**
	 * Constructor.
	 */
	public SpatialIndexResultHandler() {
		super(new CoordinateIndex<T>());
	}

	/**
	 * Record builder used to extract the coordinate fields from the result set.
	 */
	public CoordinateBuilder getCoordinateBuilder() {
		return coordinateBuilder;
	}

	/**
	 * Record builder used to extract the coordinate fields from the result set. The default handler simply uses the
	 * fields "latitude" and "longitude". You can customize the field names by changing the properties of object
	 * returned by the getter.
	 */
	public void setCoordinateBuilder(CoordinateBuilder coordinateBuilder) {
		this.coordinateBuilder = coordinateBuilder;
	}

	/**
	 * Record builder used to extract the record from the result set.
	 */
	public RecordBuilder<T> getRecordBuilder() {
		return recordBuilder;
	}

	/**
	 * Record builder used to extract the record from the result set.
	 */
	public void setRecordBuilder(RecordBuilder<T> recordBuilder) {
		this.recordBuilder = recordBuilder;
	}

	/**
	 * Iterates over the given result set, loading the build records/coordinates into a spatial index.
	 */
	public void handle(ResultSet result) {
		try {
			while (result.next()) {
				Coordinate coordinate = coordinateBuilder.buildRecord(result);
				T location = recordBuilder.buildRecord(result);
				getResult().associate(coordinate, location);
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}

}
