package com.deleidos.rtws.alertviz.events
{
	import com.deleidos.rtws.alertviz.models.Alert;
	
	import flash.events.Event;
	
	public class AlertDataCommandEvent extends Event
	{
		public static const COMMAND_KEY	:String = "alerDataCommand";
		public static const SELECTED	:String = COMMAND_KEY + ".selected";
		public static const DESELECTED	:String = COMMAND_KEY + ".deselected";
		
		private var _alert:Alert;
		
		public function AlertDataCommandEvent(type:String, alert:Alert, bubbles:Boolean=false, cancelable:Boolean=false)
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
			return new AlertDataCommandEvent(type, _alert, bubbles, cancelable);
		}
	}
}