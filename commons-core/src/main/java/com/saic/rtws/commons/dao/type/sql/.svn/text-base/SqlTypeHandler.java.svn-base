package com.saic.rtws.commons.dao.type.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.saic.rtws.commons.dao.type.AbstractTypeHandler;


public abstract class SqlTypeHandler<T> extends AbstractTypeHandler<T> {

	public static final SqlTypeHandler<Boolean> BOOLEAN = new BooleanHandler();
	
	public static final SqlTypeHandler<String> VARCHAR = new VarcharHandler();
	
	public static final SqlTypeHandler<Date> DATE = new DateHandler();
	public static final SqlTypeHandler<Date> TIME = new TimeHandler();
	public static final SqlTypeHandler<Date> TIMESTAMP = new TimestampHandler();
	
	public static final SqlTypeHandler<Number> NUMBER = new NumberHandler();
	public static final SqlTypeHandler<Integer> INTEGER = new IntegerHandler();
	public static final SqlTypeHandler<Long> LONG = new LongHandler();
	public static final SqlTypeHandler<Float> FLOAT = new FloatHandler();
	public static final SqlTypeHandler<Double> DOUBLE = new DoubleHandler();
	
	//h2 support
	public static final SqlTypeHandler<Number> DECIMAL = new DecimalHandler();
	public static final SqlTypeHandler<Number> BIGINT = new BigIntHandler();
	public static final SqlTypeHandler<Number> REAL = new RealHandler();
	public static final SqlTypeHandler<byte[]> BINARY = new BinaryHandler();
	public static final SqlTypeHandler<String> CHAR = new CharHandler();
	
	
	
	protected SqlTypeHandler(String name, Class<T> type) {
		super(name, type);
	}
	
	public final T get(Object object, String field, String format) {
		if (object == null) {
			throw new NullPointerException();
		} else if (object instanceof ResultSet) {
			return get((ResultSet) object, field);
		} else if (object instanceof PreparedStatement) {
			throw new UnsupportedOperationException();
		} else {
			throw new IllegalArgumentException("Incompatible type '" + object.getClass() + "'.");
		}
	}

	public final void set(Object object, String field, T value, String format) {
		if (object == null) {
			throw new NullPointerException();
		} else if (object instanceof ResultSet) {
			set((ResultSet) object, field, value);
		} else if (object instanceof PreparedStatement) {
			throw new UnsupportedOperationException();
		} else {
			throw new IllegalArgumentException("Incompatible type '" + object.getClass() + "'.");
		}
	}
	
	public abstract T get(ResultSet object, String field);
	public abstract void set(ResultSet object, String field, T value);
	
	
	
	
	public final T get(Object object, int field, String format) {
		if (object == null) {
			throw new NullPointerException();
		} else if (object instanceof ResultSet) {
			return get((ResultSet) object, field);
		} else if (object instanceof PreparedStatement) {
			//return get((PreparedStatement) object, field);
			throw new UnsupportedOperationException("Not yet implemented!");
		} else {
			throw new IllegalArgumentException("Incompatible type '" + object.getClass() + "'.");
		}
	}

	public final void set(Object object, int field, T value, String format) {
		if (object == null) {
			throw new NullPointerException();
		} else if (object instanceof ResultSet) {
			set((ResultSet) object, field, value);
		} else if (object instanceof PreparedStatement) {
			set((PreparedStatement) object, field, value);
		} else {
			throw new IllegalArgumentException("Incompatible type '" + object.getClass() + "'.");
		}
	}
	
	public abstract T get(ResultSet object, int field);
	//public abstract T get(PreparedStatement object, int field);
	
	public abstract void set(ResultSet object, int field, T value);
	public abstract void set(PreparedStatement object, int field, T value);
	
}
