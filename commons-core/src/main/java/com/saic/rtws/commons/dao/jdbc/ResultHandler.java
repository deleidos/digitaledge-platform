package com.saic.rtws.commons.dao.jdbc;

import java.sql.ResultSet;

/**
 * Defines an interface for classes capable of iterating over a result set.
 */
public interface ResultHandler {
	public void handle(ResultSet result);
}

