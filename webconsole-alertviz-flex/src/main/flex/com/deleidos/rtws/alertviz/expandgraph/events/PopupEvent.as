package com.deleidos.rtws.alertviz.expandgraph.events
{
	import flash.events.Event;

	public class PopupEvent extends Event
	{
		public static const SUCCESS:String = "success";
		public static const CANCEL:String = "cancel";
		public static const DONE:String = "done";
		public static const SETUP_COMPLETE:String = "setupComplete";
		
		public var result:String;
		
		public function PopupEvent(type:String, result:String = null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.result = result;
		}
		
		public override function clone():Event
		{
			return new PopupEvent(type, result, bubbles, cancelable);
		}
	}
}