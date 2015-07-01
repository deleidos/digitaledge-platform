package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class DoubleHandler extends SqlTypeHandler<Double> {

	public DoubleHandler() {
		super("DOUBLE", Double.class);
	}

	@Override
	public Double get(ResultSet object, String field) {
		try {
			double value = object.getDouble(field);
			if(object.wasNull()) {
				return null;
			} else {
				return value;
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}
	
	@Override
	public void set(ResultSet record, String field, Double value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateDouble(field, value.doubleValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public Double get(ResultSet object, int field) {
		try {
			double value = object.getDouble(field);
			if(object.wasNull()) {
				return null;
			} else {
				return value;
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}
	
	@Override
	public void set(ResultSet record, int field, Double value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateDouble(field, value.doubleValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public void set(PreparedStatement record, int field, Double value) {
		try {
			if (value == null) {
				record.setNull(field, Types.NUMERIC);
			} else {
				record.setDouble(field, value.doubleValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
