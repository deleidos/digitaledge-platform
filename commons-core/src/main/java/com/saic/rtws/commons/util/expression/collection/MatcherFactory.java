package com.saic.rtws.commons.util.expression.collection;

import com.saic.rtws.commons.util.expression.Matcher;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MatcherFactory {

	private MatcherFactory(){}
	
	@SuppressWarnings("unchecked")
	public static Matcher buildMatcher(JSON definition){
		if(definition == null) return null;
		
		if(definition instanceof JSONObject)
			return SuchThat.suchThat(((JSONObject)definition));
		else if(definition instanceof JSONArray)
			return Any.any((JSONArray)definition);
			
		return new Matcher(){ public boolean matches(Object o, JSONObject j){ return false; } }; //always returns false
	}
}
