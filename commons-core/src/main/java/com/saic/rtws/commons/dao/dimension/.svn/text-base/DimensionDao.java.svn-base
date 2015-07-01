package com.saic.rtws.commons.dao.dimension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.dao.exception.DataRetrievalException;
import com.saic.rtws.commons.dao.jdbc.DataAccessSession;
import com.saic.rtws.commons.dao.jdbc.DataAccessTransaction;
import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.dao.jdbc.DefaultStatementHandler;
import com.saic.rtws.commons.dao.jdbc.RecordBuilder;
import com.saic.rtws.commons.dao.jdbc.SingleValueResultHandler;
import com.saic.rtws.commons.dao.type.sql.SqlTypeHandler;
import com.saic.rtws.commons.model.dimension.CacheInitPolicy;
import com.saic.rtws.commons.model.dimension.DimensionColumn;
import com.saic.rtws.commons.model.dimension.DimensionColumnRole;
import com.saic.rtws.commons.model.dimension.DimensionTable;
import com.saic.rtws.commons.model.dimension.MissPolicy;
import com.saic.rtws.commons.model.tmsdb.DbConnDetails;

public class DimensionDao {
	private static final Logger LOGGER = Logger.getLogger(DimensionDao.class);
	
	private static final String GET_TENANT_RDBMS_CONNECTIONS = //
			"SELECT CONN_ID, DB_TYPE, HOST, PORT, DB_NAME, LABEL" + //
			"  FROM APPLICATION.TENANT_RDBMS_CONNECTIONS" + //
			"  WHERE tenant_id = ?"; //
	
	private static final String GET_TENANT_TABLES_SQL_PREFIX = //
			"SELECT dimTables.DIM_TABLE_ID DIM_TABLE_ID, TABLE_NAME, SURROGATE_KEY_SEQUENCE_NAME" + //
			", MISS_POLICY, DEFAULT_CACHE_INIT_POLICY, DEFAULT_CACHE_MAX_RECORDS" + //
			", DIM_COLUMN_ID, COLUMN_NAME, DIM_ROLE, DEFAULT_VALUE" + //
			", FIELD_NAME, FIELD_TYPE, FORMAT_MASK" + //
			"  FROM APPLICATION.DIM_TABLES dimTables, APPLICATION.DIM_COLUMNS dimColumns" + //
			"  WHERE dimTables.CONN_ID = ?" + //
			"  AND dimTables.DIM_TABLE_ID = dimColumns.DIM_TABLE_ID"; //
	
	private static final String FETCH_NEXT_DIM_TABLE_SEQ_VALUE = //
			" SELECT PUBLIC.DIM_TABLE_ID_SEQ.nextval FROM PUBLIC.DUAL"; //
	
	private static final String INSERT_DIM_CACHE_DETAILS_SQL = //
			"INSERT INTO APPLICATION.DIM_TABLES" + //
			"  (" + //
			"    DIM_TABLE_ID" + //
			"    , CONN_ID" + //
			"    , TABLE_NAME" + //
			"    , SURROGATE_KEY_SEQUENCE_NAME" + //
			"    , MISS_POLICY" + //
			"    , DEFAULT_CACHE_INIT_POLICY" + //
			"    , DEFAULT_CACHE_MAX_RECORDS" +
			"  )" + //
			"  VALUES" + //
			"  (" + //
			"    ?" + //
			"    , ?" + //
			"    , ?" + //
			"    , ?" + //
			"    , ?" + //
			"    , ?" + //
			"	 , ?" + //
			"  )"; //
	
	private static final String INSERT_DIM_COLUMN_SQL = //
			"INSERT INTO APPLICATION.DIM_COLUMNS" + //
			"  (" + //
			"    DIM_COLUMN_ID" + //
			"    , DIM_TABLE_ID" + //
			"    , COLUMN_NAME" + //
			"    , DIM_ROLE" +
			"    , DEFAULT_VALUE" +
			"    , FIELD_NAME" +
			"    , FIELD_TYPE" +
			"    , FORMAT_MASK" +
			"  )" + //
			"  VALUES" + //
			"  (" + //
			"    (SELECT PUBLIC.DIM_COLUMN_ID_SEQ.nextval FROM PUBLIC.DUAL)" + //
			"    , ?" + //
			"    , ?" + //
			"    , ?" + //
			"    , ?" + //
			"    , ?" + //
			"	 , ?" + //
			"    , ?" + //
			"  )"; //
	
