package com.saic.rtws.commons.datamodel.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.saic.rtws.commons.util.repository.DataModelZipFile;

public class EnrichmentCfgParseUtils
{
	protected static final String TYPE_FIELD_NAME = "enrichName";
	protected static final String CUSTOM_PARAMS_FIELD_NAME = "otherParams";
	protected static final String CUSTOM_PARAMS_SEPARATOR = ",";
	protected static final String CUSTOM_PARAMS_KV_SEPARATOR = "=";
	
	/**
	 * Convenience method to automatically fetch the enrichment config stream from the provided dataModelZipFile instance and pass it to
	 * {@link #getEnrichmentCfgs(net.sf.json.JSONArray, java.util.List)}
	 * 
	 * @param dataModelZipFile a DataModelZipFile instance
	 * @param includedTypes An optional list of type filters; pass null to return all types
	 * @return null if null is provided for dataModelZipFile; A non-null list of any matching enrichment configs otherwise.
	 * @throws IOException if the enrichment config section cannot be read from the provided dataModelZipFile
	 * @throws JSONException If dataModelZipFile does not have a well-formed enrichment config section.
	 * 
	 * @see #getEnrichmentCfgs(net.sf.json.JSONArray, java.util.List)
	 */
	public static List<JSONObject> getEnrichmentCfgs(DataModelZipFile dataModelZipFile, List<String> includedTypes) throws IOException, JSONException
	{
		List<JSONObject> result = null;
		
		if(dataModelZipFile != null)
		{
			InputStream enrichmentConfigIs = null;
			try
			{
				enrichmentConfigIs = dataModelZipFile.getEnrichmentConfig();
				StringWriter jsonWriter = new StringWriter();
				IOUtils.copy(enrichmentConfigIs, jsonWriter, "UTF-8");
				return getEnrichmentCfgs(JSONArray.fromObject(jsonWriter.toString()), includedTypes);
			}
			finally {
				try {
					enrichmentConfigIs.close();
				}catch(Exception ignored) {}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a list of enrichment configs with types matching the provided includedTypes.
	 * 
	 * @param enrichmentCfgs A JSONArray of JSONObject instances representing enrichment configs.
	 * @param includedTypes An optional list of type filters; pass null to return all types
	 * @return null if null is provided for enrichmentCfgs; A non-null list of any matching enrichment configs otherwise.
	 * @throws JSONException If enrichmentCfgs is malformed or an object in enrichmentCfgs is not a valid enrichment cfg object.
	 */
	public static List<JSONObject> getEnrichmentCfgs(JSONArray enrichmentCfgs, List<String> includedTypes) throws JSONException
	{
		List<JSONObject> result = null;
		
		if(enrichmentCfgs != null)
		{
			result = new ArrayList<JSONObject>();
			
			JSONObject currEnrichmentJson = null;
			String currEnrichmentType = null;
			for(int index=0; index < enrichmentCfgs.size(); index++)
			{
				currEnrichmentType = null;
				currEnrichmentJson = enrichmentCfgs.getJSONObject(index);
				if(currEnrichmentJson != null)
				{
					currEnrichmentType = currEnrichmentJson.optString(TYPE_FIELD_NAME, null);
				}
				
				if(StringUtils.isBlank(currEnrichmentType))
				{
					throw new JSONException("Invalid Enrichment Config Object: '" + TYPE_FIELD_NAME + " is missing or empty");
				}
				else if(includedTypes == null || includedTypes.contains(currEnrichmentType))
				{
					result.add(currEnrichmentJson);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Extracts any custom parameters for the provided enrichment config object.
	 * This method is lienient and will provide an empty list if the custom parms
	 * object does not exist, is null, or is empty.  However, if the format is invalid,
	 * a ParseException will be thrown.  [Note:] This method requires non-empty keys
	 * but allows for empty values.
	 * 
	 * @param enrichCfgJson A JSON representation of an enrichment config.
	 * @return null if null is provided; a non-null list of any key/value pairs otherwise
	 * @throws ParseException if a non-empty value is provided for the custom params and the structure is invalid
	 */
	public static List<SimpleEntry<String, String>> getCustomParams(JSONObject enrichCfgJson) throws ParseException
	{
		List<SimpleEntry<String, String>> result = null;
		
		if(enrichCfgJson != null)
		{
			result = new ArrayList<SimpleEntry<String, String>>();
			
			String customParams = enrichCfgJson.optString(CUSTOM_PARAMS_FIELD_NAME, null);
			
			// Allowing null, missing, and empty values for the custom params value
			if(StringUtils.isNotBlank(customParams))
			{
				String[] keyValuePairTokens = customParams.split(CUSTOM_PARAMS_SEPARATOR);
				if(keyValuePairTokens != null)
				{
					for(String keyValuePairToken : keyValuePairTokens)
					{
						if(StringUtils.isNotBlank(keyValuePairToken))
						{
							String[] tokens = keyValuePairToken.split(CUSTOM_PARAMS_KV_SEPARATOR);
							if(tokens != null && tokens.length == 2 && StringUtils.isNotBlank(tokens[0]))
							{
								result.add(new SimpleEntry<String, String>(tokens[0].trim(), tokens[1].trim()));
							}
							else
							{
								throw new ParseException("The Key/Value String '" + keyValuePairToken + "' is not valid", -1);
							}
						}
					}
				}
				
				if(result.size() == 0)
				{
					throw new ParseException("'" + CUSTOM_PARAMS_FIELD_NAME + "' value '" + customParams + "is not valid", -1);
				}
			}
		}
		
		return result;
	}
}
