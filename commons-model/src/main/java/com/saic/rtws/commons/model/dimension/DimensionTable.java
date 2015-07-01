package com.saic.rtws.commons.model.dimension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class DimensionTable {
	private Long id;
	private String name;
	private String surrogateKeySeqName;
	private MissPolicy missPolicy;
	private CacheInitPolicy defaultCacheInitPolicy;
	private Long defaultCacheMaxRecords;
	private Collection<DimensionColumn> columns;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurrogateKeySeqName() {
		return surrogateKeySeqName;
	}
	public void setSurrogateKeySeqName(String surrogateKeySeqName) {
		this.surrogateKeySeqName = surrogateKeySeqName;
	}
	public MissPolicy getMissPolicy() {
		return missPolicy;
	}
	public void setMissPolicy(MissPolicy missPolicy) {
		this.missPolicy = missPolicy;
	}
	public CacheInitPolicy getDefaultCacheInitPolicy() {
		return defaultCacheInitPolicy;
	}
	public void setDefaultCacheInitPolicy(CacheInitPolicy defaultCacheInitPolicy) {
		this.defaultCacheInitPolicy = defaultCacheInitPolicy;
	}
	public Long getDefaultCacheMaxRecords() {
		return defaultCacheMaxRecords;
	}
	public void setDefaultCacheMaxRecords(Long defaultCacheMaxRecords) {
		this.defaultCacheMaxRecords = defaultCacheMaxRecords;
	}
	public Collection<DimensionColumn> getColumns() {
		return columns;
	}
	public void setColumns(Collection<DimensionColumn> columns) {
		this.columns = columns;
	}
	
	public List<String> validate()
	{
		List<String> errors = new ArrayList<String>();

		errors.addAll(validateTableName());
		List<String> columnErrors = validateColumns();
		errors.addAll(columnErrors);
		errors.addAll(validateMissPolicy(columnErrors.size() == 0));
		errors.addAll(validateInitPolicy());
		errors.addAll(validateCacheSize());
		
		
		return (errors.size() > 0 ? errors : null);
	}
	
	private List<String> validateTableName()
	{
		List<String> result = new ArrayList<String>();
		
		if(StringUtils.isBlank(name))
		{
			result.add("Fully Qualified Table Name cannot be empty");
		}
		else
		{
			StringTokenizer tokenizer = new StringTokenizer(name, ".");
			if(tokenizer.countTokens() != 2)
			{
				result.add("Fully Qualified Table Name is not valid");
			}
			else
			{
				if(StringUtils.isBlank(tokenizer.nextToken()))
				{
					result.add("Table Schema is missing");
				}
				
				if(StringUtils.isBlank(tokenizer.nextToken()))
				{
					result.add("Table Name is missing");
				}
			}
		}
		
		return result;
	}
	
	private List<String> validateMissPolicy(boolean validateColumnDetails)
	{
		List<String> result = new ArrayList<String>();
		
		if(missPolicy == null)
		{
			result.add("Record Miss Policy must be specified");
		}
		else
		{
			switch (missPolicy)
			{
				case INSERT:
					if(StringUtils.isBlank(surrogateKeySeqName))
					{
						result.add("A Surrogate Key Sequence must be provided to insert records");
					}
					
					if(validateColumnDetails)
					{
						List<DimensionColumn> surrogateColumns = getColumnsByRole(DimensionColumnRole.SURROGATE_KEY);
						if(surrogateColumns == null || surrogateColumns.size() == 0)
						{
							result.add("A Surrogate Key must be identified in order to insert records");
						}
						else if(surrogateColumns.size() > 1)
						{
							result.add("Only one Surrogate Key may be identified in order to insert records");
						}
					}
					break;
				case SUBSTITUTE:
					if(validateColumnDetails)
					{
						List<DimensionColumn> nkColumns = getColumnsByRole(DimensionColumnRole.NATURAL_KEY);
						if(nkColumns == null || nkColumns.size() == 0)
						{
							result.add("At least one Natural Key column must be identified to locate the default record");
						}
						else
						{
							for(DimensionColumn currNkColumn : nkColumns)
							{
								if(StringUtils.isBlank(currNkColumn.getDefaultValue()))
								{
									result.add("'" + currNkColumn.getName() + "' must specify a default record value for look up");
								}
							}
						}
					}
					break;
				default:
					break;
			}
		}
		
		
		return result;
	}
	
	private List<String> validateInitPolicy()
	{
		List<String> result = new ArrayList<String>();
		if(defaultCacheInitPolicy == null)
		{
			result.add("Cache Load Policy must be specified");
		}
		return result;
	}
	
	private List<String> validateCacheSize()
	{
		List<String> result = new ArrayList<String>();
		
		if(defaultCacheMaxRecords == null)
		{
			result.add("Cache Size must be specified");
		}
		else if(defaultCacheMaxRecords.longValue() <= 0)
		{
			result.add("Cache Size must be greater than zero");
		}
		return result;
	}
	
	private List<String> validateColumns()
	{
		List<String> result = new ArrayList<String>();
		
		if(columns == null || columns.size() == 0)
		{
			result.add("One or more columns must be configured for enrichments to work properly");
		}
		else
		{
			Set<String> uniqueColumnNames = new HashSet<String>();
			Set<String> dupColumnNames = new HashSet<String>();
			Set<String> uniqueFieldNames = new HashSet<String>();
			Set<String> dupFieldNames = new HashSet<String>();
			
			boolean isEmptyErrorIssued = false;
			List<String> currColumnErrors = null;
			int validNkColCount = 0;
			for(DimensionColumn currColumn : columns)
			{
				if(currColumn != null)
				{
					currColumnErrors = currColumn.validate();
					result.addAll(currColumnErrors);
					if(currColumnErrors.size() == 0)
					{
						if(uniqueColumnNames.add(currColumn.getName().trim().toUpperCase()) == false)
						{
							dupColumnNames.add(currColumn.getName());
						}
						
						if(uniqueFieldNames.add(currColumn.getMappingFieldName()) == false)
						{
							dupFieldNames.add(currColumn.getMappingFieldName());
						}
						
						if(currColumn.getRole() == DimensionColumnRole.NATURAL_KEY)
						{
							validNkColCount++;
						}
					}
				}
				else if(isEmptyErrorIssued)
				{
					result.add("Empty column mappings are not allowed");
					isEmptyErrorIssued = true;
				}
			}
			
			if(dupColumnNames.size() > 0)
			{
				for(String dupColName : dupColumnNames)
				{
					result.add("Column Name '" + dupColName + "' was specified more than once.  Column names are case-insensitive");
				}
			}
			
			if(dupFieldNames.size() > 0)
			{
				for(String dupFieldName : dupFieldNames)
				{
					result.add("Cache Variable Name '" + dupFieldName + "' was specified more than once.");
				}
			}
			
			if(validNkColCount == 0)
			{
				result.add("One or more NK columns must be configured for enrichments to work properly");
			}
		}
		return result;
	}
	
	private List<DimensionColumn> getColumnsByRole(DimensionColumnRole role)
	{
		List<DimensionColumn> result = new ArrayList<DimensionColumn>();
		
		if(columns != null)
		{
			for(DimensionColumn column : columns)
			{
				if(column != null && column.getRole() == role)
				{
					result.add(column);
				}
			}
		}
		
		return result;
	}
}