	private static final String DROP_DIM_CACHE_DETAILS_BY_TABLE_SQL = //
			"DELETE FROM APPLICATION.DIM_TABLES" + //
			" WHERE CONN_ID = ? AND TABLE_NAME = ?"; //
	
	private static final String DROP_DIM_CACHE_COLUMNS_BY_TABLE_SQL = //
			"DELETE FROM APPLICATION.DIM_COLUMNS" + //
			" WHERE DIM_COLUMN_ID IN" + //
			" (" + //
			"   SELECT DIM_COLUMN_ID" + //
			"   FROM APPLICATION.DIM_TABLES dimTables, APPLICATION.DIM_COLUMNS dimColumns" + //
			"   WHERE dimTables.dim_table_id = dimColumns.dim_table_id" + //
			"     AND CONN_ID = ?" + //
			"     AND TABLE_NAME = ?" + //
			" )"; //
	
	private static final String TABLES_PREDICATE_PREFIX = "  AND dimTables.TABLE_NAME in (";
	private static final String TABLES_PREDICATE_SUFFIX = ")";
	
	private DataSource dataSource;
	private DataAccessSession session;
	
	public Collection<DbConnDetails> getConnections(String tenantId) {
		Collection<DbConnDetails> result =
				session.executeMultiRowQuery(GET_TENANT_RDBMS_CONNECTIONS, new DefaultStatementHandler(tenantId), new RdbmsConnectionBuilder());
		
		return result;
	}
	
	public Collection<DimensionTable> getConnectionDimensionTables(Long connId, List<String> tableNames) {
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		params.add(connId);
		
		StringBuilder sql = new StringBuilder(GET_TENANT_TABLES_SQL_PREFIX);
		
		Set<String> validTableNames = pruneDupsAndWhitespace(tableNames);
		if(validTableNames.size() > 0)
		{
			params.addAll(validTableNames);
			sql.append(TABLES_PREDICATE_PREFIX);
			sql.append(StringUtils.repeat("?", ", ", validTableNames.size()));
			sql.append(TABLES_PREDICATE_SUFFIX);
		}
		
		Collection<DimensionTable> result =
				session.executeMultiRowQuery(sql.toString(), new DefaultStatementHandler(params.toArray()), new DimensionTableBuilder());
		
		return result;
	}
	
	public boolean setDimensionCacheConfig(Long connId, DimensionTable dimTableCacheConfig)
	{
		if(connId == null)
		{
			throw new IllegalArgumentException("no connId provided");
		}
		validateDimTableCacheConfig(dimTableCacheConfig);
		
		boolean result = false;
		
		DataAccessTransaction transaction = null;
		try
		{
			transaction = DataAccessUtil.transaction(dataSource);
			dropDimensionCacheConfig(connId, dimTableCacheConfig.getName(), transaction, false);
			insertDimensionCacheConfig(connId, dimTableCacheConfig, transaction, false);
			transaction.commit();
			result = true;
		}
		catch(Exception e)
		{
			LOGGER.error("Error setting DimensionCacheConfig.  Rolling back Transaction.",e);
			try {
				transaction.rollback();
			}
			catch(Exception logged) {
				LOGGER.error("Error rolling back transaction.  May result in a bad database state.",logged);
			}
		}
		finally
		{
			try {
				transaction.close();
			}
			catch(Exception logged) {
				LOGGER.error("Error closing transaction.  May result in a bad database state.",logged);
			}
		}
		
		return result;
	}
	
	public boolean insertDimensionCacheConfig(Long connId, DimensionTable dimTableCacheConfig) throws SQLException {
		return insertDimensionCacheConfig(connId, dimTableCacheConfig, null);
	}
	
	public boolean insertDimensionCacheConfig(Long connId, DimensionTable dimTableCacheConfig, DataAccessTransaction transaction) throws SQLException {
		return insertDimensionCacheConfig(connId, dimTableCacheConfig, transaction, true);
	}
	
