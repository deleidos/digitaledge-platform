package com.deleidos.rtws.alertviz.events
{
	import flash.events.Event;
	
	public class AlertCriteriaCommandEvent extends Event
	{
		public static const COMMAND_KEY			:String = "alertCriteriaCommand";
		public static const REFRESH				:String = COMMAND_KEY + ".refresh";
		public static const REFRESH_SUCCESS		:String = COMMAND_KEY + ".refreshSuccess";
		public static const REFRESH_ERROR		:String = COMMAND_KEY + ".refreshError";
		
		public static const CREATE  			:String = COMMAND_KEY + ".create";
		public static const CREATE_SUCCESS  	:String = COMMAND_KEY + ".createSuccess";
		public static const CREATE_ERROR  		:String = COMMAND_KEY + ".createError";
		
		public static const UPDATE   			:String = COMMAND_KEY + ".update";
		public static const UPDATE_SUCCESS   	:String = COMMAND_KEY + ".updateSuccess";
		public static const UPDATE_ERROR   		:String = COMMAND_KEY + ".updateError";
		
		public static const REMOVE   			:String = COMMAND_KEY + ".remove";
		public static const REMOVE_SUCCESS   	:String = COMMAND_KEY + ".removeSuccess";
		public static const REMOVE_ERROR   		:String = COMMAND_KEY + ".removeError";
		
		public static const ADD_DEF				:String = COMMAND_KEY + ".addDef";
		public static const SEND_DEF			:String = COMMAND_KEY + ".sendDef";
		public static const EDIT_DEF			:String = COMMAND_KEY + ".editDef";
		public static const REMOVE_DEF			:String = COMMAND_KEY + ".removeDef";
		public static const SEND_PATH			:String = COMMAND_KEY + ".sendPath";
		public static const SEND_VAL			:String = COMMAND_KEY + ".sendVal";
		public static const RETURN_VAL			:String = COMMAND_KEY + ".returnVal";
		public static const BROWSE_CANCEL		:String = COMMAND_KEY + ".browseCancel";
		public static const IGNORE				:String = COMMAND_KEY + ".ignore";
		public static const SEND_MODEL			:String = COMMAND_KEY + ".sendModel";
		public static const CHANGE_MODEL		:String = COMMAND_KEY + ".changeModel";
		public static const ALERT_MESSAGE		:String = COMMAND_KEY + ".alertMessage";
		
		private var _parameters:Object;
		
		public function AlertCriteriaCommandEvent(type:String, parameters:Object, bubbles:Boolean=false, cancelable:Boolean=false)
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
			return new AlertCriteriaCommandEvent(type, _parameters, bubbles, cancelable);
		}
	}
}