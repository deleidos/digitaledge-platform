package com.deleidos.rtws.alertviz.events
{
	import flash.events.Event;
	
	public class WatchListCommandEvent extends Event
	{
		public static const COMMAND_KEY				:String = "watchListCommand";
		
		public static const REFRESH					:String = COMMAND_KEY + ".refresh";
		public static const REFRESH_SUCCESS  		:String = COMMAND_KEY + ".refreshSuccess"
		public static const REFERSH_ERROR  			:String = COMMAND_KEY + ".refreshError"
			
		public static const CREATE  				:String = COMMAND_KEY + ".create";
		public static const CREATE_SUCCESS  		:String = COMMAND_KEY + ".createSuccess";
		public static const CREATE_ERROR  			:String = COMMAND_KEY + ".createError";
		
		public static const REMOVE   				:String = COMMAND_KEY + ".remove";
		public static const REMOVE_SUCCESS  		:String = COMMAND_KEY + ".removeSuccess";
		public static const REMOVE_ERROR			:String = COMMAND_KEY + ".removeError";

		public static const COLOR_CHANGE			:String = COMMAND_KEY + ".colorChange";
		public static const COLOR_CHANGE_SUCCESS  	:String = COMMAND_KEY + ".colorChangeSuccess";
		public static const COLOR_CHANGE_ERROR  	:String = COMMAND_KEY + ".colorChangeError";
		
		public static const TRACK_ON_REQUEST				:String = COMMAND_KEY + ".trackOnRequest";
		public static const TRACK_ON				:String = COMMAND_KEY + ".trackOn";
		public static const TRACK_OFF_REQUEST				:String = COMMAND_KEY + ".trackOffRequest";
		public static const TRACK_OFF				:String = COMMAND_KEY + ".trackOff";
		
		private var _parameters:Object;
		
		public function WatchListCommandEvent(type:String, parameters:Object, bubbles:Boolean=false, cancelable:Boolean=false)
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
			return new WatchListCommandEvent(type, _parameters, bubbles, cancelable);
		}
	}
}