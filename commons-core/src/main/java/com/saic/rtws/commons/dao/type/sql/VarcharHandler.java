package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class VarcharHandler extends SqlTypeHandler<String> {

	public VarcharHandler() {
		super("VARCHAR", String.class);
	}

	@Override
	public String get(ResultSet object, String field) {
		try {
			String value = object.getString(field);
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
	public void set(ResultSet record, String field, String value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateString(field, value);
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public String get(ResultSet object, int field) {
		try {
			String value = object.getString(field);
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
	public void set(ResultSet record, int field, String value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateString(field, value);
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

	@Override
	public void set(PreparedStatement record, int field, String value) {
		try {
			if (value == null) {
				record.setNull(field, Types.VARCHAR);
			} else {
				record.setString(field, value);
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
