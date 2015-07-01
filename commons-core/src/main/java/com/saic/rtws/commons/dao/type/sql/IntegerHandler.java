package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class IntegerHandler extends SqlTypeHandler<Integer> {

	public IntegerHandler() {
		super("INTEGER", Integer.class);
	}

	@Override
	public Integer get(ResultSet object, String field) {
		try {
			int value = object.getInt(field);
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
	public void set(ResultSet record, String field, Integer value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateInt(field, value.intValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public Integer get(ResultSet object, int field) {
		try {
			int value = object.getInt(field);
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
	public void set(ResultSet record, int field, Integer value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateInt(field, value.intValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public void set(PreparedStatement record, int field, Integer value) {
		try {
			if (value == null) {
				record.setNull(field, Types.NUMERIC);
			} else {
				record.setInt(field, value.intValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
