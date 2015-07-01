package com.saic.rtws.commons.model.user;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

public class Filter {

	private Long key;
	private String name;
	private String model;
	private JSON definition;
	private String emailSubject;
	private String emailMessage;
	
	public Filter() {
		super();
	}
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public JSON getDefinition() {
		return definition;
	}
	public void setDefinition(JSON definition) {
		this.definition = definition;
	}
	
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	
	public String getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}
	
	public String getDescription() {
		StringBuilder buffer = new StringBuilder();
		buildDescription("", definition, buffer);
		return buffer.substring(0, Math.max(0, buffer.length() - 2));
	}
	
	private static void buildDescription(String field, Object object, StringBuilder buffer) {
		if(object == null) {
			// Ignore
		} else if(object instanceof JSONObject) {
			JSONObject map = (JSONObject)object;
			for(Object key : map.keySet()) {
				buildDescription(key.toString(), map.get(key), buffer);
			}
		} else if(object instanceof JSONArray) {
			for(Object element : (JSONArray)object) {
				buildDescription(field, element, buffer);
			}
		} else if(object instanceof JSONNull) {
			// Ignore
		} else {
			buffer.append(field);
			buffer.append(" ");
			buffer.append(object);
			buffer.append(", ");
		}
	}

	public String toString() {
		return "Filter [key=" + key + ", name=" + name + ", model=" + model
				+ ", definition=" + definition + ", emailSubject="
				+ emailSubject + ", emailMessage=" + emailMessage + "]";
	}
	
}
