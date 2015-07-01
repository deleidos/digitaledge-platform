package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class FloatHandler extends SqlTypeHandler<Float> {

	public FloatHandler() {
		super("FLOAT", Float.class);
	}

	@Override
	public Float get(ResultSet object, String field) {
		try {
			float value = object.getFloat(field);
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
	public void set(ResultSet record, String field, Float value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateFloat(field, value.floatValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public Float get(ResultSet object, int field) {
		try {
			float value = object.getFloat(field);
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
	public void set(ResultSet record, int field, Float value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateFloat(field, value.floatValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public void set(PreparedStatement record, int field, Float value) {
		try {
			if (value == null) {
				record.setNull(field, Types.NUMERIC);
			} else {
				record.setFloat(field, value.floatValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
