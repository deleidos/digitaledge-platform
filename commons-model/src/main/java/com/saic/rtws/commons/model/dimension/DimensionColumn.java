package com.saic.rtws.commons.model.dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DimensionColumn {
	private List<String> VALID_TYPES = Arrays.asList("STRING", "NUMBER", "DATETIME");
	private List<String> REQUIRED_FORMAT_MASK_TYPES = Arrays.asList("DATETIME");
	
	private Long id;
	private String name;
	private DimensionColumnRole role;
	private String defaultValue;
	private String mappingFieldName;
	private String mappingFieldType;
	private String formatMask;
	
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
	public DimensionColumnRole getRole() {
		return role;
	}
	public void setRole(DimensionColumnRole role) {
		this.role = role;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getMappingFieldName() {
		return mappingFieldName;
	}
	public void setMappingFieldName(String mappingFieldName) {
		this.mappingFieldName = mappingFieldName;
	}
	public String getMappingFieldType() {
		return mappingFieldType;
	}
	public void setMappingFieldType(String mappingFieldType) {
		this.mappingFieldType = mappingFieldType;
	}
	
	public String getFormatMask(){
		return formatMask;
	}
	
	public void setFormatMask(String formatMask){
		this.formatMask = formatMask;
	}
	
	public List<String> validate()
	{
		List<String> errors = new ArrayList<String>();

		if(StringUtils.isBlank(name))
		{
			errors.add("A Column Name cannot be empty");
		}
		if(role == null)
		{
			errors.add((StringUtils.isNotBlank(name) ? (name + ": ") : "" ) + "Role cannot be empty");
		}
		if(StringUtils.isBlank(mappingFieldName))
		{
			errors.add((StringUtils.isNotBlank(name) ? (name + ": ") : "" ) + "Field Variable Name cannot be empty");
		}
		errors.addAll(validateFieldType());
		
		return errors;
	}
	
	private List<String> validateFieldType()
	{
		List<String> errors = new ArrayList<String>();
		
		if(StringUtils.isBlank(mappingFieldType))
		{
			errors.add((StringUtils.isNotBlank(name) ? (name + ": ") : "" ) + "Field Type cannot be empty");
		}
		else if(VALID_TYPES.contains(mappingFieldType) == false)
		{
			errors.add((StringUtils.isNotBlank(name) ? (name + ": ") : "" ) + "unsupported Field Type");
		}
		else if(REQUIRED_FORMAT_MASK_TYPES.contains(mappingFieldType) && StringUtils.isBlank(formatMask))
		{
			errors.add((StringUtils.isNotBlank(name) ? (name + ": ") : "" ) + "A Format Mask is required for this Field Type");
		}
		
		return errors;
	}
}
