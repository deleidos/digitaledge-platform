package com.deleidos.rtws.alertviz.expandgraph.events
{
	import flash.events.Event;

	public class GraphConfigEvent extends Event
	{
		protected static const COMMAND_KEY		:String = "graphconfig";
		public static const GET_LOCATIONS		:String = COMMAND_KEY + ".getLocations";
		public static const GET_CONFIG			:String = COMMAND_KEY + ".getConfig";
		public static const SAVE  				:String = COMMAND_KEY + ".save";
		public static const CREATE_FOLDER  		:String = COMMAND_KEY + ".createFolder";
		public static const UPDATE   			:String = COMMAND_KEY + ".update";
		public static const MOVE	   			:String = COMMAND_KEY + ".move";
		public static const RENAME	   			:String = COMMAND_KEY + ".rename";
		public static const REMOVE   			:String = COMMAND_KEY + ".remove";
		
		public static const SERVICE_COMPLETE  	:String = COMMAND_KEY + ".serviceComplete";
		public static const SERVICE_ERROR  		:String = COMMAND_KEY + ".serviceError";
		
		private var _parameters:Object;
		
		public function GraphConfigEvent(type:String, parameters:Object, bubbles:Boolean=false, cancelable:Boolean=false)
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
			return new GraphConfigEvent(type, _parameters, bubbles, cancelable);
		}
	}
}