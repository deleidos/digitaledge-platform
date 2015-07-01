package com.saic.rtws.commons.dao.jdbc.range;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.util.index.Range;

/**
 * Class for creating integer ranges from query results.
 * @author floydac
 *
 */
public class IntegerRangeBuilder extends RangeBuilder<Integer> {

	public IntegerRangeBuilder(){
		super();
	}
	
	public IntegerRangeBuilder(String low, String high){
		super(low,high);
	}

	/**
	 * Given the result of a query, extracts the low and high values within it
	 */
	public Range<Integer> buildRecord(ResultSet result) {
		try {
			Integer low = result.getInt(getLowFieldName());
			Integer high = result.getInt(getHighFieldName());
			Range<Integer> range = new Range<Integer>(low,high);
			
			return range;
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}

}
