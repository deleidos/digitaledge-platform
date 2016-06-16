package com.deleidos.rtws.alertviz.events
{
	import flash.events.Event;
	
	public class CommandEvent extends Event
	{
		public static const COMMAND_KEY			:String = "command";
		public static const QUEUE_LIMIT_CHANGE	:String = COMMAND_KEY + ".queueLimitChange";
		public static const VIEW_FORMAT_CHANGE	:String = COMMAND_KEY + ".viewFormatChange";
		
		private var _parameters:Object;
		
		public function CommandEvent(type:String, parameters:Object, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_parameters = parameters;
		}
		
		public function get parameters():Object
		{
			return _parameters;	
		}
		
		public override function clone():Event
		{
			return new CommandEvent(type, _parameters, bubbles, cancelable);
		}
	}
}