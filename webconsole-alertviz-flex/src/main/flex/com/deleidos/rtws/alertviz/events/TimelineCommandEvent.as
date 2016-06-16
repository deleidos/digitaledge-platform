package com.deleidos.rtws.alertviz.events
{
	import com.deleidos.rtws.alertviz.models.Alert;
	
	import flash.events.Event;
	
	public class TimelineCommandEvent extends Event
	{
		public static const COMMAND_KEY	:String = "timelineCommand";
		public static const SELECTED	:String = COMMAND_KEY + ".selected";
		public static const DESELECTED	:String = COMMAND_KEY + ".deselected";
		
		private var _alert:Alert;
		
		public function TimelineCommandEvent(type:String, alert:Alert, bubbles:Boolean=false, cancelable:Boolean=false)
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
			return new TimelineCommandEvent(type, _alert, bubbles, cancelable);
		}
	}
}