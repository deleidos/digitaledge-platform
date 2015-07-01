package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import com.saic.rtws.commons.dao.exception.DataAccessException;
import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.exception.DataStorageException;

public class TimestampHandler extends SqlTypeHandler<Date> {

	public TimestampHandler() {
		super("TIMESTAMP", Date.class);
	}

	@Override
	public Date get(ResultSet object, String field) {
		try {
			Timestamp value = object.getTimestamp(field);
			if(object.wasNull()) {
				return null;
			} else {
				return new Date(value.getTime());
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}
	
	@Override
	public void set(ResultSet record, String field, Date value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateTimestamp(field, new Timestamp(value.getTime()));
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public Date get(ResultSet object, int field) {
		try {
			Timestamp value = object.getTimestamp(field);
			if(object.wasNull()) {
				return null;
			} else {
				return new Date(value.getTime());
			}
		} catch (SQLException e) {
			throw new DataRetrievalException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}
	
	@Override
	public void set(ResultSet record, int field, Date value) {
		try {
			if (value == null) {
				record.updateNull(field);
			} else {
				record.updateTimestamp(field, new Timestamp(value.getTime()));
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}

	}

	@Override
	public void set(PreparedStatement record, int field, Date value) {
		try {
			if (value == null) {
				record.setNull(field, Types.TIMESTAMP);
			} else {
				record.setTimestamp(field, new Timestamp(value.getTime()));
			}
		} catch (SQLException e) {
			throw new DataStorageException(e);
		} catch (Exception e) {
			throw new DataAccessException("Unexpected error.", e);
		}
	}

}
