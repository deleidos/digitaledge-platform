package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class DateHandler extends SqlTypeHandler<java.util.Date> {

	public DateHandler() {
		super("DATE", java.util.Date.class);
	}

	@Override
	public java.util.Date get(ResultSet object, String field) {
		try {
			java.sql.Date value = object.getDate(field);
			if(object.wasNull()) {
				return null;
			} else {
				return new java.util.Date(value.getTime());
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}
	
	@Override
	public void set(ResultSet record, String field, java.util.Date value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateDate(field, new java.sql.Date(value.getTime()));
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public java.util.Date get(ResultSet object, int field) {
		try {
			java.sql.Date value = object.getDate(field);
			if(object.wasNull()) {
				return null;
			} else {
				return new java.util.Date(value.getTime());
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}
	
	@Override
	public void set(ResultSet record, int field, java.util.Date value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateDate(field, new java.sql.Date(value.getTime()));
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public void set(PreparedStatement record, int field, java.util.Date value) {
		try {
			if (value == null) {
				record.setNull(field, Types.TIMESTAMP);
			} else {
				record.setDate(field, new java.sql.Date(value.getTime()));
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
