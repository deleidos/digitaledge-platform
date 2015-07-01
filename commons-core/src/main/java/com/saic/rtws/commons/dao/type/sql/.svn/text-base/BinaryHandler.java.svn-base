package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class BinaryHandler extends SqlTypeHandler<byte[]> {

	public BinaryHandler() {
		super("BINARY", byte[].class);
	}

	@Override
	public byte[] get(ResultSet object, String field) {
		try {
			byte[] value = object.getBytes(field);
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
	public void set(ResultSet record, String field, byte[] value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateBytes(field, value);
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public byte[] get(ResultSet object, int field) {
		try {
			byte[] value = object.getBytes(field);
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
	public void set(ResultSet record, int field, byte[] value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateBytes(field, value);
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public void set(PreparedStatement record, int field, byte[] value) {
		try {
			if (value == null) {
				record.setNull(field, Types.NUMERIC);
			} else {
				record.setBytes(field, value);
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
