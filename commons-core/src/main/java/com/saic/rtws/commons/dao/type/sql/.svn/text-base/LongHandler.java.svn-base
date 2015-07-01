package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class LongHandler extends SqlTypeHandler<Long> {

	public LongHandler() {
		super("LONG", Long.class);
	}

	@Override
	public Long get(ResultSet object, String field) {
		try {
			long value = object.getLong(field);
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
	public void set(ResultSet record, String field, Long value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateLong(field, value.longValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public Long get(ResultSet object, int field) {
		try {
			long value = object.getLong(field);
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
	public void set(ResultSet record, int field, Long value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateLong(field, value.longValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public void set(PreparedStatement record, int field, Long value) {
		try {
			if (value == null) {
				record.setNull(field, Types.NUMERIC);
			} else {
				record.setLong(field, value.longValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
