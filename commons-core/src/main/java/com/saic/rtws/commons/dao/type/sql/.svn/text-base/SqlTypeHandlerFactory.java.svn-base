package com.saic.rtws.commons.dao.type.sql;

import com.saic.rtws.commons.dao.type.TypeHandlerFactory;

public class SqlTypeHandlerFactory extends TypeHandlerFactory {

	public SqlTypeHandlerFactory() {
		super();
		registerHandler(SqlTypeHandler.BOOLEAN);
		registerHandler(SqlTypeHandler.VARCHAR);
		registerHandler(SqlTypeHandler.DATE);
		registerHandler(SqlTypeHandler.TIME);
		registerHandler(SqlTypeHandler.TIMESTAMP);
		registerHandler(SqlTypeHandler.NUMBER);
		registerHandler(SqlTypeHandler.INTEGER);
		registerHandler(SqlTypeHandler.LONG);
		registerHandler(SqlTypeHandler.FLOAT);
		registerHandler(SqlTypeHandler.DOUBLE);
		
		//h2 support
		registerHandler(SqlTypeHandler.DECIMAL);
		registerHandler(SqlTypeHandler.BIGINT);
		registerHandler(SqlTypeHandler.REAL);
		registerHandler(SqlTypeHandler.BINARY);
		registerHandler(SqlTypeHandler.CHAR);		
	}
	
}