	private boolean insertDimensionCacheConfig(Long connId, DimensionTable dimTableCacheConfig, DataAccessTransaction transaction, boolean validateInputs) throws SQLException {
		if(validateInputs)
		{
			if(connId == null)
			{
				throw new IllegalArgumentException("no connId provided");
			}
			
			validateDimTableCacheConfig(dimTableCacheConfig);
		}
		
		boolean result = false;
		boolean isLocalTransaction = false;
		try
		{
			if(transaction == null)
			{
				isLocalTransaction = true;
				transaction = DataAccessUtil.transaction(this.dataSource);
			}
			
			Long tableId = transaction.executeQuery(FETCH_NEXT_DIM_TABLE_SEQ_VALUE, null, new SingleValueResultHandler<Long>(1, SqlTypeHandler.LONG));
			
			transaction.executeStatement(INSERT_DIM_CACHE_DETAILS_SQL,
					new DefaultStatementHandler(
							tableId,
							connId,
							dimTableCacheConfig.getName(),
							dimTableCacheConfig.getSurrogateKeySeqName(),
							dimTableCacheConfig.getMissPolicy().name(),
							dimTableCacheConfig.getDefaultCacheInitPolicy().name(),
							dimTableCacheConfig.getDefaultCacheMaxRecords()));
			
			Collection<DimensionColumn> columns = dimTableCacheConfig.getColumns();
			if(columns != null && columns.size() > 0)
			{
				for(DimensionColumn currColumn : columns)
				{
					if(currColumn != null)
					{
						transaction.executeStatement(INSERT_DIM_COLUMN_SQL,
								new DefaultStatementHandler(
										tableId,
										currColumn.getName(),
										currColumn.getRole().name(),
										currColumn.getDefaultValue(),
										currColumn.getMappingFieldName(),
										currColumn.getMappingFieldType(),
										currColumn.getFormatMask()));
					}
				}
			}
			
			if(isLocalTransaction)
			{
				transaction.commit();
			}
			result = true;
		}
		catch(Exception e)
		{
			if(isLocalTransaction)
			{
				try {
					transaction.rollback();
				}
				catch(Exception ignored) {}
			}
			else
			{
				throw new SQLException("Operaiton Failed");
			}
		}
		finally
		{
			if(isLocalTransaction)
			{
				try {
					transaction.close();
				}
				catch(Exception ignored) {}
			}
		}
		
		return result;
	}
	
	public boolean dropDimensionCacheConfig(Long connId, String tableSchema, String tableName) throws SQLException {
		if(StringUtils.isBlank(tableSchema))
		{
			throw new IllegalArgumentException("empty tableSchema provided");
		}
		if(StringUtils.isBlank(tableName))
		{
			throw new IllegalArgumentException("empty tableName provided");
		}
		
		return dropDimensionCacheConfig(connId, (tableSchema.trim() + "." + tableName.trim()));
	}
	
	public boolean dropDimensionCacheConfig(Long connId, String fqTableName) throws SQLException {
		DataAccessTransaction transaction = null;
		return dropDimensionCacheConfig(connId, fqTableName, transaction);
	}
	
	public boolean dropDimensionCacheConfig(Long connId, String fqTableName, DataAccessTransaction transaction) throws SQLException {
		return dropDimensionCacheConfig(connId, fqTableName, transaction, true);
	}
	
	public boolean dropDimensionCacheConfig(Long connId, String fqTableName, DataAccessTransaction transaction, boolean validateInputs) throws SQLException {
		if(validateInputs)
		{
			if(connId == null)
			{
				throw new IllegalArgumentException("no connId provided");
			}
			if(StringUtils.isBlank(fqTableName))
			{
				throw new IllegalArgumentException("empty fqTableName provided");
			}
		}
		
		boolean result = false;
		boolean isLocalTransaction = false;
		try
		{
			if(transaction == null)
			{
				isLocalTransaction = true;
				transaction = DataAccessUtil.transaction(this.dataSource);
			}
			
			transaction.executeStatement(DROP_DIM_CACHE_COLUMNS_BY_TABLE_SQL, new DefaultStatementHandler(connId, fqTableName));
			
			transaction.executeStatement(DROP_DIM_CACHE_DETAILS_BY_TABLE_SQL, new DefaultStatementHandler(connId, fqTableName));
			
			if(isLocalTransaction)
			{
				transaction.commit();
			}
			result = true;
		}
		catch(Exception e)
		{
			if(isLocalTransaction)
			{
				try {
					transaction.rollback();
				}
				catch(Exception ignored) {}
			}
			else
			{
				throw new SQLException("Operaiton Failed");
			}
		}
		finally
		{
			if(isLocalTransaction)
			{
				try {
					transaction.close();
				}
				catch(Exception ignored) {}
			}
		}
		
		return result;
	}
	
