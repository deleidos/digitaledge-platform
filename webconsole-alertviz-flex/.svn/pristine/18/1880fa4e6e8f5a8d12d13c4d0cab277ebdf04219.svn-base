package com.deleidos.rtws.alertviz.events
{
	import com.deleidos.rtws.alertviz.models.Alert;
	
	import flash.events.Event;
	
	public class AlertCommandEvent extends Event
	{
		public static const COMMAND_KEY	:String = "alertCommand";
		public static const ADD			:String = COMMAND_KEY + ".add";
		
		private var _alert:Alert;
		
		public function AlertCommandEvent(type:String, alert:Alert, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_alert = alert;
		}
		
		public function get alert():Alert
		{
			return _alert;	
		}
		
		public override function clone():Event
		{
			return new AlertCommandEvent(type, _alert, bubbles, cancelable);
		}
	}
}