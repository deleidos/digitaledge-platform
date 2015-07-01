package com.saic.rtws.commons.webapp.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.configuration.ConversionException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.RtwsConfig;
import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.webapp.BaseHttpServlet;

/**
 * This servlet provides access to RTWS common properties, system properties, and environment variables.
 * 
 * A list of property keys that this servlet is allowed to provide is obtained
 * from ServletConfig initialization parameters.  Separate keys by any whitespace.
 * 
 * <pre>
 * 	<servlet>
 *		<servlet-name>PropertiesServlet</servlet-name>
 *		<servlet-class>com.saic.rtws.commons.webapp.servlet.PropertiesLookupServlet</servlet-class>
 *		<init-param>
 *			<param-name>appTypeTags</param-name>
 *			<param-value>[system|tms]</param-value>
 *		</init-param>
 *		<init-param>
 *			<param-name>allowedPropertyKeys</param-name>
 *			<param-value>
 *				build.domain
 *				h2.connection.url h2.app.connection.user h2.app.connection.password
 *				master.host.name
 *				RTWS_TENANT_ID
 *			</param-value>
 *		</init-param>
 *	</servlet>
 *    <servlet-mapping>
 *        <servlet-name>PropertiesServlet</servlet-name>
 *        <url-pattern>/properties/*</url-pattern>
 *    </servlet-mapping>
 * </pre>
 *
 * Clients can request the entire list, or they can ask for one or more
 * individual properties, by key, using the property parameter.
 * 
 * Examples:
 * http://server/app/properties (returns all properties)
 * http://server/app/properties?property=foo (returns just the property value for key "foo" if in the allowed list)
 * http://server/app/properties?property=foo&property=bar (returns the property value for keys "foo" and "bar" if in the allowed list)
 */
public class PropertiesLookupServlet extends BaseHttpServlet {
	private static final Logger logger = Logger.getLogger(PropertiesLookupServlet.class);
	
	private static final long serialVersionUID = 7625287297845944492L;
	
	private static final String SYSTEM_APP_TYPE_TAG_NAME = "system";
	private static final String TMS_APP_TYPE_TAG_NAME = "tms";
	private static final Map<String, List<String>> appTypeTagDefaultPropertiesMap = new HashMap<String, List<String>>();
	
	Map<String, String> allowedPropertiesMap = new HashMap<String, String>();
	
	static
	{
		List<String> systemAppDefaultProperties = new ArrayList<String>();
		systemAppDefaultProperties.add(UserDataProperties.RTWS_SW_VERSION);
		systemAppDefaultProperties.add(UserDataProperties.RTWS_DOMAIN);
		systemAppDefaultProperties.add(UserDataProperties.RTWS_TENANT_ID);
		appTypeTagDefaultPropertiesMap.put(SYSTEM_APP_TYPE_TAG_NAME, systemAppDefaultProperties);
		
		List<String> tmsAppDefaultProperties = new ArrayList<String>();
		tmsAppDefaultProperties.add(UserDataProperties.RTWS_SW_VERSION);
		appTypeTagDefaultPropertiesMap.put(TMS_APP_TYPE_TAG_NAME, tmsAppDefaultProperties);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		
		Set<String> requestedPropertyKeys = new HashSet<String>();

		String appTypeTagsParam = config.getInitParameter("appTypeTags");
		if(StringUtils.isNotBlank(appTypeTagsParam))
		{
			String[] appTypeTags = appTypeTagsParam.split("\\s");
			for (String currAppTypeTag : appTypeTags)
			{
				if(StringUtils.isBlank(currAppTypeTag))
				{
					continue;
				}
				
				if(appTypeTagDefaultPropertiesMap.containsKey(currAppTypeTag) == false)
				{
					throw new ServletException("'" + currAppTypeTag + "' is not a valid app type tag.  Recognized app type tags are: " + appTypeTagDefaultPropertiesMap.keySet());
				}
				
				requestedPropertyKeys.addAll(appTypeTagDefaultPropertiesMap.get(currAppTypeTag));
			}
		}
		
		String explicitPropNamesParam = config.getInitParameter("allowedPropertyKeys");
		if(StringUtils.isNotBlank(explicitPropNamesParam))
		{
			String[] explicitPropNames = explicitPropNamesParam.split("\\s");
			for (String currExplicitPropName : explicitPropNames)
			{
				if(StringUtils.isBlank(currExplicitPropName))
				{
					continue;
				}
				
				requestedPropertyKeys.add(currExplicitPropName);
			}
		}
		
		if(requestedPropertyKeys.size() > 0)
		{
			String currPropValue = null;
			for(String currPropKey : requestedPropertyKeys)
			{
				try
				{
					currPropValue = RtwsConfig.getInstance().getString(currPropKey, null);
				}
				catch (ConversionException conversionException)
				{
					// Not a string prop
					throw new ServletException("'" + currPropKey + "' is not a string property and cannot be accessed via this service");
				}
				
				if (currPropValue == null) {
					// next try to find the key in system properties
					try { currPropValue = System.getProperty(currPropKey); } catch (Exception ignore) {}
				}
				if (currPropValue == null) {
					// next try to find the key in environment variables
					try { currPropValue = System.getenv(currPropKey); } catch (Exception ignore) {}
				}
				if (currPropValue == null) {
					// next try to find the key in user data variables
					currPropValue = UserDataProperties.getInstance().getString(currPropKey);
				}
				
				if(currPropValue == null)
				{
					logger.error("'" + currPropValue + "' was requested, but is unavailable");
				}
				allowedPropertiesMap.put(currPropKey, currPropValue);
			}
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// assume we will return all of the properties we're allowed to
		Map<String, String> returnedPropertiesMap = allowedPropertiesMap;

		// if the request asks for specific properties, then return just those properties
		String[] requestedPropertyKeys = request.getParameterValues("property");
		if (requestedPropertyKeys != null && requestedPropertyKeys.length > 0) {
			returnedPropertiesMap = new HashMap<String, String>();
			for (String key : requestedPropertyKeys) {
				if (allowedPropertiesMap.containsKey(key)) {
					returnedPropertiesMap.put(key, allowedPropertiesMap.get(key));
				}
			}
		}

		// build the response
		try {
			JSONObject jsonObj = JSONObject.fromObject(returnedPropertiesMap);

			response.setContentType("application/json");
			response.getWriter().write(jsonObj.toString());
		} catch (Exception ex) {
			try {
				printErrorResponse(response, "Failed to marshall JSON object in doGet()", ex);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