	private void validateDimTableCacheConfig(DimensionTable dimTableCacheConfig)
	{
		String errorMsg = null;
		
		if(dimTableCacheConfig == null)
		{
			errorMsg = "A null Cache Config was provided";
		}
		else
		{
			List<String> cacheErrors = dimTableCacheConfig.validate();
			if(cacheErrors != null && cacheErrors.size() > 0)
			{
				errorMsg = "Invalid cache params provided:\n" + StringUtils.join(cacheErrors, "\n");
			}
		}
		
		if(errorMsg != null)
		{
			LOGGER.info(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * Data source through which SQL operations should be performed.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.session = DataAccessUtil.session(dataSource);
	}
	
	/**
	 * Removes null, whitespace, and duplicate strings as well as trimming whitespace
	 * from valid strings.
	 */
	private Set<String> pruneDupsAndWhitespace(List<String> values)
	{
		Set<String> result = new HashSet<String>();
		
		if(values != null && values.size() > 0)
		{
			for(String value : values)
			{
				if(StringUtils.isNotBlank(value))
				{
					result.add(value.trim());
				}
			}
		}
		
		return result;
	}
	
	protected static final class DimensionTableBuilder implements RecordBuilder<DimensionTable> {
		public DimensionTable buildRecord(ResultSet rs) {
			try {
				DimensionTable result = null;
				
				do
				{
					long currTableId = rs.getLong("DIM_TABLE_ID");
					
					if(result == null)
					{
						result = new DimensionTable();
						result.setId(currTableId);
						result.setName(rs.getString("TABLE_NAME"));
						result.setSurrogateKeySeqName(rs.getString("SURROGATE_KEY_SEQUENCE_NAME"));
						result.setMissPolicy(MissPolicy.valueOf(rs.getString("MISS_POLICY")));
						
						CacheInitPolicy defaultInitPolicy = null;
						try
						{
							defaultInitPolicy = CacheInitPolicy.valueOf(rs.getString("DEFAULT_CACHE_INIT_POLICY"));
						}
						catch(Exception ignored) {}
						result.setDefaultCacheInitPolicy(defaultInitPolicy);
						
						Long defaultCacheMaxRecords = rs.getLong("DEFAULT_CACHE_MAX_RECORDS");
						if(rs.wasNull())
						{
							defaultCacheMaxRecords = null;
						}
						result.setDefaultCacheMaxRecords(defaultCacheMaxRecords);
						
						result.setColumns(new ArrayList<DimensionColumn>());
					}
					else if(result.getId() != currTableId)
					{
						rs.previous();
						break;
					}
					
					DimensionColumn currColumn = new DimensionColumn();
					
					currColumn.setId(rs.getLong("DIM_COLUMN_ID"));
					currColumn.setName(rs.getString("COLUMN_NAME"));
					
					DimensionColumnRole columnRole = null;
					try
					{
						columnRole = DimensionColumnRole.valueOf(rs.getString("DIM_ROLE"));
					}
					catch(Exception ignored) {}
					currColumn.setRole(columnRole);
					
					currColumn.setDefaultValue(rs.getString("DEFAULT_VALUE"));
					currColumn.setMappingFieldName(rs.getString("FIELD_NAME"));
					currColumn.setMappingFieldType(rs.getString("FIELD_TYPE"));
					currColumn.setFormatMask(rs.getString("FORMAT_MASK"));
					
					result.getColumns().add(currColumn);
				}
				while(rs.next());
				
				return result;
			} catch (SQLException e) {
				LOGGER.error("Failed to Parse Dimension Table results.", e);
				throw new DataRetrievalException(e);
			}
		}
	}
	
	protected static final class RdbmsConnectionBuilder implements RecordBuilder<DbConnDetails> {
		public DbConnDetails buildRecord(ResultSet rs) {
			try {
				DbConnDetails result = new DbConnDetails();
				
				result.setId(rs.getLong("CONN_ID"));
				result.setDbType(rs.getString("DB_TYPE"));
				result.setHost(rs.getString("HOST"));
				
				Integer portValue = null;
				int portInt = rs.getInt("PORT");
				if(!rs.wasNull())
				{
					portValue = new Integer(portInt);
				}
				result.setPort(portValue);
				result.setDbName(rs.getString("DB_NAME"));
				result.setLabel(rs.getString("LABEL"));
				
				return result;
			} catch (SQLException e) {
				LOGGER.error("Failed to Parse Dimension Table results.", e);
				throw new DataRetrievalException(e);
			}
		}
	}
}
