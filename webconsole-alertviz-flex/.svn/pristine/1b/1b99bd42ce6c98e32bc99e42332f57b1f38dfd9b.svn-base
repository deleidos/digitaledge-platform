package com.deleidos.rtws.alertviz.events
{
	import flash.events.Event;
	
	public class ApplicationCommandEvent extends Event
	{
		public static const COMMAND_KEY			:String = "ApplicationCommand";
		public static const CLEAR				:String = COMMAND_KEY + ".clear";
		public static const SHUTDOWN			:String = COMMAND_KEY + ".shutdown";
		
		private var _parameters:Object;
		
		public function ApplicationCommandEvent(type:String, parameters:Object, bubbles:Boolean=false, cancelable:Boolean=false)
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
			return new ApplicationCommandEvent(type, _parameters, bubbles, cancelable);
		}
	}
}