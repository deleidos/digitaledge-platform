package com.deleidos.rtws.alertviz.events
{
	import flash.events.Event;

	public class HTTPQueueEvent extends Event
	{
		public static const COMMAND_KEY	:String = "httpQueue";
		public static const QUEUE		:String = COMMAND_KEY + ".queue";
		public static const COMPLETE	:String = COMMAND_KEY + ".complete";
		
		private var _request:Function;
		private var _parameters:Array;
		
		public function HTTPQueueEvent(type:String, request:Function = null, parameters:Array = null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_request = request;
			_parameters = parameters;
		}
		
		public function get request():Function{
			return _request;
		}
		
		public function get parameters():Array
		{
			return _parameters;	
		}
		
		public override function clone():Event
		{
			return new HTTPQueueEvent(type, _request, _parameters, bubbles, cancelable);
		}
	}
}