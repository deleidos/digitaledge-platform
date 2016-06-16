package com.deleidos.rtws.alertviz.services.repository.events
{
	import flash.events.Event;
	
	public class ServiceStatusEvent extends Event
	{
		public static const SUCCESS:String = "com.deleidos.rtws.alertviz.services.repository.events.ServiceStatusEvent.SUCCESS";
		public static const IN_PROGRESS:String = "com.deleidos.rtws.alertviz.services.repository.events.ServiceStatusEvent.PROGRESS";
		public static const FAILURE:String = "com.deleidos.rtws.alertviz.services.repository.events.ServiceStatusEvent.FAILURE";
		
		private var _userMsg:String = null;
		
		public function ServiceStatusEvent(type:String, userMsg:String=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this._userMsg = userMsg;
		}
		
		public function get userMsg():String
		{
			return _userMsg;
		}
		
		public function set userMsg(userMsg:String):void
		{
			this._userMsg = userMsg;
		}
	}
}