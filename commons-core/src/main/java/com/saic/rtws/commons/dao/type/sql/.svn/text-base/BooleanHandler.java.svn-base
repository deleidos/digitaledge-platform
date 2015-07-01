package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class BooleanHandler extends SqlTypeHandler<Boolean> {

	public BooleanHandler() {
		super("BOOLEAN", Boolean.class);
	}

	@Override
	public Boolean get(ResultSet object, String field) {
		try {
			boolean value = object.getBoolean(field);
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
	public void set(ResultSet record, String field, Boolean value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateBoolean(field, value.booleanValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public Boolean get(ResultSet object, int field) {
		try {
			boolean value = object.getBoolean(field);
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
	public void set(ResultSet record, int field, Boolean value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateBoolean(field, value.booleanValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public void set(PreparedStatement record, int field, Boolean value) {
		try {
			if (value == null) {
				record.setNull(field, Types.BOOLEAN);
			} else {
				record.setBoolean(field, value.booleanValue());
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
