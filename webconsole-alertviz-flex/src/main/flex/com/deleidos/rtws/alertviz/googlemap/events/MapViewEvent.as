package com.deleidos.rtws.alertviz.googlemap.events
{
	import flash.events.Event;

	public class MapViewEvent extends Event
	{
		public static const COMMAND_KEY		:String = "map";
		public static const MAP_READY:String = COMMAND_KEY + ".mapReady";
		public static const OVERRIDE		:String = COMMAND_KEY + ".override";
		public static const SELECTED		:String = COMMAND_KEY + ".selected";
		
		private var _parameters:Object;
		
		public function MapViewEvent(type:String, parameters:Object, bubbles:Boolean=false, cancelable:Boolean=false)
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
			return new MapViewEvent(type, _parameters, bubbles, cancelable);
		}
	}
}
