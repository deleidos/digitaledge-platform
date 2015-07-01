package com.saic.rtws.commons.dao.jdbc.range;

import java.sql.ResultSet;

import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.util.index.Range;

/**
 * Record builder implementation that builds a Range object from a low and high field.
 * @author floydac
 * 
 */
public abstract class RangeBuilder<T extends Comparable<T>> implements RecordBuilder<Range<T>>{

	/** The name of the result set field that represents the low value. */
	private String lowFieldName;

	/** The name of the result set field that represents the high value. */
	private String highFieldName;

	/**
	 * Constructor. Defaults lowFieldName and highFieldName.
	 */
	public RangeBuilder() {
		this("low", "high");
	}

	/**
	 * Constructor.
	 * 
	 * @param lowFieldName
	 *            The name of the result set field that represents the low value.
	 * @param highFieldName
	 *            The name of the result set field that represents the high value.
	 */
	public RangeBuilder(String lowFieldName, String highFieldName) {
		this.lowFieldName = lowFieldName;
		this.highFieldName = highFieldName;
	}

	/**
	 * The name of the result set field that represents the low value.
	 */
	public String getLowFieldName() {
		return lowFieldName;
	}

	/**
	 * The name of the result set field that represents the low value.
	 */
	public void setLowFieldName(String lowFieldName) {
		this.lowFieldName = lowFieldName;
	}

	/**
	 * The name of the result set field that represents the high value.
	 */
	public String getHighFieldName() {
		return highFieldName;
	}

	/**
	 * The name of the result set field that represents the high value.
	 */
	public void setHighFieldName(String highFieldName) {
		this.highFieldName = highFieldName;
	}

	/**
	 * Builds the range object.
	 */
	public abstract Range<T> buildRecord(ResultSet result);

}
