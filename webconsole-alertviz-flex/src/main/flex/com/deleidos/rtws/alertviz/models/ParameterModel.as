package com.deleidos.rtws.alertviz.models
{
	import org.robotlegs.mvcs.Actor;
	
	public class ParameterModel extends Actor
	{	
		private var _systemName:String;
		private var _tenantName:String;
		private var _selfPrefixUrl:String;
		private var _username:String;
		private var _alertQueueLimit:int;
		private var _timelineAutopan:Boolean;
		private var _serverTimeout:int = 10;
		
		public function get systemName():String {
			return _systemName;
		}
		
		public function set systemName(value:String):void {
			_systemName = value;
		}
		
		public function get tenantName():String {
			return _tenantName;
		}
		
		public function set tenantName(value:String):void {
			_tenantName = value;
		}
		
		public function get selfPrefixUrl():String {
			return _selfPrefixUrl;
		}
		
		public function set selfPrefixUrl(value:String):void {
			_selfPrefixUrl = value;
		}
		
		public function get username():String {
			return _username;
		}
		
		public function set username(value:String):void {
			_username = value;
		}
		
		public function get alertQueueLimit():int {
			return _alertQueueLimit;
		}
		
		public function set alertQueueLimit(value:int):void {
			_alertQueueLimit = value;
		}
		
		public function get timelineAutopan():Boolean {
			return _timelineAutopan;
		}
		
		public function set timelineAutopan(value:Boolean):void {
			_timelineAutopan = value;
		}
		
		public function get serverTimeout():int {
			return _serverTimeout;
		}
		public function set serverTimeout(value:int):void {
			_serverTimeout = value;
		}
	}
}