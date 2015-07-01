package com.saic.rtws.commons.dao.jdbc.range;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.AbstractResultHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.util.index.Index;
import com.saic.rtws.commons.util.index.NestedMapRangeIndex;
import com.saic.rtws.commons.util.index.Range;
import com.saic.rtws.commons.util.index.RangeIndex;

/**
 * Result handler implementation that loads data into a RangeIndex.
 * @author floydac
 *
 * @param <T> The type of value that the range extends over (ex. integers, etc)
 * @param <E> The object representation of a record from a table. (ex. JSON, XML, etc)
 */
public class RangeIndexResultHandler<T extends Comparable<T>,E> extends AbstractResultHandler<RangeIndex<T,E>> {

	/** Record builder used to extract the range fields from the result set. */
	private RangeBuilder<T> rangeBuilder;

	/** Record builder used to extract the record from the result set. */
	private RecordBuilder<E> recordBuilder;

	/**
	 * Constructor.
	 */
	public RangeIndexResultHandler(){
		super();
	}
	
	public RangeIndexResultHandler(RangeIndex<T,E> index){
		super(index);
	}

	/**
	 * Record builder used to extract the  fields from the result set.
	 */
	public RangeBuilder<T> getRangeBuilder() {
		return rangeBuilder;
	}

	/**
	 * Record builder used to extract the range fields from the result set. The default handler has no
	 * default field names. You must add the field names by changing the properties of object
	 * returned by the getter.
	 */
	public void setRangeBuilder(RangeBuilder<T> rangeBuilder) {
		this.rangeBuilder = rangeBuilder;
	}

	/**
	 * Record builder used to extract the record from the result set.
	 */
	public RecordBuilder<E> getRecordBuilder() {
		return recordBuilder;
	}

	/**
	 * Record builder used to extract the record from the result set.
	 */
	public void setRecordBuilder(RecordBuilder<E> recordBuilder) {
		this.recordBuilder = recordBuilder;
	}

	/**
	 * Iterates over the given result set, loading the build records/ranges into a range index.
	 */
	public void handle(ResultSet result) {
		try {
			while (result.next()) {
				Range<T> range = rangeBuilder.buildRecord(result);
				E value = recordBuilder.buildRecord(result);
				getResult().associate(range, value);
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}
	
	
}
