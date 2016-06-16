package com.deleidos.rtws.alertviz.expandgraph.events
{
	import flash.events.Event;
	
	public class FunctionEditEvent extends Event
	{
		public static const FUNCTION_INVALIDATED:String = "invalidFunction";
		public static const ALL_VALID:String = "allValid";
		
		public function FunctionEditEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		public override function clone():Event
		{
			return new FunctionEditEvent(type, bubbles, cancelable);
		}
	}
}