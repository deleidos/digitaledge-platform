package com.deleidos.rtws.alertviz.models
{
	import com.adobe.errors.IllegalStateError;
	import com.deleidos.rtws.commons.tenantutils.model.properties.SystemAppPropertiesModel;
	import com.deleidos.rtws.commons.util.NumberUtils;
	import com.deleidos.rtws.commons.util.StringUtils;
	
	public class AlertVizPropertiesModel extends SystemAppPropertiesModel
	{
		private static const GOOGLE_EARTH_DATA_SERVER_PROTOCOL:String = "https";
		private static const GOOGLE_EARTH_DATA_SERVER_HOST_PROP_NAME:String = "webapp.alertviz.servlet.googleEarthHost";
		private static const GOOGLE_EARTH_DATA_SERVER_PORT_PROP_NAME:String = "webapp.alertviz.servlet.googleEarthPort";
		private static const GOOGLE_EARTH_DATA_SERVER_SERVLET_PATH:String = "/GoogleEarthJSON";
		
		public function AlertVizPropertiesModel()
		{
			super();
		}
		
		public function get googleEarthDataServerProtocol():String
		{
			// NICETOHAVE Expose this as a proper property if we ever intend to deliver AlertViz as a product
			return GOOGLE_EARTH_DATA_SERVER_PROTOCOL;
		}
		
		public function get googleEarthDataServerHost():String
		{
			return (getGlobalProperty(GOOGLE_EARTH_DATA_SERVER_HOST_PROP_NAME) as String);
		}
		
		public function get googleEarthDataServerPort():Number
		{
			return NumberUtils.parseNumber(getGlobalProperty(GOOGLE_EARTH_DATA_SERVER_PORT_PROP_NAME) as String);
		}
		
		public function get googleEarthDataServerServletPath():String {
			return GOOGLE_EARTH_DATA_SERVER_SERVLET_PATH;
		}
		
		public function get googleEarthDataServerUrl():String {
			var result:String = null;
			
			var tmp:String = googleEarthDataServerProtocol;
			if(StringUtils.isBlank(tmp)) {
				throw new IllegalStateError("Protocol information for Google Earth Data Server is unavailable");
			}
			result = tmp;
			
			tmp = googleEarthDataServerHost;
			if(StringUtils.isBlank(tmp)) {
				throw new IllegalStateError("Host information for Google Earth Data Server is unavailable");
			}
			result += "://" + tmp;
			
			var tmpPort:Number = googleEarthDataServerPort;
			if(isNaN(tmpPort)) {
				throw new IllegalStateError("Port information for Google Earth Data Server is unavailable");
			}
			result += ":" + tmpPort;
			
			tmp = googleEarthDataServerServletPath;
			if(StringUtils.isBlank(tmp)) {
				throw new IllegalStateError("Servlet Path information for Google Earth Data Server is unavailable");
			}
			result += tmp;
			
			return result;
		}
		
		/**
		 * The google maps props should be system specific as the api key is tied to a domain.
		 * However, the google maps flash API is deprecated and google is no longer issuing
		 * new API keys.  Thus, we'll hard code these values in this class as a place holder
		 * to facilitate loading the key without passing it via flashvars.
		 */
		
		public function get googleMapsSensorEnabled():Boolean {
			return false;
		}
		
		public function get googleMapsApiKey():String {
			return "ABQIAAAA1gXRQG156rMc027_t5LRLxQs73rBFE2tbc_-U0Eyptobl3ksdxTX6h8xgjs66PAA_CO8My_XwT5DDg"
		}
	}
}