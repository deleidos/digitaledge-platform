package com.saic.rtws.commons.dao.type;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeUtil {

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS z";
	
	public static Boolean toBoolean(String value) {
		return (value == null) ? null : Boolean.valueOf(value);
	}
	
	public static Boolean toBoolean(Number value) {
		return (value == null) ? null : Boolean.valueOf(value.intValue() == 0);
	}

	
	public static Date toDate(String value, String format) throws ParseException {
		if(value == null) {
			return null;
		} else if(format == null) {
			return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(value);
		} else {
			return new SimpleDateFormat(format).parse(value); 
		}
	}
	
	public static Date toDate(Long value) {
		return (value == null) ? null : new Date(value.longValue());
	}
	
	
	public static String toString(Boolean value) {
		return (value == null) ? null : value.toString();
	}
	
	public static String toString(Date value, String format) {
		if(value == null) {
			return null;
		} else if(format == null) {
			return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(value);
		} else {
			return new SimpleDateFormat(format).format(value); 
		}
	}
	
	public static String toString(Number value, String format) {
		if(value == null) {
			return null;
		} else if(format == null) {
			return value.toString();
		} else {
			return new DecimalFormat(format).format(value.doubleValue()); 
		}
	}
	
	
	public static Byte toByte(String value, String format) throws ParseException {
		if(value == null) {
			return null;
		} else if(format == null) {
			return Byte.parseByte(value);
		} else {
			return new Byte(new DecimalFormat(format).parse(value).byteValue()); 
		}
	}
	
	public static Byte toByte(Number value) {
		return new Byte(value.byteValue());
	}
	
	public static Short toShort(String value, String format) throws ParseException {
		if(value == null) {
			return null;
		} else if(format == null) {
			return Short.parseShort(value);
		} else {
			return new Short(new DecimalFormat(format).parse(value).shortValue()); 
		}
	}
	
	public static Short toShort(Number value) {
		return new Short(value.shortValue());
	}
	
	public static Integer toInteger(String value, String format) throws ParseException {
		if(value == null) {
			return null;
		} else if(format == null) {
			return Integer.parseInt(value);
		} else {
			return new Integer(new DecimalFormat(format).parse(value).intValue()); 
		}
	}
	
	public static Integer toInteger(Number value) {
		return new Integer(value.intValue());
	}
	
	public static Long toLong(Date value) throws ParseException {
		return (value == null) ? null : new Long(value.getTime());
	}
	
	public static Long toLong(String value, String format) throws ParseException {
		if(value == null) {
			return null;
		} else if(format == null) {
			return Long.parseLong(value);
		} else {
			return new Long(new DecimalFormat(format).parse(value).longValue()); 
		}
	}
	
	public static Long toLong(Number value) {
		return new Long(value.longValue());
	}
	
	public static Float toFloat(String value, String format) throws ParseException {
		if(value == null) {
			return null;
		} else if(format == null) {
			return Float.parseFloat(value);
		} else {
			return new Float(new DecimalFormat(format).parse(value).floatValue()); 
		}
	}
	
	public static Float toFloat(Number value) {
		return new Float(value.floatValue());
	}
	
	public static Double toDouble(String value, String format) throws ParseException {
		if(value == null) {
			return null;
		} else if(format == null) {
			return Double.parseDouble(value);
		} else {
			return new Double(new DecimalFormat(format).parse(value).doubleValue()); 
		}
	}
	
	public static Double toDouble(Number value) {
		return new Double(value.doubleValue());
	}
	
}
