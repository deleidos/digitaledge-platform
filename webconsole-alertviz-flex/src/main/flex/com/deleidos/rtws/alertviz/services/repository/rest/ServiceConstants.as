package com.deleidos.rtws.alertviz.services.repository.rest
{
	public class ServiceConstants
	{
		private static const DEFAULT_SYSTEM_NAME:String = "athena";
		private static const DEFAULT_USERID:String = "athena";
		private static const DEFAULT_PASSWORD:String = "athena";
		
		public static function getUserId(tenantName:String):String{
			return tenantName == "FILL_IN_SYSTEM_NAME" ? DEFAULT_USERID : tenantName;
		}
		
		public static function getPassword(tenantName:String):String{
			return tenantName == "FILL_IN_SYSTEM_NAME" ? DEFAULT_PASSWORD : tenantName;
		}
		
		public static function getTenantPath(tenantName:String):String{
			return tenantName == "FILL_IN_SYSTEM_NAME" ?  DEFAULT_SYSTEM_NAME : tenantName;
		}
	}
}