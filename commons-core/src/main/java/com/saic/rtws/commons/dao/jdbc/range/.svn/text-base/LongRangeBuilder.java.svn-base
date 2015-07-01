package com.saic.rtws.commons.dao.jdbc.range;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.util.index.Range;

/**
 * Class for creating long ranges from query results.
 * @author floydac
 *
 */
public class LongRangeBuilder extends RangeBuilder<Long>{
	
	public LongRangeBuilder(){
		super();
	}
	
	public LongRangeBuilder(String low, String high){
		super(low,high);
	}
	
	public Range<Long> buildRecord(ResultSet result) {
		try {
			Long low = result.getLong(getLowFieldName());
			Long high = result.getLong(getHighFieldName());
			Range<Long> range = new Range<Long>(low,high);
			
			return range;
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		}
	}
}
